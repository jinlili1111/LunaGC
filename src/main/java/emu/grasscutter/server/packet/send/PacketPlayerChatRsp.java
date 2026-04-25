package emu.grasscutter.server.packet.send;

import emu.grasscutter.game.chat.ChatProto;
import emu.grasscutter.net.packet.*;

public class PacketPlayerChatRsp extends BasePacket {

    public PacketPlayerChatRsp() {
        super(PacketOpcodes.PlayerChatRsp);

        this.setData(ChatProto.playerChatRsp(0));
    }
}
