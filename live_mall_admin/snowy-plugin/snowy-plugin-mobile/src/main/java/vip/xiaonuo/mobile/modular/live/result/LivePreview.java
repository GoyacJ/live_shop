package vip.xiaonuo.mobile.modular.live.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LivePreview {

    /** id： */
    @ApiModelProperty(value = "id：", position = 2)
    private String id;

    /** 图片地址： */
    @ApiModelProperty(value = "图片地址：", position = 1)
    private String image;

    /** 标题： */
    @ApiModelProperty(value = "标题：", position = 3)
    private String title;

    /** 主播名称： */
    @ApiModelProperty(value = "主播名称：", position = 3)
    private String realName;

    /** 主播头像： */
    @ApiModelProperty(value = "主播头像：", position = 3)
    private String pictureUrl;
}
