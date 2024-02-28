package com.goodbird.salamanderlib.example.client.model;

import com.goodbird.salamanderlib.example.SalamanderLib;
import com.goodbird.salamanderlib.example.tile.TileMagicTorch;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MagicTorchModel extends AnimatedGeoModel<TileMagicTorch> {
    @Override
    public ResourceLocation getAnimationFileLocation(TileMagicTorch entity) {
        return new ResourceLocation(SalamanderLib.MODID, "animations/magic_torch.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(TileMagicTorch animatable) {
        return new ResourceLocation(SalamanderLib.MODID, "geo/magic_torch.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(TileMagicTorch entity) {
        return new ResourceLocation(SalamanderLib.MODID, "textures/block/magic_torch.png");
    }
}