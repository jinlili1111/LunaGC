package emu.grasscutter.game.chat;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public final class ChatProto {
    private static final int CHAT_TIME = 1;
    private static final int CHAT_IS_READ = 2;
    private static final int CHAT_TO_UID = 3;
    private static final int CHAT_UID = 14;
    private static final int CHAT_SEQUENCE = 13;
    private static final int CHAT_TEXT = 960;
    private static final int CHAT_ICON = 362;
    private static final int CHAT_SYSTEM_HINT = 1757;

    private ChatProto() {}

    public static ParsedPrivateChat parsePrivateChatReq(byte[] payload) throws IOException {
        CodedInputStream input = CodedInputStream.newInstance(payload);
        int targetUid = 0;
        String text = null;
        Integer icon = null;

        while (!input.isAtEnd()) {
            int tag = input.readTag();
            if (tag == 0) {
                break;
            }

            switch (tag >>> 3) {
                case 13 -> targetUid = input.readUInt32();
                case 11 -> text = input.readString();
                case 2 -> icon = input.readUInt32();
                default -> {
                    if (!input.skipField(tag)) {
                        return new ParsedPrivateChat(targetUid, text, icon);
                    }
                }
            }
        }

        return new ParsedPrivateChat(targetUid, text, icon);
    }

    public static ParsedPlayerChat parsePlayerChatReq(byte[] payload) throws IOException {
        CodedInputStream input = CodedInputStream.newInstance(payload);
        int channelId = 0;
        String text = null;
        Integer icon = null;

        while (!input.isAtEnd()) {
            int tag = input.readTag();
            if (tag == 0) {
                break;
            }

            switch (tag >>> 3) {
                case 2 -> channelId = input.readUInt32();
                case 6 -> {
                    var chat = parseChatInfo(input.readByteArray());
                    text = chat.text();
                    icon = chat.icon();
                }
                default -> {
                    if (!input.skipField(tag)) {
                        return new ParsedPlayerChat(channelId, text, icon);
                    }
                }
            }
        }

        return new ParsedPlayerChat(channelId, text, icon);
    }

    public static byte[] privateChatNotify(ChatMessage message) {
        return write(output -> output.writeByteArray(4, chatInfo(message)));
    }

    public static byte[] privateChatRsp(int retcode) {
        return write(
                output -> {
                    if (retcode != 0) {
                        output.writeInt32(6, retcode);
                    }
                });
    }

    public static byte[] pullPrivateChatRsp(List<ChatMessage> history, int retcode) {
        return write(
                output -> {
                    if (retcode != 0) {
                        output.writeInt32(7, retcode);
                    }
                    if (history != null) {
                        for (var message : history) {
                            output.writeByteArray(9, chatInfo(message));
                        }
                    }
                });
    }

    public static byte[] pullRecentChatRsp(List<ChatMessage> messages, int retcode) {
        return write(
                output -> {
                    if (retcode != 0) {
                        output.writeInt32(8, retcode);
                    }
                    if (messages != null) {
                        for (var message : messages) {
                            output.writeByteArray(13, chatInfo(message));
                        }
                    }
                });
    }

    public static byte[] playerChatNotify(ChatMessage message, int channelId) {
        return write(
                output -> {
                    output.writeByteArray(8, chatInfo(message));
                    output.writeUInt32(15, channelId);
                });
    }

    public static byte[] playerChatSystemHintNotify(
            int uid, int channelId, int hintType, List<Integer> uidList) {
        return write(
                output -> {
                    output.writeByteArray(8, systemHintChatInfo(uid, hintType, uidList));
                    output.writeUInt32(15, channelId);
                });
    }

    public static byte[] playerChatRsp(int retcode) {
        return write(
                output -> {
                    if (retcode != 0) {
                        output.writeInt32(8, retcode);
                    }
                });
    }

    public static byte[] readPrivateChatRsp(int retcode) {
        return write(
                output -> {
                    if (retcode != 0) {
                        output.writeInt32(13, retcode);
                    }
                });
    }

    private static ParsedChatInfo parseChatInfo(byte[] payload) throws IOException {
        CodedInputStream input = CodedInputStream.newInstance(payload);
        String text = null;
        Integer icon = null;

        while (!input.isAtEnd()) {
            int tag = input.readTag();
            if (tag == 0) {
                break;
            }

            switch (tag >>> 3) {
                case CHAT_TEXT -> text = input.readString();
                case CHAT_ICON -> icon = input.readUInt32();
                default -> {
                    if (!input.skipField(tag)) {
                        return new ParsedChatInfo(text, icon);
                    }
                }
            }
        }

        return new ParsedChatInfo(text, icon);
    }

    private static byte[] chatInfo(ChatMessage message) {
        return write(
                output -> {
                    output.writeUInt32(CHAT_TIME, message.getTime());
                    output.writeBool(CHAT_IS_READ, false);
                    output.writeUInt32(CHAT_TO_UID, message.getToUid());
                    output.writeUInt32(CHAT_UID, message.getUid());
                    output.writeUInt32(CHAT_SEQUENCE, message.getSequence());

                    if (message.hasText()) {
                        output.writeString(CHAT_TEXT, message.getText());
                    } else if (message.hasIcon()) {
                        output.writeUInt32(CHAT_ICON, message.getIcon());
                    }
                });
    }

    private static byte[] systemHintChatInfo(int uid, int hintType, List<Integer> uidList) {
        return write(
                output -> {
                    output.writeUInt32(CHAT_TIME, (int) (System.currentTimeMillis() / 1000));
                    output.writeUInt32(CHAT_UID, uid);
                    output.writeByteArray(CHAT_SYSTEM_HINT, systemHint(hintType, uidList));
                });
    }

    private static byte[] systemHint(int hintType, List<Integer> uidList) {
        return write(
                output -> {
                    output.writeUInt32(8, hintType);
                    if (uidList != null) {
                        for (int uid : uidList) {
                            output.writeUInt32(5, uid);
                        }
                    }
                });
    }

    private static byte[] write(Writer writer) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            CodedOutputStream output = CodedOutputStream.newInstance(stream);
            writer.write(output);
            output.flush();
            return stream.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to build chat packet", e);
        }
    }

    @FunctionalInterface
    private interface Writer {
        void write(CodedOutputStream output) throws IOException;
    }

    private record ParsedChatInfo(String text, Integer icon) {}

    public record ParsedPrivateChat(int targetUid, String text, Integer icon) {
        public boolean hasText() {
            return text != null && !text.isEmpty();
        }

        public boolean hasIcon() {
            return icon != null;
        }
    }

    public record ParsedPlayerChat(int channelId, String text, Integer icon) {
        public boolean hasText() {
            return text != null && !text.isEmpty();
        }

        public boolean hasIcon() {
            return icon != null;
        }
    }
}
