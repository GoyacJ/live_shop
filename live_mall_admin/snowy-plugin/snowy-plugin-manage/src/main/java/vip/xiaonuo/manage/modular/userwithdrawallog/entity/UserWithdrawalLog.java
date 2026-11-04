/*
 * Copyright [2022] [https://www.xiaonuo.vip]
 *
 * Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Snowy源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.xiaonuo.vip
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队xiaonuobase@qq.com商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package vip.xiaonuo.manage.modular.userwithdrawallog.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户提现记录实体
 *
 * @author yuezisheng
 * @date  2025/02/18 20:31
 **/
@Getter
@Setter
@Table(value = "user_withdrawal_log")
public class UserWithdrawalLog {

    /** ID */
    @Id
    @ApiModelProperty(value = "ID", position = 1)
    private String id;

    /** 提现金额 */
    @ApiModelProperty(value = "提现金额", position = 2)
    private String amount;

    /** 审核原因 */
    @ApiModelProperty(value = "审核原因", position = 3)
    private String reason;

    /** 状态，待处理/已打款/拒绝申请 */
    @ApiModelProperty(value = "状态，待处理/已打款/拒绝申请", position = 4)
    private String status;

    /** 删除标志 */
    @ApiModelProperty(value = "删除标志", position = 5)
    private String deleteFlag;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间", position = 6)
    private Date createTime;

    /** 创建用户 */
    @ApiModelProperty(value = "创建用户", position = 7)
    private String createUser;

    /** 修改时间 */
    @ApiModelProperty(value = "修改时间", position = 8)
    private Date updateTime;

    /** 修改用户 */
    @ApiModelProperty(value = "修改用户", position = 9)
    private String updateUser;
}
