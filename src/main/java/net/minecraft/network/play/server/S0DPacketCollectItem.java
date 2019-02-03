package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S0DPacketCollectItem extends Packet
{
    private int field_149357_a;
    private int field_149356_b;

    public S0DPacketCollectItem() {}

    public S0DPacketCollectItem(int p_i45232_1_, int p_i45232_2_)
    {
        this.field_149357_a = p_i45232_1_;
        this.field_149356_b = p_i45232_2_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_149357_a = data.readInt();
        this.field_149356_b = data.readInt();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeInt(this.field_149357_a);
        data.writeInt(this.field_149356_b);
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleCollectItem(this);
    }

    public int func_149354_c()
    {
        return this.field_149357_a;
    }

    public int func_149353_d()
    {
        return this.field_149356_b;
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}