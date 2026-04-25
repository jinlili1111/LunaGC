package emu.grasscutter.server.packet.send;

import emu.grasscutter.game.chat.ChatMessage;
import emu.grasscutter.game.chat.ChatProto;
import emu.grasscutter.net.packet.*;

public class PacketPrivateChatNotify extends BasePacket {
    private final ChatMessage info;

    public PacketPrivateChatNotify(int senderId, int recvId, String message) {
        super(PacketOpcodes.PrivateChatNotify);

        ChatMessage info = ChatMessage.text(senderId, recvId, message);
        this.info = info;

        this.setData(ChatProto.privateChatNotify(info));
    }

    public PacketPrivateChatNotify(int senderId, int recvId, int emote) {
        super(PacketOpcodes.PrivateChatNotify);

        ChatMessage info = ChatMessage.icon(senderId, recvId, emote);
        this.info = info;

        this.setData(ChatProto.privateChatNotify(info));
    }

    public ChatMessage getChatInfo() {
        return this.info;
    }
}
