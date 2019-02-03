package net.minecraft.world.gen.layer;

public class GenLayerEdge extends GenLayer
{
    private final GenLayerEdge.Mode field_151627_c;

    public GenLayerEdge(long p_i45474_1_, GenLayer p_i45474_3_, GenLayerEdge.Mode p_i45474_4_)
    {
        super(p_i45474_1_);
        this.parent = p_i45474_3_;
        this.field_151627_c = p_i45474_4_;
    }

    /**
     * Returns a list of integer values generated by this layer. These may be interpreted as temperatures, rainfall
     * amounts, or biomeList[] indices based on the particular GenLayer subclass.
     */
    public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
    {
        switch (GenLayerEdge.SwitchMode.field_151642_a[this.field_151627_c.ordinal()])
        {
            case 1:
            default:
                return this.getIntsCoolWarm(areaX, areaY, areaWidth, areaHeight);

            case 2:
                return this.getIntsHeatIce(areaX, areaY, areaWidth, areaHeight);

            case 3:
                return this.getIntsSpecial(areaX, areaY, areaWidth, areaHeight);
        }
    }

    private int[] getIntsCoolWarm(int p_151626_1_, int p_151626_2_, int p_151626_3_, int p_151626_4_)
    {
        int var5 = p_151626_1_ - 1;
        int var6 = p_151626_2_ - 1;
        int var7 = 1 + p_151626_3_ + 1;
        int var8 = 1 + p_151626_4_ + 1;
        int[] var9 = this.parent.getInts(var5, var6, var7, var8);
        int[] var10 = IntCache.getIntCache(p_151626_3_ * p_151626_4_);

        for (int var11 = 0; var11 < p_151626_4_; ++var11)
        {
            for (int var12 = 0; var12 < p_151626_3_; ++var12)
            {
                this.initChunkSeed((long)(var12 + p_151626_1_), (long)(var11 + p_151626_2_));
                int var13 = var9[var12 + 1 + (var11 + 1) * var7];

                if (var13 == 1)
                {
                    int var14 = var9[var12 + 1 + (var11 + 1 - 1) * var7];
                    int var15 = var9[var12 + 1 + 1 + (var11 + 1) * var7];
                    int var16 = var9[var12 + 1 - 1 + (var11 + 1) * var7];
                    int var17 = var9[var12 + 1 + (var11 + 1 + 1) * var7];
                    boolean var18 = var14 == 3 || var15 == 3 || var16 == 3 || var17 == 3;
                    boolean var19 = var14 == 4 || var15 == 4 || var16 == 4 || var17 == 4;

                    if (var18 || var19)
                    {
                        var13 = 2;
                    }
                }

                var10[var12 + var11 * p_151626_3_] = var13;
            }
        }

        return var10;
    }

    private int[] getIntsHeatIce(int p_151624_1_, int p_151624_2_, int p_151624_3_, int p_151624_4_)
    {
        int var5 = p_151624_1_ - 1;
        int var6 = p_151624_2_ - 1;
        int var7 = 1 + p_151624_3_ + 1;
        int var8 = 1 + p_151624_4_ + 1;
        int[] var9 = this.parent.getInts(var5, var6, var7, var8);
        int[] var10 = IntCache.getIntCache(p_151624_3_ * p_151624_4_);

        for (int var11 = 0; var11 < p_151624_4_; ++var11)
        {
            for (int var12 = 0; var12 < p_151624_3_; ++var12)
            {
                int var13 = var9[var12 + 1 + (var11 + 1) * var7];

                if (var13 == 4)
                {
                    int var14 = var9[var12 + 1 + (var11 + 1 - 1) * var7];
                    int var15 = var9[var12 + 1 + 1 + (var11 + 1) * var7];
                    int var16 = var9[var12 + 1 - 1 + (var11 + 1) * var7];
                    int var17 = var9[var12 + 1 + (var11 + 1 + 1) * var7];
                    boolean var18 = var14 == 2 || var15 == 2 || var16 == 2 || var17 == 2;
                    boolean var19 = var14 == 1 || var15 == 1 || var16 == 1 || var17 == 1;

                    if (var19 || var18)
                    {
                        var13 = 3;
                    }
                }

                var10[var12 + var11 * p_151624_3_] = var13;
            }
        }

        return var10;
    }

    private int[] getIntsSpecial(int p_151625_1_, int p_151625_2_, int p_151625_3_, int p_151625_4_)
    {
        int[] var5 = this.parent.getInts(p_151625_1_, p_151625_2_, p_151625_3_, p_151625_4_);
        int[] var6 = IntCache.getIntCache(p_151625_3_ * p_151625_4_);

        for (int var7 = 0; var7 < p_151625_4_; ++var7)
        {
            for (int var8 = 0; var8 < p_151625_3_; ++var8)
            {
                this.initChunkSeed((long)(var8 + p_151625_1_), (long)(var7 + p_151625_2_));
                int var9 = var5[var8 + var7 * p_151625_3_];

                if (var9 != 0 && this.nextInt(13) == 0)
                {
                    var9 |= 1 + this.nextInt(15) << 8 & 3840;
                }

                var6[var8 + var7 * p_151625_3_] = var9;
            }
        }

        return var6;
    }

    public enum Mode
    {
        COOL_WARM("COOL_WARM", 0),
        HEAT_ICE("HEAT_ICE", 1),
        SPECIAL("SPECIAL", 2);

        private static final GenLayerEdge.Mode[] $VALUES = {COOL_WARM, HEAT_ICE, SPECIAL};

        Mode(String p_i45473_1_, int p_i45473_2_) {}
    }

    static final class SwitchMode
    {
        static final int[] field_151642_a = new int[GenLayerEdge.Mode.values().length];

        static
        {
            try
            {
                field_151642_a[GenLayerEdge.Mode.COOL_WARM.ordinal()] = 1;
            }
            catch (NoSuchFieldError var3)
            {
            }

            try
            {
                field_151642_a[GenLayerEdge.Mode.HEAT_ICE.ordinal()] = 2;
            }
            catch (NoSuchFieldError var2)
            {
            }

            try
            {
                field_151642_a[GenLayerEdge.Mode.SPECIAL.ordinal()] = 3;
            }
            catch (NoSuchFieldError var1)
            {
            }
        }
    }
}