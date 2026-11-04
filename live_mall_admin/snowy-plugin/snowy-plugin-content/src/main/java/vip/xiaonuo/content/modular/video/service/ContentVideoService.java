package vip.xiaonuo.content.modular.video.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import vip.xiaonuo.common.pojo.CommonValidList;
import vip.xiaonuo.content.api.dto.VideoByActionPageDTO;
import vip.xiaonuo.content.modular.video.dto.ContentVideoDTO;
import vip.xiaonuo.content.modular.video.entity.ContentVideo;
import vip.xiaonuo.content.modular.video.param.*;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/13 13:30
 */
public interface ContentVideoService extends IService<ContentVideo> {

    /**
     * 分页条件查询
     * @param param 条件
     * @return 分页
     */
    Page<ContentVideoDTO> page(ContentVideoPageParam param);

    /**
     * 详情
     * @param id 视频Id
     * @return 详情
     */
    ContentVideo detail(String id);

    /**
     * 新增
     * @param param 参数
     */
    void add(ContentVideoAddParam param);

    /**
     * 编辑
     * @param param 参数
     */
    void edit(ContentVideoEditParam param);

    /**
     * 删除
     * @param param 参数
     */
    void delete(CommonValidList<ContentVideoIdParam> param);

    void changeStatus(ContentVideoChangeStatusParam param);

    /**
     * 批量新增
     * @param params 参数
     */
    void batchAdd(CommonValidList<ContentVideoAddParam> params);

    /**
     * 视频分页
     * @param dto 参数
     * @return 结果
     */
    Page<ContentVideoDTO> videoByActionPage(VideoByActionPageDTO dto);

    /**
     * 获取视频信息
     * @param videoId 视频Id
     * @return 结果
     */
    ContentVideoDTO getVideoByAction(String videoId);
}
