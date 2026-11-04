package vip.xiaonuo.content.modular.shortplay.param;

import io.swagger.annotations.ApiModelProperty;
import org.noear.solon.validation.annotation.NotBlank;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/13 14:57
 */
public record ContentShortPlayEpisodePageParam(

        /* 商品标题 */
        @ApiModelProperty(value = "标题")
        String title,

        /* 短剧ID */
        @ApiModelProperty(value = "短剧ID")
        @NotBlank(message = "短剧ID不能为空")
        String shortPlayId,

        /* 当前页 */
        @ApiModelProperty(value = "当前页码")
        Integer current,

        /* 每页条数 */
        @ApiModelProperty(value = "每页条数")
        Integer size,

        /* 排序字段 */
        @ApiModelProperty(value = "排序字段，字段驼峰名称，如：userName")
        String sortField,

        /* 排序方式 */
        @ApiModelProperty(value = "排序方式，升序：ASCEND；降序：DESCEND")
        String sortOrder,

        /* 关键词 */
        @ApiModelProperty(value = "关键词")
        String searchKey,

        /* 创建时间开始 */
        @ApiModelProperty(value = "创建时间开始")
        String startCreateTime,

        /* 创建时间结束 */
        @ApiModelProperty(value = "创建时间结束")
        String endCreateTime
) {
}
