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
package vip.xiaonuo.manage.modular.userbalancelog.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import manage.enums.IntegralSettleTimeType;
import manage.enums.UserBalanceChangeType;
import manage.enums.UserBalanceType;
import vip.xiaonuo.common.pojo.CommonEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 平台-用户余额变更记录实体
 *
 * @author yuezisheng
 * @date  2025/02/18 17:55
 **/
@Getter
@Setter
@Table(value = "user_balance_log")
public class UserBalanceLog extends CommonEntity {

    /** ID */
    @Id
    @ApiModelProperty(value = "ID", position = 1)
    private String id;

    @ApiModelProperty(value = "用户id", position = 1)
    private String userId;

    /** 变更类型，任务奖励/购物奖励/直播福利/直播福袋/直播红包/积分兑换/现金提现/积分抵扣 */
    @ApiModelProperty(value = "变更类型，任务奖励/购物奖励/直播福利/直播福袋/直播红包/积分兑换/现金提现/积分抵扣", position = 2)
    private UserBalanceChangeType type;

    /** 余额类型，现金/冻结现金/积分/冻结积分 */
    @ApiModelProperty(value = "积分/冻结积分", position = 3)
    private UserBalanceType balanceType;

    @ApiModelProperty(value = "积分结算时间类型", position = 3)
    private IntegralSettleTimeType settleTimeType;

    @ApiModelProperty(value = "积分结算时间", position = 3)
    private LocalDateTime settleTime;

    /** 变更前 */
    @ApiModelProperty(value = "变更前", position = 4)
    private BigDecimal beforeBalance;

    /** 变更 */
    @ApiModelProperty(value = "变更", position = 5)
    private BigDecimal changeBalance;

    /** 变更后 */
    @ApiModelProperty(value = "变更后", position = 6)
    private BigDecimal afterBalance;

    /** 变更原因 */
    @ApiModelProperty(value = "变更原因", position = 7)
    private String reason;

    /** 订单id/提现记录id/任务奖励记录id··· */
    @ApiModelProperty(value = "订单id/提现记录id/任务奖励记录id···", position = 8)
    private String reasonId;

}
