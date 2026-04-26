package emu.grasscutter.server.packet.send;

import emu.grasscutter.net.packet.*;
import emu.grasscutter.net.proto.ChatInfoOuterClass.ChatInfo;
import emu.grasscutter.net.proto.RetcodeOuterClass.Retcode;
import emu.grasscutter.server.packet.util.ChatProtoCodec;
import java.util.List;

public class PacketPullPrivateChatRsp extends BasePacket {

    public PacketPullPrivateChatRsp(List<ChatInfo> history) {
        super(PacketOpcodes.PullPrivateChatRsp);

        int retcode = history == null ? Retcode.RET_FAIL_VALUE : Retcode.RET_SUCC_VALUE;
        this.setData(ChatProtoCodec.encodePullPrivateChatRsp(history, retcode));
    }
}
