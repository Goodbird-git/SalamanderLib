package com.goodbird.salamanderlib.example;

import com.goodbird.salamanderlib.example.client.renderer.MagicTorchTileRenderer;
import com.goodbird.salamanderlib.example.client.renderer.ParticleExampleEntityRenderer;
import com.goodbird.salamanderlib.example.entity.ParticleExampleEntity;
import com.goodbird.salamanderlib.example.tile.TileMagicTorch;
import com.goodbird.salamanderlib.particles.BedrockLibrary;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import software.bernie.example.block.tile.BotariumTileEntity;
import software.bernie.example.client.renderer.tile.BotariumTileRenderer;

import java.io.File;

@Mod(modid = SalamanderLib.MODID,
        name = "SalamanderLib",
        version = "1.0",
        dependencies = "required-after:geckolib3")

public class SalamanderLib {
    public static final String MODID = "salamanderlib";
    public static BedrockLibrary particleLibraryInstance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        particleLibraryInstance = new BedrockLibrary(new File("./particle"));
        particleLibraryInstance.reload();
    }

    @SideOnly(Side.CLIENT)
    @Mod.EventHandler
    public void registerRenderers(FMLPreInitializationEvent event) {
        ClientRegistry.bindTileEntitySpecialRenderer(TileMagicTorch.class, new MagicTorchTileRenderer());
        RenderingRegistry.registerEntityRenderingHandler(ParticleExampleEntity.class, ParticleExampleEntityRenderer::new);
    }
}
