package com.goodbird.salamanderlib.mclib.utils.resources;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.shadowed.eliotlash.mclib.utils.MathUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;

public class GifTexture extends Texture
{
    public static int globalTick = 0;
    public static int entityTick = -1;

    public static boolean tried = false;
    public static Field fieldMultiTex = null;

    public ResourceLocation base;
    public ResourceLocation[] frames;
    public int[] delays;

    public int duration;

    public static void bindTexture(ResourceLocation location, int ticks, float partialTicks)
    {
        TextureManager textures = Minecraft.getInstance().textureManager;

        if (location.getPath().endsWith("gif"))
        {
            Texture object = textures.getTexture(location);

            if (object instanceof GifTexture)
            {
                GifTexture texture = (GifTexture) object;

                location = texture.getFrame(ticks, partialTicks);
            }
        }

        textures.bind(location);
    }

    public static void updateTick()
    {
        globalTick += 1;
    }

    public GifTexture(ResourceLocation texture, int[] delays, ResourceLocation[] frames)
    {
        this.base = texture;
        this.delays = Arrays.copyOf(delays, delays.length);
        this.frames = frames;
    }

    public void calculateDuration()
    {
        this.duration = 0;

        for (int delay : this.delays)
        {
            this.duration += delay;
        }
    }
    @Override
    public int getId() {
        Minecraft mc = Minecraft.getInstance();
        TextureManager textures = mc.textureManager;
        ResourceLocation rl = this.getFrame(entityTick > -1 ? entityTick : globalTick, mc.getFrameTime());

        textures.bind(rl);

        Texture texture = textures.getTexture(rl);

        this.updateMultiTex(texture);

        return texture.getId();
    }

    @Override
    public void releaseId()
    {}

    public ResourceLocation getFrame(int ticks, float partialTicks)
    {
        int tick = (int) ((ticks + partialTicks) * 5 % this.duration);

        int duration = 0;
        int index = 0;

        for (int delay : this.delays)
        {
            duration += delay;

            if (tick < duration)
            {
                break;
            }

            index++;
        }

        index = MathUtils.clamp(index, 0, this.frames.length - 1);

        return this.frames[index];
    }

    private void updateMultiTex(Texture texture)
    {
        if (!tried)
        {
            try
            {
                fieldMultiTex = Texture.class.getField("multiTex");
            }
            catch (NoSuchFieldException | SecurityException e)
            {
                fieldMultiTex = null;
            }

            tried = true;
        }

        if (texture instanceof Texture && fieldMultiTex != null)
        {
            try
            {
                Object obj = fieldMultiTex.get(texture);

                fieldMultiTex.set(this, obj);
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {}
        }
    }

    @Override
    public void load(IResourceManager p_195413_1_) throws IOException {

    }
}
