package com.goodbird.salamanderlib.example.registry;

import com.goodbird.salamanderlib.example.SalamanderLib;
import com.goodbird.salamanderlib.example.entity.ParticleExampleEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.example.entity.GeoExampleEntity;

public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES,
            SalamanderLib.MODID);

    public static final RegistryObject<EntityType<ParticleExampleEntity>> PARTICLE_EXAMPLE_ENTITY = buildEntity(
            ParticleExampleEntity::new, ParticleExampleEntity.class, .7F, 1.3F);

    public static <T extends Entity> RegistryObject<EntityType<T>> buildEntity(EntityType.IFactory<T> entity,
                                                                               Class<T> entityClass, float width, float height) {
        String name = entityClass.getSimpleName().toLowerCase();
        return ENTITIES.register(name,
                () -> EntityType.Builder.of(entity, EntityClassification.CREATURE).sized(width, height).build(name));
    }
}
