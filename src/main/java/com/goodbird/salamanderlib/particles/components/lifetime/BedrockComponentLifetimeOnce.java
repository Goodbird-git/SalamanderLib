package com.goodbird.salamanderlib.particles.components.lifetime;

import com.goodbird.salamanderlib.particles.emitter.BedrockEmitter;

public class BedrockComponentLifetimeOnce extends BedrockComponentLifetime
{
    @Override
    public void update(BedrockEmitter emitter)
    {
        double time = this.activeTime.get();

        emitter.lifetime = (int) (time * 20);

        if (emitter.getAge() >= time)
        {
            emitter.stop();
        }
    }
}