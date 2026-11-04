package vip.xiaonuo.mobile.modular.auth.param;

import io.swagger.annotations.ApiModelProperty;
import org.noear.solon.validation.annotation.NotBlank;

/**
 * <p>设备访问信息</p>
 *
 * @author goya
 * @since 2025/4/30 10:50
 */
public record MobileDeviceAccessParam(
        @ApiModelProperty("设备Id")
        @NotBlank(message = "设备Id不能为空")
        String clientId,

        @ApiModelProperty("品牌")
        String brand,

        @ApiModelProperty("型号")
        String model,

        @ApiModelProperty("系统版本")
        String version,

        @ApiModelProperty("是否模拟器 0: 否 1: 是")
        Integer simulator
) {
}
