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
package vip.xiaonuo.live.modular.marketingcampaigns.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import vip.xiaonuo.live.modular.marketingcampaigns.entity.MarketingCampaigns;
import vip.xiaonuo.live.modular.marketingcampaigns.param.MarketingCampaignsAddParam;
import vip.xiaonuo.live.modular.marketingcampaigns.param.MarketingCampaignsEditParam;
import vip.xiaonuo.live.modular.marketingcampaigns.param.MarketingCampaignsIdParam;
import vip.xiaonuo.live.modular.marketingcampaigns.param.MarketingCampaignsPageParam;

import java.util.List;

/**
 * 营销活动表Service接口
 *
 * @author lgq
 * @date  2025/03/04 14:15
 **/
public interface MarketingCampaignsService extends IService<MarketingCampaigns> {

    /**
     * 获取营销活动表分页
     *
     * @author lgq
     * @date  2025/03/04 14:15
     */
    Page<MarketingCampaigns> page(MarketingCampaignsPageParam marketingCampaignsPageParam);

    /**
     * 添加营销活动表
     *
     * @author lgq
     * @date  2025/03/04 14:15
     */
    boolean add(MarketingCampaignsAddParam marketingCampaignsAddParam);

    /**
     * 编辑营销活动表
     *
     * @author lgq
     * @date  2025/03/04 14:15
     */
    void edit(MarketingCampaignsEditParam marketingCampaignsEditParam);

    /**
     * 删除营销活动表
     *
     * @author lgq
     * @date  2025/03/04 14:15
     */
    void delete(List<MarketingCampaignsIdParam> marketingCampaignsIdParamList);

    /**
     * 获取营销活动表详情
     *
     * @author lgq
     * @date  2025/03/04 14:15
     */
    MarketingCampaigns detail(MarketingCampaignsIdParam marketingCampaignsIdParam);

    /**
     * 获取营销活动表详情
     *
     * @author lgq
     * @date  2025/03/04 14:15
     **/
    MarketingCampaigns queryEntity(String id);

    /**
     * 获取营销活动表详情
     *
     * @author lgq
     * @date  2025/03/04 14:15
     **/
    MarketingCampaigns getOne(String marketingId);

    /**
     * 更新营销活动状态
     * @param marketingId
     * @param marketingState
     * @return
     */
    int updateMarketingId(String marketingId,String marketingState);
}
