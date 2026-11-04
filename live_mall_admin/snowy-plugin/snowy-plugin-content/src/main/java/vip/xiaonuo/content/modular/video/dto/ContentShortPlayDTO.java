package vip.xiaonuo.content.modular.video.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.xiaonuo.content.api.dto.ContentStatsDTO;
import vip.xiaonuo.content.modular.shortplay.entity.ContentShortPlay;

import java.io.Serial;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/11 19:57
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ContentShortPlayDTO extends ContentShortPlay {

    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "统计信息")
    private ContentStatsDTO contentStats;
}
