package vip.xiaonuo.mobile.modular.live.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Author: shaojiang
 * CreateTime: 2025/4/8 16:53
 * Description: 支付方式类
 */
@Getter
@Setter
public class Payment {
    /** 支付类型ID */
    @ApiModelProperty(value = "支付类型ID", position = 1)
    private String id;

    /** 类型标识 */
    @ApiModelProperty(value = "类型标识", position = 3)
    private String type;

    /** 类型标题 */
    @ApiModelProperty(value = "类型标题", position = 3)
    private String title;

    /** 类型图标 */
    @ApiModelProperty(value = "图标", position = 4)
    private String icon;

    @ApiModelProperty(value = "描述", position = 4)
    private String remark;

}
