package net.minecraft.nbt;

public class NBTSizeTracker
{
    public static final NBTSizeTracker INFINITE = new NBTSizeTracker(0L)
    {
        public void addSpaceRead(long size) {}
    };
    private final long spaceAllocated;
    private long spaceRead;

    public NBTSizeTracker(long size)
    {
        this.spaceAllocated = size;
    }

    public void addSpaceRead(long size)
    {
        this.spaceRead += size / 8L;

        if (this.spaceRead > this.spaceAllocated)
        {
            throw new RuntimeException("Tried to read NBT tag that was too big; tried to allocate: " + this.spaceRead + "bytes where max allowed: " + this.spaceAllocated);
        }
    }
}