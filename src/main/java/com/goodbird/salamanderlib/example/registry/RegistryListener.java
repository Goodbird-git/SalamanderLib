package com.goodbird.salamanderlib.example.registry;

import com.goodbird.salamanderlib.example.SalamanderLib;
import com.goodbird.salamanderlib.example.block.MagicTorchBlock;
import com.goodbird.salamanderlib.example.entity.ParticleExampleEntity;
import com.goodbird.salamanderlib.example.tile.TileMagicTorch;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import software.bernie.example.GeckoLibMod;
import software.bernie.example.registry.ItemRegistry;

@Mod.EventBusSubscriber(modid=SalamanderLib.MODID)
public class RegistryListener {
    private static Block magicTorch = new MagicTorchBlock();
    private static Item magicTorchItem;

    @SubscribeEvent
    public static void onRegisterBlocks(RegistryEvent.Register<Block> event) {
        registerBlock(event.getRegistry(), magicTorch, "magictorch");
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> evt) {
        magicTorchItem = new ItemBlock(magicTorch);
        registerItem(evt.getRegistry(), magicTorchItem, "magictorch");
    }


    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
        int id = 0;
        event.getRegistry().register(EntityEntryBuilder.create().entity(ParticleExampleEntity.class).name("ParticleExample").id(new ResourceLocation(SalamanderLib.MODID, "particleexample"), id++).tracker(160, 2, false).build());

        GameRegistry.registerTileEntity(TileMagicTorch.class, new ResourceLocation(SalamanderLib.MODID, "magictorchtile"));
    }


    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onModelRegistry(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(magicTorchItem, 0, new ModelResourceLocation("salamanderlib:magictorch", "inventory"));
    }

    public static void registerBlock(IForgeRegistry<Block> blockRegistry, Block block, String name) {
        registerBlock(blockRegistry, block, new ResourceLocation(SalamanderLib.MODID, name));
    }

    public static void registerBlock(IForgeRegistry<Block> blockRegistry, Block block, ResourceLocation name) {
        blockRegistry.register(block.setRegistryName(name).setUnlocalizedName(name.toString().replace(":", ".")));
    }

    public static <T extends Item> T registerItem(IForgeRegistry<Item> itemRegistry, T item, String name) {
        registerItem(itemRegistry, item, new ResourceLocation(SalamanderLib.MODID, name));
        return item;
    }

    public static <T extends Item> T registerItem(IForgeRegistry<Item> itemRegistry, T item, ResourceLocation name) {
        itemRegistry.register(((Item)item.setRegistryName(name)).setUnlocalizedName(name.toString().replace(":", ".")));
        return item;
    }
}
