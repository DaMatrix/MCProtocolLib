package ch.spacebase.mc.protocol.packet.ingame.server.entity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ch.spacebase.mc.protocol.data.game.attribute.Attribute;
import ch.spacebase.mc.protocol.data.game.attribute.AttributeModifier;
import ch.spacebase.mc.protocol.data.game.values.MagicValues;
import ch.spacebase.mc.protocol.data.game.values.ModifierOperation;
import ch.spacebase.mc.protocol.data.game.values.ModifierType;
import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerEntityPropertiesPacket implements Packet {
	
	private int entityId;
	private List<Attribute> attributes;
	
	@SuppressWarnings("unused")
	private ServerEntityPropertiesPacket() {
	}
	
	public ServerEntityPropertiesPacket(int entityId, List<Attribute> attributes) {
		this.entityId = entityId;
		this.attributes = attributes;
	}
	
	public int getEntityId() {
		return this.entityId;
	}
	
	public List<Attribute> getAttributes() {
		return this.attributes;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.entityId = in.readVarInt();
		this.attributes = new ArrayList<Attribute>();
		int length = in.readVarInt();
		for(int index = 0; index < length; index++) {
			String key = in.readString();
			double value = in.readDouble();
			List<AttributeModifier> modifiers = new ArrayList<AttributeModifier>();
			short len = in.readShort();
			for(int ind = 0; ind < len; ind++) {
				modifiers.add(new AttributeModifier(MagicValues.key(ModifierType.class, new UUID(in.readLong(), in.readLong())), in.readDouble(), MagicValues.key(ModifierOperation.class, in.readByte())));
			}
			
			this.attributes.add(new Attribute(Attribute.Type.fromKey(key), value, modifiers));
		}
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeVarInt(this.entityId);
		out.writeVarInt(this.attributes.size());
		for(Attribute attribute : this.attributes) {
			out.writeString(attribute.getType().getKey());
			out.writeDouble(attribute.getValue());
			out.writeShort(attribute.getModifiers().size());
			for(AttributeModifier modifier : attribute.getModifiers()) {
				UUID uuid = MagicValues.value(UUID.class, modifier.getType());
				out.writeLong(uuid.getMostSignificantBits());
				out.writeLong(uuid.getLeastSignificantBits());
				out.writeDouble(modifier.getAmount());
				out.writeByte(MagicValues.value(Integer.class, modifier.getOperation()));
			}
		}
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
