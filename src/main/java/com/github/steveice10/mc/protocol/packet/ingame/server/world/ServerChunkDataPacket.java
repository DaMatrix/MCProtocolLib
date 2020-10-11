package com.github.steveice10.mc.protocol.packet.ingame.server.world;

import com.github.steveice10.mc.protocol.data.game.chunk.Chunk;
import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.mc.protocol.util.NetUtil;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.io.stream.StreamNetOutput;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ServerChunkDataPacket extends MinecraftPacket {
    private Chunk chunk;

    @SuppressWarnings("unused")
    private ServerChunkDataPacket() {
    }

    public ServerChunkDataPacket(Chunk chunk) {
        this.chunk = chunk;
    }

    public Chunk getColumn() {
        return this.chunk;
    }

    @Override
    public void read(NetInput in) throws IOException {
        int x = in.readInt();
        int z = in.readInt();
        boolean fullChunk = in.readBoolean();
        int chunkMask = in.readVarInt();
        byte data[] = in.readBytes(in.readVarInt());
        CompoundTag[] tileEntities = new CompoundTag[in.readVarInt()];
        for(int i = 0; i < tileEntities.length; i++) {
            tileEntities[i] = NetUtil.readNBT(in);
        }

        this.chunk = NetUtil.readColumn(data, x, z, fullChunk, false, chunkMask, tileEntities);
    }

    @Override
    public void write(NetOutput out) throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        NetOutput netOut = new StreamNetOutput(byteOut);
        int mask = NetUtil.writeColumn(netOut, this.chunk, this.chunk.hasBiomeData(), this.chunk.hasSkylight());

        out.writeInt(this.chunk.getX());
        out.writeInt(this.chunk.getZ());
        out.writeBoolean(this.chunk.hasBiomeData());
        out.writeVarInt(mask);
        out.writeVarInt(byteOut.size());
        out.writeBytes(byteOut.toByteArray(), byteOut.size());
        out.writeVarInt(this.chunk.getTileEntities().length);
        for(CompoundTag tag : this.chunk.getTileEntities()) {
            NetUtil.writeNBT(out, tag);
        }
    }
}
