package com.goodbird.salamanderlib.particles.components.expiration;

import com.goodbird.salamanderlib.particles.components.BedrockComponentBase;
import com.goodbird.salamanderlib.particles.emitter.BedrockEmitter;
import com.goodbird.salamanderlib.particles.emitter.BedrockParticle;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib3.core.molang.MolangException;
import com.goodbird.salamanderlib.molang.MolangParser;

import javax.vecmath.Vector3d;
import java.util.ArrayList;
import java.util.List;

public abstract class BedrockComponentExpireBlocks extends BedrockComponentBase
{
    public List<Block> blocks = new ArrayList<Block>();

    private BlockPos.Mutable pos = new BlockPos.Mutable();

    @Override
    public BedrockComponentBase fromJson(JsonElement element, MolangParser parser) throws MolangException
    {
        if (element.isJsonArray())
        {
            for (JsonElement value : element.getAsJsonArray())
            {
                ResourceLocation location = new ResourceLocation(value.getAsString());
                Block block = ForgeRegistries.BLOCKS.getValue(location);

                if (block != null)
                {
                    this.blocks.add(block);
                }
            }
        }

        return super.fromJson(element, parser);
    }

    @Override
    public JsonElement toJson()
    {
        JsonArray array = new JsonArray();

        for (Block block : this.blocks)
        {
            ResourceLocation rl = ForgeRegistries.BLOCKS.getKey(block);

            if (rl != null)
            {
                array.add(rl.toString());
            }
        }

        return array;
    }

    public Block getBlock(BedrockEmitter emitter, BedrockParticle particle)
    {
        if (emitter.world == null)
        {
            return Blocks.AIR;
        }

        Vector3d position = particle.getGlobalPosition(emitter);

        this.pos.set(position.getX(), position.getY(), position.getZ());

        return emitter.world.getBlockState(this.pos).getBlock();
    }
}