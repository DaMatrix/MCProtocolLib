package ch.spacebase.mc.protocol.packet.ingame.client;

import java.io.IOException;

import ch.spacebase.mc.protocol.data.game.values.ClientRequest;
import ch.spacebase.mc.protocol.data.game.values.MagicValues;
import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ClientRequestPacket implements Packet {
	
	private ClientRequest request;
	
	@SuppressWarnings("unused")
	private ClientRequestPacket() {
	}
	
	public ClientRequestPacket(ClientRequest request) {
		this.request = request;
	}
	
	public ClientRequest getRequest() {
		return this.request;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.request = MagicValues.key(ClientRequest.class, in.readUnsignedByte());
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeByte(MagicValues.value(Integer.class, this.request));
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}
	
}
