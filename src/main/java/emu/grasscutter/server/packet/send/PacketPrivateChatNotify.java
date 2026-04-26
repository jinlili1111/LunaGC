package emu.grasscutter.server.packet.send;

import emu.grasscutter.net.packet.*;
import emu.grasscutter.net.proto.ChatInfoOuterClass.ChatInfo;
import emu.grasscutter.server.packet.util.ChatProtoCodec;

public class PacketPrivateChatNotify extends BasePacket {
    private final ChatInfo info;

    public PacketPrivateChatNotify(int senderId, int recvId, String message) {
        this(senderId, recvId, message, 0);
    }

    public PacketPrivateChatNotify(int senderId, int recvId, String message, int sequence) {
        super(PacketOpcodes.PrivateChatNotify);

        ChatInfo info =
                ChatInfo.newBuilder()
                        .setTime((int) (System.currentTimeMillis() / 1000))
                        .setUid(senderId)
                        .setToUid(recvId)
                        .setSequence(sequence)
                        .setText(message)
                        .build();
        this.info = info;

        this.setData(ChatProtoCodec.encodePrivateChatNotify(info));
    }

    public PacketPrivateChatNotify(int senderId, int recvId, int emote) {
        this(senderId, recvId, emote, 0);
    }

    public PacketPrivateChatNotify(int senderId, int recvId, int emote, int sequence) {
        super(PacketOpcodes.PrivateChatNotify);

        ChatInfo info =
                ChatInfo.newBuilder()
                        .setTime((int) (System.currentTimeMillis() / 1000))
                        .setUid(senderId)
                        .setToUid(recvId)
                        .setSequence(sequence)
                        .setIcon(emote)
                        .build();
        this.info = info;

        this.setData(ChatProtoCodec.encodePrivateChatNotify(info));
    }

    public ChatInfo getChatInfo() {
        return this.info;
    }
}
