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
package vip.xiaonuo.live.modular.spectatorgrade.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryCondition;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import org.noear.solon.annotation.Component;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.live.modular.spectatorgrade.entity.LiveSpectatorGrade;
import vip.xiaonuo.live.modular.spectatorgrade.mapper.LiveSpectatorGradeMapper;
import vip.xiaonuo.live.modular.spectatorgrade.param.LiveSpectatorGradeAddParam;
import vip.xiaonuo.live.modular.spectatorgrade.param.LiveSpectatorGradeEditParam;
import vip.xiaonuo.live.modular.spectatorgrade.param.LiveSpectatorGradeIdParam;
import vip.xiaonuo.live.modular.spectatorgrade.param.LiveSpectatorGradePageParam;
import vip.xiaonuo.live.modular.spectatorgrade.service.LiveSpectatorGradeService;

import java.util.List;

/**
 * 直播观众成长表Service接口实现类
 *
 * @author GMQ
 * @date  2025/04/06 13:06
 **/
@Component
public class LiveSpectatorGradeServiceImpl extends ServiceImpl<LiveSpectatorGradeMapper, LiveSpectatorGrade> implements LiveSpectatorGradeService {

    @Override
    public Page<LiveSpectatorGrade> page(LiveSpectatorGradePageParam liveSpectatorGradePageParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(ObjectUtil.isAllNotEmpty(liveSpectatorGradePageParam.getSortField(), liveSpectatorGradePageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(liveSpectatorGradePageParam.getSortOrder());
            queryWrapper.orderBy(StrUtil.toUnderlineCase(liveSpectatorGradePageParam.getSortField()),liveSpectatorGradePageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(LiveSpectatorGrade::getId);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Tran
    @Override
    public void add(LiveSpectatorGradeAddParam liveSpectatorGradeAddParam) {
        LiveSpectatorGrade liveSpectatorGrade = BeanUtil.toBean(liveSpectatorGradeAddParam, LiveSpectatorGrade.class);
        //没有对应数据就新增
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.from(LiveSpectatorGrade.class);
        queryWrapper.eq(LiveSpectatorGrade::getSpectatorId,liveSpectatorGrade.getSpectatorId());
        queryWrapper.eq(LiveSpectatorGrade::getAnchorId,liveSpectatorGrade.getAnchorId());
        LiveSpectatorGrade liveSpectatorGrade1 = this.getOne(queryWrapper);
        if (liveSpectatorGrade1 == null) {
            this.save(liveSpectatorGrade);
        }
    }

    @Tran
    @Override
    public void edit(LiveSpectatorGradeEditParam liveSpectatorGradeEditParam) {
        LiveSpectatorGrade liveSpectatorGrade = this.queryEntity(liveSpectatorGradeEditParam.getId());
        BeanUtil.copyProperties(liveSpectatorGradeEditParam, liveSpectatorGrade);
        this.updateById(liveSpectatorGrade);
    }

    @Tran
    @Override
    public void delete(List<LiveSpectatorGradeIdParam> liveSpectatorGradeIdParamList) {
        // 执行删除
        this.removeByIds(CollStreamUtil.toList(liveSpectatorGradeIdParamList, LiveSpectatorGradeIdParam::getId));
    }

    @Override
    public LiveSpectatorGrade detail(LiveSpectatorGradeIdParam liveSpectatorGradeIdParam) {
        return this.queryEntity(liveSpectatorGradeIdParam.getId());
    }

    @Override
    public LiveSpectatorGrade queryEntity(String id) {
        LiveSpectatorGrade liveSpectatorGrade = this.getById(id);
        if(ObjectUtil.isEmpty(liveSpectatorGrade)) {
            throw new CommonException("直播观众成长表不存在，id值为：{}", id);
        }
        return liveSpectatorGrade;
    }
}
