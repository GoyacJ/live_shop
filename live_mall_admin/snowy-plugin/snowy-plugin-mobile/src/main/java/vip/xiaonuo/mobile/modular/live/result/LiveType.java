package vip.xiaonuo.mobile.modular.live.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class LiveType {

    /** 图片 */
    @ApiModelProperty(value = "图片", position = 1)
    private String id;

    /** 标题 */
    @ApiModelProperty(value = "标题", position = 1)
    private String title;

    /**  */
    @ApiModelProperty(value = "", position = 1)
    private List<LivePreview> list;

}
