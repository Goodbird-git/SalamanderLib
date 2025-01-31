package com.goodbird.salamanderlib.particles.components.rate;

import com.goodbird.salamanderlib.particles.components.BedrockComponentBase;
import com.goodbird.salamanderlib.particles.components.IComponentParticleMorphRender;
import com.goodbird.salamanderlib.particles.components.IComponentParticleRender;
import com.goodbird.salamanderlib.particles.emitter.BedrockEmitter;
import com.goodbird.salamanderlib.particles.emitter.BedrockParticle;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.BufferBuilder;
import software.bernie.geckolib3.core.molang.MolangException;
import com.goodbird.salamanderlib.molang.MolangParser;
import com.goodbird.salamanderlib.molang.expressions.MolangExpression;
import com.goodbird.salamanderlib.molang.expressions.MolangValue;
import software.bernie.shadowed.eliotlash.mclib.math.Constant;

public class BedrockComponentRateSteady extends BedrockComponentRate implements IComponentParticleRender, IComponentParticleMorphRender
{
    public static final MolangExpression DEFAULT_PARTICLES = new MolangValue(null, new Constant(50));

    public MolangExpression spawnRate = MolangParser.ONE;

    public BedrockComponentRateSteady()
    {
        this.particles = DEFAULT_PARTICLES;
    }

    public BedrockComponentBase fromJson(JsonElement elem, MolangParser parser) throws MolangException
    {
        if (!elem.isJsonObject()) return super.fromJson(elem, parser);

        JsonObject element = elem.getAsJsonObject();

        if (element.has("spawn_rate")) this.spawnRate = parser.parseJson(element.get("spawn_rate"));
        if (element.has("max_particles")) this.particles = parser.parseJson(element.get("max_particles"));

        return super.fromJson(element, parser);
    }

    @Override
    public JsonElement toJson()
    {
        JsonObject object = new JsonObject();

        if (!MolangExpression.isOne(this.spawnRate)) object.add("spawn_rate", this.spawnRate.toJson());
        if (!MolangExpression.isConstant(this.particles, 50)) object.add("max_particles", this.particles.toJson());

        return object;
    }

    @Override
    public void preRender(BedrockEmitter emitter, float partialTicks)
    {}

    @Override
    public void render(BedrockEmitter emitter, BedrockParticle particle, BufferBuilder builder, float partialTicks)
    {}

    @Override
    public void renderOnScreen(BedrockParticle particle, int x, int y, float scale, float partialTicks)
    {}

    @Override
    public void postRender(BedrockEmitter emitter, float partialTicks)
    {
        if (emitter.playing)
        {
            double particles = emitter.getAge(partialTicks) * this.spawnRate.get();
            double diff = particles - emitter.spawnedParticles;
            double spawn = Math.round(diff);

            if (spawn > 0)
            {
                emitter.setEmitterVariables(partialTicks);

                double track = spawn;

                for (int i = 0; i < spawn; i++)
                {
                    if (emitter.particles.size() < this.particles.get())
                    {
                        emitter.spawnParticle();
                    }
                    else
                    {
                        track -= 1;
                    }
                }

                emitter.spawnedParticles += track;
            }
        }
    }

    @Override
    public int getSortingIndex()
    {
        return 10;
    }
}