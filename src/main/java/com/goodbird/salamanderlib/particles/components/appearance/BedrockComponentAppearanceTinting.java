package com.goodbird.salamanderlib.particles.components.appearance;

import com.goodbird.salamanderlib.particles.BedrockSchemeJsonAdapter;
import com.goodbird.salamanderlib.particles.components.BedrockComponentBase;
import com.goodbird.salamanderlib.particles.components.IComponentParticleRender;
import com.goodbird.salamanderlib.particles.emitter.BedrockEmitter;
import com.goodbird.salamanderlib.particles.emitter.BedrockParticle;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.BufferBuilder;
import software.bernie.geckolib3.core.molang.MolangException;
import software.bernie.geckolib3.core.molang.MolangParser;

public class BedrockComponentAppearanceTinting extends BedrockComponentBase implements IComponentParticleRender
{
    public Tint color = new Tint.Solid();

    @Override
    public BedrockComponentBase fromJson(JsonElement elem, MolangParser parser) throws MolangException
    {
        if (!elem.isJsonObject()) return super.fromJson(elem, parser);

        JsonObject element = elem.getAsJsonObject();

        if (element.has("color"))
        {
            JsonElement color = element.get("color");

            if (color.isJsonArray() || color.isJsonPrimitive())
            {
                this.color = Tint.parseColor(color, parser);
            }
            else if (color.isJsonObject())
            {
                this.color = Tint.parseGradient(color.getAsJsonObject(), parser);
            }
        }

        return super.fromJson(element, parser);
    }

    @Override
    public JsonElement toJson()
    {
        JsonObject object = new JsonObject();
        JsonElement element = this.color.toJson();

        if (!BedrockSchemeJsonAdapter.isEmpty(element))
        {
            object.add("color", element);
        }

        return object;
    }

    /* Interface implementations */

    @Override
    public void preRender(BedrockEmitter emitter, float partialTicks)
    {}

    @Override
    public void render(BedrockEmitter emitter, BedrockParticle particle, BufferBuilder builder, float partialTicks)
    {
        this.renderOnScreen(particle, 0, 0, 0, 0);
    }

    @Override
    public void renderOnScreen(BedrockParticle particle, int x, int y, float scale, float partialTicks)
    {
        if (this.color != null)
        {
            this.color.compute(particle);
        }
        else
        {
            particle.r = particle.g = particle.b = particle.a = 1;
        }
    }

    @Override
    public void postRender(BedrockEmitter emitter, float partialTicks)
    {}

    @Override
    public int getSortingIndex()
    {
        return -10;
    }
}