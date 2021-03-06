package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C0FPacketConfirmTransaction extends Packet
{
    private int id;
    private short uid;
    private boolean accepted;

    public C0FPacketConfirmTransaction() {}

    public C0FPacketConfirmTransaction(int p_i45244_1_, short p_i45244_2_, boolean p_i45244_3_)
    {
        this.id = p_i45244_1_;
        this.uid = p_i45244_2_;
        this.accepted = p_i45244_3_;
    }

    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processConfirmTransaction(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.id = data.readByte();
        this.uid = data.readShort();
        this.accepted = data.readByte() != 0;
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeByte(this.id);
        data.writeShort(this.uid);
        data.writeByte(this.accepted ? 1 : 0);
    }

    /**
     * Returns a string formatted as comma separated [field]=[value] values. Used by Minecraft for logging purposes.
     */
    public String serialize()
    {
        return String.format("id=%d, uid=%d, accepted=%b", Integer.valueOf(this.id), Short.valueOf(this.uid), Boolean.valueOf(this.accepted));
    }

    public int getId()
    {
        return this.id;
    }

    public short getUid()
    {
        return this.uid;
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayServer)handler);
    }
}