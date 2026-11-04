package vip.xiaonuo.mobile.modular.auth.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Author: shaojiang
 * CreateTime: 2025/4/16 11:21
 * Description:
 */
@Getter
@Setter
public class ChatContactResult {
    @ApiModelProperty(value = "企微二维码", position = 2)
    private String qrcode;

    @ApiModelProperty(value = "联系电话", position = 2)
    private String mobile;
}
