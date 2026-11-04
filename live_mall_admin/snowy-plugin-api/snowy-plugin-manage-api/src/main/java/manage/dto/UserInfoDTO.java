package manage.dto;

import com.mybatisflex.annotation.Id;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import manage.enums.RegisterUrlStatus;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/7 15:00
 */
@Data
public class UserInfoDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -7960531085765083704L;


    /** ID */
    @Id
    @ApiModelProperty(value = "ID", position = 1)
    private String id;

    /** 手机号 */
    @ApiModelProperty(value = "手机号", position = 2)
    private String mobile;

    /** 头像 */
    @ApiModelProperty(value = "头像", position = 3)
    private String avatar;

    /** 等级id */
    @ApiModelProperty(value = "等级id", position = 4)
    private String levelId;

    /** 昵称 */
    @ApiModelProperty(value = "昵称", position = 5)
    private String nickname;

    /** 积分账户 */
    @ApiModelProperty(value = "积分账户", position = 6)
    private int integral;

    /** 冻结积分 */
    @ApiModelProperty(value = "冻结积分", position = 7)
    private int frozenIntegral;

    /** 现金账户 */
    @ApiModelProperty(value = "现金账户", position = 8)
    private BigDecimal cash;

    /** 冻结现金 */
    @ApiModelProperty(value = "冻结现金", position = 9)
    private BigDecimal frozenCash;

    /** 上次登录ip */
    @ApiModelProperty(value = "上次登录ip", position = 10)
    private String lastLoginIp;

    /** 上次登录地点 */
    @ApiModelProperty(value = "上次登录地点", position = 11)
    private String lastLoginAddress;

    /** 上次登录时间 */
    @ApiModelProperty(value = "上次登录时间", position = 12)
    private Date lastLoginTime;

    /** 上次登录设备 */
    @ApiModelProperty(value = "上次登录设备", position = 13)
    private String lastLoginDevice;

    /** 用户状态 */
    @ApiModelProperty(value = "用户状态", position = 14)
    private String userStatus;

    /** 用户邀请码 */
    @ApiModelProperty(value = "用户邀请码", position = 5)
    private String rcode;

    /** 邀请人id */
    @ApiModelProperty(value = "邀请人id", position = 5)
    private String pId;

    /**
     * 推广注册链接状态
     */
    @ApiModelProperty(value = "推广注册链接状态")
    private RegisterUrlStatus registerUrlStatus;
}
