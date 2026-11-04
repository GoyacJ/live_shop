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
package vip.xiaonuo.live.modular.focusrelationships.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import vip.xiaonuo.live.modular.focusrelationships.entity.LiveFocusRelationships;
import vip.xiaonuo.live.modular.focusrelationships.param.LiveFocusRelationshipsAddParam;
import vip.xiaonuo.live.modular.focusrelationships.param.LiveFocusRelationshipsEditParam;
import vip.xiaonuo.live.modular.focusrelationships.param.LiveFocusRelationshipsIdParam;
import vip.xiaonuo.live.modular.focusrelationships.param.LiveFocusRelationshipsPageParam;

import java.util.List;

/**
 * 关注关系表Service接口
 *
 * @author lgq
 * @date  2025/03/04 14:07
 **/
public interface LiveFocusRelationshipsService extends IService<LiveFocusRelationships> {

    /**
     * 获取关注关系表分页
     *
     * @author lgq
     * @date  2025/03/04 14:07
     */
    Page<LiveFocusRelationships> page(LiveFocusRelationshipsPageParam liveFocusRelationshipsPageParam);

    /**
     * 添加关注关系表
     *
     * @author lgq
     * @date  2025/03/04 14:07
     */
    void add(LiveFocusRelationshipsAddParam liveFocusRelationshipsAddParam);

    /**
     * 编辑关注关系表
     *
     * @author lgq
     * @date  2025/03/04 14:07
     */
    void edit(LiveFocusRelationshipsEditParam liveFocusRelationshipsEditParam);

    /**
     * 删除关注关系表
     *
     * @author lgq
     * @date  2025/03/04 14:07
     */
    void delete(List<LiveFocusRelationshipsIdParam> liveFocusRelationshipsIdParamList);

    /**
     * 获取关注关系表详情
     *
     * @author lgq
     * @date  2025/03/04 14:07
     */
    LiveFocusRelationships detail(LiveFocusRelationshipsIdParam liveFocusRelationshipsIdParam);

    /**
     * 获取关注关系表详情
     *
     * @author lgq
     * @date  2025/03/04 14:07
     **/
    LiveFocusRelationships queryEntity(String id);
}
