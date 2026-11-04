package vip.xiaonuo.live.api.openim.dto;

import cn.hutool.json.JSONObject;

/**
 * <p>群组信息</p>
 *
 * @author goya
 * @since 2025/4/30 16:08
 */
public record OpenImGroupInfoDTO(
        String groupID,
        String groupName,
        String notification,
        String introduction,
        String faceURL,
        String ownerUserID,
        long createTime,
        int memberCount,
        String ex,
        int status,
        String creatorUserID,
        int groupType,
        int needVerification,
        int lookMemberInfo,
        int applyMemberFriend,
        long notificationUpdateTime,
        String notificationUserID
) {

    public static OpenImGroupInfoDTO of(JSONObject json) {
        return new OpenImGroupInfoDTO(
                json.getStr("groupID"),
                json.getStr("groupName"),
                json.getStr("notification"),
                json.getStr("introduction"),
                json.getStr("faceURL"),
                json.getStr("ownerUserID"),
                json.getLong("createTime"),
                json.getInt("memberCount"),
                json.getStr("ex"),
                json.getInt("status"),
                json.getStr("creatorUserID"),
                json.getInt("groupType"),
                json.getInt("needVerification"),
                json.getInt("lookMemberInfo"),
                json.getInt("applyMemberFriend"),
                json.getLong("notificationUpdateTime"),
                json.getStr("notificationUserID")
        );
    }
}
