package emu.grasscutter.game.friends;

import static emu.grasscutter.config.Configuration.GAME_INFO;

import emu.grasscutter.GameConstants;
import emu.grasscutter.net.proto.*;
import emu.grasscutter.net.proto.FriendBriefOuterClass.FriendBrief;
import emu.grasscutter.net.proto.FriendOnlineStateOuterClass.FriendOnlineState;
import emu.grasscutter.net.proto.ProfilePictureOuterClass.ProfilePicture;
import emu.grasscutter.net.proto.SocialDetailOuterClass.SocialDetail;

public final class ServerFriend {
    private ServerFriend() {}

    public static FriendBrief toFriendBrief() {
        var serverAccount = GAME_INFO.serverAccount;

        return FriendBrief.newBuilder()
                .setUid(GameConstants.SERVER_CONSOLE_UID)
                .setNickname(serverAccount.nickName)
                .setRemarkName(serverAccount.nickName)
                .setLevel(serverAccount.adventureRank)
                .setProfilePicture(ProfilePicture.newBuilder().setAvatarId(serverAccount.avatarId))
                .setWorldLevel(serverAccount.worldLevel)
                .setSignature(serverAccount.signature)
                .setOnlineState(FriendOnlineState.FriendOnlineState_FRIEND_ONLINE)
                .setIsMpModeAvailable(true)
                .setLastActiveTime((int) (System.currentTimeMillis() / 1000f))
                .setNameCardId(serverAccount.nameCardId)
                .setParam(1)
                .setIsGameSource(true)
                .setPlatformType(PlatformTypeOuterClass.PlatformType.PlatformType_PC)
                .setFriendEnterHomeOptionValue(
                        FriendEnterHomeOptionOuterClass.FriendEnterHomeOption
                                .FriendEnterHomeOption_REFUSE_VALUE)
                .build();
    }

    public static SocialDetail.Builder toSocialDetail() {
        var serverAccount = GAME_INFO.serverAccount;

        return SocialDetail.newBuilder()
                .setUid(GameConstants.SERVER_CONSOLE_UID)
                .setNickname(serverAccount.nickName)
                .setRemarkName(serverAccount.nickName)
                .setLevel(serverAccount.adventureRank)
                .setProfilePicture(ProfilePicture.newBuilder().setAvatarId(serverAccount.avatarId))
                .setAvatarId(serverAccount.avatarId)
                .setWorldLevel(serverAccount.worldLevel)
                .setSignature(serverAccount.signature)
                .setOnlineState(FriendOnlineState.FriendOnlineState_FRIEND_ONLINE)
                .setIsFriend(true)
                .setIsMpModeAvailable(true)
                .setNameCardId(serverAccount.nameCardId)
                .setOnlineId(serverAccount.nickName)
                .setParam(1)
                .setPlatformType(PlatformTypeOuterClass.PlatformType.PlatformType_PC)
                .setFriendEnterHomeOptionValue(
                        FriendEnterHomeOptionOuterClass.FriendEnterHomeOption
                                .FriendEnterHomeOption_REFUSE_VALUE);
    }
}
