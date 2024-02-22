package com.goodbird.salamanderlib.particles.components;

import com.goodbird.salamanderlib.particles.emitter.BedrockEmitter;
import com.goodbird.salamanderlib.particles.emitter.BedrockParticle;
import net.minecraft.client.renderer.BufferBuilder;

public interface IComponentRenderBase extends IComponentBase
{
    public void render(BedrockEmitter emitter, BedrockParticle particle, BufferBuilder builder, float partialTicks);

    public void renderOnScreen(BedrockParticle particle, int x, int y, float scale, float partialTicks);

    public void preRender(BedrockEmitter emitter, float partialTicks);

    public void postRender(BedrockEmitter emitter, float partialTicks);
}
