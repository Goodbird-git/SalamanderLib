package com.goodbird.salamanderlib.example.registry;

import com.goodbird.salamanderlib.example.SalamanderLib;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.example.GeckoLibMod;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SalamanderLib.MODID);

    public static final RegistryObject<BlockItem> MAGIC_TORCH = ITEMS.register("magictorch",
            () -> new BlockItem(BlockRegistry.MAGIC_TORCH_BLOCK.get(), new Item.Properties().tab(GeckoLibMod.geckolibItemGroup)));
}
