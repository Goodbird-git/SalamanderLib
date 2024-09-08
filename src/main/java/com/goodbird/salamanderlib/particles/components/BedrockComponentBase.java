package com.goodbird.salamanderlib.particles.components;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import software.bernie.geckolib3.core.molang.MolangException;
import software.bernie.geckolib3.core.molang.MolangParser;

public abstract class BedrockComponentBase
{
    public BedrockComponentBase fromJson(JsonElement element, MolangParser parser) throws MolangException
    {
        return this;
    }

    public JsonElement toJson()
    {
        return new JsonObject();
    }

    public boolean canBeEmpty()
    {
        return false;
    }
}