package net.minecraft.tileentity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;

public class TileEntityPiston extends TileEntity
{
    private Block storedBlock;
    private int storedMetadata;
    private int storedOrientation;
    private boolean extending;
    private boolean shouldHeadBeRendered;
    private float progress;
    private float lastProgress;
    private final List pushedObjects = new ArrayList();

    public TileEntityPiston() {}

    public TileEntityPiston(Block p_i45444_1_, int p_i45444_2_, int p_i45444_3_, boolean p_i45444_4_, boolean p_i45444_5_)
    {
        this.storedBlock = p_i45444_1_;
        this.storedMetadata = p_i45444_2_;
        this.storedOrientation = p_i45444_3_;
        this.extending = p_i45444_4_;
        this.shouldHeadBeRendered = p_i45444_5_;
    }

    public Block getStoredBlockID()
    {
        return this.storedBlock;
    }

    public int getBlockMetadata()
    {
        return this.storedMetadata;
    }

    public boolean isExtending()
    {
        return this.extending;
    }

    public int getPistonOrientation()
    {
        return this.storedOrientation;
    }

    public boolean shouldPistonHeadBeRendered()
    {
        return this.shouldHeadBeRendered;
    }

    public float func_145860_a(float p_145860_1_)
    {
        if (p_145860_1_ > 1.0F)
        {
            p_145860_1_ = 1.0F;
        }

        return this.lastProgress + (this.progress - this.lastProgress) * p_145860_1_;
    }

    public float func_145865_b(float p_145865_1_)
    {
        return this.extending ? (this.func_145860_a(p_145865_1_) - 1.0F) * (float)Facing.offsetsXForSide[this.storedOrientation] : (1.0F - this.func_145860_a(p_145865_1_)) * (float)Facing.offsetsXForSide[this.storedOrientation];
    }

    public float func_145862_c(float p_145862_1_)
    {
        return this.extending ? (this.func_145860_a(p_145862_1_) - 1.0F) * (float)Facing.offsetsYForSide[this.storedOrientation] : (1.0F - this.func_145860_a(p_145862_1_)) * (float)Facing.offsetsYForSide[this.storedOrientation];
    }

    public float func_145859_d(float p_145859_1_)
    {
        return this.extending ? (this.func_145860_a(p_145859_1_) - 1.0F) * (float)Facing.offsetsZForSide[this.storedOrientation] : (1.0F - this.func_145860_a(p_145859_1_)) * (float)Facing.offsetsZForSide[this.storedOrientation];
    }

    private void func_145863_a(float p_145863_1_, float p_145863_2_)
    {
        if (this.extending)
        {
            p_145863_1_ = 1.0F - p_145863_1_;
        }
        else
        {
            --p_145863_1_;
        }

        AxisAlignedBB var3 = Blocks.piston_extension.func_149964_a(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.storedBlock, p_145863_1_, this.storedOrientation);

        if (var3 != null)
        {
            List var4 = this.worldObj.getEntitiesWithinAABBExcludingEntity((Entity)null, var3);

            if (!var4.isEmpty())
            {
                this.pushedObjects.addAll(var4);
                Iterator var5 = this.pushedObjects.iterator();

                while (var5.hasNext())
                {
                    Entity var6 = (Entity)var5.next();
                    var6.moveEntity((double)(p_145863_2_ * (float)Facing.offsetsXForSide[this.storedOrientation]), (double)(p_145863_2_ * (float)Facing.offsetsYForSide[this.storedOrientation]), (double)(p_145863_2_ * (float)Facing.offsetsZForSide[this.storedOrientation]));
                }

                this.pushedObjects.clear();
            }
        }
    }

    public void clearPistonTileEntity()
    {
        if (this.lastProgress < 1.0F && this.worldObj != null)
        {
            this.lastProgress = this.progress = 1.0F;
            this.worldObj.removeTileEntity(this.xCoord, this.yCoord, this.zCoord);
            this.invalidate();

            if (this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord) == Blocks.piston_extension)
            {
                this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, this.storedBlock, this.storedMetadata, 3);
                this.worldObj.notifyBlockOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.storedBlock);
            }
        }
    }

    public void updateEntity()
    {
        this.lastProgress = this.progress;

        if (this.lastProgress >= 1.0F)
        {
            this.func_145863_a(1.0F, 0.25F);
            this.worldObj.removeTileEntity(this.xCoord, this.yCoord, this.zCoord);
            this.invalidate();

            if (this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord) == Blocks.piston_extension)
            {
                this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, this.storedBlock, this.storedMetadata, 3);
                this.worldObj.notifyBlockOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.storedBlock);
            }
        }
        else
        {
            this.progress += 0.5F;

            if (this.progress >= 1.0F)
            {
                this.progress = 1.0F;
            }

            if (this.extending)
            {
                this.func_145863_a(this.progress, this.progress - this.lastProgress + 0.0625F);
            }
        }
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.storedBlock = Block.getBlockById(compound.getInteger("blockId"));
        this.storedMetadata = compound.getInteger("blockData");
        this.storedOrientation = compound.getInteger("facing");
        this.lastProgress = this.progress = compound.getFloat("progress");
        this.extending = compound.getBoolean("extending");
    }

    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("blockId", Block.getIdFromBlock(this.storedBlock));
        compound.setInteger("blockData", this.storedMetadata);
        compound.setInteger("facing", this.storedOrientation);
        compound.setFloat("progress", this.lastProgress);
        compound.setBoolean("extending", this.extending);
    }
}