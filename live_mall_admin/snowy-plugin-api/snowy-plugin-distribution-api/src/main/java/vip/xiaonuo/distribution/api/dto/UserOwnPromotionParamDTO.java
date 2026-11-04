package vip.xiaonuo.distribution.api.dto;

import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

/**
 * 用户推广接口入参
 *
 * @author zhaishaojiang
 * @date 2025/02/17 17:28
 **/
public record UserOwnPromotionParamDTO(

        String userId,

        @ApiModelProperty(value = "当前页码")
        Integer current,

        @ApiModelProperty(value = "每页条数")
        Integer size,

        @ApiModelProperty(value = "开始时间")
        LocalDateTime startTime,

        @ApiModelProperty(value = "结束时间")
        LocalDateTime endTime
) {

}
