package emu.grasscutter.server.packet.recv;

import com.google.protobuf.UnknownFieldSet;
import emu.grasscutter.data.GameData;
import emu.grasscutter.data.binout.ScenePointEntry;
import emu.grasscutter.net.packet.*;
import emu.grasscutter.net.proto.RetcodeOuterClass;
import emu.grasscutter.net.proto.UnlockTransPointReqOuterClass.UnlockTransPointReq;
import emu.grasscutter.server.game.GameSession;
import emu.grasscutter.server.packet.send.PacketUnlockTransPointRsp;
import java.util.Locale;

@Opcodes(PacketOpcodes.UnlockTransPointReq)
public class HandlerUnlockTransPointReq extends PacketHandler {
    @Override
    public void handle(GameSession session, byte[] header, byte[] payload) throws Exception {
        UnlockTransPointReq req = UnlockTransPointReq.parseFrom(payload);
        int sceneId = getFieldOrUnknown(req.getSceneId(), req.getUnknownFields(), 3);
        int pointId = getFieldOrUnknown(req.getPointId(), req.getUnknownFields(), 8);

        var player = session.getPlayer();
        ScenePointEntry scenePointEntry = GameData.getScenePointEntryById(sceneId, pointId);
        boolean unlocked = false;
        if (scenePointEntry != null) {
            unlocked =
                    player.getUnlockedScenePoints(sceneId).contains(pointId)
                            || player.getProgressManager()
                                    .unlockTransPoint(sceneId, pointId, isStatue(scenePointEntry));
        }
        session
                .getPlayer()
                .sendPacket(
                        new PacketUnlockTransPointRsp(
                                unlocked
                                        ? RetcodeOuterClass.Retcode.RET_SUCC
                                        : RetcodeOuterClass.Retcode.RET_FAIL));
    }

    private int getFieldOrUnknown(int value, UnknownFieldSet unknownFields, int fieldNumber) {
        if (value != 0) return value;
        var field = unknownFields.getField(fieldNumber);
        if (field == null || field.getVarintList().isEmpty()) return 0;
        return field.getVarintList().get(0).intValue();
    }

    private boolean isStatue(ScenePointEntry scenePointEntry) {
        var pointData = scenePointEntry.getPointData();
        if (pointData == null || pointData.getType() == null) return false;
        return pointData.getType().toLowerCase(Locale.ROOT).contains("statue");
    }
}
