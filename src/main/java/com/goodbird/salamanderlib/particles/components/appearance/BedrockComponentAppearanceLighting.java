package com.goodbird.salamanderlib.particles.components.appearance;

import com.goodbird.salamanderlib.particles.components.BedrockComponentBase;
import com.goodbird.salamanderlib.particles.components.IComponentEmitterInitialize;
import com.goodbird.salamanderlib.particles.emitter.BedrockEmitter;

public class BedrockComponentAppearanceLighting extends BedrockComponentBase implements IComponentEmitterInitialize
{
    @Override
    public void apply(BedrockEmitter emitter)
    {
        emitter.lit = false;
    }

    @Override
    public boolean canBeEmpty()
    {
        return true;
    }
}