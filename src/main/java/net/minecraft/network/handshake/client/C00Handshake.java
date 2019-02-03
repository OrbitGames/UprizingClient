package net.minecraft.network.handshake.client;

import java.io.IOException;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.handshake.INetHandlerHandshakeServer;
import uprizing.Versions;
import uprizing.util.Constants;

public class C00Handshake extends Packet
{
    private int protocolVersion;
    private String ip;
    private int port;
    private EnumConnectionState requestedState;
    private static final String __OBFID = "CL_00001372";

    public C00Handshake() {}

    public C00Handshake(int p_i45266_1_, String p_i45266_2_, int p_i45266_3_, EnumConnectionState p_i45266_4_)
    {
        this.protocolVersion = p_i45266_1_;
        this.ip = p_i45266_2_;
        this.port = p_i45266_3_;
        this.requestedState = p_i45266_4_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.protocolVersion = data.readVarIntFromBuffer();
        this.ip = data.readStringFromBuffer(255);
        this.port = data.readUnsignedShort();
        this.requestedState = EnumConnectionState.getById(data.readVarIntFromBuffer());
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeVarIntToBuffer(this.protocolVersion);
        data.writeStringToBuffer(this.ip);
        data.writeShort(this.port);
        data.writeVarIntToBuffer(this.requestedState.getId());

        if (requestedState == EnumConnectionState.LOGIN && protocolVersion == Constants.ALGERIAN_PROTOCOL) {
            data.writeByte(Versions.CURRENT.getId());
        }
    }

    public void processPacket(INetHandlerHandshakeServer handler)
    {
        handler.processHandshake(this);
    }

    /**
     * If true, the network manager will process the packet immediately when received, otherwise it will queue it for
     * processing. Currently true for: Disconnect, LoginSuccess, KeepAlive, ServerQuery/Info, Ping/Pong
     */
    public boolean hasPriority()
    {
        return true;
    }

    public EnumConnectionState getRequestedState()
    {
        return this.requestedState;
    }

    public int getProtocolVersion()
    {
        return this.protocolVersion;
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerHandshakeServer)handler);
    }
}