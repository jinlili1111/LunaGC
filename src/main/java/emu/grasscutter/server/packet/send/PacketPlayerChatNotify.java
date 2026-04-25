package emu.grasscutter.server.packet.send;

import emu.grasscutter.game.chat.ChatMessage;
import emu.grasscutter.game.chat.ChatProto;
import emu.grasscutter.game.player.Player;
import emu.grasscutter.net.packet.*;
import emu.grasscutter.net.proto.SystemHintOuterClass;

public class PacketPlayerChatNotify extends BasePacket {

    public PacketPlayerChatNotify(Player sender, int channelId, String message) {
        super(PacketOpcodes.PlayerChatNotify);

        this.setData(ChatProto.playerChatNotify(ChatMessage.text(sender.getUid(), 0, message), channelId));
    }

    public PacketPlayerChatNotify(Player sender, int channelId, int emote) {
        super(PacketOpcodes.PlayerChatNotify);

        this.setData(ChatProto.playerChatNotify(ChatMessage.icon(sender.getUid(), 0, emote), channelId));
    }

    public PacketPlayerChatNotify(Player sender, int channelId, SystemHintOuterClass.SystemHint systemHint) {
        super(PacketOpcodes.PlayerChatNotify);

        this.setData(
                ChatProto.playerChatSystemHintNotify(
                        sender.getUid(), channelId, systemHint.getType(), java.util.List.of()));
    }
}
