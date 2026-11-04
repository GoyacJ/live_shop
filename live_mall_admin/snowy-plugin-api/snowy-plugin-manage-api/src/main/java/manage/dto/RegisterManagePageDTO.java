package manage.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import manage.enums.RegisterUrlStatus;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/21 14:06
 */
@Data
public class RegisterManagePageDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("注册时间")
    private LocalDateTime registerTime;

    @ApiModelProperty("UUID")
    private String uuid;

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("用户Id")
    private String userId;

    @ApiModelProperty("上级账户")
    private String parentAccount;

    @ApiModelProperty("上级Id")
    private String parentId;

    @ApiModelProperty("推广注册链接")
    private String registerUrl;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("邀请码")
    private String invitationCode;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 推广注册链接状态
     */
    @ApiModelProperty(value = "推广注册链接状态")
    private RegisterUrlStatus registerUrlStatus;

}
