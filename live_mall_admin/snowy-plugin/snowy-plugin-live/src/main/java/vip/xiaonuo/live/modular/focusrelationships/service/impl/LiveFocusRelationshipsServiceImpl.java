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
package vip.xiaonuo.live.modular.focusrelationships.service.impl;

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
import vip.xiaonuo.live.modular.focusrelationships.entity.LiveFocusRelationships;
import vip.xiaonuo.live.modular.focusrelationships.mapper.LiveFocusRelationshipsMapper;
import vip.xiaonuo.live.modular.focusrelationships.param.LiveFocusRelationshipsAddParam;
import vip.xiaonuo.live.modular.focusrelationships.param.LiveFocusRelationshipsEditParam;
import vip.xiaonuo.live.modular.focusrelationships.param.LiveFocusRelationshipsIdParam;
import vip.xiaonuo.live.modular.focusrelationships.param.LiveFocusRelationshipsPageParam;
import vip.xiaonuo.live.modular.focusrelationships.service.LiveFocusRelationshipsService;

import java.util.List;

/**
 * 关注关系表Service接口实现类
 *
 * @author lgq
 * @date  2025/03/04 14:07
 **/
@Component
public class LiveFocusRelationshipsServiceImpl extends ServiceImpl<LiveFocusRelationshipsMapper, LiveFocusRelationships> implements LiveFocusRelationshipsService {

    @Override
    public Page<LiveFocusRelationships> page(LiveFocusRelationshipsPageParam liveFocusRelationshipsPageParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(ObjectUtil.isNotEmpty(liveFocusRelationshipsPageParam.getFollowedId())) {
            queryWrapper.eq(LiveFocusRelationships::getFollowedId, liveFocusRelationshipsPageParam.getFollowedId());
        }
        if(ObjectUtil.isNotEmpty(liveFocusRelationshipsPageParam.getFollowersId())) {
            queryWrapper.eq(LiveFocusRelationships::getFollowersId, liveFocusRelationshipsPageParam.getFollowersId());
        }
        if(ObjectUtil.isAllNotEmpty(liveFocusRelationshipsPageParam.getSortField(), liveFocusRelationshipsPageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(liveFocusRelationshipsPageParam.getSortOrder());
            queryWrapper.orderBy(StrUtil.toUnderlineCase(liveFocusRelationshipsPageParam.getSortField()),liveFocusRelationshipsPageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(LiveFocusRelationships::getId);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Tran
    @Override
    public void add(LiveFocusRelationshipsAddParam liveFocusRelationshipsAddParam) {
        LiveFocusRelationships liveFocusRelationships = BeanUtil.toBean(liveFocusRelationshipsAddParam, LiveFocusRelationships.class);
        this.save(liveFocusRelationships);
    }

    @Tran
    @Override
    public void edit(LiveFocusRelationshipsEditParam liveFocusRelationshipsEditParam) {
        LiveFocusRelationships liveFocusRelationships = this.queryEntity(liveFocusRelationshipsEditParam.getId());
        BeanUtil.copyProperties(liveFocusRelationshipsEditParam, liveFocusRelationships);
        this.updateById(liveFocusRelationships);
    }

    @Tran
    @Override
    public void delete(List<LiveFocusRelationshipsIdParam> liveFocusRelationshipsIdParamList) {
        // 执行删除
        this.removeByIds(CollStreamUtil.toList(liveFocusRelationshipsIdParamList, LiveFocusRelationshipsIdParam::getId));
    }

    @Override
    public LiveFocusRelationships detail(LiveFocusRelationshipsIdParam liveFocusRelationshipsIdParam) {
        return this.queryEntity(liveFocusRelationshipsIdParam.getId());
    }

    @Override
    public LiveFocusRelationships queryEntity(String id) {
        LiveFocusRelationships liveFocusRelationships = this.getById(id);
        if(ObjectUtil.isEmpty(liveFocusRelationships)) {
            throw new CommonException("关注关系表不存在，id值为：{}", id);
        }
        return liveFocusRelationships;
    }
}
