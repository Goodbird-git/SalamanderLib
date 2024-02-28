package com.goodbird.salamanderlib.example.block;

import com.goodbird.salamanderlib.example.tile.TileMagicTorch;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;
import software.bernie.example.GeckoLibMod;
import software.bernie.example.block.tile.BotariumTileEntity;

import javax.annotation.Nullable;

public class MagicTorchBlock extends Block implements ITileEntityProvider {
    public MagicTorchBlock() {
        super(Material.ROCK);
        this.setCreativeTab(GeckoLibMod.getGeckolibItemGroup());
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileMagicTorch();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }


    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }
}