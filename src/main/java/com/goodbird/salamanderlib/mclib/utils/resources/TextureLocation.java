package com.goodbird.salamanderlib.mclib.utils.resources;

import com.goodbird.salamanderlib.mixin.impl.IResLoc;
import net.minecraft.util.ResourceLocation;

/**
 * Texture location
 *
 * A hack class that allows to use uppercase characters in the path 1.11.2 and
 * up.
 */
public class TextureLocation extends ResourceLocation
{
    public TextureLocation(String domain, String path)
    {
        super(domain, path);

        this.set(domain, path);
    }

    public TextureLocation(String string)
    {
        super(string);

        this.set(string);
    }

    public void set(String location)
    {
        String[] split = location.split(":");
        String domain = split.length > 0 ? split[0] : "minecraft";
        String path = split.length > 1 ? split[1] : "";

        this.set(domain, path);
    }

    public void set(String domain, String path)
    {
        ((IResLoc)this).setNamespace(domain);
        ((IResLoc)this).setPath(path);
    }
}
