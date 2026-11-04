package vip.xiaonuo.content.modular.shortplay.param;

import io.swagger.annotations.ApiModelProperty;
import vip.xiaonuo.content.modular.shortplay.entity.ContentShortPlay;

import java.time.LocalDateTime;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/13 14:57
 */
public record ContentShortPlayEditParam(
        @ApiModelProperty(value = "ID")
        String id,

        @ApiModelProperty(value = "标题")
        String title,

        @ApiModelProperty(value = "分类")
        String category,

        @ApiModelProperty(value = "标签列表")
        String tags,

        @ApiModelProperty(value = "封面图")
        String cover,

        @ApiModelProperty(value = "海报图")
        String poster,

        @ApiModelProperty(value = "描述")
        String description,

        @ApiModelProperty(value = "排序")
        Integer sort,

        @ApiModelProperty(value = "状态（0-未发布，1-已发布）")
        Integer status,

        @ApiModelProperty(value = "推荐等级（1-普通，2-推荐，3-强推等）")
        Integer recommendLevel,

        @ApiModelProperty(value = "发布时间")
        LocalDateTime publishTime,

        @ApiModelProperty(value = "是否允许评论")
        Boolean allowComment,

        @ApiModelProperty(value = "是否允许分享")
        Boolean allowShare,

        @ApiModelProperty(value = "是否免费")
        Boolean isFree
) {

        public ContentShortPlay toBean(){
                ContentShortPlay contentShortPlay = new ContentShortPlay();
                contentShortPlay.setId(id);
                contentShortPlay.setTitle(title);
                contentShortPlay.setCategory(category);
                contentShortPlay.setTags(tags);
                contentShortPlay.setCover(cover);
                contentShortPlay.setPoster(poster);
                contentShortPlay.setDescription(description);
                contentShortPlay.setSort(sort);
                contentShortPlay.setStatus(status);
                contentShortPlay.setRecommendLevel(recommendLevel);
                contentShortPlay.setPublishTime(publishTime);
                contentShortPlay.setAllowComment(allowComment);
                contentShortPlay.setAllowShare(allowShare);
                contentShortPlay.setIsFree(isFree);
                return contentShortPlay;
        }
}
