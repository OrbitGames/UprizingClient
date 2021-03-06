package net.minecraft.network.login.server;

import com.mojang.authlib.GameProfile;
import java.io.IOException;
import java.util.UUID;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginClient;

public class S02PacketLoginSuccess extends Packet
{
    private GameProfile profile;

    public S02PacketLoginSuccess() {}

    public S02PacketLoginSuccess(GameProfile profileIn)
    {
        this.profile = profileIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        String var2 = data.readStringFromBuffer(36);
        String var3 = data.readStringFromBuffer(16);
        UUID var4 = UUID.fromString(var2);
        this.profile = new GameProfile(var4, var3);
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        UUID var2 = this.profile.getId();
        data.writeStringToBuffer(var2 == null ? "" : var2.toString());
        data.writeStringToBuffer(this.profile.getName());
    }

    public void processPacket(INetHandlerLoginClient handler)
    {
        handler.handleLoginSuccess(this);
    }

    /**
     * If true, the network manager will process the packet immediately when received, otherwise it will queue it for
     * processing. Currently true for: Disconnect, LoginSuccess, KeepAlive, ServerQuery/Info, Ping/Pong
     */
    public boolean hasPriority()
    {
        return true;
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerLoginClient)handler);
    }
}