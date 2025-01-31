package com.goodbird.salamanderlib.particles.components.shape;

import com.goodbird.salamanderlib.particles.components.BedrockComponentBase;
import com.goodbird.salamanderlib.particles.emitter.BedrockEmitter;
import com.goodbird.salamanderlib.particles.emitter.BedrockParticle;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import software.bernie.geckolib3.core.molang.MolangException;
import com.goodbird.salamanderlib.molang.MolangParser;
import com.goodbird.salamanderlib.molang.expressions.MolangExpression;

import com.goodbird.salamanderlib.vecmath.Vector3f;

public class BedrockComponentShapeSphere extends BedrockComponentShapeBase
{
    public MolangExpression radius = MolangParser.ZERO;

    @Override
    public BedrockComponentBase fromJson(JsonElement elem, MolangParser parser) throws MolangException
    {
        if (!elem.isJsonObject()) return super.fromJson(elem, parser);

        JsonObject element = elem.getAsJsonObject();

        if (element.has("radius")) this.radius = parser.parseJson(element.get("radius"));

        return super.fromJson(element, parser);
    }

    @Override
    public JsonElement toJson()
    {
        JsonObject object = (JsonObject) super.toJson();

        if (!MolangExpression.isZero(this.radius)) object.add("radius", this.radius.toJson());

        return object;
    }

    @Override
    public void apply(BedrockEmitter emitter, BedrockParticle particle)
    {
        float centerX = (float) this.offset[0].get();
        float centerY = (float) this.offset[1].get();
        float centerZ = (float) this.offset[2].get();
        float radius = (float) this.radius.get();

        Vector3f direction = new Vector3f((float) Math.random() * 2 - 1, (float) Math.random() * 2 - 1, (float) Math.random() * 2 - 1);
        direction.normalize();

        if (!this.surface)
        {
            radius *= Math.random();
        }

        direction.scale(radius);

        particle.position.x = centerX + direction.x;
        particle.position.y = centerY + direction.y;
        particle.position.z = centerZ + direction.z;

        this.direction.applyDirection(particle, centerX, centerY, centerZ);
    }
}