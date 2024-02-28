package com.goodbird.salamanderlib.example.client.renderer;

import com.goodbird.salamanderlib.example.client.model.MagicTorchModel;
import com.goodbird.salamanderlib.example.tile.TileMagicTorch;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class MagicTorchTileRenderer extends GeoBlockRenderer<TileMagicTorch> {
    public MagicTorchTileRenderer() {
        super(new MagicTorchModel());
    }
}
