package vip.xiaonuo.content.modular.shortplay.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import vip.xiaonuo.content.modular.shortplay.entity.ContentShortPlayEpisode;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/13 14:57
 */
@ApiModel(description = "短剧剧集信息")
public record ContentShortPlayEpisodeAddParam(

        @ApiModelProperty(value = "短剧ID")
        String shortPlayId,

        @ApiModelProperty(value = "标题")
        String title,

        @ApiModelProperty(value = "剧集编号")
        Integer episodeNumber,

        @ApiModelProperty(value = "封面图")
        String cover,

        @ApiModelProperty(value = "视频ID")
        String videoId,

        @ApiModelProperty(value = "视频地址")
        String videoUrl,

        @ApiModelProperty(value = "视频时长（秒）")
        Integer duration,

        @ApiModelProperty(value = "描述")
        String description,

        @ApiModelProperty(value = "是否免费")
        Boolean isFree,

        @ApiModelProperty(value = "排序")
        Integer sort,

        @ApiModelProperty("商品Id")
        String goodsId,

        @ApiModelProperty("展示商品开始秒数")
        Integer startShowGoodsSecond,

        @ApiModelProperty("展示商品秒数")
        Integer showGoodsSecond
) {

    public ContentShortPlayEpisode toBean() {
        ContentShortPlayEpisode bean = new ContentShortPlayEpisode();
        bean.setShortPlayId(shortPlayId);
        bean.setTitle(title);
        bean.setEpisodeNumber(episodeNumber);
        bean.setCover(cover);
        bean.setVideoId(videoId);
        bean.setVideoUrl(videoUrl);
        bean.setDuration(duration);
        bean.setDescription(description);
        bean.setIsFree(isFree);
        bean.setSort(sort);
        bean.setGoodsId(goodsId);
        bean.setStartShowGoodsSecond(startShowGoodsSecond);
        bean.setShowGoodsSecond(showGoodsSecond);
        return bean;
    }
}
