package com.goodbird.salamanderlib.example;

import com.goodbird.salamanderlib.example.registry.EntityRegistry;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SalamanderLib.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonListener {
    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
            event.put(EntityRegistry.PARTICLE_EXAMPLE_ENTITY.get(), CreatureEntity.createMobAttributes()
                    .add(Attributes.FOLLOW_RANGE, 16.0D).add(Attributes.MAX_HEALTH, 1.0D).build());
    }
}
