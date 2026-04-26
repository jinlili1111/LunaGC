package emu.grasscutter.server.packet.send;

import emu.grasscutter.net.packet.BasePacket;
import emu.grasscutter.net.packet.PacketOpcodes;
import emu.grasscutter.net.proto.RetcodeOuterClass.Retcode;
import emu.grasscutter.server.packet.util.ChatProtoCodec;

public class PacketPrivateChatRsp extends BasePacket {
    public PacketPrivateChatRsp() {
        this(Retcode.RET_SUCC_VALUE);
    }

    public PacketPrivateChatRsp(int retcode) {
        super(PacketOpcodes.PrivateChatRsp);

        this.setData(ChatProtoCodec.encodePrivateChatRsp(retcode));
    }
}
