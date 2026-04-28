package emu.grasscutter.server.packet.send;

import com.google.protobuf.CodedOutputStream;
import emu.grasscutter.game.entity.GameEntity;
import emu.grasscutter.game.props.FightProperty;
import emu.grasscutter.net.packet.*;
import java.io.ByteArrayOutputStream;
import java.util.Collection;

public class PacketEntityFightPropUpdateNotify extends BasePacket {
    public PacketEntityFightPropUpdateNotify(GameEntity entity, FightProperty prop) {
        super(PacketOpcodes.EntityFightPropUpdateNotify);

        this.setData(encode(entity, java.util.List.of(prop)));
    }

    public PacketEntityFightPropUpdateNotify(GameEntity entity, Collection<FightProperty> props) {
        super(PacketOpcodes.EntityFightPropUpdateNotify);

        this.setData(encode(entity, props));
    }

    private byte[] encode(GameEntity entity, Collection<FightProperty> props) {
        try {
            var out = new ByteArrayOutputStream();
            var coded = CodedOutputStream.newInstance(out);
            coded.writeUInt32(10, entity.getId());
            for (FightProperty prop : props) {
                coded.writeByteArray(15, encodeFightPropEntry(prop.getId(), entity.getFightProperty(prop)));
            }
            coded.flush();
            return out.toByteArray();
        } catch (Exception ignored) {
            return new byte[0];
        }
    }

    private byte[] encodeFightPropEntry(int propId, float value) throws java.io.IOException {
        var out = new ByteArrayOutputStream();
        var coded = CodedOutputStream.newInstance(out);
        coded.writeUInt32(1, propId);
        coded.writeFloat(2, value);
        coded.flush();
        return out.toByteArray();
    }
}
