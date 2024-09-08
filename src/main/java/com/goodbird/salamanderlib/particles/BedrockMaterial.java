package com.goodbird.salamanderlib.particles;

import com.mojang.blaze3d.platform.GlStateManager;
import org.lwjgl.opengl.GL11;

public enum  BedrockMaterial
{
    OPAQUE("particles_opaque"), ALPHA("particles_alpha"), BLEND("particles_blend"), ADDITIVE("particles_add");

    public final String id;

    public static BedrockMaterial fromString(String material)
    {
        for (BedrockMaterial mat : values())
        {
            if (mat.id.equals(material))
            {
                return mat;
            }
        }

        return OPAQUE;
    }

    private BedrockMaterial(String id)
    {
        this.id = id;
    }

    public void beginGL()
    {
        switch (this)
        {
            case OPAQUE:
                GlStateManager._blendFunc(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.value);
                GlStateManager._alphaFunc(GL11.GL_GREATER, 0F);
                GlStateManager._disableBlend();
                GlStateManager._enableAlphaTest();
                break;
            case ALPHA:
                GlStateManager._blendFunc(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.value);
                GlStateManager._alphaFunc(GL11.GL_GREATER, 0.1F);
                GlStateManager._disableBlend();
                GlStateManager._enableAlphaTest();
                break;
            case BLEND:
                GlStateManager._blendFunc(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.value);
                GlStateManager._alphaFunc(GL11.GL_GREATER, 0.0F);
                GlStateManager._enableBlend();
                GlStateManager._enableAlphaTest();
                break;
            case ADDITIVE:
                GlStateManager._blendFunc(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.DestFactor.ONE.value);
                GlStateManager._alphaFunc(GL11.GL_GREATER, 0.0F);
                GlStateManager._enableBlend();
                GlStateManager._enableAlphaTest();
                break;
        }
    }

    public void endGL()
    {
        switch (this)
        {
            case OPAQUE:
            case ALPHA:
            case BLEND:
            case ADDITIVE:
                GlStateManager._blendFunc(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.value);
                GlStateManager._disableBlend();
                GlStateManager._enableAlphaTest();
                GlStateManager._alphaFunc(GL11.GL_GREATER, 0.1F);
                break;
        }
    }

}