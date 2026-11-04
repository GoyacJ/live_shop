package manage.dto;

import io.swagger.annotations.ApiModelProperty;
import org.noear.solon.validation.annotation.NotBlank;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/4/30 11:17
 */
public record MobileDeviceAccessDTO(
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
