package vip.xiaonuo.live.api.openim.dto;

import cn.hutool.json.JSONObject;

/**
 * <p>群成员信息</p>
 *
 * @author goya
 * @since 2025/4/30 16:31
 */
public record OpenImGroupMemberInfoDTO(
        String groupID,
        String userID,
        int roleLevel,
        long joinTime,
        String nickname,
        String faceURL,
        int appMangerLevel,
        int joinSource,
        String operatorUserID,
        String ex,
        long muteEndTime,
        String inviterUserID
) {

    public static OpenImGroupMemberInfoDTO of(JSONObject json) {
        return new OpenImGroupMemberInfoDTO(
                json.getStr("groupID"),
                json.getStr("userID"),
                json.getInt("roleLevel"),
                json.getLong("joinTime"),
                json.getStr("nickname"),
                json.getStr("faceURL"),
                json.getInt("appMangerLevel"),
                json.getInt("joinSource"),
                json.getStr("operatorUserID"),
                json.getStr("ex"),
                json.getLong("muteEndTime"),
                json.getStr("inviterUserID")
        );
    }
}
