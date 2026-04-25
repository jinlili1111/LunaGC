package emu.grasscutter.server.packet.send;

import emu.grasscutter.game.chat.ChatMessage;
import emu.grasscutter.game.chat.ChatProto;
import emu.grasscutter.net.packet.*;
import emu.grasscutter.net.proto.RetcodeOuterClass.Retcode;
import java.util.List;

public class PacketPullPrivateChatRsp extends BasePacket {

    public PacketPullPrivateChatRsp(List<ChatMessage> history) {
        super(PacketOpcodes.PullPrivateChatRsp);

        this.setData(
                ChatProto.pullPrivateChatRsp(
                        history, history == null ? Retcode.RET_FAIL_VALUE : Retcode.RET_SUCC_VALUE));
    }
}
