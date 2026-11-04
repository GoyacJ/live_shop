package vip.xiaonuo.task.modular.task.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/6 15:18
 */
@Data
public class TaskCacheDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("最后操作时间")
    private LocalDateTime lastOperationTime;

    @ApiModelProperty("次数")
    private Integer count;

    @ApiModelProperty("用户Id")
    private String userId;

    @ApiModelProperty("任务Id")
    private String taskId;

    @ApiModelProperty("积分")
    private BigDecimal integral;

    @ApiModelProperty("业务Id")
    private String businessId;

}
