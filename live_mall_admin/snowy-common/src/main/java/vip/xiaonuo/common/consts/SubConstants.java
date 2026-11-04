package vip.xiaonuo.common.consts;

import org.springframework.stereotype.Component;

@Component
public class SubConstants {

    /**
     * openIm接口mapping
     */
    public static final String GET_USER_TOKEN_URL = "/auth/get_user_token";
    public static final String USER_REGISTER_URL = "/user/user_register";
    public static final String GET_ADMIN_TOKEN_URL  = "/auth/get_admin_token";
    public static final String CREATE_GROUP_URL = "/group/create_group";
    public static final String ADD_USER_TO_GROUP = "/group/join_group";
    public static final String QUIT_GROUP = "/group/quit_group";
    public static final String SEND_MSG = "/msg/send_msg";

    /**
     * 获取群组信息
     */
    public static final String GROUPS_INFO = "/group/get_groups_info";

    /**
     * 修改群组信息
     */
    public static final String SET_GROUPS_INFO = "/group/set_group_info_ex";

    /**
     * 获取群成员列表
     */
    public static final String GROUP_MEMBER_INFO = "/group/get_group_member_list";

    /**
     * 获取指定用户在线状态
     */
    public static final String USERS_ONLINE_STATUS = "/user/get_users_online_status";

    /**
     * 发送业务通知
     */
    public static final String SEND_BUSINESS_NOTIFICATION = "/msg/send_business_notification";

    /**
     * openIm token redis key
     */
    public static final String OPENIM_TOKEN_REDIS_KEY = "openim_token";

    private SubConstants() {}
}
