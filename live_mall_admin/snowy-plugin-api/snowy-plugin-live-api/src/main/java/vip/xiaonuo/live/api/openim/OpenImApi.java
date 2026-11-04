package vip.xiaonuo.live.api.openim;

import vip.xiaonuo.live.api.openim.dto.*;

import java.util.List;
import java.util.Map;

public interface OpenImApi {

    /**
     * 获取用户 token。
     *
     * @param platformId 平台ID
     * @param userId     用户ID
     * @return 用户 token 和 token有效期
     */
    Map<String, String> getUserToken(int platformId, String userId);

    /**
     * 获取管理员 token。
     *
     * @return 管理员 token
     */
    Map<String, String> getAdminToken();

    /**
     * 注册用户。
     *
     * @param userId   用户ID
     * @param nickname 用户昵称
     * @param faceURL  用户头像URL
     * @return 注册结果，0 表示成功，其他值表示失败
     */
    Map<String, String> registerUser(String userId, String nickname, String faceURL);

    /**
     * 创建群组。
     *
     * @param ownerUserID 群组所有者的用户ID
     * @param groupName   群组名称
     * @return 创建群组的结果，0 表示成功，其他值表示失败
     */
    Map<String, String> createGroup(String ownerUserID, String groupName);

    /**
     * 敏感词过滤
     *
     * @param txt 待过滤文本
     * @return 过滤后的文本
     */
    Map<String, String> checkSensitiveWord(String txt);

    /**
     * 用户进到群组
     *
     * @param groupId
     * @param invitedUserID
     * @return
     */
    Map<String, String> addUserToGroup(String groupId, String invitedUserID);

    /**
     * 更新群组信息
     * @param dto 群组信息
     * @return 更新结果
     */
    OpenImErrorResponseDTO updateGroupInfo(OpenImGroupUpdateRequestDTO dto);

    /**
     * 用户退出群组
     *
     * @param groupId
     * @param userId
     * @return
     */
    Map<String, String> quitGroup(String groupId, String userId);

    /**
     * 发送弹幕
     *
     * @param groupId
     * @param userId
     * @param content
     */
    Map<String, String> sendBarrage(String groupId, String userId, String content);

    /**
     * 获取群组信息
     *
     * @param groupIds 群组Ids
     * @return 群组信息
     */
    List<OpenImGroupInfoDTO> getGroupsInfo(List<String> groupIds);

    /**
     * 获取群成员列表
     *
     * @param groupId     群组Id
     * @param pageNumber  页码
     * @param memberInfos 成员列表
     * @return 群成员信息
     */
    List<OpenImGroupMemberInfoDTO> getMembersInfo(String groupId, Integer pageNumber, List<OpenImGroupMemberInfoDTO> memberInfos);

    /**
     * 获取用户在线状态
     *
     * @param userIds 用户Ids
     * @return 结果
     */
    List<OpenImUserOnlineInfoDTO> checkUserOnLine(List<String> userIds);

    /**
     * 发送业务通知
     *
     * @param sendId  系统通知号ID，或用户ID
     * @param groupId 接收群ID，与userID只能选其中一个
     * @param userId  接收者用户ID，与groupID只能选其中一个
     * @param type    根据业务分类，客户端可以通过改字段用不同方法处理message
     * @param message 消息
     * @return 发送结果
     */
    OpenImSendBusinessNotificationResponseRecord sendBusinessNotification(String sendId, String groupId, String userId, String type, String message);

}
