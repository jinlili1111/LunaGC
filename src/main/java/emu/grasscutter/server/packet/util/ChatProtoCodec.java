package emu.grasscutter.server.packet.util;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.WireFormat;
import emu.grasscutter.net.proto.ChatInfoOuterClass.ChatInfo;
import emu.grasscutter.net.proto.RetcodeOuterClass.Retcode;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public final class ChatProtoCodec {
    private static final int PRIVATE_CHAT_REQ_ICON_TAG = 16; // field 2, varint
    private static final int PRIVATE_CHAT_REQ_TEXT_TAG = 90; // field 11, length-delimited
    private static final int PRIVATE_CHAT_REQ_TARGET_UID_TAG = 104; // field 13, varint

    private ChatProtoCodec() {}

    public enum ContentType {
        NONE,
        TEXT,
        ICON
    }

    public record PrivateChatRequest(int targetUid, ContentType contentType, String text, int icon) {}

    public static PrivateChatRequest decodePrivateChatReq(byte[] payload) throws IOException {
        var input = CodedInputStream.newInstance(payload);
        int targetUid = 0;
        ContentType contentType = ContentType.NONE;
        String text = "";
        int icon = 0;

        while (true) {
            int tag = input.readTag();
            if (tag == 0) {
                break;
            }

            switch (tag) {
                case PRIVATE_CHAT_REQ_ICON_TAG -> {
                    icon = input.readUInt32();
                    contentType = ContentType.ICON;
                }
                case PRIVATE_CHAT_REQ_TEXT_TAG -> {
                    text = input.readStringRequireUtf8();
                    contentType = ContentType.TEXT;
                }
                case PRIVATE_CHAT_REQ_TARGET_UID_TAG -> targetUid = input.readUInt32();
                default -> {
                    if (!input.skipField(tag)) {
                        return new PrivateChatRequest(targetUid, contentType, text, icon);
                    }
                }
            }
        }

        return new PrivateChatRequest(targetUid, contentType, text, icon);
    }

    public static byte[] encodePrivateChatRsp(int retcode) {
        return encode(
                output -> {
                    if (retcode != Retcode.RET_SUCC_VALUE) {
                        output.writeInt32(6, retcode);
                    }
                });
    }

    public static byte[] encodePrivateChatNotify(ChatInfo info) {
        byte[] chatInfo = encodeChatInfo(info);
        return encodeMessageFields(output -> writeRawMessage(output, 4, chatInfo));
    }

    public static byte[] encodePullPrivateChatRsp(List<ChatInfo> history, int retcode) {
        return encodeMessageFields(
                output -> {
                    if (retcode != Retcode.RET_SUCC_VALUE) {
                        output.writeInt32(7, retcode);
                    }
                    if (history != null) {
                        for (var info : history) {
                            writeRawMessage(output, 9, encodeChatInfo(info));
                        }
                    }
                });
    }

    public static byte[] encodePullRecentChatRsp(List<ChatInfo> messages, int retcode) {
        return encodeMessageFields(
                output -> {
                    if (retcode != Retcode.RET_SUCC_VALUE) {
                        output.writeInt32(8, retcode);
                    }
                    if (messages != null) {
                        for (var info : messages) {
                            writeRawMessage(output, 13, encodeChatInfo(info));
                        }
                    }
                });
    }

    private static byte[] encodeChatInfo(ChatInfo info) {
        return encode(
                output -> {
                    if (info.getTime() != 0) {
                        output.writeUInt32(1, info.getTime());
                    }
                    if (info.getIsRead()) {
                        output.writeBool(2, true);
                    }
                    if (info.getToUid() != 0) {
                        output.writeUInt32(3, info.getToUid());
                    }
                    if (info.getPlatformType() != 0) {
                        output.writeUInt32(10, info.getPlatformType());
                    }
                    if (info.getSequence() != 0) {
                        output.writeUInt32(13, info.getSequence());
                    }
                    if (info.getUid() != 0) {
                        output.writeUInt32(14, info.getUid());
                    }
                    if (info.hasIcon()) {
                        output.writeUInt32(362, info.getIcon());
                    }
                    if (info.hasText()) {
                        output.writeString(960, info.getText());
                    }
                });
    }

    private static byte[] encodeMessageFields(ProtoWriter writer) {
        return encode(writer);
    }

    private static byte[] encode(ProtoWriter writer) {
        try {
            var stream = new ByteArrayOutputStream();
            var output = CodedOutputStream.newInstance(stream);
            writer.write(output);
            output.flush();
            return stream.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to encode chat packet", e);
        }
    }

    private static void writeRawMessage(CodedOutputStream output, int fieldNumber, byte[] message)
            throws IOException {
        output.writeTag(fieldNumber, WireFormat.WIRETYPE_LENGTH_DELIMITED);
        output.writeUInt32NoTag(message.length);
        output.writeRawBytes(message);
    }

    @FunctionalInterface
    private interface ProtoWriter {
        void write(CodedOutputStream output) throws IOException;
    }
}
