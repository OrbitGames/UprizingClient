package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S00PacketKeepAlive extends Packet
{
    private int field_149136_a;
    private static final String __OBFID = "CL_00001303";

    public S00PacketKeepAlive() {}

    public S00PacketKeepAlive(int p_i45195_1_)
    {
        this.field_149136_a = p_i45195_1_;
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleKeepAlive(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_149136_a = data.readInt();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeInt(this.field_149136_a);
    }

    /**
     * If true, the network manager will process the packet immediately when received, otherwise it will queue it for
     * processing. Currently true for: Disconnect, LoginSuccess, KeepAlive, ServerQuery/Info, Ping/Pong
     */
    public boolean hasPriority()
    {
        return true;
    }

    public int func_149134_c()
    {
        return this.field_149136_a;
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}