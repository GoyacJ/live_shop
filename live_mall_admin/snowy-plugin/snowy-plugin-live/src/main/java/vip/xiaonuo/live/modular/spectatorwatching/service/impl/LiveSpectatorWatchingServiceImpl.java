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
package vip.xiaonuo.live.modular.spectatorwatching.service.impl;

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
import vip.xiaonuo.live.modular.spectatorwatching.entity.LiveSpectatorWatching;
import vip.xiaonuo.live.modular.spectatorwatching.mapper.LiveSpectatorWatchingMapper;
import vip.xiaonuo.live.modular.spectatorwatching.param.LiveSpectatorWatchingAddParam;
import vip.xiaonuo.live.modular.spectatorwatching.param.LiveSpectatorWatchingEditParam;
import vip.xiaonuo.live.modular.spectatorwatching.param.LiveSpectatorWatchingIdParam;
import vip.xiaonuo.live.modular.spectatorwatching.param.LiveSpectatorWatchingPageParam;
import vip.xiaonuo.live.modular.spectatorwatching.service.LiveSpectatorWatchingService;

import java.util.List;

/**
 * 直播观众观看时间表Service接口实现类
 *
 * @author GMQ
 * @date  2025/04/06 13:10
 **/
@Component
public class LiveSpectatorWatchingServiceImpl extends ServiceImpl<LiveSpectatorWatchingMapper, LiveSpectatorWatching> implements LiveSpectatorWatchingService {

    @Override
    public Page<LiveSpectatorWatching> page(LiveSpectatorWatchingPageParam liveSpectatorWatchingPageParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(ObjectUtil.isAllNotEmpty(liveSpectatorWatchingPageParam.getSortField(), liveSpectatorWatchingPageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(liveSpectatorWatchingPageParam.getSortOrder());
            queryWrapper.orderBy(StrUtil.toUnderlineCase(liveSpectatorWatchingPageParam.getSortField()),liveSpectatorWatchingPageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(LiveSpectatorWatching::getId);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Tran
    @Override
    public void add(LiveSpectatorWatchingAddParam liveSpectatorWatchingAddParam) {
        LiveSpectatorWatching liveSpectatorWatching = BeanUtil.toBean(liveSpectatorWatchingAddParam, LiveSpectatorWatching.class);

        //取最新的一条未结束的记录

        this.save(liveSpectatorWatching);
    }

    @Tran
    @Override
    public void edit(LiveSpectatorWatchingEditParam liveSpectatorWatchingEditParam) {
        LiveSpectatorWatching liveSpectatorWatching = this.queryEntity(liveSpectatorWatchingEditParam.getId());
        BeanUtil.copyProperties(liveSpectatorWatchingEditParam, liveSpectatorWatching);
        this.updateById(liveSpectatorWatching);
    }

    @Tran
    @Override
    public void delete(List<LiveSpectatorWatchingIdParam> liveSpectatorWatchingIdParamList) {
        // 执行删除
        this.removeByIds(CollStreamUtil.toList(liveSpectatorWatchingIdParamList, LiveSpectatorWatchingIdParam::getId));
    }

    @Override
    public LiveSpectatorWatching detail(LiveSpectatorWatchingIdParam liveSpectatorWatchingIdParam) {
        return this.queryEntity(liveSpectatorWatchingIdParam.getId());
    }

    @Override
    public LiveSpectatorWatching queryEntity(String id) {
        LiveSpectatorWatching liveSpectatorWatching = this.getById(id);
        if(ObjectUtil.isEmpty(liveSpectatorWatching)) {
            throw new CommonException("直播观众观看时间表不存在，id值为：{}", id);
        }
        return liveSpectatorWatching;
    }
}
