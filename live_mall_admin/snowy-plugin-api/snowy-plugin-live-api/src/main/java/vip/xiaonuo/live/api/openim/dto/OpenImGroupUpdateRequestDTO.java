package vip.xiaonuo.live.api.openim.dto;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/9 17:17
 */
public record OpenImGroupUpdateRequestDTO(
        String groupId,
        String groupName,
        String notification,
        String introduction,
        String faceUrl,
        String ex,
        Integer needVerification,
        Integer lookMemberInfo,
        Integer applyMemberFriend
) {

    public static OpenImGroupUpdateRequestDTO of(String groupId, String ex) {
        return new OpenImGroupUpdateRequestDTO(groupId, null, null, null, null, ex, null, null, null);
    }
}
