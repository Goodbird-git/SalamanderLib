package com.goodbird.salamanderlib.particles.components;

import com.goodbird.salamanderlib.particles.emitter.BedrockEmitter;

public interface IComponentEmitterUpdate extends IComponentBase
{
    public void update(BedrockEmitter emitter);
}