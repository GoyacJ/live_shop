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
package vip.xiaonuo.mobile.modular.manage.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.handle.Context;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.manage.modular.useraddress.entity.UserAddress;
import vip.xiaonuo.manage.modular.useraddress.mapper.UserAddressMapper;
import vip.xiaonuo.manage.modular.useraddress.param.UserAddressAddParam;
import vip.xiaonuo.manage.modular.useraddress.param.UserAddressEditParam;
import vip.xiaonuo.mobile.modular.manage.result.UserAddressResult;
import vip.xiaonuo.mobile.modular.manage.service.UserAddressService;

import java.util.ArrayList;
import java.util.List;

import static vip.xiaonuo.common.util.CommonServletUtil.getRequest;

/**
 * 用户收货地址Service接口实现类
 *
 **/
@Component
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements UserAddressService {

    @Tran
    @Override
    public void add(UserAddressAddParam userAddressAddParam) {
        Object uid = getRequest().attr("uid");
        if (ObjectUtil.isEmpty(uid)) {
            throw new CommonException("用户id不能为空");
        }
        UserAddress userAddress = BeanUtil.toBean(userAddressAddParam, UserAddress.class);
        userAddress.setCreateUser(Convert.toStr(uid));
        this.save(userAddress);
    }

    @Tran
    @Override
    public void edit(UserAddressEditParam userAddressEditParam) {
        Object uid = getRequest().attr("uid");
        if (ObjectUtil.isEmpty(uid)) {
            throw new CommonException("用户id不能为空");
        }
        UserAddress userAddress = this.getById(userAddressEditParam.getId());
        if(ObjectUtil.isEmpty(userAddress) || !userAddress.getCreateUser().equals(Convert.toStr(uid))) {
            throw new CommonException("用户收货地址不存在");
        }
        BeanUtil.copyProperties(userAddressEditParam, userAddress);
        this.updateById(userAddress);
    }

    @Override
    public UserAddressResult getUserAddress(String addressId) {
        UserAddress userAddress = this.getById(addressId);
        if(ObjectUtil.isEmpty(userAddress)) {
            throw new CommonException("用户收货地址不存在，id值为：{}", addressId);
        }
        UserAddressResult userAddressResult = new UserAddressResult();
        userAddressResult.setId(userAddress.getId());
        userAddressResult.setMobile(userAddress.getMobile());
        userAddressResult.setNickname(userAddress.getNickname());
        userAddressResult.setProvinceName(userAddress.getProvinceName());
        userAddressResult.setProvinceCode(userAddress.getProvinceCode());
        userAddressResult.setCityName(userAddress.getCityName());
        userAddressResult.setCityCode(userAddress.getCityCode());
        userAddressResult.setCountiesName(userAddress.getCountiesName());
        userAddressResult.setCountiesCode(userAddress.getCountiesCode());
        userAddressResult.setAddress(userAddress.getAddress());
        userAddressResult.setIsDefault(userAddress.getIsDefault());
        userAddressResult.setCreateTime(userAddress.getCreateTime());
        return userAddressResult;
    }

    @Override
    public List<UserAddressResult> getUserAddressList(String uid) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(!"-".equals(uid)) {
            queryWrapper.eq("create_user", uid).orderBy(UserAddress::getCreateTime,false);
        }
        List<UserAddress> list = this.list(queryWrapper);
        List<UserAddressResult> resultList = new ArrayList<>();
        if (list != null && !list.isEmpty()){
            for (UserAddress userAddress : list) {
                UserAddressResult userAddressResult = new UserAddressResult();
                userAddressResult.setId(userAddress.getId());
                userAddressResult.setMobile(userAddress.getMobile());
                userAddressResult.setNickname(userAddress.getNickname());
                userAddressResult.setProvinceName(userAddress.getProvinceName());
                userAddressResult.setProvinceCode(userAddress.getProvinceCode());
                userAddressResult.setCityName(userAddress.getCityName());
                userAddressResult.setCityCode(userAddress.getCityCode());
                userAddressResult.setCountiesName(userAddress.getCountiesName());
                userAddressResult.setCountiesCode(userAddress.getCountiesCode());
                userAddressResult.setAddress(userAddress.getAddress());
                userAddressResult.setIsDefault(userAddress.getIsDefault());
                userAddressResult.setCreateTime(userAddress.getCreateTime());
                resultList.add(userAddressResult);
            }
        }
        return resultList;
    }

    @Override
    public void deleteUserAddress(String uid, String addressId) {
        UserAddress userAddress = this.getById(addressId);
        if(ObjectUtil.isEmpty(userAddress)) {
            throw new CommonException("用户收货地址不存在，id值为：{}", addressId);
        }
        if(!userAddress.getCreateUser().equals(uid)) {
            throw new CommonException("用户收货地址不存在，id值为：{}", addressId);
        }
        this.removeById(addressId);
    }

    @Override
    public void setDefaultUserAddress(String uid, String addressId) {
        UserAddress userAddress = this.getById(addressId);
        if(ObjectUtil.isEmpty(userAddress)) {
            throw new CommonException("用户收货地址不存在", addressId);
        }
        if(!userAddress.getCreateUser().equals(uid)) {
            throw new CommonException("用户收货地址不存在", addressId);
        }
        userAddress.setIsDefault("1");
        this.updateById(userAddress);
        // this.update()
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("create_user", uid).ne("id", addressId);
        // this.update(queryWrapper);
        UserAddress batchUserAddress = new UserAddress();
        batchUserAddress.setIsDefault("2");
        this.update(batchUserAddress, queryWrapper);

    }


    @Override
    public UserAddressResult getDefaultAddress() {
        Object uid = getRequest().attr("uid");
        if (ObjectUtil.isEmpty(uid)) {
            throw new CommonException("用户id不能为空");
        }

        QueryWrapper queryWrapper = QueryWrapper.create();
        queryWrapper.eq(UserAddress::getCreateUser, Convert.toStr(uid));
        queryWrapper.eq(UserAddress::getIsDefault, "1");
        UserAddress userAddress = this.getOne(queryWrapper);
        if (ObjectUtil.isEmpty(userAddress)) {
            return new UserAddressResult();
        }
        return Convert.convert(UserAddressResult.class, userAddress);
    }
}
