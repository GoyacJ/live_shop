package vip.xiaonuo.integral.modular.info.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.xiaonuo.common.enums.SimpleStatus;
import vip.xiaonuo.common.pojo.CommonEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>积分信息</p>
 *
 * @author goya
 * @since 2025/5/21 10:35
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table("integral_info")
public class IntegralInfo extends CommonEntity {

    @Id
    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "积分")
    private BigDecimal integral;

    @ApiModelProperty(value = "冻结积分")
    private BigDecimal frozenIntegral;

    @ApiModelProperty(value = "过期积分")
    private BigDecimal expireIntegral;

    @ApiModelProperty(value = "最后修改时间")
    private LocalDateTime lastModifyTime;

    @ApiModelProperty(value = "最后修改人ID")
    private String lastModifyUserId;

    @ApiModelProperty(value = "积分状态")
    private SimpleStatus status;
}
