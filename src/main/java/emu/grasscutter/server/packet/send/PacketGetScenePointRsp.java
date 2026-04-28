package emu.grasscutter.server.packet.send;

import com.google.protobuf.CodedOutputStream;
import emu.grasscutter.data.GameData;
import emu.grasscutter.game.player.Player;
import emu.grasscutter.net.packet.*;
import java.io.ByteArrayOutputStream;

public class PacketGetScenePointRsp extends BasePacket {

    public PacketGetScenePointRsp(Player player, int sceneId) {
        super(PacketOpcodes.GetScenePointRsp);

        try {
            var out = new ByteArrayOutputStream();
            var coded = CodedOutputStream.newInstance(out);
            coded.writeUInt32(3, player.getUid());
            for (int i = 1; i < 9; i++) {
                coded.writeUInt32(4, i);
            }

            if (GameData.getScenePointIdList().size() == 0) {
                for (int i = 1; i < 1000; i++) {
                    coded.writeUInt32(7, i);
                    coded.writeUInt32(9, i);
                }
            } else {
                for (int pointId : player.getUnlockedScenePoints(sceneId)) {
                    coded.writeUInt32(7, pointId);
                    coded.writeUInt32(9, pointId);
                }
            }

            coded.writeInt32(11, 0);
            coded.writeUInt32(13, sceneId);
            coded.flush();
            this.setData(out.toByteArray());
        } catch (Exception ignored) {
            this.setData(new byte[0]);
        }
    }
}
