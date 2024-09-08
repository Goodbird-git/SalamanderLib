package com.goodbird.salamanderlib.example;

import com.goodbird.salamanderlib.example.SalamanderLib;
import com.goodbird.salamanderlib.example.client.renderer.MagicTorchTileRenderer;
import com.goodbird.salamanderlib.example.client.renderer.ParticleExampleEntityRenderer;
import com.goodbird.salamanderlib.example.entity.ParticleExampleEntity;
import com.goodbird.salamanderlib.example.registry.EntityRegistry;
import com.goodbird.salamanderlib.example.registry.TileRegistry;
import com.goodbird.salamanderlib.example.tile.TileMagicTorch;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(
        modid = SalamanderLib.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class ClientListener {
    public ClientListener() {
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerRenderers(FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntityRenderer(TileRegistry.MAGIC_TORCH_TILE.get(), (d) -> new MagicTorchTileRenderer(d));
        RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.PARTICLE_EXAMPLE_ENTITY.get(), ParticleExampleEntityRenderer::new);
    }
}
