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
package vip.xiaonuo.manage.modular.userbalancelog.service.impl;

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
import vip.xiaonuo.manage.modular.userbalancelog.mapper.UserBalanceLogMapper;
import vip.xiaonuo.manage.modular.userbalancelog.param.*;
import vip.xiaonuo.manage.modular.userbalancelog.service.UserBalanceLogService;

import java.util.List;

/**
 * 平台-用户余额变更记录Service接口实现类
 *
 * @author yuezisheng
 * @date  2025/02/18 17:55
 **/
@Component
public class UserBalanceLogServiceImpl extends ServiceImpl<UserBalanceLogMapper, UserBalanceLog> implements UserBalanceLogService {

    @Override
    public Page<UserBalanceLog> page(UserBalanceLogPageParam userBalanceLogPageParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(ObjectUtil.isNotEmpty(userBalanceLogPageParam.getType())) {
            queryWrapper.eq(UserBalanceLog::getType, userBalanceLogPageParam.getType());
        }
        if(ObjectUtil.isNotEmpty(userBalanceLogPageParam.getBalanceType())) {
            queryWrapper.eq(UserBalanceLog::getBalanceType, userBalanceLogPageParam.getBalanceType());
        }
        if(ObjectUtil.isAllNotEmpty(userBalanceLogPageParam.getSortField(), userBalanceLogPageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(userBalanceLogPageParam.getSortOrder());
            queryWrapper.orderBy(StrUtil.toUnderlineCase(userBalanceLogPageParam.getSortField()),userBalanceLogPageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(UserBalanceLog::getCreateTime, false);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Tran
    @Override
    public void add(UserBalanceLogAddParam userBalanceLogAddParam) {
        UserBalanceLog userBalanceLog = BeanUtil.toBean(userBalanceLogAddParam, UserBalanceLog.class);
        this.save(userBalanceLog);
    }

    @Tran
    @Override
    public void edit(UserBalanceLogEditParam userBalanceLogEditParam) {
        UserBalanceLog userBalanceLog = this.queryEntity(userBalanceLogEditParam.getId());
        BeanUtil.copyProperties(userBalanceLogEditParam, userBalanceLog);
        this.updateById(userBalanceLog);
    }

    @Tran
    @Override
    public void delete(List<UserBalanceLogIdParam> userBalanceLogIdParamList) {
        // 执行删除
        this.removeByIds(CollStreamUtil.toList(userBalanceLogIdParamList, UserBalanceLogIdParam::getId));
    }

    @Override
    public UserBalanceLog detail(UserBalanceLogIdParam userBalanceLogIdParam) {
        return this.queryEntity(userBalanceLogIdParam.getId());
    }

    @Override
    public UserBalanceLog queryEntity(String id) {
        UserBalanceLog userBalanceLog = this.getById(id);
        if(ObjectUtil.isEmpty(userBalanceLog)) {
            throw new CommonException("平台-用户余额变更记录不存在，id值为：{}", id);
        }
        return userBalanceLog;
    }

    @Override
    public List<UserBalanceLog> getdetailByUserId(MobileUserBalanceLogUserIdParam userBalanceLogReasonIdParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        //只查詢部分字段
        queryWrapper.select(UserBalanceLog::getId, UserBalanceLog::getReason, UserBalanceLog::getChangeBalance, UserBalanceLog::getCreateTime, UserBalanceLog::getCreateUser);
        if(ObjectUtil.isNotEmpty(userBalanceLogReasonIdParam.getCreateUser())) {
            queryWrapper.eq(UserBalanceLog::getCreateUser, userBalanceLogReasonIdParam.getCreateUser());
        }
        if(ObjectUtil.isNotEmpty(userBalanceLogReasonIdParam.getCreateUser())) {
            queryWrapper.eq(UserBalanceLog::getCreateUser, userBalanceLogReasonIdParam.getCreateUser());
            if (userBalanceLogReasonIdParam.getBalanceType() != null) {
                queryWrapper.eq(UserBalanceLog::getBalanceType, userBalanceLogReasonIdParam.getBalanceType());
            }
        }
        //积分变更查询
        List<UserBalanceLog> list = this.list(queryWrapper);
        return list;
    }
}
