package net.minecraft.util;

public class Direction
{
    public static final int[] offsetX = {0, -1, 0, 1};
    public static final int[] offsetZ = {1, 0, -1, 0};
    public static final String[] directions = {"SOUTH", "WEST", "NORTH", "EAST"};

    /** Maps a Direction value (2D) to a Facing value (3D). */
    public static final int[] directionToFacing = {3, 4, 2, 5};

    /** Maps a Facing value (3D) to a Direction value (2D). */
    public static final int[] facingToDirection = { -1, -1, 2, 0, 1, 3};

    /** Maps a direction to that opposite of it. */
    public static final int[] rotateOpposite = {2, 3, 0, 1};

    /** Maps a direction to that to the right of it. */
    public static final int[] rotateRight = {1, 2, 3, 0};

    /** Maps a direction to that to the left of it. */
    public static final int[] rotateLeft = {3, 0, 1, 2};
    public static final int[][] bedDirection = {{1, 0, 3, 2, 5, 4}, {1, 0, 5, 4, 2, 3}, {1, 0, 2, 3, 4, 5}, {1, 0, 4, 5, 3, 2}};
    private static final String __OBFID = "CL_00001506";

    /**
     * Returns the movement direction from a velocity vector.
     */
    public static int getMovementDirection(double p_82372_0_, double p_82372_2_)
    {
        return MathHelper.abs((float)p_82372_0_) > MathHelper.abs((float)p_82372_2_) ? (p_82372_0_ > 0.0D ? 1 : 3) : (p_82372_2_ > 0.0D ? 2 : 0);
    }
}