package com.goodbird.salamanderlib.example;

import com.goodbird.salamanderlib.example.client.renderer.ParticleExampleEntityRenderer;
import com.goodbird.salamanderlib.example.entity.ParticleExampleEntity;
import com.goodbird.salamanderlib.particles.BedrockLibrary;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
}
