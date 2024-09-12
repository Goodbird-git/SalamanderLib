package com.goodbird.salamanderlib.mixin.impl;

import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ResourceLocation.class)
public interface IResLoc {
    @Accessor
    @Mutable
    void setPath(String path);
    @Accessor
    @Mutable
    void setNamespace(String namespace);
}
