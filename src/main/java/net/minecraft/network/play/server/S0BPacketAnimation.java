package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import uprizing.util.NameAndValue;

public class S0BPacketAnimation extends Packet
{
    private int entityId;
    private int type;

    public S0BPacketAnimation() {}

    public S0BPacketAnimation(Entity ent, int animationType)
    {
        this.entityId = ent.getEntityId();
        this.type = animationType;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.entityId = data.readVarIntFromBuffer();
        this.type = data.readUnsignedByte();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeVarIntToBuffer(this.entityId);
        data.writeByte(this.type);
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleAnimation(this);
    }

    /**
     * Returns a string formatted as comma separated [field]=[value] values. Used by Minecraft for logging purposes.
     */
    public String serialize()
    {
        return String.format("id=%d, type=%d", Integer.valueOf(this.entityId), Integer.valueOf(this.type));
    }

    public int func_148978_c()
    {
        return this.entityId;
    }

    public int func_148977_d()
    {
        return this.type;
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }

    @Override
    public NameAndValue[] sex() {
        return new NameAndValue[] {
            new NameAndValue("Entity Id", entityId),
            new NameAndValue("Type", type)
        };
    }
}