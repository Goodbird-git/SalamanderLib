package com.goodbird.salamanderlib.mixin.impl;

import com.goodbird.salamanderlib.mixin.IAdvController;
import com.goodbird.salamanderlib.particles.BedrockLibrary;
import com.goodbird.salamanderlib.particles.emitter.BedrockEmitter;
import com.goodbird.salamanderlib.toast.CustomToast;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.util.text.StringTextComponent;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.builder.Animation;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.ParticleKeyFrameEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.keyframe.EventKeyFrame;
import software.bernie.geckolib3.core.keyframe.ParticleEventKeyFrame;
import software.bernie.geckolib3.core.molang.MolangParser;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.core.snapshot.BoneSnapshot;

import java.util.*;
import java.util.logging.Logger;

@Mixin(value = AnimationController.class, remap = false)
public class MixinAnimationController<T extends IAnimatable> implements IAdvController {
    @Shadow protected Animation currentAnimation;
    @Unique
    Set<EventKeyFrame<?>> executedParticleKeyFrames = new HashSet<>();
    @Shadow protected T animatable;
    @Shadow private AnimationController.IParticleListener<T> particleListener;
    @Unique
    public List<BedrockEmitter> emitters = new ArrayList<>();
    @Unique
    public long lastTick = 0;

    @Inject(method = "process", at = @At(
            value = "INVOKE",
            shift = At.Shift.BY,
            target = "Lsoftware/bernie/geckolib3/core/controller/AnimationController;adjustTick(D)D",
            by = 2
    ))
    private void onProcessUpdate(double tick, AnimationEvent<T> event, List<IBone> modelRendererList, Map<String, Pair<IBone, BoneSnapshot>> boneSnapshotCollection, MolangParser parser, boolean crashWhenCantFindBone, CallbackInfo ci) {
        updateEmitters();
    }

    @Inject(method = "process", at = @At(
            value = "INVOKE",
            shift = At.Shift.AFTER,
            target = "Lsoftware/bernie/geckolib3/core/controller/AnimationController;resetEventKeyFrames()V"
    ))
    private void onProcessReset(double tick, AnimationEvent<T> event, List<IBone> modelRendererList, Map<String, Pair<IBone, BoneSnapshot>> boneSnapshotCollection, MolangParser parser, boolean crashWhenCantFindBone, CallbackInfo ci) {
        setAllStopping();
    }

    @Inject(method = "processCurrentAnimation", at = @At(
            value = "INVOKE",
            shift = At.Shift.BY,
            target = "Lsoftware/bernie/geckolib3/core/builder/ILoopType;isRepeatingAfterEnd()Z",
            by = 2
    ))
    private void onProcessCurStop(double tick, double actualTick, MolangParser parser, boolean crashWhenCantFindBone, CallbackInfo ci) {
        setAllStopping();
    }

    @Inject(method = "processCurrentAnimation", at = @At(
            value = "INVOKE",
            shift = At.Shift.AFTER,
            target = "Lsoftware/bernie/geckolib3/core/controller/AnimationController;setAnimTime(Lsoftware/bernie/geckolib3/core/molang/MolangParser;D)V"
    ))
    private void onProcessCurRemove(double tick, double actualTick, MolangParser parser, boolean crashWhenCantFindBone, CallbackInfo ci) {
        removeAllStopped();
    }

    @Inject(method = "processCurrentAnimation", at = @At(
            value = "FIELD",
            shift = At.Shift.BEFORE,
            target = "Lsoftware/bernie/geckolib3/core/controller/AnimationController;transitionLengthTicks:D"
    ))
    private void onProcessCurProcess(double tick, double actualTick, MolangParser parser, boolean crashWhenCantFindBone, CallbackInfo ci) {
        for (ParticleEventKeyFrame particleEventKeyFrame : currentAnimation.particleKeyFrames) {
            if (!hasParticleOnLoc(particleEventKeyFrame.effect,particleEventKeyFrame.locator)
                    && tick >= particleEventKeyFrame.getStartTick() && !executedParticleKeyFrames.contains(particleEventKeyFrame)) {
                ParticleKeyFrameEvent<T> event = new ParticleKeyFrameEvent<T>(animatable, tick,
                        particleEventKeyFrame.effect, particleEventKeyFrame.locator, particleEventKeyFrame.script,
                        (AnimationController) (Object)this);
                processBedrockParticleEvent(event);
                executedParticleKeyFrames.add(particleEventKeyFrame);
            }
        }
    }

    @Inject(method = "resetEventKeyFrames", at = @At("TAIL"))
    private void onReset(CallbackInfo ci){
        executedParticleKeyFrames.clear();
    }

    @Unique
    public List<BedrockEmitter> getEmitters() {
        return emitters;
    }

    @Unique
    public long getLastTick() {
        return lastTick;
    }

    @Unique
    public void setLastTick(long lastTick) {
        this.lastTick = lastTick;
    }


    public void processBedrockParticleEvent(ParticleKeyFrameEvent event) {
        if (BedrockLibrary.instance.has(event.effect)) {
            BedrockEmitter emitter = new BedrockEmitter();
            emitter.setScheme(BedrockLibrary.instance.get(event.effect));
            emitter.setTarget(Minecraft.getInstance().player);
            emitter.locator = event.locator;
            try {
                String[] values = event.script.split(";");
                if (values.length > 0) {
                    emitter.disableAfter = Integer.parseInt(values[0]);
                }
            } catch (Exception ignored) {
                emitter.disableAfter = -1;
            }
            emitter.start();
            emitters.add(emitter);
        }else{
            CustomToast.addOrUpdate(Minecraft.getInstance().getToasts(), CustomToast.multiline(Minecraft.getInstance(), "salamanderlib:noparticlewithid", new StringTextComponent("SalamanderLib error"), new StringTextComponent("Can't find find a particle with identifier \""+event.effect+"\"")));
        }
    }


    public void removeAllStopped() {
        for (int i = 0; i < emitters.size(); i++) {
            if (!emitters.get(i).playing && emitters.get(i).particles.isEmpty()) {
                emitters.remove(i);
                i--;
            }
        }
    }

    public void setAllStopping() {
        for (BedrockEmitter emitter : emitters) {
            emitter.setLastLoop();
        }
    }

    public void updateEmitters() {
        if(Minecraft.getInstance().level!=null) {
            long time = Minecraft.getInstance().level.getGameTime();
            if (time > getLastTick()) {
                setLastTick(time);

                for (int i = 0; i < emitters.size(); i++) {
                    BedrockEmitter emitter = emitters.get(i);
                    String name = emitter.scheme.identifier;
                    String loc = emitter.locator;
                    if (!emitter.scheme.toReload) {
                        emitters.get(i).update();
                    } else {
                        if (BedrockLibrary.instance.has(name)) {
                            emitter.stop();
                            emitter = new BedrockEmitter();
                            emitter.setScheme(BedrockLibrary.instance.get(name));
                            emitter.setTarget(Minecraft.getInstance().player);
                            emitter.locator = loc;
                            emitter.start();
                        }
                        emitters.set(i, emitter);
                    }
                }
            }
        }
    }

    public boolean hasParticleOnLoc(String effect, String locator) {
        for (BedrockEmitter emitter : emitters) {
            if (emitter.scheme != null && emitter.scheme.name != null && emitter.scheme.name.equals(effect) &&
                    emitter.locator != null && emitter.locator.equals(locator) && emitter.isLooping()) {
                return true;
            }
        }
        return false;
    }
}
