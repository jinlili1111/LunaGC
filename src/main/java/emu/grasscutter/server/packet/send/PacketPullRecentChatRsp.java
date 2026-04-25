package emu.grasscutter.server.packet.send;

import emu.grasscutter.game.chat.ChatMessage;
import emu.grasscutter.game.chat.ChatProto;
import emu.grasscutter.net.packet.*;
import java.util.List;

public class PacketPullRecentChatRsp extends BasePacket {
    public PacketPullRecentChatRsp(List<ChatMessage> messages) {
        super(PacketOpcodes.PullRecentChatRsp);

        this.setData(ChatProto.pullRecentChatRsp(messages, 0));
    }
}
