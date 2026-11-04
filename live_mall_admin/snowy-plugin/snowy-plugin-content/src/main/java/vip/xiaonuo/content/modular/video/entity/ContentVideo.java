package vip.xiaonuo.content.modular.video.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.xiaonuo.common.pojo.CommonEntity;

import java.io.Serial;

/**
 * <p>视频</p>
 *
 * @author goya
 * @since 2025/5/13 13:14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table("content_video")
public class ContentVideo extends CommonEntity {
    @Serial
    private static final long serialVersionUID = 6383069226203528292L;

    /**
     * ID
     */
    @Id
    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "分类")
    private String category;

    @ApiModelProperty(value = "标签")
    private String tags;

    @ApiModelProperty(value = "封面图")
    private String cover;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "视频地址")
    private String videoUrl;

    @ApiModelProperty(value = "视频时长（秒）")
    private Integer duration;

    @ApiModelProperty(value = "状态（0-未发布，1-已发布）")
    private Integer status;

    @ApiModelProperty(value = "来源")
    private String source;

    @ApiModelProperty(value = "是否可下载 0是1否")
    private Integer downloadable;

    @ApiModelProperty(value = "是否可分享 0是1否")
    private Integer shareable;

    @ApiModelProperty(value = "是否可评论 0是1否")
    private Integer allowComment;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty("商品Id")
    private String goodsId;

    @ApiModelProperty("展示商品开始秒数")
    private Integer startShowGoodsSecond;

    @ApiModelProperty("展示商品秒数")
    private Integer showGoodsSecond;


}
