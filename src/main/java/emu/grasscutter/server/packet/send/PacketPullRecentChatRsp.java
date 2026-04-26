package emu.grasscutter.server.packet.send;

import emu.grasscutter.net.packet.*;
import emu.grasscutter.net.proto.ChatInfoOuterClass.ChatInfo;
import emu.grasscutter.net.proto.RetcodeOuterClass.Retcode;
import emu.grasscutter.server.packet.util.ChatProtoCodec;
import java.util.List;

public class PacketPullRecentChatRsp extends BasePacket {
    public PacketPullRecentChatRsp(List<ChatInfo> messages) {
        super(PacketOpcodes.PullRecentChatRsp);

        int retcode = messages == null ? Retcode.RET_FAIL_VALUE : Retcode.RET_SUCC_VALUE;
        this.setData(ChatProtoCodec.encodePullRecentChatRsp(messages, retcode));
    }
}
