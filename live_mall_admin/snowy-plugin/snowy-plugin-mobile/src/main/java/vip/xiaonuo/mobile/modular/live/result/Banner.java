package vip.xiaonuo.mobile.modular.live.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Banner {

    /** 图片 */
    @ApiModelProperty(value = "图片", position = 1)
    private String image;

    /** 标题 */
    @ApiModelProperty(value = "标题", position = 2)
    private String title;

    @ApiModelProperty(value = "启用/禁用 DISABLED/ENABLED", position = 3)
    private String whetherToClick;

    @ApiModelProperty(value = "跳转方式", position = 3)
    private String skipMode;

    @ApiModelProperty(value = "跳转地址", position = 3)
    private String url;
}
