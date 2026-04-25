package emu.grasscutter.game.chat;

import java.util.concurrent.atomic.AtomicInteger;

public final class ChatMessage {
    private static final AtomicInteger NEXT_SEQUENCE = new AtomicInteger(1);

    private final int uid;
    private final int toUid;
    private final int time;
    private final int sequence;
    private final String text;
    private final Integer icon;

    private ChatMessage(int uid, int toUid, String text, Integer icon) {
        this.uid = uid;
        this.toUid = toUid;
        this.time = (int) (System.currentTimeMillis() / 1000);
        this.sequence = NEXT_SEQUENCE.getAndUpdate(value -> value == Integer.MAX_VALUE ? 1 : value + 1);
        this.text = text;
        this.icon = icon;
    }

    public static ChatMessage text(int uid, int toUid, String text) {
        return new ChatMessage(uid, toUid, text, null);
    }

    public static ChatMessage icon(int uid, int toUid, int icon) {
        return new ChatMessage(uid, toUid, null, icon);
    }

    public int getUid() {
        return uid;
    }

    public int getToUid() {
        return toUid;
    }

    public int getTime() {
        return time;
    }

    public int getSequence() {
        return sequence;
    }

    public String getText() {
        return text;
    }

    public Integer getIcon() {
        return icon;
    }

    public boolean hasText() {
        return text != null && !text.isEmpty();
    }

    public boolean hasIcon() {
        return icon != null;
    }
}
