package com.goodbird.salamanderlib.mclib.utils.resources;

import com.goodbird.salamanderlib.mclib.utils.ICopy;
import com.google.gson.JsonElement;
import net.minecraft.nbt.INBT;

public interface IWritableLocation<T> extends ICopy<T>
{
    public void fromNbt(INBT nbt) throws Exception;

    public void fromJson(JsonElement element) throws Exception;

    public INBT writeNbt();

    public JsonElement writeJson();
}
