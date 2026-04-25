package emu.grasscutter.server.packet.send;

import emu.grasscutter.game.chat.ChatProto;
import emu.grasscutter.net.packet.*;

public class PacketPrivateChatRsp extends BasePacket {
    public PacketPrivateChatRsp(int retcode) {
        super(PacketOpcodes.PrivateChatRsp);

        this.setData(ChatProto.privateChatRsp(retcode));
    }
}
