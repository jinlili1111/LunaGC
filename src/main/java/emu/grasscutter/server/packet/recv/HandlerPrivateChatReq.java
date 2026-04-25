package emu.grasscutter.server.packet.recv;

import emu.grasscutter.game.chat.ChatProto;
import emu.grasscutter.net.packet.*;
import emu.grasscutter.net.proto.RetcodeOuterClass.Retcode;
import emu.grasscutter.server.game.GameSession;
import emu.grasscutter.server.packet.send.PacketPrivateChatRsp;

@Opcodes(PacketOpcodes.PrivateChatReq)
public class HandlerPrivateChatReq extends PacketHandler {

    @Override
    public void handle(GameSession session, byte[] header, byte[] payload) throws Exception {
        var req = ChatProto.parsePrivateChatReq(payload);

        if (req.targetUid() <= 0) {
            session.send(new PacketPrivateChatRsp(Retcode.RET_FAIL_VALUE));
            return;
        }

        if (req.hasText()) {
            session
                    .getServer()
                    .getChatSystem()
                    .sendPrivateMessage(session.getPlayer(), req.targetUid(), req.text());
        } else if (req.hasIcon()) {
            session
                    .getServer()
                    .getChatSystem()
                    .sendPrivateMessage(session.getPlayer(), req.targetUid(), req.icon());
        }

        session.send(new PacketPrivateChatRsp(Retcode.RET_SUCC_VALUE));
    }
}
