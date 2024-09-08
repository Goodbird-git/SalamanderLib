package com.goodbird.salamanderlib.mixin.impl;

import net.minecraft.util.math.vector.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Matrix4f.class)
public interface IMatrix4f {

    @Accessor(value = "m00")
    float m00();
    @Accessor(value = "m01")
    float m01();
    @Accessor(value = "m02")
    float m02();
    @Accessor(value = "m03")
    float m03();

    @Accessor(value = "m10")
    float m10();
    @Accessor(value = "m11")
    float m11();
    @Accessor(value = "m12")
    float m12();
    @Accessor(value = "m13")
    float m13();

    @Accessor(value = "m20")
    float m20();
    @Accessor(value = "m21")
    float m21();
    @Accessor(value = "m22")
    float m22();
    @Accessor(value = "m23")
    float m23();

    @Accessor(value = "m30")
    float m30();
    @Accessor(value = "m31")
    float m31();
    @Accessor(value = "m32")
    float m32();
    @Accessor(value = "m33")
    float m33();

}
