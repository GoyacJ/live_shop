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
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.handle.Context;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.common.cache.CommonCacheOperator;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.manage.modular.userbalancelog.entity.UserBalanceLog;
import vip.xiaonuo.manage.modular.userbalancelog.mapper.UserBalanceLogMapper;
import vip.xiaonuo.manage.modular.userbalancelog.param.UserBalanceLogAddParam;
import vip.xiaonuo.manage.modular.userbalancelog.param.UserBalanceLogPageParam;
import vip.xiaonuo.mobile.modular.manage.result.UserBalanceLogResult;
import vip.xiaonuo.mobile.modular.manage.service.UserBalanceLogService;

import java.util.ArrayList;
import java.util.List;

import static vip.xiaonuo.common.util.CommonServletUtil.getRequest;

/**
 * 平台-用户余额变更记录Service接口实现类
 *
 **/
@Component
public class UserBalanceLogServiceImpl extends ServiceImpl<UserBalanceLogMapper, UserBalanceLog> implements UserBalanceLogService {
    @Inject
    private CommonCacheOperator commonCacheOperator;

    @Override
    public Page<UserBalanceLogResult> page(UserBalanceLogPageParam userBalanceLogPageParam) {
        Context context = getRequest();
        String uid = context.attrOrDefault("uid", "-");
        QueryWrapper queryWrapper = new QueryWrapper();
        //只查詢部分字段
        queryWrapper.select(UserBalanceLog::getId, UserBalanceLog::getReason, UserBalanceLog::getChangeBalance, UserBalanceLog::getCreateTime, UserBalanceLog::getCreateUser);
        if(ObjectUtil.isNotEmpty(uid)) {
            queryWrapper.eq(UserBalanceLog::getCreateUser, uid);
            if (userBalanceLogPageParam.getBalanceType() != null) {
                //BalanceType1/2为现金 3/4为积分
                queryWrapper.eq(UserBalanceLog::getBalanceType, userBalanceLogPageParam.getBalanceType()).orderBy(UserBalanceLog::getCreateTime,false);
            }
        }
        Page<UserBalanceLog> pageList =  this.page(CommonPageRequest.defaultPage(), queryWrapper);
        List<UserBalanceLog> oldlist = this.list(queryWrapper);
        List<UserBalanceLogResult> list = new ArrayList<UserBalanceLogResult>();
        if (oldlist != null && oldlist.size() > 0) {
            for (UserBalanceLog userBalanceLog : oldlist) {
                UserBalanceLogResult userBalanceLogResult = new UserBalanceLogResult();
                BeanUtil.copyProperties(userBalanceLog, userBalanceLogResult);
                list.add(userBalanceLogResult);
            }
        }
        Page<UserBalanceLogResult> pageListResult = new Page<>(pageList.getCurrent(), pageList.getPageSize(), pageList.getTotal());
        pageListResult.setRecords(list);
        return pageListResult;
    }

    @Tran
    @Override
    public void add(UserBalanceLogAddParam userBalanceLogAddParam) {
        UserBalanceLog userBalanceLog = BeanUtil.toBean(userBalanceLogAddParam, UserBalanceLog.class);
        this.save(userBalanceLog);
    }

}
