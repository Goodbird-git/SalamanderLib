package com.goodbird.salamanderlib.particles.components;

import com.goodbird.salamanderlib.particles.emitter.BedrockEmitter;

public interface IComponentEmitterInitialize extends IComponentBase
{
    public void apply(BedrockEmitter emitter);
}