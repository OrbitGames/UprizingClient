package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockLeaves extends BlockLeavesBase
{
    int[] field_150128_a;
    protected int field_150127_b;
    protected IIcon[][] field_150129_M = new IIcon[2][];

    public BlockLeaves()
    {
        super(Material.leaves, false);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setHardness(0.2F);
        this.setLightOpacity(1);
        this.setStepSound(soundTypeGrass);
    }

    public int getBlockColor()
    {
        double var1 = 0.5D;
        double var3 = 1.0D;
        return ColorizerFoliage.getFoliageColor(var1, var3);
    }

    /**
     * Returns the color this block should be rendered. Used by leaves.
     */
    public int getRenderColor(int meta)
    {
        return ColorizerFoliage.getFoliageColorBasic();
    }

    /**
     * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called
     * when first determining what to render.
     */
    public int colorMultiplier(IBlockAccess worldIn, int x, int y, int z)
    {
        int var5 = 0;
        int var6 = 0;
        int var7 = 0;

        for (int var8 = -1; var8 <= 1; ++var8)
        {
            for (int var9 = -1; var9 <= 1; ++var9)
            {
                int var10 = worldIn.getBiomeGenForCoords(x + var9, z + var8).getBiomeFoliageColor(x + var9, y, z + var8);
                var5 += (var10 & 16711680) >> 16;
                var6 += (var10 & 65280) >> 8;
                var7 += var10 & 255;
            }
        }

        return (var5 / 9 & 255) << 16 | (var6 / 9 & 255) << 8 | var7 / 9 & 255;
    }

    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta)
    {
        byte var7 = 1;
        int var8 = var7 + 1;

        if (worldIn.checkChunksExist(x - var8, y - var8, z - var8, x + var8, y + var8, z + var8))
        {
            for (int var9 = -var7; var9 <= var7; ++var9)
            {
                for (int var10 = -var7; var10 <= var7; ++var10)
                {
                    for (int var11 = -var7; var11 <= var7; ++var11)
                    {
                        if (worldIn.getBlock(x + var9, y + var10, z + var11).getMaterial() == Material.leaves)
                        {
                            int var12 = worldIn.getBlockMetadata(x + var9, y + var10, z + var11);
                            worldIn.setBlockMetadataWithNotify(x + var9, y + var10, z + var11, var12 | 8, 4);
                        }
                    }
                }
            }
        }
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World worldIn, int x, int y, int z, Random random)
    {
        if (!worldIn.isClient)
        {
            int var6 = worldIn.getBlockMetadata(x, y, z);

            if ((var6 & 8) != 0 && (var6 & 4) == 0)
            {
                byte var7 = 4;
                int var8 = var7 + 1;
                byte var9 = 32;
                int var10 = var9 * var9;
                int var11 = var9 / 2;

                if (this.field_150128_a == null)
                {
                    this.field_150128_a = new int[var9 * var9 * var9];
                }

                int var12;

                if (worldIn.checkChunksExist(x - var8, y - var8, z - var8, x + var8, y + var8, z + var8))
                {
                    int var13;
                    int var14;

                    for (var12 = -var7; var12 <= var7; ++var12)
                    {
                        for (var13 = -var7; var13 <= var7; ++var13)
                        {
                            for (var14 = -var7; var14 <= var7; ++var14)
                            {
                                Block var15 = worldIn.getBlock(x + var12, y + var13, z + var14);

                                if (var15 != Blocks.log && var15 != Blocks.log2)
                                {
                                    if (var15.getMaterial() == Material.leaves)
                                    {
                                        this.field_150128_a[(var12 + var11) * var10 + (var13 + var11) * var9 + var14 + var11] = -2;
                                    }
                                    else
                                    {
                                        this.field_150128_a[(var12 + var11) * var10 + (var13 + var11) * var9 + var14 + var11] = -1;
                                    }
                                }
                                else
                                {
                                    this.field_150128_a[(var12 + var11) * var10 + (var13 + var11) * var9 + var14 + var11] = 0;
                                }
                            }
                        }
                    }

                    for (var12 = 1; var12 <= 4; ++var12)
                    {
                        for (var13 = -var7; var13 <= var7; ++var13)
                        {
                            for (var14 = -var7; var14 <= var7; ++var14)
                            {
                                for (int var16 = -var7; var16 <= var7; ++var16)
                                {
                                    if (this.field_150128_a[(var13 + var11) * var10 + (var14 + var11) * var9 + var16 + var11] == var12 - 1)
                                    {
                                        if (this.field_150128_a[(var13 + var11 - 1) * var10 + (var14 + var11) * var9 + var16 + var11] == -2)
                                        {
                                            this.field_150128_a[(var13 + var11 - 1) * var10 + (var14 + var11) * var9 + var16 + var11] = var12;
                                        }

                                        if (this.field_150128_a[(var13 + var11 + 1) * var10 + (var14 + var11) * var9 + var16 + var11] == -2)
                                        {
                                            this.field_150128_a[(var13 + var11 + 1) * var10 + (var14 + var11) * var9 + var16 + var11] = var12;
                                        }

                                        if (this.field_150128_a[(var13 + var11) * var10 + (var14 + var11 - 1) * var9 + var16 + var11] == -2)
                                        {
                                            this.field_150128_a[(var13 + var11) * var10 + (var14 + var11 - 1) * var9 + var16 + var11] = var12;
                                        }

                                        if (this.field_150128_a[(var13 + var11) * var10 + (var14 + var11 + 1) * var9 + var16 + var11] == -2)
                                        {
                                            this.field_150128_a[(var13 + var11) * var10 + (var14 + var11 + 1) * var9 + var16 + var11] = var12;
                                        }

                                        if (this.field_150128_a[(var13 + var11) * var10 + (var14 + var11) * var9 + (var16 + var11 - 1)] == -2)
                                        {
                                            this.field_150128_a[(var13 + var11) * var10 + (var14 + var11) * var9 + (var16 + var11 - 1)] = var12;
                                        }

                                        if (this.field_150128_a[(var13 + var11) * var10 + (var14 + var11) * var9 + var16 + var11 + 1] == -2)
                                        {
                                            this.field_150128_a[(var13 + var11) * var10 + (var14 + var11) * var9 + var16 + var11 + 1] = var12;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                var12 = this.field_150128_a[var11 * var10 + var11 * var9 + var11];

                if (var12 >= 0)
                {
                    worldIn.setBlockMetadataWithNotify(x, y, z, var6 & -9, 4);
                }
                else
                {
                    this.removeLeaves(worldIn, x, y, z);
                }
            }
        }
    }

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    public void randomDisplayTick(World worldIn, int x, int y, int z, Random random)
    {
        if (worldIn.canLightningStrikeAt(x, y + 1, z) && !World.doesBlockHaveSolidTopSurface(worldIn, x, y - 1, z) && random.nextInt(15) == 1)
        {
            double var6 = (double)((float)x + random.nextFloat());
            double var8 = (double)y - 0.05D;
            double var10 = (double)((float)z + random.nextFloat());
            worldIn.spawnParticle("dripWater", var6, var8, var10, 0.0D, 0.0D, 0.0D);
        }
    }

    private void removeLeaves(World p_150126_1_, int p_150126_2_, int p_150126_3_, int p_150126_4_)
    {
        this.dropBlockAsItem(p_150126_1_, p_150126_2_, p_150126_3_, p_150126_4_, p_150126_1_.getBlockMetadata(p_150126_2_, p_150126_3_, p_150126_4_), 0);
        p_150126_1_.setBlockToAir(p_150126_2_, p_150126_3_, p_150126_4_);
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random random)
    {
        return random.nextInt(20) == 0 ? 1 : 0;
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return Item.getItemFromBlock(Blocks.sapling);
    }

    /**
     * Drops the block items with a specified chance of dropping the specified items
     */
    public void dropBlockAsItemWithChance(World worldIn, int x, int y, int z, int meta, float chance, int fortune)
    {
        if (!worldIn.isClient)
        {
            int var8 = this.func_150123_b(meta);

            if (fortune > 0)
            {
                var8 -= 2 << fortune;

                if (var8 < 10)
                {
                    var8 = 10;
                }
            }

            if (worldIn.rand.nextInt(var8) == 0)
            {
                Item var9 = this.getItemDropped(meta, worldIn.rand, fortune);
                this.dropBlockAsItem_do(worldIn, x, y, z, new ItemStack(var9, 1, this.damageDropped(meta)));
            }

            var8 = 200;

            if (fortune > 0)
            {
                var8 -= 10 << fortune;

                if (var8 < 40)
                {
                    var8 = 40;
                }
            }

            this.func_150124_c(worldIn, x, y, z, meta, var8);
        }
    }

    protected void func_150124_c(World p_150124_1_, int p_150124_2_, int p_150124_3_, int p_150124_4_, int p_150124_5_, int p_150124_6_) {}

    protected int func_150123_b(int p_150123_1_)
    {
        return 20;
    }

    public void harvestBlock(World worldIn, EntityPlayer player, int x, int y, int z, int meta)
    {
        if (!worldIn.isClient && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == Items.shears)
        {
            player.addStat(StatList.mineBlockStatArray[Block.getIdFromBlock(this)], 1);
            this.dropBlockAsItem_do(worldIn, x, y, z, new ItemStack(Item.getItemFromBlock(this), 1, meta & 3));
        }
        else
        {
            super.harvestBlock(worldIn, player, x, y, z, meta);
        }
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int meta)
    {
        return meta & 3;
    }

    public boolean isOpaqueCube()
    {
        return !this.field_150121_P;
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    public abstract IIcon getIcon(int side, int meta);

    public void setGraphicsLevel(boolean p_150122_1_)
    {
        this.field_150121_P = p_150122_1_;
        this.field_150127_b = p_150122_1_ ? 0 : 1;
    }

    /**
     * Returns an item stack containing a single instance of the current block type. 'i' is the block's subtype/damage
     * and is ignored for blocks which do not support subtypes. Blocks which cannot be harvested should return null.
     */
    protected ItemStack createStackedBlock(int meta)
    {
        return new ItemStack(Item.getItemFromBlock(this), 1, meta & 3);
    }

    public abstract String[] func_150125_e();
}