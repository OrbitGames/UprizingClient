package net.minecraft.client.gui;

import com.google.common.base.Charsets;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import java.awt.image.BufferedImage;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import javax.imageio.ImageIO;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import uprizing.util.Constants;

public class ServerListEntryNormal implements GuiListExtended.IGuiListEntry {

    private static final Logger logger = LogManager.getLogger();
    private static final ThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(5, new ThreadFactoryBuilder().setNameFormat("Server Pinger #%d").setDaemon(true).build());

    private final GuiMultiplayer guiMultiplayer;
    private final Minecraft minecraft;
    @Getter private final ServerData serverData;
    private long lastClick;
    private String iconData;
    private DynamicTexture field_148305_h;
    private final ResourceLocation field_148306_i;

    protected ServerListEntryNormal(GuiMultiplayer p_i45048_1_, ServerData p_i45048_2_) {
        this.guiMultiplayer = p_i45048_1_;
        this.serverData = p_i45048_2_;
        this.minecraft = Minecraft.getInstance();
        this.field_148306_i = new ResourceLocation("servers/" + p_i45048_2_.serverIP + "/icon");
        this.field_148305_h = (DynamicTexture) this.minecraft.getTextureManager().getTexture(this.field_148306_i);
    }

    protected int getProtocol() {
        return Constants.NOTCHIAN_PROTOCOL;
    }

    public boolean isMovable() {
        return true;
    }

    public void drawEntry(int p_148279_1_, int x, int y, int p_148279_4_, int p_148279_5_, Tessellator p_148279_6_, int p_148279_7_, int p_148279_8_, boolean p_148279_9_) {
        if (!serverData.field_78841_f) {
            serverData.field_78841_f = true;
            serverData.pingToServer = -2L;
            serverData.serverMOTD = "";
            serverData.populationInfo = "";

            executor.submit(new Runnable() {
                public void run() {
                    try {
                        ServerListEntryNormal.this.guiMultiplayer.getOldServerPinger().ping(ServerListEntryNormal.this.serverData, getProtocol());
                    } catch (UnknownHostException var2) {
                        ServerListEntryNormal.this.serverData.pingToServer = -1L;
                        ServerListEntryNormal.this.serverData.serverMOTD = EnumChatFormatting.DARK_RED + "Can\'t resolve hostname";
                    } catch (Exception var3) {
                        ServerListEntryNormal.this.serverData.pingToServer = -1L;
                        ServerListEntryNormal.this.serverData.serverMOTD = EnumChatFormatting.DARK_RED + "Can\'t connect to server.";
                    }
                }
            });
        }

        boolean var10 = serverData.version > 5;
        boolean var11 = serverData.version < 5;
        boolean var12 = serverData.version != Constants.ALGERIAN_PROTOCOL && (var10 || var11);

        minecraft.fontRenderer.drawString(serverData.serverName, x + 32 + 3, y + 1, 16777215);
        List var13 = minecraft.fontRenderer.listFormattedStringToWidth(this.serverData.serverMOTD, p_148279_4_ - 32 - 2);

        for (int var14 = 0; var14 < Math.min(var13.size(), 2); ++var14) {
            minecraft.fontRenderer.drawString((String) var13.get(var14), x + 32 + 3, y + 12 + this.minecraft.fontRenderer.FONT_HEIGHT * var14, 8421504);
        }

        String var22 = var12 ? EnumChatFormatting.DARK_RED + this.serverData.gameVersion : this.serverData.populationInfo;
        int var15 = this.minecraft.fontRenderer.getStringWidth(var22);

        minecraft.fontRenderer.drawString(var22, x + p_148279_4_ - var15 - 15 - 2, y + 1, 8421504);

        byte var16 = 0;
        String var18 = null;
        int var17;
        String var19;

        if (var12) {
            var17 = 5;
            var19 = var10 ? "Client out of date!" : "Server out of date!";
            var18 = serverData.playerList;
        } else if (serverData.field_78841_f && this.serverData.pingToServer != -2L) {
            if (serverData.pingToServer < 0L) {
                var17 = 5;
            } else if (serverData.pingToServer < 150L) {
                var17 = 0;
            } else if (serverData.pingToServer < 300L) {
                var17 = 1;
            } else if (serverData.pingToServer < 600L) {
                var17 = 2;
            } else if (serverData.pingToServer < 1000L) {
                var17 = 3;
            } else {
                var17 = 4;
            }

            if (serverData.pingToServer < 0L) {
                var19 = "(no connection)";
            } else {
                var19 = serverData.pingToServer + "ms";
                var18 = serverData.playerList;
            }
        } else {
            var16 = 1;
            var17 = (int) (Minecraft.getSystemTime() / 100L + (long) (p_148279_1_ * 2) & 7L);

            if (var17 > 4) {
                var17 = 8 - var17;
            }

            var19 = "Pinging...";
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bindTexture(Gui.icons);
        Gui.drawModalRectWithCustomSizedTexture(x + p_148279_4_ - 15, y, (float) (var16 * 10), (float) (176 + var17 * 8), 10, 8, 256.0F, 256.0F);

        if (serverData.getBase64EncodedIconData() != null && !this.serverData.getBase64EncodedIconData().equals(this.iconData)) {
            iconData = this.serverData.getBase64EncodedIconData();
            prepareServerIcon();
            guiMultiplayer.getServerList().saveServerList();
        }

        if (field_148305_h != null) {
            minecraft.getTextureManager().bindTexture(this.field_148306_i);
            Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
        }

        int var20 = p_148279_7_ - x;
        int var21 = p_148279_8_ - y;

        if (var20 >= p_148279_4_ - 15 && var20 <= p_148279_4_ - 5 && var21 >= 0 && var21 <= 8) {
            guiMultiplayer.func_146793_a(var19);
        } else if (var20 >= p_148279_4_ - var15 - 15 - 2 && var20 <= p_148279_4_ - 15 - 2 && var21 >= 0 && var21 <= 8) {
            guiMultiplayer.func_146793_a(var18);
        }
    }

    private void prepareServerIcon() {
        if (serverData.getBase64EncodedIconData() == null) {
            minecraft.getTextureManager().deleteTexture(this.field_148306_i);
            field_148305_h = null;
        } else {
            ByteBuf var2 = Unpooled.copiedBuffer(this.serverData.getBase64EncodedIconData(), Charsets.UTF_8);
            ByteBuf var3 = Base64.decode(var2);
            BufferedImage var1;

            label74: {
                try {
                    var1 = ImageIO.read(new ByteBufInputStream(var3));
                    Validate.validState(var1.getWidth() == 64, "Must be 64 pixels wide");
                    Validate.validState(var1.getHeight() == 64, "Must be 64 pixels high");
                    break label74;
                } catch (Exception var8) {
                    logger.error("Invalid icon for server " + this.serverData.serverName + " (" + this.serverData.serverIP + ")", var8);
                    this.serverData.setBase64EncodedIconData((String) null);
                } finally {
                    var2.release();
                    var3.release();
                }

                return;
            }

            if (field_148305_h == null) {
                field_148305_h = new DynamicTexture(var1.getWidth(), var1.getHeight());
                minecraft.getTextureManager().loadTexture(this.field_148306_i, this.field_148305_h);
            }

            var1.getRGB(0, 0, var1.getWidth(), var1.getHeight(), this.field_148305_h.getTextureData(), 0, var1.getWidth());
            field_148305_h.updateDynamicTexture();
        }
    }

    public boolean mousePressed(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_) {
        guiMultiplayer.selectServer(p_148278_1_);

        if (Minecraft.getSystemTime() - lastClick < 250L) {
            guiMultiplayer.connectToSelected();
        }

        lastClick = Minecraft.getSystemTime();
        return false;
    }

    public void mouseReleased(int p_148277_1_, int p_148277_2_, int p_148277_3_, int p_148277_4_, int p_148277_5_, int p_148277_6_) {}
}