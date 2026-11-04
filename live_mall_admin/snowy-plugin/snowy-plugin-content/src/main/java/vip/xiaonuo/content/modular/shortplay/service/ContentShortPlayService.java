package vip.xiaonuo.content.modular.shortplay.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import vip.xiaonuo.common.pojo.CommonValidList;
import vip.xiaonuo.content.api.dto.ShortPlayByActionPageDTO;
import vip.xiaonuo.content.modular.shortplay.entity.ContentShortPlay;
import vip.xiaonuo.content.modular.shortplay.entity.ContentShortPlayEpisode;
import vip.xiaonuo.content.modular.shortplay.param.*;
import vip.xiaonuo.content.modular.video.dto.ContentShortPlayDTO;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/13 13:33
 */
public interface ContentShortPlayService extends IService<ContentShortPlay> {

    /**
     * 分页条件查询
     * @param param 条件
     * @return 结果
     */
    Page<ContentShortPlayDTO> page(ContentShortPlayPageParam param);

    /**
     * 查看详情
     * @param id id
     * @return 结果
     */
    ContentShortPlay detail(String id);

    /**
     * 新增
     * @param param 参数
     */
    void add(ContentShortPlayAddParam param);

    /**
     * 编辑
     * @param param 参数
     */
    void edit(ContentShortPlayEditParam param);

    /**
     * 删除
     * @param param 参数
     */
    void delete(CommonValidList<ContentShortPlayIdParam> param);

    /**
     * 修改状态
     * @param param 参数
     */
    void changeStatus(ContentShortPlayChangeStatusParam param);

    /**
     * 剧集分页
     * @param param 参数
     * @return 结果
     */
    Page<ContentShortPlayEpisode> episodePage(ContentShortPlayEpisodePageParam param);

    /**
     * 剧集详情
     * @param id 剧集id
     * @return 结果
     */
    ContentShortPlayEpisode episodeDetail(String id);

    /**
     * 新增剧集
     * @param param 参数
     */
    void episodeAdd(ContentShortPlayEpisodeAddParam param);

    /**
     * 编辑剧集
     * @param param 参数
     */
    void episodeEdit(ContentShortPlayEpisodeEditParam param);

    /**
     * 删除剧集
     * @param param 参数
     */
    void episodeDelete(CommonValidList<ContentShortPlayEpisodeIdParam> param);

    /**
     * 批量新增剧集
     * @param params 参数
     */
    void episodeBatchAdd(CommonValidList<ContentShortPlayEpisodeAddParam> params);


    /**
     * 短剧首页
     * @param dto 参数
     * @return 结果
     */
    Page<ContentShortPlay> shortPlayIndexByActionPage(ShortPlayByActionPageDTO dto);

    /**
     * 短剧分页
     * @param dto 参数
     * @return 结果
     */
    Page<ContentShortPlayDTO> shortPlayByActionPage(ShortPlayByActionPageDTO dto);
}
