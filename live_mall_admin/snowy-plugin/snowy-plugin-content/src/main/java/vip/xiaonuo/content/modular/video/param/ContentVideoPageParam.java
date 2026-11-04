package vip.xiaonuo.content.modular.video.param;

import io.swagger.annotations.ApiModelProperty;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/13 14:57
 */
public record ContentVideoPageParam(

        /* 商品标题 */
        @ApiModelProperty(value = "视频标题")
        String title,

        @ApiModelProperty(value = "分类")
        String category,

        @ApiModelProperty(value = "状态（0-未发布，1-已发布）")
        Integer status,

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
