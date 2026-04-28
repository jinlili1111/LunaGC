package emu.grasscutter.server.packet.send;

import com.google.protobuf.CodedOutputStream;
import emu.grasscutter.net.packet.*;
import java.io.ByteArrayOutputStream;

public class PacketScenePointUnlockNotify extends BasePacket {
    public PacketScenePointUnlockNotify(int sceneId, int pointId) {
        super(PacketOpcodes.ScenePointUnlockNotify);

        this.setData(encode(sceneId, java.util.List.of(pointId)));
    }

    public PacketScenePointUnlockNotify(int sceneId, Iterable<Integer> pointIds) {
        super(PacketOpcodes.ScenePointUnlockNotify);

        this.setData(encode(sceneId, pointIds));
    }

    private byte[] encode(int sceneId, Iterable<Integer> pointIds) {
        try {
            var out = new ByteArrayOutputStream();
            var coded = CodedOutputStream.newInstance(out);
            for (int pointId : pointIds) {
                coded.writeUInt32(10, pointId);
                coded.writeUInt32(12, pointId);
            }
            coded.writeUInt32(11, sceneId);
            coded.flush();
            return out.toByteArray();
        } catch (Exception ignored) {
            return new byte[0];
        }
    }
}
