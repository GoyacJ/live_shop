package vip.xiaonuo.content.modular.shortplay.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import vip.xiaonuo.common.pojo.CommonValidList;
import vip.xiaonuo.content.api.dto.ShortPlayEpisodeByActionPageDTO;
import vip.xiaonuo.content.modular.shortplay.entity.ContentShortPlayEpisode;
import vip.xiaonuo.content.modular.shortplay.param.ContentShortPlayEpisodeAddParam;
import vip.xiaonuo.content.modular.shortplay.param.ContentShortPlayEpisodeEditParam;
import vip.xiaonuo.content.modular.shortplay.param.ContentShortPlayEpisodeIdParam;
import vip.xiaonuo.content.modular.shortplay.param.ContentShortPlayEpisodePageParam;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/14 13:42
 */
public interface ContentShortPlayEpisodeService extends IService<ContentShortPlayEpisode> {

    /**
     * 剧集分页
     * @param param 参数
     * @return 结果
     */
    Page<ContentShortPlayEpisode> page(ContentShortPlayEpisodePageParam param);

    /**
     * 新增剧集
     * @param param 参数
     */
    void add(ContentShortPlayEpisodeAddParam param);

    /**
     * 编辑剧集
     * @param param 参数
     */
    void edit(ContentShortPlayEpisodeEditParam param);

    /**
     * 删除剧集
     * @param param 参数
     */
    void delete(CommonValidList<ContentShortPlayEpisodeIdParam> param);

    /**
     * 批量新增
     * @param params 参数
     */
    void batchAdd(CommonValidList<ContentShortPlayEpisodeAddParam> params);

    /**
     * 剧集分页
     * @param dto 参数
     * @return 结果
     */
    Page<ContentShortPlayEpisode> shortPlayEpisodeByActionPage(ShortPlayEpisodeByActionPageDTO dto);
}
