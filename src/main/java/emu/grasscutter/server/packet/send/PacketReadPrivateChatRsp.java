package emu.grasscutter.server.packet.send;

import emu.grasscutter.game.chat.ChatProto;
import emu.grasscutter.net.packet.*;

public class PacketReadPrivateChatRsp extends BasePacket {
    public PacketReadPrivateChatRsp(int retcode) {
        super(PacketOpcodes.ReadPrivateChatRsp);

        this.setData(ChatProto.readPrivateChatRsp(retcode));
    }
}
