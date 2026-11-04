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
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.handle.Context;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.manage.modular.userintegral.entity.UserIntegral;
import vip.xiaonuo.manage.modular.userwithdrawallog.entity.UserWithdrawalLog;
import vip.xiaonuo.manage.modular.userwithdrawallog.mapper.UserWithdrawalLogMapper;
import vip.xiaonuo.manage.modular.userwithdrawallog.param.UserWithdrawalLogAddParam;
import vip.xiaonuo.manage.modular.userwithdrawallog.param.UserWithdrawalLogPageParam;
import vip.xiaonuo.mobile.modular.manage.result.UserIntegralResult;
import vip.xiaonuo.mobile.modular.manage.result.UserWithdrawalLogResult;
import vip.xiaonuo.mobile.modular.manage.service.UserWithdrawalLogService;

import java.util.ArrayList;
import java.util.List;

import static vip.xiaonuo.common.util.CommonServletUtil.getRequest;

/**
 * 用户提现记录Service接口实现类
 *
 **/
@Component
public class UserWithdrawalLogServiceImpl extends ServiceImpl<UserWithdrawalLogMapper, UserWithdrawalLog> implements UserWithdrawalLogService {

    @Override
    public Page<UserWithdrawalLogResult> page(UserWithdrawalLogPageParam userWithdrawalLogPageParam) {

        Context context = getRequest();
        String uid = context.attrOrDefault("uid", "-");

        QueryWrapper queryWrapper = new QueryWrapper();
        if(ObjectUtil.isNotEmpty(uid)) {
            queryWrapper.eq("create_user", uid).orderBy(UserWithdrawalLog::getCreateTime,false);
        }
        Page<UserWithdrawalLog> pageList =  this.page(CommonPageRequest.defaultPage(), queryWrapper);
        List<UserWithdrawalLog> loglist = this.list(queryWrapper);
        List<UserWithdrawalLogResult> resultList = new ArrayList<>();
        if (loglist != null && loglist.size() > 0) {
            for (UserWithdrawalLog userWithdrawalLog : loglist) {
                UserWithdrawalLogResult userWithdrawalLogResult = new UserWithdrawalLogResult();
                userWithdrawalLogResult.setId(userWithdrawalLog.getId());
                userWithdrawalLogResult.setCreateTime(userWithdrawalLog.getCreateTime());
                userWithdrawalLogResult.setAmount(userWithdrawalLog.getAmount());
                userWithdrawalLogResult.setReason(userWithdrawalLog.getReason());
                userWithdrawalLogResult.setStatus(userWithdrawalLog.getStatus());
                resultList.add(userWithdrawalLogResult);
            }
        }
        Page<UserWithdrawalLogResult> pageListResult = new Page<>(pageList.getCurrent(), pageList.getPageSize(), pageList.getTotal());
        pageListResult.setRecords(resultList);
        return pageListResult;
    }

    @Tran
    @Override
    public void add(UserWithdrawalLogAddParam userWithdrawalLogAddParam) {
        UserWithdrawalLog userWithdrawalLog = BeanUtil.toBean(userWithdrawalLogAddParam, UserWithdrawalLog.class);
        this.save(userWithdrawalLog);
    }
}
