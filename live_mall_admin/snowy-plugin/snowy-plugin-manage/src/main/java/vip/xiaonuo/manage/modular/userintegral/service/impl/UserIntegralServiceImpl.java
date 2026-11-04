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
package vip.xiaonuo.manage.modular.userintegral.service.impl;

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
import vip.xiaonuo.manage.modular.userbalancelog.entity.UserBalanceLog;
import vip.xiaonuo.manage.modular.userintegral.entity.UserIntegral;
import vip.xiaonuo.manage.modular.userintegral.mapper.UserIntegralMapper;
import vip.xiaonuo.manage.modular.userintegral.param.*;
import vip.xiaonuo.manage.modular.userintegral.service.UserIntegralService;

import java.util.List;

/**
 * 用户积分兑换Service接口实现类
 *
 * @author yuezisheng
 * @date  2025/02/18 11:07
 **/
@Component
public class UserIntegralServiceImpl extends ServiceImpl<UserIntegralMapper, UserIntegral> implements UserIntegralService {

    @Override
    public Page<UserIntegral> page(UserIntegralPageParam userIntegralPageParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(ObjectUtil.isNotEmpty(userIntegralPageParam.getStartCreateTime()) && ObjectUtil.isNotEmpty(userIntegralPageParam.getEndCreateTime())) {
            queryWrapper.between(UserIntegral::getCreateTime, userIntegralPageParam.getStartCreateTime(), userIntegralPageParam.getEndCreateTime());
        }
        if(ObjectUtil.isAllNotEmpty(userIntegralPageParam.getSortField(), userIntegralPageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(userIntegralPageParam.getSortOrder());
            queryWrapper.orderBy(StrUtil.toUnderlineCase(userIntegralPageParam.getSortField()),userIntegralPageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(UserIntegral::getCreateTime,false);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Tran
    @Override
    public void add(UserIntegralAddParam userIntegralAddParam) {
        UserIntegral userIntegral = BeanUtil.toBean(userIntegralAddParam, UserIntegral.class);
        this.save(userIntegral);
    }

    @Tran
    @Override
    public void edit(UserIntegralEditParam userIntegralEditParam) {
        UserIntegral userIntegral = this.queryEntity(userIntegralEditParam.getId());
        BeanUtil.copyProperties(userIntegralEditParam, userIntegral);
        this.updateById(userIntegral);
    }

    @Tran
    @Override
    public void delete(List<UserIntegralIdParam> userIntegralIdParamList) {
        // 执行删除
        this.removeByIds(CollStreamUtil.toList(userIntegralIdParamList, UserIntegralIdParam::getId));
    }

    @Override
    public UserIntegral detail(UserIntegralIdParam userIntegralIdParam) {
        return this.queryEntity(userIntegralIdParam.getId());
    }

    @Override
    public UserIntegral queryEntity(String id) {
        UserIntegral userIntegral = this.getById(id);
        if(ObjectUtil.isEmpty(userIntegral)) {
            throw new CommonException("用户积分兑换不存在，id值为：{}", id);
        }
        return userIntegral;
    }

    @Override
    public List<UserIntegral> getUserIntegralByUserId(UserIntegralUserIdParam userIntegralUserIdParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        //只查詢部分字段
        queryWrapper.select(UserIntegral::getId, UserIntegral::getCreateUser);
        if(ObjectUtil.isNotEmpty(userIntegralUserIdParam.getCreateUser())) {
            queryWrapper.eq(UserIntegral::getCreateUser, userIntegralUserIdParam.getCreateUser());
        }
        List<UserIntegral> list = this.list(queryWrapper);
        return list;
    }

    @Override
    public void mobileAddUserIntegral(UserIntegralAddParam userIntegralAddParam) {
        //将信息存入user_integral
        UserIntegral userIntegral = BeanUtil.toBean(userIntegralAddParam, UserIntegral.class);
        this.save(userIntegral);
        //user_info中减去相应积分
    }
}
