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
package vip.xiaonuo.live.modular.marketingcampaigns.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import org.noear.solon.annotation.Component;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.live.modular.livebiz.entity.LiveBiz;
import vip.xiaonuo.live.modular.marketingcampaigns.entity.MarketingCampaigns;
import vip.xiaonuo.live.modular.marketingcampaigns.mapper.MarketingCampaignsMapper;
import vip.xiaonuo.live.modular.marketingcampaigns.param.MarketingCampaignsAddParam;
import vip.xiaonuo.live.modular.marketingcampaigns.param.MarketingCampaignsEditParam;
import vip.xiaonuo.live.modular.marketingcampaigns.param.MarketingCampaignsIdParam;
import vip.xiaonuo.live.modular.marketingcampaigns.param.MarketingCampaignsPageParam;
import vip.xiaonuo.live.modular.marketingcampaigns.service.MarketingCampaignsService;

import java.util.List;

/**
 * 营销活动表Service接口实现类
 *
 * @author lgq
 * @date  2025/03/04 14:15
 **/
@Component
public class MarketingCampaignsServiceImpl extends ServiceImpl<MarketingCampaignsMapper, MarketingCampaigns> implements MarketingCampaignsService {

    @Override
    public Page<MarketingCampaigns> page(MarketingCampaignsPageParam marketingCampaignsPageParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(ObjectUtil.isNotEmpty(marketingCampaignsPageParam.getMarketingId())) {
            queryWrapper.like(MarketingCampaigns::getMarketingId, marketingCampaignsPageParam.getMarketingId());
        }
        if(ObjectUtil.isNotEmpty(marketingCampaignsPageParam.getLiveId())) {
            queryWrapper.like(MarketingCampaigns::getLiveId, marketingCampaignsPageParam.getLiveId());
        }
        if(ObjectUtil.isAllNotEmpty(marketingCampaignsPageParam.getSortField(), marketingCampaignsPageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(marketingCampaignsPageParam.getSortOrder());
            queryWrapper.orderBy(StrUtil.toUnderlineCase(marketingCampaignsPageParam.getSortField()),marketingCampaignsPageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(MarketingCampaigns::getId);
        }

        queryWrapper.select("live_marketing_campaigns.*", "live_biz.main_title as live_name");
        queryWrapper.leftJoin(LiveBiz.class).on(MarketingCampaigns::getLiveId, LiveBiz::getId);
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Tran
    @Override
    public boolean add(MarketingCampaignsAddParam marketingCampaignsAddParam) {
        MarketingCampaigns marketingCampaigns = BeanUtil.toBean(marketingCampaignsAddParam, MarketingCampaigns.class);
        return this.save(marketingCampaigns);
    }

    @Tran
    @Override
    public void edit(MarketingCampaignsEditParam marketingCampaignsEditParam) {
        MarketingCampaigns marketingCampaigns = this.queryEntity(marketingCampaignsEditParam.getId());
        BeanUtil.copyProperties(marketingCampaignsEditParam, marketingCampaigns);
        this.updateById(marketingCampaigns);
    }

    @Tran
    @Override
    public void delete(List<MarketingCampaignsIdParam> marketingCampaignsIdParamList) {
        // 执行删除
        this.removeByIds(CollStreamUtil.toList(marketingCampaignsIdParamList, MarketingCampaignsIdParam::getId));
    }

    @Override
    public MarketingCampaigns detail(MarketingCampaignsIdParam marketingCampaignsIdParam) {
        return this.queryEntity(marketingCampaignsIdParam.getId());
    }

    @Override
    public MarketingCampaigns queryEntity(String id) {
        MarketingCampaigns marketingCampaigns = this.getById(id);
        if(ObjectUtil.isEmpty(marketingCampaigns)) {
            throw new CommonException("营销活动表不存在，id值为：{}", id);
        }
        return marketingCampaigns;
    }


    @Override
    public MarketingCampaigns getOne(String marketingId) {
        QueryWrapper queryWrapper = new QueryWrapper().eq("MARKETING_ID", marketingId);
        MarketingCampaigns marketingCampaigns = this.getOne(queryWrapper);
        if(ObjectUtil.isEmpty(marketingCampaigns)) {
            throw new CommonException("营销活动表不存在，id值为：{}", marketingId);
        }
        return marketingCampaigns;
    }

    @Override
    public int updateMarketingId(String marketingId, String marketingState) {
        QueryWrapper queryWrapper = new QueryWrapper().eq("MARKETING_ID", marketingId);
        MarketingCampaigns one = getOne(queryWrapper);
        one.setMarketingState(Integer.parseInt(marketingState));
        boolean b = this.updateById(one);
        return b ? 1 : 0;
    }
}
