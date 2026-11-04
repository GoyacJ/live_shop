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
package vip.xiaonuo.manage.modular.userlevel.service.impl;

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
import vip.xiaonuo.manage.modular.userlevel.entity.UserLevel;
import vip.xiaonuo.manage.modular.userlevel.mapper.UserLevelMapper;
import vip.xiaonuo.manage.modular.userlevel.param.UserLevelAddParam;
import vip.xiaonuo.manage.modular.userlevel.param.UserLevelEditParam;
import vip.xiaonuo.manage.modular.userlevel.param.UserLevelIdParam;
import vip.xiaonuo.manage.modular.userlevel.param.UserLevelPageParam;
import vip.xiaonuo.manage.modular.userlevel.service.UserLevelService;

import java.util.List;

/**
 * 平台-用户等级Service接口实现类
 *
 * @author yuezisheng
 * @date  2025/02/18 09:16
 **/
@Component
public class UserLevelServiceImpl extends ServiceImpl<UserLevelMapper, UserLevel> implements UserLevelService {

    @Override
    public Page<UserLevel> page(UserLevelPageParam userLevelPageParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(ObjectUtil.isNotEmpty(userLevelPageParam.getName())) {
            queryWrapper.like(UserLevel::getName, userLevelPageParam.getName());
        }
        if(ObjectUtil.isNotEmpty(userLevelPageParam.getStartCreateTime()) && ObjectUtil.isNotEmpty(userLevelPageParam.getEndCreateTime())) {
            queryWrapper.between(UserLevel::getCreateTime, userLevelPageParam.getStartCreateTime(), userLevelPageParam.getEndCreateTime());
        }
        if(ObjectUtil.isAllNotEmpty(userLevelPageParam.getSortField(), userLevelPageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(userLevelPageParam.getSortOrder());
            queryWrapper.orderBy(StrUtil.toUnderlineCase(userLevelPageParam.getSortField()),userLevelPageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(UserLevel::getCreateTime,false);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Tran
    @Override
    public void add(UserLevelAddParam userLevelAddParam) {
        UserLevel userLevel = BeanUtil.toBean(userLevelAddParam, UserLevel.class);
        this.save(userLevel);
    }

    @Tran
    @Override
    public void edit(UserLevelEditParam userLevelEditParam) {
        UserLevel userLevel = this.queryEntity(userLevelEditParam.getId());
        BeanUtil.copyProperties(userLevelEditParam, userLevel);
        this.updateById(userLevel);
    }

    @Tran
    @Override
    public void delete(List<UserLevelIdParam> userLevelIdParamList) {
        // 执行删除
        this.removeByIds(CollStreamUtil.toList(userLevelIdParamList, UserLevelIdParam::getId));
    }

    @Override
    public UserLevel detail(UserLevelIdParam userLevelIdParam) {
        return this.queryEntity(userLevelIdParam.getId());
    }

    @Override
    public UserLevel queryEntity(String id) {
        UserLevel userLevel = this.getById(id);
        if(ObjectUtil.isEmpty(userLevel)) {
            throw new CommonException("平台-用户等级不存在，id值为：{}", id);
        }
        return userLevel;
    }
}
