package com.goodbird.salamanderlib.particles.components.expiration;

import com.goodbird.salamanderlib.particles.components.IComponentParticleUpdate;
import com.goodbird.salamanderlib.particles.emitter.BedrockEmitter;
import com.goodbird.salamanderlib.particles.emitter.BedrockParticle;
import net.minecraft.block.Block;

public class BedrockComponentExpireNotInBlocks extends BedrockComponentExpireBlocks implements IComponentParticleUpdate
{
    @Override
    public void update(BedrockEmitter emitter, BedrockParticle particle)
    {
        if (particle.dead || emitter.world == null)
        {
            return;
        }

        Block current = this.getBlock(emitter, particle);

        for (Block block : this.blocks)
        {
            if (block == current)
            {
                return;
            }
        }

        particle.dead = true;
    }
}