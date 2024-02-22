package com.goodbird.salamanderlib.particles.components;

import com.goodbird.salamanderlib.particles.emitter.BedrockEmitter;
import com.goodbird.salamanderlib.particles.emitter.BedrockParticle;

public interface IComponentParticleUpdate extends IComponentBase
{
    public void update(BedrockEmitter emitter, BedrockParticle particle);
}