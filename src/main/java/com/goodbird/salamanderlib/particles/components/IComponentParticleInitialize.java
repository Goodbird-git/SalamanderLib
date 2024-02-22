package com.goodbird.salamanderlib.particles.components;

import com.goodbird.salamanderlib.particles.emitter.BedrockEmitter;
import com.goodbird.salamanderlib.particles.emitter.BedrockParticle;

public interface IComponentParticleInitialize extends IComponentBase
{
    public void apply(BedrockEmitter emitter, BedrockParticle particle);
}