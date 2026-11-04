package vip.xiaonuo.shop.modular.specialtopic.param;

import io.swagger.annotations.ApiModelProperty;
import org.noear.solon.validation.annotation.NotBlank;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/12 11:29
 */
public record MallGoodsSpecialTopicDetailPageQueryParam(
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


        @ApiModelProperty("专题Id")
        @NotBlank(message = "专题Id不能为空")
        String specialTopicId,

        /* 创建时间开始 */
        @ApiModelProperty(value = "创建时间开始")
        String startCreateTime,

        /* 创建时间结束 */
        @ApiModelProperty(value = "创建时间结束")
        String endCreateTime
) {
    public static MallGoodsSpecialTopicDetailPageQueryParam of(Integer current, Integer size,String specialTopicId) {
        return new MallGoodsSpecialTopicDetailPageQueryParam(current, size, null, null, null, specialTopicId, null, null);
    }
}
