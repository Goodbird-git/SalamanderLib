package com.goodbird.salamanderlib.asm.methods;

import com.goodbird.salamanderlib.asm.IAdvController;
import com.goodbird.salamanderlib.particles.BedrockLibrary;
import com.goodbird.salamanderlib.particles.emitter.BedrockEmitter;
import net.minecraft.client.Minecraft;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.ParticleKeyFrameEvent;

import java.util.ArrayList;

public class AnimationControllerMethods {
    public static void processBedrockParticleEvent(AnimationController controller, ParticleKeyFrameEvent event) {
        if(controller==null){
            return;
        }
        ArrayList<BedrockEmitter> emitters = ((IAdvController)controller).getEmitters();
        if (BedrockLibrary.instance.presets.containsKey(event.effect)) {
            BedrockEmitter emitter = new BedrockEmitter();
            emitter.setScheme(BedrockLibrary.instance.get(event.effect));
            emitter.setTarget(Minecraft.getMinecraft().player);
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
        }
    }


    public static void removeAllStopped(AnimationController controller) {
        ArrayList<BedrockEmitter> emitters = ((IAdvController)controller).getEmitters();
        for (int i = 0; i < emitters.size(); i++) {
            if (!emitters.get(i).playing && emitters.get(i).particles.isEmpty()) {
                emitters.remove(i);
                i--;
            }
        }
    }

    public static void setAllStopping(AnimationController controller) {
        ArrayList<BedrockEmitter> emitters = ((IAdvController)controller).getEmitters();
        for (BedrockEmitter emitter : emitters) {
            emitter.setLastLoop();
        }
    }

    public static void updateEmitters(AnimationController controller) {
        if(Minecraft.getMinecraft().world!=null) {
            long time = Minecraft.getMinecraft().world.getTotalWorldTime();
            if (time > ((IAdvController)controller).getLastTick()) {
                ((IAdvController)controller).setLastTick(time);

                ArrayList<BedrockEmitter> emitters = ((IAdvController) controller).getEmitters();
                for (int i = 0; i < emitters.size(); i++) {
                    BedrockEmitter emitter = emitters.get(i);
                    String name = emitter.scheme.name;
                    String loc = emitter.locator;
                    if (!emitter.scheme.toReload) {
                        emitters.get(i).update();
                    } else {
                        emitter.stop();
                        emitter = new BedrockEmitter();
                        emitter.setScheme(BedrockLibrary.instance.get(name));
                        emitter.setTarget(Minecraft.getMinecraft().player);
                        emitter.locator = loc;
                        emitter.start();
                        emitters.set(i, emitter);
                    }
                }
            }
        }
    }

    public static boolean hasParticleOnLoc(AnimationController controller, String effect, String locator) {
        ArrayList<BedrockEmitter> emitters = ((IAdvController)controller).getEmitters();
        for (BedrockEmitter emitter : emitters) {
            if (emitter.scheme != null && emitter.scheme.name != null && emitter.scheme.name.equals(effect) &&
                    emitter.locator != null && emitter.locator.equals(locator) && emitter.isLooping()) {
                return true;
            }
        }
        return false;
    }
}
