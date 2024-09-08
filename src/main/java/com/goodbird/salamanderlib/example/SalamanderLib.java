package com.goodbird.salamanderlib.example;

import com.goodbird.salamanderlib.example.client.renderer.MagicTorchTileRenderer;
import com.goodbird.salamanderlib.example.client.renderer.ParticleExampleEntityRenderer;
import com.goodbird.salamanderlib.example.entity.ParticleExampleEntity;
import com.goodbird.salamanderlib.example.registry.BlockRegistry;
import com.goodbird.salamanderlib.example.registry.EntityRegistry;
import com.goodbird.salamanderlib.example.registry.ItemRegistry;
import com.goodbird.salamanderlib.example.registry.TileRegistry;
import com.goodbird.salamanderlib.example.tile.TileMagicTorch;
import com.goodbird.salamanderlib.particles.BedrockLibrary;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.io.File;

@Mod(SalamanderLib.MODID)

public class SalamanderLib {
    public static final String MODID = "salamanderlib";
    public static BedrockLibrary particleLibraryInstance;

    public SalamanderLib() {
        particleLibraryInstance = new BedrockLibrary(new File("./particle"));
        particleLibraryInstance.reload();
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        EntityRegistry.ENTITIES.register(bus);
        ItemRegistry.ITEMS.register(bus);
        TileRegistry.TILES.register(bus);
        BlockRegistry.BLOCKS.register(bus);
    }
}
