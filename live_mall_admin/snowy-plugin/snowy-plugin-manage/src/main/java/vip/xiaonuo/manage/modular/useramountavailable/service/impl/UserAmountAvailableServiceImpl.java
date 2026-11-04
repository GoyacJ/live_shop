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
package vip.xiaonuo.manage.modular.useramountavailable.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import org.noear.solon.annotation.Component;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.manage.modular.useramountavailable.entity.UserAmountAvailable;
import vip.xiaonuo.manage.modular.useramountavailable.mapper.UserAmountAvailableMapper;
import vip.xiaonuo.manage.modular.useramountavailable.param.UserAmountAvailableAddParam;
import vip.xiaonuo.manage.modular.useramountavailable.param.UserAmountAvailableEditParam;
import vip.xiaonuo.manage.modular.useramountavailable.param.UserAmountAvailableIdParam;
import vip.xiaonuo.manage.modular.useramountavailable.param.UserAmountAvailablePageParam;
import vip.xiaonuo.manage.modular.useramountavailable.service.UserAmountAvailableService;

import java.util.List;

/**
 * 可选金额Service接口实现类
 *
 */
@Component
public class UserAmountAvailableServiceImpl extends ServiceImpl<UserAmountAvailableMapper, UserAmountAvailable> implements UserAmountAvailableService {

    @Override
    public Page<UserAmountAvailable> page(UserAmountAvailablePageParam userAmountAvailablePageParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.orderBy(UserAmountAvailable::getCreateTime,false);
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Override
    public void edit(UserAmountAvailableEditParam userAmountAvailableEditParam) {
        UserAmountAvailable userAmountAvailable = this.getById(userAmountAvailableEditParam.getId());
        if(ObjectUtil.isEmpty(userAmountAvailable)) {
            throw new CommonException("可选金额不存在，id值为：{}", userAmountAvailableEditParam.getId());
        }
        BeanUtil.copyProperties(userAmountAvailableEditParam, userAmountAvailable);
        this.updateById(userAmountAvailable);
    }

    @Override
    public void delete(List<UserAmountAvailableIdParam> userAmountAvailableIdParamList) {
        this.removeByIds(CollStreamUtil.toList(userAmountAvailableIdParamList, UserAmountAvailableIdParam::getId));
    }

    @Override
    public UserAmountAvailable detail(UserAmountAvailableIdParam userAmountAvailableIdParam) {
        UserAmountAvailable userAmountAvailable = this.getById(userAmountAvailableIdParam.getId());
        if(ObjectUtil.isEmpty(userAmountAvailable)) {
            throw new CommonException("可选金额不存在，id值为：{}", userAmountAvailableIdParam.getId());
        }
        return userAmountAvailable;
    }

    @Override
    public void add(UserAmountAvailableAddParam userAmountAvailableAddParam) {
        UserAmountAvailable userAmountAvailable = BeanUtil.toBean(userAmountAvailableAddParam, UserAmountAvailable.class);
        this.save(userAmountAvailable);
    }
}
