package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;

public class EntityAIOpenDoor extends EntityAIDoorInteract
{
    boolean closeDoor;
    int closeDoorTemporisation;

    public EntityAIOpenDoor(EntityLiving p_i1644_1_, boolean p_i1644_2_)
    {
        super(p_i1644_1_);
        this.theEntity = p_i1644_1_;
        this.closeDoor = p_i1644_2_;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return this.closeDoor && this.closeDoorTemporisation > 0 && super.continueExecuting();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.closeDoorTemporisation = 20;
        this.doorBlock.func_150014_a(this.theEntity.worldObj, this.entityPosX, this.entityPosY, this.entityPosZ, true);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        if (this.closeDoor)
        {
            this.doorBlock.func_150014_a(this.theEntity.worldObj, this.entityPosX, this.entityPosY, this.entityPosZ, false);
        }
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        --this.closeDoorTemporisation;
        super.updateTask();
    }
}