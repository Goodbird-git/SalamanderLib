package com.goodbird.salamanderlib.example.registry;

import com.goodbird.salamanderlib.example.SalamanderLib;
import com.goodbird.salamanderlib.example.block.MagicTorchBlock;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
            SalamanderLib.MODID);

    public static final RegistryObject<MagicTorchBlock> MAGIC_TORCH_BLOCK = BLOCKS.register("magictorch",
            MagicTorchBlock::new);
}
