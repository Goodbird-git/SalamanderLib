package com.goodbird.salamanderlib.example.entity;

import com.goodbird.salamanderlib.example.SalamanderLib;
import com.goodbird.salamanderlib.example.registry.EntityRegistry;
import com.goodbird.salamanderlib.particles.BedrockLibrary;
import com.goodbird.salamanderlib.particles.emitter.BedrockEmitter;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import software.bernie.example.GeckoLibMod;
import software.bernie.example.entity.GeoExampleEntity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class ParticleExampleEntity extends CreatureEntity implements IAnimatable, IAnimationTickable {
    private AnimationFactory factory = new AnimationFactory(this);
    public BedrockEmitter emitter;
    public String particleName = "magic";
    public boolean restart = false;
    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.bat.fly", true));
        return PlayState.CONTINUE;
    }

    public ParticleExampleEntity(EntityType<ParticleExampleEntity> type, World worldIn) {
        super(type, worldIn);
        emitter = new BedrockEmitter();
        emitter.setScheme(BedrockLibrary.instance.presets.get(particleName));
        emitter.start();
    }

    public ParticleExampleEntity(World worldIn) {
        this(EntityRegistry.PARTICLE_EXAMPLE_ENTITY.get(), worldIn);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 50, this::predicate));
    }


    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        super.registerGoals();
    }

    @Override
    public int tickTimer() {
        return tickCount;
    }

    @Override
    public void tick() {
        //super.onLivingUpdate();
        if(level.isClientSide) {
            //setRotation(0,0);
            if (!restart && emitter.scheme!=null && !emitter.scheme.toReload) {
                emitter.update();
            } else {
                emitter.stop();
                emitter = new BedrockEmitter();
                emitter.setScheme(BedrockLibrary.instance.presets.get(particleName));
                emitter.start();
                restart = false;
            }
        }
    }
}
