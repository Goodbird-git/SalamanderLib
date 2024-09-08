package com.goodbird.salamanderlib.mclib.utils;

import java.util.Arrays;

import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.HandSide;
import net.minecraft.world.World;

/**
 * Dummy entity
 *
 * This class is used in model editor as a player substitution for the model
 * methods.
 */
public class DummyEntity extends LivingEntity
{
    private final ItemStack[] held;
    public ItemStack right;
    public ItemStack left;

    public DummyEntity(World worldIn)
    {
        super(null, worldIn);

        this.right = new ItemStack(Items.DIAMOND_SWORD);
        this.left = new ItemStack(Items.GOLDEN_SWORD);
        this.held = new ItemStack[] {ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY};
    }

    public void setItems(ItemStack left, ItemStack right)
    {
        this.left = left;
        this.right = right;
    }

    public void toggleItems(boolean toggle)
    {
        int main = EquipmentSlotType.MAINHAND.getIndex();
        int off = EquipmentSlotType.OFFHAND.getIndex();

        if (toggle)
        {
            this.held[main] = this.right;
            this.held[off] = this.left;
        }
        else
        {
            this.held[main] = this.held[off] = ItemStack.EMPTY;
        }
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Arrays.asList(this.held);
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlotType slotIn)
    {
        return this.held[slotIn.getIndex()];
    }

    @Override
    public void setItemSlot(EquipmentSlotType slotIn, ItemStack stack) {
        this.held[slotIn.getIndex()] = stack;
    }

    @Override
    public HandSide getMainArm() {
        return HandSide.RIGHT;
    }
}