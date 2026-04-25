package emu.grasscutter.server.packet.recv;

import emu.grasscutter.net.packet.*;
import emu.grasscutter.net.proto.RetcodeOuterClass.Retcode;
import emu.grasscutter.server.game.GameSession;
import emu.grasscutter.server.packet.send.PacketReadPrivateChatRsp;

@Opcodes(PacketOpcodes.ReadPrivateChatReq)
public class HandlerReadPrivateChatReq extends PacketHandler {

    @Override
    public void handle(GameSession session, byte[] header, byte[] payload) {
        session.send(new PacketReadPrivateChatRsp(Retcode.RET_SUCC_VALUE));
    }
}
