package com.goodbird.salamanderlib.mclib.utils.resources;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;

public class FilteredResourceLocation implements IWritableLocation<FilteredResourceLocation>
{
    public static final int DEFAULT_COLOR = 0xffffffff;

    public ResourceLocation path;

    public boolean autoSize = true;
    public int sizeW;
    public int sizeH;

    public int color = DEFAULT_COLOR;
    public float scale = 1F;
    public boolean scaleToLargest;
    public int shiftX;
    public int shiftY;

    /* Filters */
    public int pixelate = 1;
    public boolean erase;

    public static FilteredResourceLocation from(INBT base)
    {
        try
        {
            FilteredResourceLocation location = new FilteredResourceLocation();

            location.fromNbt(base);

            return location;
        }
        catch (Exception e)
        {}

        return null;
    }

    public static FilteredResourceLocation from(JsonElement element)
    {
        try
        {
            FilteredResourceLocation location = new FilteredResourceLocation();

            location.fromJson(element);

            return location;
        }
        catch (Exception e)
        {}

        return null;
    }

    public FilteredResourceLocation()
    {}

    public FilteredResourceLocation(ResourceLocation path)
    {
        this.path = path;
    }

    public int getWidth(int width)
    {
        if (!this.autoSize && this.sizeW > 0)
        {
            return this.sizeW;
        }

        return width;
    }

    public int getHeight(int height)
    {
        if (!this.autoSize && this.sizeH > 0)
        {
            return this.sizeH;
        }

        return height;
    }

    @Override
    public String toString()
    {
        return this.path == null ? "" : this.path.toString();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (super.equals(obj))
        {
            return true;
        }

        if (obj instanceof FilteredResourceLocation)
        {
            FilteredResourceLocation frl = (FilteredResourceLocation) obj;

            return Objects.equals(this.path, frl.path)
                && this.autoSize == frl.autoSize
                && this.sizeW == frl.sizeW
                && this.sizeH == frl.sizeH
                && this.scaleToLargest == frl.scaleToLargest
                && this.color == frl.color
                && this.scale == frl.scale
                && this.shiftX == frl.shiftX
                && this.shiftY == frl.shiftY
                && this.pixelate == frl.pixelate
                && this.erase == frl.erase;
        }

        return false;
    }

    @Override
    public int hashCode()
    {
        int hashCode = this.path.hashCode();

        hashCode = 31 * hashCode + (this.autoSize ? 1 : 0);
        hashCode = 31 * hashCode + this.sizeW;
        hashCode = 31 * hashCode + this.sizeH;
        hashCode = 31 * hashCode + (this.scaleToLargest ? 1 : 0);
        hashCode = 31 * hashCode + this.color;
        hashCode = 31 * hashCode + (int) (this.scale * 1000);
        hashCode = 31 * hashCode + this.shiftX;
        hashCode = 31 * hashCode + this.shiftY;
        hashCode = 31 * hashCode + this.pixelate;
        hashCode = 31 * hashCode + (this.erase ? 1 : 0);

        return hashCode;
    }

    public boolean isDefault()
    {
        return (this.autoSize || (this.sizeW == 0 && this.sizeH == 0)) && this.color == DEFAULT_COLOR && !this.scaleToLargest && this.scale == 1F && this.shiftX == 0 && this.shiftY == 0 && this.pixelate <= 1 && !this.erase;
    }

    @Override
    public void fromNbt(INBT nbt) throws Exception
    {
        if (nbt instanceof StringNBT)
        {
            this.path = RLUtils.create(nbt);

            return;
        }

        CompoundNBT tag = (CompoundNBT) nbt;

        this.path = RLUtils.create(tag.getString("Path"));

        if (tag.contains("Color"))
        {
            this.color = tag.getInt("Color");
        }

        if (tag.contains("Scale"))
        {
            this.scale = tag.getFloat("Scale");
        }

        if (tag.contains("ScaleToLargest"))
        {
            this.scaleToLargest = tag.getBoolean("ScaleToLargest");
        }

        if (tag.contains("ShiftX"))
        {
            this.shiftX = tag.getInt("ShiftX");
        }

        if (tag.contains("ShiftY"))
        {
            this.shiftY = tag.getInt("ShiftY");
        }

        if (tag.contains("Pixelate"))
        {
            this.pixelate = tag.getInt("Pixelate");
        }

        if (tag.contains("Erase"))
        {
            this.erase = tag.getBoolean("Erase");
        }

        if (tag.contains("AutoSize"))
        {
            this.autoSize = tag.getBoolean("AutoSize");
        }

        if (tag.contains("SizeW"))
        {
            this.sizeW = tag.getInt("SizeW");
        }

        if (tag.contains("SizeH"))
        {
            this.sizeH = tag.getInt("SizeH");
        }
    }

    @Override
    public void fromJson(JsonElement element) throws Exception
    {
        if (element.isJsonPrimitive())
        {
            this.path = RLUtils.create(element);

            return;
        }

        JsonObject object = element.getAsJsonObject();

        this.path = RLUtils.create(object.get("path").getAsString());

        if (object.has("color"))
        {
            this.color = object.get("color").getAsInt();
        }

        if (object.has("scale"))
        {
            this.scale = object.get("scale").getAsFloat();
        }

        if (object.has("scaleToLargest"))
        {
            this.scaleToLargest = object.get("scaleToLargest").getAsBoolean();
        }

        if (object.has("shiftX"))
        {
            this.shiftX = object.get("shiftX").getAsInt();
        }

        if (object.has("shiftY"))
        {
            this.shiftY = object.get("shiftY").getAsInt();
        }

        if (object.has("pixelate"))
        {
            this.pixelate = object.get("pixelate").getAsInt();
        }

        if (object.has("erase"))
        {
            this.erase = object.get("erase").getAsBoolean();
        }

        if (object.has("autoSize"))
        {
            this.autoSize = object.get("autoSize").getAsBoolean();
        }

        if (object.has("sizeW"))
        {
            this.sizeW = object.get("sizeW").getAsInt();
        }

        if (object.has("sizeH"))
        {
            this.sizeH = object.get("sizeH").getAsInt();
        }
    }

    @Override
    public INBT writeNbt()
    {
        CompoundNBT tag = new CompoundNBT();

        tag.putString("Path", this.toString());

        if (this.color != DEFAULT_COLOR) tag.putInt("Color", this.color);
        if (this.scale != 1) tag.putFloat("Scale", this.scale);
        if (this.scaleToLargest) tag.putBoolean("ScaleToLargest", this.scaleToLargest);
        if (this.shiftX != 0) tag.putInt("ShiftX", this.shiftX);
        if (this.shiftY != 0) tag.putInt("ShiftY", this.shiftY);
        if (this.pixelate > 1) tag.putInt("Pixelate", this.pixelate);
        if (this.erase) tag.putBoolean("Erase", this.erase);
        if (!this.autoSize) tag.putBoolean("AutoSize", this.autoSize);
        if (this.sizeW > 0) tag.putInt("SizeW", this.sizeW);
        if (this.sizeH > 0) tag.putInt("SizeH", this.sizeH);

        return tag;
    }

    @Override
    public JsonElement writeJson()
    {
        JsonObject object = new JsonObject();

        object.addProperty("path", this.toString());

        if (this.color != DEFAULT_COLOR) object.addProperty("color", this.color);
        if (this.scale != 1) object.addProperty("scale", this.scale);
        if (this.scaleToLargest) object.addProperty("scaleToLargest", this.scaleToLargest);
        if (this.shiftX != 0) object.addProperty("shiftX", this.shiftX);
        if (this.shiftY != 0) object.addProperty("shiftY", this.shiftY);
        if (this.pixelate > 1) object.addProperty("pixelate", this.pixelate);
        if (this.erase) object.addProperty("erase", this.erase);
        if (!this.autoSize) object.addProperty("autoSize", this.autoSize);
        if (this.sizeW > 0) object.addProperty("sizeW", this.sizeW);
        if (this.sizeH > 0) object.addProperty("sizeH", this.sizeH);

        return object;
    }

    @Override
    public ResourceLocation clone()
    {
        return RLUtils.clone(this.path);
    }

    @Override
    public FilteredResourceLocation copy()
    {
        return FilteredResourceLocation.from(this.writeNbt());
    }
}
