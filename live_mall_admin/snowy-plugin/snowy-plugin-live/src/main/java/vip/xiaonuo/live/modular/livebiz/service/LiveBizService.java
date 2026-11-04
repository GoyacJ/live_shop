/*
 * Copyright [2022] [https://www.xiaonuo.vip]
 *
 * Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Snowy源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.xiaonuo.vip
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队xiaonuobase@qq.com商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package vip.xiaonuo.live.modular.livebiz.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import vip.xiaonuo.live.modular.livebiz.entity.LiveBiz;
import vip.xiaonuo.live.modular.livebiz.param.LiveBizAddParam;
import vip.xiaonuo.live.modular.livebiz.param.LiveBizEditParam;
import vip.xiaonuo.live.modular.livebiz.param.LiveBizIdParam;
import vip.xiaonuo.live.modular.livebiz.param.LiveBizPageParam;

import java.util.List;

/**
 * 直播列表Service接口
 *
 * @author zsj
 * @date  2025/04/02 16:57
 **/
public interface LiveBizService extends IService<LiveBiz> {

    /**
     * 获取直播列表分页
     *
     * @author zsj
     * @date  2025/04/02 16:57
     */
    Page<LiveBiz> page(LiveBizPageParam liveBizPageParam);

    /**
     * 添加直播列表
     *
     * @author zsj
     * @date  2025/04/02 16:57
     */
    void add(LiveBizAddParam liveBizAddParam);

    /**
     * 编辑直播列表
     *
     * @author zsj
     * @date  2025/04/02 16:57
     */
    void edit(LiveBizEditParam liveBizEditParam);

    /**
     * 删除直播列表
     *
     * @author zsj
     * @date  2025/04/02 16:57
     */
    void delete(List<LiveBizIdParam> liveBizIdParamList);

    /**
     * 获取直播列表详情
     *
     * @author zsj
     * @date  2025/04/02 16:57
     */
    LiveBiz detail(LiveBizIdParam liveBizIdParam);

    /**
     * 获取直播列表详情
     *
     * @author zsj
     * @date  2025/04/02 16:57
     **/
    LiveBiz queryEntity(String id);
}
