package vip.xiaonuo.mobile.modular.h5.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.noear.solon.validation.annotation.NotBlank;
import org.noear.solon.validation.annotation.NotNull;

/**
 * Author: shaojiang
 * CreateTime: 2025/4/12 16:27
 * Description: 请求参数
 */
@Getter
@Setter
public class H5LoginParam {
    @ApiModelProperty(value = "手机号", required = true, position = 1)
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @ApiModelProperty(value = "验证码", required = true, position = 2)
    @NotBlank(message = "验证码不能为空")
    private String code;

    @ApiModelProperty(value = "邀请码", required = true, position = 3)
    private String rcode;
}
