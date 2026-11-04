package vip.xiaonuo.content.modular.video.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import vip.xiaonuo.content.modular.video.entity.ContentVideo;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/13 14:57
 */
@ApiModel(description = "视频信息记录")
public record ContentVideoAddParam(
        @ApiModelProperty(value = "标题")
        String title,

        @ApiModelProperty(value = "分类")
        String category,

        @ApiModelProperty(value = "标签")
        String tags,

        @ApiModelProperty(value = "封面图")
        String cover,

        @ApiModelProperty(value = "描述")
        String description,

        @ApiModelProperty(value = "视频地址")
        String videoUrl,

        @ApiModelProperty(value = "视频时长（秒）")
        Integer duration,

        @ApiModelProperty(value = "状态（0-未发布，1-已发布）")
        Integer status,

        @ApiModelProperty(value = "来源")
        String source,

        @ApiModelProperty(value = "是否可下载 0是1否")
        Integer downloadable,

        @ApiModelProperty(value = "是否可分享 0是1否")
        Integer shareable,

        @ApiModelProperty(value = "是否可评论 0是1否")
        Integer allowComment,

        @ApiModelProperty(value = "排序")
        Integer sort,

        @ApiModelProperty("商品Id")
        String goodsId,

        @ApiModelProperty("展示商品开始秒数")
        Integer startShowGoodsSecond,

        @ApiModelProperty("展示商品秒数")
        Integer showGoodsSecond
) {
        
        public ContentVideo toBean() {
                ContentVideo contentVideo = new ContentVideo();
                contentVideo.setTitle(title());
                contentVideo.setCategory(category());
                contentVideo.setTags(tags());
                contentVideo.setCover(cover());
                contentVideo.setDescription(description());
                contentVideo.setVideoUrl(videoUrl());
                contentVideo.setDuration(duration());
                contentVideo.setStatus(status());
                contentVideo.setSource(source());
                contentVideo.setDownloadable(downloadable());
                contentVideo.setShareable(shareable());
                contentVideo.setAllowComment(allowComment());
                contentVideo.setSort(sort());
                contentVideo.setGoodsId(goodsId());
                contentVideo.setStartShowGoodsSecond(startShowGoodsSecond());
                contentVideo.setShowGoodsSecond(showGoodsSecond());
                return contentVideo;
        }
}
