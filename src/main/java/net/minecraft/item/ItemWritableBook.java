package net.minecraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class ItemWritableBook extends Item
{

    public ItemWritableBook()
    {
        this.setMaxStackSize(1);
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer player)
    {
        player.displayGUIBook(itemStackIn);
        return itemStackIn;
    }

    /**
     * If this function returns true (or the item is damageable), the ItemStack's NBT tag will be sent to the client.
     */
    public boolean getShareTag()
    {
        return true;
    }

    public static boolean validBookPageTagContents(NBTTagCompound p_150930_0_)
    {
        if (p_150930_0_ == null)
        {
            return false;
        }
        else if (!p_150930_0_.hasKey("pages", 9))
        {
            return false;
        }
        else
        {
            NBTTagList var1 = p_150930_0_.getTagList("pages", 8);

            for (int var2 = 0; var2 < var1.tagCount(); ++var2)
            {
                String var3 = var1.getStringTagAt(var2);

                if (var3 == null)
                {
                    return false;
                }

                if (var3.length() > 256)
                {
                    return false;
                }
            }

            return true;
        }
    }
}