package com.goodbird.salamanderlib.particles.components.appearance;

import com.goodbird.salamanderlib.particles.components.BedrockComponentBase;
import com.goodbird.salamanderlib.particles.components.IComponentParticleRender;
import com.goodbird.salamanderlib.particles.emitter.BedrockEmitter;
import com.goodbird.salamanderlib.particles.emitter.BedrockParticle;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.BufferBuilder;
import software.bernie.geckolib3.core.molang.MolangException;
import software.bernie.geckolib3.core.molang.MolangParser;
import software.bernie.geckolib3.core.molang.expressions.MolangExpression;

import java.util.Map;
import java.util.Set;

public class BedrockComponentCollisionTinting extends BedrockComponentAppearanceTinting implements IComponentParticleRender
{
    public MolangExpression enabled = MolangParser.ZERO;

    @Override
    public BedrockComponentBase fromJson(JsonElement elem, MolangParser parser) throws MolangException
    {
        if (!elem.isJsonObject()) return super.fromJson(elem, parser);

        JsonObject element = elem.getAsJsonObject();

        if (element.has("enabled")) this.enabled = parser.parseJson(element.get("enabled"));

        return super.fromJson(element, parser);
    }

    @Override
    public JsonElement toJson()
    {
        JsonObject object = new JsonObject();
        
        object.add("enabled", this.enabled.toJson());

        /* add the default stuff from super */
        JsonObject superJson = (JsonObject) super.toJson();
        Set<Map.Entry<String, JsonElement>> entries = superJson.entrySet();

        for(Map.Entry<String, JsonElement> entry : entries)
        {
            object.add(entry.getKey(), entry.getValue());
        }

        return object;
    }

    @Override
    public void render(BedrockEmitter emitter, BedrockParticle particle, BufferBuilder builder, float partialTicks)
    {
        if (particle.isCollisionTinting(emitter))
        {
            this.renderOnScreen(particle, 0, 0, 0, 0);
        }
    }

    @Override
    public int getSortingIndex()
    {
        return -5;
    }
}