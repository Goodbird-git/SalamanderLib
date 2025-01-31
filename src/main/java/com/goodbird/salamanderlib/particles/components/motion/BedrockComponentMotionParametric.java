package com.goodbird.salamanderlib.particles.components.motion;

import com.goodbird.salamanderlib.particles.components.BedrockComponentBase;
import com.goodbird.salamanderlib.particles.components.IComponentParticleUpdate;
import com.goodbird.salamanderlib.particles.emitter.BedrockEmitter;
import com.goodbird.salamanderlib.particles.emitter.BedrockParticle;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.goodbird.salamanderlib.particles.components.IComponentParticleInitialize;
import software.bernie.geckolib3.core.molang.MolangException;
import com.goodbird.salamanderlib.molang.MolangParser;
import com.goodbird.salamanderlib.molang.expressions.MolangExpression;

import com.goodbird.salamanderlib.vecmath.Vector3f;

public class BedrockComponentMotionParametric extends BedrockComponentMotion implements IComponentParticleInitialize, IComponentParticleUpdate
{
    public MolangExpression[] position = {MolangParser.ZERO, MolangParser.ZERO, MolangParser.ZERO};
    public MolangExpression rotation = MolangParser.ZERO;

    @Override
    public BedrockComponentBase fromJson(JsonElement elem, MolangParser parser) throws MolangException
    {
        if (!elem.isJsonObject()) return super.fromJson(elem, parser);

        JsonObject element = elem.getAsJsonObject();

        if (element.has("relative_position") && element.get("relative_position").isJsonArray())
        {
            JsonArray array = element.get("relative_position").getAsJsonArray();

            this.position[0] = parser.parseJson(array.get(0));
            this.position[1] = parser.parseJson(array.get(1));
            this.position[2] = parser.parseJson(array.get(2));
        }

        if (element.has("rotation"))
        {
            this.rotation = parser.parseJson(element.get("rotation"));
        }

        return super.fromJson(element, parser);
    }

    @Override
    public JsonElement toJson()
    {
        JsonObject object = new JsonObject();
        JsonArray position = new JsonArray();

        for (MolangExpression expression : this.position)
        {
            position.add(expression.toJson());
        }

        object.add("relative_position", position);

        if (!MolangExpression.isZero(this.rotation)) object.add("rotation", this.rotation.toJson());

        return object;
    }

    @Override
    public void apply(BedrockEmitter emitter, BedrockParticle particle)
    {
        Vector3f position = new Vector3f((float) this.position[0].get(), (float) this.position[1].get(), (float) this.position[2].get());

        particle.manual = true;
        particle.initialPosition.set(particle.position);

        particle.matrix.transform(position);
        particle.position.x = particle.initialPosition.x + position.x;
        particle.position.y = particle.initialPosition.y + position.y;
        particle.position.z = particle.initialPosition.z + position.z;
        particle.rotation = (float) this.rotation.get();
    }

    @Override
    public void update(BedrockEmitter emitter, BedrockParticle particle)
    {
        Vector3f position = new Vector3f((float) this.position[0].get(), (float) this.position[1].get(), (float) this.position[2].get());

        particle.matrix.transform(position);
        particle.position.x = particle.initialPosition.x + position.x;
        particle.position.y = particle.initialPosition.y + position.y;
        particle.position.z = particle.initialPosition.z + position.z;
        particle.rotation = (float) this.rotation.get();
    }

    @Override
    public int getSortingIndex()
    {
        return 10;
    }
}