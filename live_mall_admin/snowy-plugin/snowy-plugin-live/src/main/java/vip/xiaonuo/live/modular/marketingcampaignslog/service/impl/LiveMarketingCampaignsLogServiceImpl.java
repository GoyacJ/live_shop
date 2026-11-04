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
package vip.xiaonuo.live.modular.marketingcampaignslog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import org.noear.solon.annotation.Component;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.live.modular.marketingcampaignslog.entity.LiveMarketingCampaignsLog;
import vip.xiaonuo.live.modular.marketingcampaignslog.mapper.LiveMarketingCampaignsLogMapper;
import vip.xiaonuo.live.modular.marketingcampaignslog.param.LiveMarketingCampaignsLogAddParam;
import vip.xiaonuo.live.modular.marketingcampaignslog.param.LiveMarketingCampaignsLogEditParam;
import vip.xiaonuo.live.modular.marketingcampaignslog.param.LiveMarketingCampaignsLogIdParam;
import vip.xiaonuo.live.modular.marketingcampaignslog.param.LiveMarketingCampaignsLogPageParam;
import vip.xiaonuo.live.modular.marketingcampaignslog.service.LiveMarketingCampaignsLogService;

import java.util.List;

/**
 * 营销活动记录表Service接口实现类
 *
 * @author lgq
 * @date  2025/03/13 21:51
 **/
@Component
public class LiveMarketingCampaignsLogServiceImpl extends ServiceImpl<LiveMarketingCampaignsLogMapper, LiveMarketingCampaignsLog> implements LiveMarketingCampaignsLogService {

    @Override
    public Page<LiveMarketingCampaignsLog> page(LiveMarketingCampaignsLogPageParam liveMarketingCampaignsLogPageParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(ObjectUtil.isNotEmpty(liveMarketingCampaignsLogPageParam.getMarketingId())) {
            queryWrapper.eq(LiveMarketingCampaignsLog::getMarketingId, liveMarketingCampaignsLogPageParam.getMarketingId());
        }
        if(ObjectUtil.isNotEmpty(liveMarketingCampaignsLogPageParam.getUserId())) {
            queryWrapper.eq(LiveMarketingCampaignsLog::getUserId, liveMarketingCampaignsLogPageParam.getUserId());
        }
        if(ObjectUtil.isAllNotEmpty(liveMarketingCampaignsLogPageParam.getSortField(), liveMarketingCampaignsLogPageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(liveMarketingCampaignsLogPageParam.getSortOrder());
            queryWrapper.orderBy(StrUtil.toUnderlineCase(liveMarketingCampaignsLogPageParam.getSortField()),liveMarketingCampaignsLogPageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(LiveMarketingCampaignsLog::getId);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Tran
    @Override
    public void add(LiveMarketingCampaignsLogAddParam liveMarketingCampaignsLogAddParam) {
        LiveMarketingCampaignsLog liveMarketingCampaignsLog = BeanUtil.toBean(liveMarketingCampaignsLogAddParam, LiveMarketingCampaignsLog.class);
        this.save(liveMarketingCampaignsLog);
    }

    @Tran
    @Override
    public void edit(LiveMarketingCampaignsLogEditParam liveMarketingCampaignsLogEditParam) {
        LiveMarketingCampaignsLog liveMarketingCampaignsLog = this.queryEntity(liveMarketingCampaignsLogEditParam.getId());
        BeanUtil.copyProperties(liveMarketingCampaignsLogEditParam, liveMarketingCampaignsLog);
        this.updateById(liveMarketingCampaignsLog);
    }

    @Tran
    @Override
    public void delete(List<LiveMarketingCampaignsLogIdParam> liveMarketingCampaignsLogIdParamList) {
        // 执行删除
        this.removeByIds(CollStreamUtil.toList(liveMarketingCampaignsLogIdParamList, LiveMarketingCampaignsLogIdParam::getId));
    }

    @Override
    public LiveMarketingCampaignsLog detail(LiveMarketingCampaignsLogIdParam liveMarketingCampaignsLogIdParam) {
        return this.queryEntity(liveMarketingCampaignsLogIdParam.getId());
    }

    @Override
    public LiveMarketingCampaignsLog queryEntity(String id) {
        LiveMarketingCampaignsLog liveMarketingCampaignsLog = this.getById(id);
        if(ObjectUtil.isEmpty(liveMarketingCampaignsLog)) {
            throw new CommonException("营销活动记录表不存在，id值为：{}", id);
        }
        return liveMarketingCampaignsLog;
    }
}
