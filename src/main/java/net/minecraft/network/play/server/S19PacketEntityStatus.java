package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.World;
import uprizing.util.NameAndValue;

public class S19PacketEntityStatus extends Packet
{
    private int field_149164_a;
    private byte field_149163_b;

    public S19PacketEntityStatus() {}

    public S19PacketEntityStatus(Entity p_i46335_1_, byte p_i46335_2_)
    {
        this.field_149164_a = p_i46335_1_.getEntityId();
        this.field_149163_b = p_i46335_2_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_149164_a = data.readInt();
        this.field_149163_b = data.readByte();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeInt(this.field_149164_a);
        data.writeByte(this.field_149163_b);
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleEntityStatus(this);
    }

    public Entity func_149161_a(World p_149161_1_)
    {
        return p_149161_1_.getEntityByID(this.field_149164_a);
    }

    public byte func_149160_c()
    {
        return this.field_149163_b;
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }

    @Override
    public NameAndValue[] sex() {
        return new NameAndValue[] {
            new NameAndValue("Entity Id", field_149164_a),
            new NameAndValue("Status", field_149163_b)
        };
    }
}