package com.goodbird.salamanderlib.example.registry;

import com.goodbird.salamanderlib.example.SalamanderLib;
import com.goodbird.salamanderlib.example.tile.TileMagicTorch;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileRegistry {
    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister
            .create(ForgeRegistries.TILE_ENTITIES, SalamanderLib.MODID);

    public static final RegistryObject<TileEntityType<TileMagicTorch>> MAGIC_TORCH_TILE = TILES.register("magictorch",
            () -> TileEntityType.Builder.of(TileMagicTorch::new, BlockRegistry.MAGIC_TORCH_BLOCK.get()).build(null));
}
