package emu.grasscutter.server.packet.recv;

import emu.grasscutter.net.packet.*;
import emu.grasscutter.net.proto.RetcodeOuterClass.Retcode;
import emu.grasscutter.server.game.GameSession;
import emu.grasscutter.server.packet.send.PacketPrivateChatRsp;
import emu.grasscutter.server.packet.util.ChatProtoCodec;

@Opcodes(PacketOpcodes.PrivateChatReq)
public class HandlerPrivateChatReq extends PacketHandler {

    @Override
    public void handle(GameSession session, byte[] header, byte[] payload) throws Exception {
        var req = ChatProtoCodec.decodePrivateChatReq(payload);
        int retcode = Retcode.RET_RPIVATE_CHAT_INVALID_CONTENT_TYPE_VALUE;

        if (req.contentType() == ChatProtoCodec.ContentType.TEXT) {
            retcode =
                    session
                    .getServer()
                    .getChatSystem()
                    .sendPrivateMessage(session.getPlayer(), req.targetUid(), req.text());
        } else if (req.contentType() == ChatProtoCodec.ContentType.ICON) {
            retcode =
                    session
                    .getServer()
                    .getChatSystem()
                    .sendPrivateMessage(session.getPlayer(), req.targetUid(), req.icon());
        }

        session.send(new PacketPrivateChatRsp(retcode));
    }
}
