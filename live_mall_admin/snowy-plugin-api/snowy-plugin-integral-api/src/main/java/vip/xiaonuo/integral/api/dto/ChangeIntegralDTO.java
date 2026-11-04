package vip.xiaonuo.integral.api.dto;

import io.swagger.annotations.ApiModelProperty;
import org.noear.solon.validation.annotation.NotBlank;
import org.noear.solon.validation.annotation.NotNull;
import vip.xiaonuo.common.enums.SimpleStatus;
import vip.xiaonuo.integral.api.enums.IntegralCategory;
import vip.xiaonuo.integral.api.enums.IntegralChangeType;
import vip.xiaonuo.integral.api.enums.IntegralOperationSource;
import vip.xiaonuo.integral.api.enums.IntegralSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/29 18:19
 */
public record ChangeIntegralDTO(

        @ApiModelProperty(value = "用户ID")
        @NotBlank(message = "用户Id不能为空")
        String userId,

        @ApiModelProperty(value = "积分")
        BigDecimal integral,

        @ApiModelProperty(value = "积分状态")
        SimpleStatus status,

        @ApiModelProperty(value = "变动类型")
        @NotNull(message = "变动类型不能为空")
        IntegralChangeType changeType,

        @ApiModelProperty(value = "积分分类")
        IntegralCategory integralCategory,

        @ApiModelProperty(value = "积分来源")
        @NotNull(message = "积分来源不能为空")
        IntegralSource integralSource,

        @ApiModelProperty(value = "积分来源ID")
        String integralSourceId,

        @ApiModelProperty(value = "操作来源")
        @NotNull(message = "操作来源不能为空")
        IntegralOperationSource operationSource,

        @ApiModelProperty(value = "操作人ID")
        String operationUserId,

        @ApiModelProperty("积分有效期")
        LocalDateTime validityPeriod,

        @ApiModelProperty(value = "备注")
        String remark
) {

    public static ChangeIntegralDTO adjust(
            String userId,
            BigDecimal integral,
            IntegralCategory integralCategory,
            LocalDateTime validityPeriod,
            String remark
    ) {
        return new ChangeIntegralDTO(
                userId,
                integral,
                null,
                IntegralChangeType.ADJUST,
                integralCategory,
                IntegralSource.SYSTEM,
                "",
                IntegralOperationSource.ADMIN,
                "10000",
                validityPeriod,
                remark);
    }

    /**
     * 增加积分
     *
     * @param userId           用户Id
     * @param integral         积分
     * @param modifyUserId     操作用户Id
     * @param integralSource   来源
     * @param integralSourceId 来源Id
     * @param operationSource  操作来源
     * @param validityPeriod   积分有效期
     * @param remark           备注
     * @return 结果
     */
    public static ChangeIntegralDTO increaseIntegral(
            String userId,
            BigDecimal integral,
            String modifyUserId,
            IntegralSource integralSource,
            String integralSourceId,
            IntegralOperationSource operationSource,
            LocalDateTime validityPeriod,
            String remark
    ) {
        return new ChangeIntegralDTO(
                userId,
                integral,
                null,
                IntegralChangeType.INCREASE,
                IntegralCategory.INTEGRAL,
                integralSource,
                integralSourceId,
                operationSource,
                modifyUserId,
                validityPeriod,
                remark);
    }

    /**
     * 增加冻结积分
     *
     * @param userId           用户Id
     * @param integral         积分
     * @param modifyUserId     操作用户Id
     * @param integralSource   来源
     * @param integralSourceId 来源Id
     * @param operationSource  操作来源
     * @param remark           备注
     * @return 结果
     */
    public static ChangeIntegralDTO increaseFrozenIntegral(
            String userId,
            BigDecimal integral,
            String modifyUserId,
            IntegralSource integralSource,
            String integralSourceId,
            IntegralOperationSource operationSource,
            String remark
    ) {
        return new ChangeIntegralDTO(
                userId,
                integral,
                null,
                IntegralChangeType.INCREASE,
                IntegralCategory.FROZEN_INTEGRAL,
                integralSource,
                integralSourceId,
                operationSource,
                modifyUserId,
                null,
                remark);
    }

    /**
     * 增加过期积分
     *
     * @param userId           用户Id
     * @param integral         积分
     * @param modifyUserId     操作用户Id
     * @param integralSource   来源
     * @param integralSourceId 来源Id
     * @param operationSource  操作来源
     * @param remark           备注
     * @return 结果
     */
    public static ChangeIntegralDTO increaseExpireIntegral(
            String userId,
            BigDecimal integral,
            String modifyUserId,
            IntegralSource integralSource,
            String integralSourceId,
            IntegralOperationSource operationSource,
            String remark
    ) {
        return new ChangeIntegralDTO(
                userId,
                integral,
                null,
                IntegralChangeType.INCREASE,
                IntegralCategory.EXPIRE_INTEGRAL,
                integralSource,
                integralSourceId,
                operationSource,
                modifyUserId,
                null,
                remark);
    }

    /**
     * 减少积分
     *
     * @param userId           用户Id
     * @param integral         积分
     * @param modifyUserId     操作用户Id
     * @param integralSource   来源
     * @param integralSourceId 来源Id
     * @param operationSource  操作来源
     * @param remark           备注
     * @return 结果
     */
    public static ChangeIntegralDTO decreaseIntegral(
            String userId,
            BigDecimal integral,
            String modifyUserId,
            IntegralSource integralSource,
            String integralSourceId,
            IntegralOperationSource operationSource,
            String remark
    ) {
        return new ChangeIntegralDTO(
                userId,
                integral,
                null,
                IntegralChangeType.DECREASE,
                IntegralCategory.INTEGRAL,
                integralSource,
                integralSourceId,
                operationSource,
                modifyUserId,
                null,
                remark);
    }

    /**
     * 减少冻结积分
     *
     * @param userId           用户Id
     * @param integral         积分
     * @param modifyUserId     操作用户Id
     * @param integralSource   来源
     * @param integralSourceId 来源Id
     * @param operationSource  操作来源
     * @param remark           备注
     * @return 结果
     */
    public static ChangeIntegralDTO decreaseFrozenIntegral(
            String userId,
            BigDecimal integral,
            String modifyUserId,
            IntegralSource integralSource,
            String integralSourceId,
            IntegralOperationSource operationSource,
            String remark
    ) {
        return new ChangeIntegralDTO(
                userId,
                integral,
                null,
                IntegralChangeType.DECREASE,
                IntegralCategory.FROZEN_INTEGRAL,
                integralSource,
                integralSourceId,
                operationSource,
                modifyUserId,
                null,
                remark);
    }

    /**
     * 减少过期积分
     *
     * @param userId           用户Id
     * @param integral         积分
     * @param modifyUserId     操作用户Id
     * @param integralSource   来源
     * @param integralSourceId 来源Id
     * @param operationSource  操作来源
     * @param remark           备注
     * @return 结果
     */
    public static ChangeIntegralDTO decreaseExpireIntegral(
            String userId,
            BigDecimal integral,
            String modifyUserId,
            IntegralSource integralSource,
            String integralSourceId,
            IntegralOperationSource operationSource,
            String remark
    ) {
        return new ChangeIntegralDTO(
                userId,
                integral,
                null,
                IntegralChangeType.DECREASE,
                IntegralCategory.EXPIRE_INTEGRAL,
                integralSource,
                integralSourceId,
                operationSource,
                modifyUserId,
                null,
                remark);
    }

    /**
     * 调整积分
     *
     * @param userId           用户Id
     * @param integral         积分
     * @param modifyUserId     操作用户Id
     * @param integralSource   来源
     * @param integralSourceId 来源Id
     * @param operationSource  操作来源
     * @param remark           备注
     * @return 结果
     */
    public static ChangeIntegralDTO adjustmentIntegral(
            String userId,
            BigDecimal integral,
            String modifyUserId,
            IntegralSource integralSource,
            String integralSourceId,
            IntegralOperationSource operationSource,
            String remark
    ) {
        return new ChangeIntegralDTO(
                userId,
                integral,
                null,
                IntegralChangeType.ADJUST,
                IntegralCategory.INTEGRAL,
                integralSource,
                integralSourceId,
                operationSource,
                modifyUserId,
                null,
                remark);
    }

    /**
     * 调整冻结积分
     *
     * @param userId           用户Id
     * @param integral         积分
     * @param modifyUserId     操作用户Id
     * @param integralSource   来源
     * @param integralSourceId 来源Id
     * @param operationSource  操作来源
     * @param remark           备注
     * @return 结果
     */
    public static ChangeIntegralDTO adjustmentFrozenIntegral(
            String userId,
            BigDecimal integral,
            String modifyUserId,
            IntegralSource integralSource,
            String integralSourceId,
            IntegralOperationSource operationSource,
            String remark
    ) {
        return new ChangeIntegralDTO(
                userId,
                integral,
                null,
                IntegralChangeType.ADJUST,
                IntegralCategory.FROZEN_INTEGRAL,
                integralSource,
                integralSourceId,
                operationSource,
                modifyUserId,
                null,
                remark);
    }

    /**
     * 调整过期积分
     *
     * @param userId           用户Id
     * @param integral         积分
     * @param modifyUserId     操作用户Id
     * @param integralSource   来源
     * @param integralSourceId 来源Id
     * @param operationSource  操作来源
     * @param remark           备注
     * @return 结果
     */
    public static ChangeIntegralDTO adjustmentExpireIntegral(
            String userId,
            BigDecimal integral,
            String modifyUserId,
            IntegralSource integralSource,
            String integralSourceId,
            IntegralOperationSource operationSource,
            String remark
    ) {
        return new ChangeIntegralDTO(
                userId,
                integral,
                null,
                IntegralChangeType.ADJUST,
                IntegralCategory.EXPIRE_INTEGRAL,
                integralSource,
                integralSourceId,
                operationSource,
                modifyUserId,
                null,
                remark);
    }

    /**
     * 解锁冻结积分
     *
     * @param userId           用户Id
     * @param integral         积分
     * @param modifyUserId     操作用户Id
     * @param integralSource   来源
     * @param integralSourceId 来源Id
     * @param operationSource  操作来源
     * @param remark           备注
     * @return 结果
     */
    public static ChangeIntegralDTO unlockFrozenIntegral(
            String userId,
            BigDecimal integral,
            String modifyUserId,
            IntegralSource integralSource,
            String integralSourceId,
            IntegralOperationSource operationSource,
            String remark
    ) {
        return new ChangeIntegralDTO(
                userId,
                integral,
                null,
                IntegralChangeType.UNLOCK,
                null,
                integralSource,
                integralSourceId,
                operationSource,
                modifyUserId,
                null,
                remark);
    }

    /**
     * 更改积分状态
     *
     * @param userId           用户Id
     * @param status           积分状态
     * @param modifyUserId     操作用户Id
     * @param integralSource   来源
     * @param integralSourceId 来源Id
     * @param operationSource  操作来源
     * @param remark           备注
     * @return 结果
     */
    public static ChangeIntegralDTO changeIntegralStatus(
            String userId,
            SimpleStatus status,
            String modifyUserId,
            IntegralSource integralSource,
            String integralSourceId,
            IntegralOperationSource operationSource,
            String remark
    ) {
        return new ChangeIntegralDTO(
                userId,
                null,
                status,
                IntegralChangeType.CHANGE_STATUS,
                null,
                integralSource,
                integralSourceId,
                operationSource,
                modifyUserId,
                null,
                remark);
    }

    /**
     * 更改积分状态
     *
     * @param userId           用户Id
     * @param status           积分状态
     * @return 结果
     */
    public static ChangeIntegralDTO changeIntegralStatus(
            String userId,
            SimpleStatus status
    ) {
        return new ChangeIntegralDTO(
                userId,
                null,
                status,
                IntegralChangeType.CHANGE_STATUS,
                null,
                IntegralSource.SYSTEM,
                "",
                IntegralOperationSource.ADMIN,
                "10000",
                null,
                null);
    }

    public static ChangeIntegralDTO expireIntegral(
            String userId,
            BigDecimal integral,
            String modifyUserId,
            IntegralSource integralSource,
            String integralSourceId,
            IntegralOperationSource operationSource,
            String remark
    ) {
        return new ChangeIntegralDTO(
                userId,
                integral,
                null,
                IntegralChangeType.EXPIRE,
                IntegralCategory.INTEGRAL,
                integralSource,
                integralSourceId,
                operationSource,
                modifyUserId,
                null,
                remark);
    }
}
