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
package vip.xiaonuo.manage.modular.userwithdrawallog.service.impl;

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
import vip.xiaonuo.manage.modular.userwithdrawallog.entity.UserWithdrawalLog;
import vip.xiaonuo.manage.modular.userwithdrawallog.mapper.UserWithdrawalLogMapper;
import vip.xiaonuo.manage.modular.userwithdrawallog.param.UserWithdrawalLogAddParam;
import vip.xiaonuo.manage.modular.userwithdrawallog.param.UserWithdrawalLogEditParam;
import vip.xiaonuo.manage.modular.userwithdrawallog.param.UserWithdrawalLogIdParam;
import vip.xiaonuo.manage.modular.userwithdrawallog.param.UserWithdrawalLogPageParam;
import vip.xiaonuo.manage.modular.userwithdrawallog.service.UserWithdrawalLogService;

import java.util.List;

/**
 * 用户提现记录Service接口实现类
 *
 * @author yuezisheng
 * @date  2025/02/18 20:31
 **/
@Component
public class UserWithdrawalLogServiceImpl extends ServiceImpl<UserWithdrawalLogMapper, UserWithdrawalLog> implements UserWithdrawalLogService {

    @Override
    public Page<UserWithdrawalLog> page(UserWithdrawalLogPageParam userWithdrawalLogPageParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(ObjectUtil.isNotEmpty(userWithdrawalLogPageParam.getStatus())) {
            queryWrapper.eq(UserWithdrawalLog::getStatus, userWithdrawalLogPageParam.getStatus());
        }
        if(ObjectUtil.isNotEmpty(userWithdrawalLogPageParam.getStartCreateTime()) && ObjectUtil.isNotEmpty(userWithdrawalLogPageParam.getEndCreateTime())) {
            queryWrapper.between(UserWithdrawalLog::getCreateTime, userWithdrawalLogPageParam.getStartCreateTime(), userWithdrawalLogPageParam.getEndCreateTime());
        }
        if(ObjectUtil.isAllNotEmpty(userWithdrawalLogPageParam.getSortField(), userWithdrawalLogPageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(userWithdrawalLogPageParam.getSortOrder());
            queryWrapper.orderBy(StrUtil.toUnderlineCase(userWithdrawalLogPageParam.getSortField()),userWithdrawalLogPageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(UserWithdrawalLog::getCreateTime, false);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Tran
    @Override
    public void add(UserWithdrawalLogAddParam userWithdrawalLogAddParam) {
        UserWithdrawalLog userWithdrawalLog = BeanUtil.toBean(userWithdrawalLogAddParam, UserWithdrawalLog.class);
        this.save(userWithdrawalLog);
    }

    @Tran
    @Override
    public void edit(UserWithdrawalLogEditParam userWithdrawalLogEditParam) {
        UserWithdrawalLog userWithdrawalLog = this.queryEntity(userWithdrawalLogEditParam.getId());
        BeanUtil.copyProperties(userWithdrawalLogEditParam, userWithdrawalLog);
        this.updateById(userWithdrawalLog);
    }

    @Tran
    @Override
    public void delete(List<UserWithdrawalLogIdParam> userWithdrawalLogIdParamList) {
        // 执行删除
        this.removeByIds(CollStreamUtil.toList(userWithdrawalLogIdParamList, UserWithdrawalLogIdParam::getId));
    }

    @Override
    public UserWithdrawalLog detail(UserWithdrawalLogIdParam userWithdrawalLogIdParam) {
        return this.queryEntity(userWithdrawalLogIdParam.getId());
    }

    @Override
    public UserWithdrawalLog queryEntity(String id) {
        UserWithdrawalLog userWithdrawalLog = this.getById(id);
        if(ObjectUtil.isEmpty(userWithdrawalLog)) {
            throw new CommonException("用户提现记录不存在，id值为：{}", id);
        }
        return userWithdrawalLog;
    }
}
