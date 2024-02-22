package com.goodbird.salamanderlib.particles.components.shape;

import com.goodbird.salamanderlib.particles.emitter.BedrockEmitter;
import com.goodbird.salamanderlib.particles.emitter.BedrockParticle;

public class BedrockComponentShapePoint extends BedrockComponentShapeBase
{
    @Override
    public void apply(BedrockEmitter emitter, BedrockParticle particle)
    {
        particle.position.x = (float) this.offset[0].get();
        particle.position.y = (float) this.offset[1].get();
        particle.position.z = (float) this.offset[2].get();

        if (this.direction instanceof ShapeDirection.Vector)
        {
            this.direction.applyDirection(particle, particle.position.x, particle.position.y, particle.position.z);
        }
    }
}