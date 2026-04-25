package emu.grasscutter.server.packet.recv;

import emu.grasscutter.game.chat.ChatProto;
import emu.grasscutter.net.packet.*;
import emu.grasscutter.server.game.GameSession;
import emu.grasscutter.server.packet.send.PacketPlayerChatRsp;

@Opcodes(PacketOpcodes.PlayerChatReq)
public class HandlerPlayerChatReq extends PacketHandler {

    @Override
    public void handle(GameSession session, byte[] header, byte[] payload) throws Exception {
        var req = ChatProto.parsePlayerChatReq(payload);

        if (req.hasText()) {
            session
                    .getServer()
                    .getChatSystem()
                    .sendTeamMessage(session.getPlayer(), req.channelId(), req.text());
        } else if (req.hasIcon()) {
            session
                    .getServer()
                    .getChatSystem()
                    .sendTeamMessage(session.getPlayer(), req.channelId(), req.icon());
        }

        session.send(new PacketPlayerChatRsp());
    }
}
