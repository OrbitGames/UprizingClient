package net.minecraft.client.audio;

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class MusicTicker implements IUpdatePlayerListBox
{
    private final Random rand = new Random();
    private final Minecraft mc;
    private ISound currentMusic;
    private int timeUntilNextMusic = 100;
    private static final String __OBFID = "CL_00001138";

    public MusicTicker(Minecraft p_i45112_1_)
    {
        this.mc = p_i45112_1_;
    }

    /**
     * Updates the JList with a new model.
     */
    public void update()
    {
        MusicTicker.MusicType var1 = this.mc.getAmbientMusicType();

        if (this.currentMusic != null)
        {
            if (!var1.getMusicTickerLocation().equals(this.currentMusic.getSoundLocation()))
            {
                this.mc.getSoundHandler().stopSound(this.currentMusic);
                this.timeUntilNextMusic = MathHelper.getRandomIntegerInRange(this.rand, 0, var1.getMinDelay() / 2);
            }

            if (!this.mc.getSoundHandler().isSoundPlaying(this.currentMusic))
            {
                this.currentMusic = null;
                this.timeUntilNextMusic = Math.min(MathHelper.getRandomIntegerInRange(this.rand, var1.getMinDelay(), var1.getMaxDelay()), this.timeUntilNextMusic);
            }
        }

        if (this.currentMusic == null && this.timeUntilNextMusic-- <= 0)
        {
            this.currentMusic = PositionedSoundRecord.createPositionedSoundRecord(var1.getMusicTickerLocation());
            this.mc.getSoundHandler().playSound(this.currentMusic);
            this.timeUntilNextMusic = Integer.MAX_VALUE;
        }
    }

    public enum MusicType
    {
        MENU("MENU", 0, new ResourceLocation("minecraft:music.menu"), 20, 600),
        GAME("GAME", 1, new ResourceLocation("minecraft:music.game"), 12000, 24000),
        CREATIVE("CREATIVE", 2, new ResourceLocation("minecraft:music.game.creative"), 1200, 3600),
        CREDITS("CREDITS", 3, new ResourceLocation("minecraft:music.game.end.credits"), Integer.MAX_VALUE, Integer.MAX_VALUE),
        NETHER("NETHER", 4, new ResourceLocation("minecraft:music.game.nether"), 1200, 3600),
        END_BOSS("END_BOSS", 5, new ResourceLocation("minecraft:music.game.end.dragon"), 0, 0),
        END("END", 6, new ResourceLocation("minecraft:music.game.end"), 6000, 24000);
        private final ResourceLocation field_148645_h;
        private final int minDelay;
        private final int maxDelay;

        private static final MusicTicker.MusicType[] $VALUES = {MENU, GAME, CREATIVE, CREDITS, NETHER, END_BOSS, END};
        private static final String __OBFID = "CL_00001139";

        MusicType(String p_i45111_1_, int p_i45111_2_, ResourceLocation p_i45111_3_, int p_i45111_4_, int p_i45111_5_)
        {
            this.field_148645_h = p_i45111_3_;
            this.minDelay = p_i45111_4_;
            this.maxDelay = p_i45111_5_;
        }

        public ResourceLocation getMusicTickerLocation()
        {
            return this.field_148645_h;
        }

        public int getMinDelay()
        {
            return this.minDelay;
        }

        public int getMaxDelay()
        {
            return this.maxDelay;
        }
    }
}