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
package vip.xiaonuo.manage.modular.useraddress.service.impl;

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
import vip.xiaonuo.manage.modular.useraddress.entity.UserAddress;
import vip.xiaonuo.manage.modular.useraddress.mapper.UserAddressMapper;
import vip.xiaonuo.manage.modular.useraddress.param.UserAddressAddParam;
import vip.xiaonuo.manage.modular.useraddress.param.UserAddressEditParam;
import vip.xiaonuo.manage.modular.useraddress.param.UserAddressIdParam;
import vip.xiaonuo.manage.modular.useraddress.param.UserAddressPageParam;
import vip.xiaonuo.manage.modular.useraddress.service.UserAddressService;

import java.util.List;

/**
 * 商城-用户收获地址Service接口实现类
 *
 * @author yuezisheng
 * @date  2025/02/18 21:28
 **/
@Component
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements UserAddressService {

    @Override
    public Page<UserAddress> page(UserAddressPageParam userAddressPageParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(ObjectUtil.isNotEmpty(userAddressPageParam.getMobile())) {
            queryWrapper.like(UserAddress::getMobile, userAddressPageParam.getMobile());
        }
        if(ObjectUtil.isNotEmpty(userAddressPageParam.getNickname())) {
            queryWrapper.like(UserAddress::getNickname, userAddressPageParam.getNickname());
        }
        if(ObjectUtil.isNotEmpty(userAddressPageParam.getStartCreateTime()) && ObjectUtil.isNotEmpty(userAddressPageParam.getEndCreateTime())) {
            queryWrapper.between(UserAddress::getCreateTime, userAddressPageParam.getStartCreateTime(), userAddressPageParam.getEndCreateTime());
        }
        if(ObjectUtil.isAllNotEmpty(userAddressPageParam.getSortField(), userAddressPageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(userAddressPageParam.getSortOrder());
            queryWrapper.orderBy(StrUtil.toUnderlineCase(userAddressPageParam.getSortField()),userAddressPageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(UserAddress::getCreateTime, false);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Tran
    @Override
    public void add(UserAddressAddParam userAddressAddParam) {
        UserAddress userAddress = BeanUtil.toBean(userAddressAddParam, UserAddress.class);
        this.save(userAddress);
    }

    @Tran
    @Override
    public void edit(UserAddressEditParam userAddressEditParam) {
        UserAddress userAddress = this.queryEntity(userAddressEditParam.getId());
        BeanUtil.copyProperties(userAddressEditParam, userAddress);
        this.updateById(userAddress);
    }

    @Tran
    @Override
    public void delete(List<UserAddressIdParam> userAddressIdParamList) {
        // 执行删除
        this.removeByIds(CollStreamUtil.toList(userAddressIdParamList, UserAddressIdParam::getId));
    }

    @Override
    public UserAddress detail(UserAddressIdParam userAddressIdParam) {
        return this.queryEntity(userAddressIdParam.getId());
    }

    @Override
    public UserAddress queryEntity(String id) {
        UserAddress userAddress = this.getById(id);
        if(ObjectUtil.isEmpty(userAddress)) {
            throw new CommonException("商城-用户收获地址不存在，id值为：{}", id);
        }
        return userAddress;
    }
}
