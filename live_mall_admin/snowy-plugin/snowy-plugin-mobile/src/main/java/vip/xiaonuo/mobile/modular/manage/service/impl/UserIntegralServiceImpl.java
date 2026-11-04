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
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.handle.Context;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.common.cache.CommonCacheOperator;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.integral.api.IntegralApi;
import vip.xiaonuo.manage.modular.userintegral.entity.UserIntegral;
import vip.xiaonuo.manage.modular.userintegral.mapper.UserIntegralMapper;
import vip.xiaonuo.manage.modular.userintegral.param.UserIntegralAddParam;
import vip.xiaonuo.manage.modular.userintegral.param.UserIntegralPageParam;
import vip.xiaonuo.mobile.modular.manage.result.UserIntegralResult;
import vip.xiaonuo.mobile.modular.manage.service.UserInfoService;
import vip.xiaonuo.mobile.modular.manage.service.UserIntegralService;

import java.util.ArrayList;
import java.util.List;

import static vip.xiaonuo.common.util.CommonServletUtil.getRequest;

/**
 * 用户积分兑换Service接口实现类
 *
 **/
@Component
public class UserIntegralServiceImpl extends ServiceImpl<UserIntegralMapper, UserIntegral> implements UserIntegralService {

    @Inject
    private UserInfoService userInfoService;

    @Inject
    private CommonCacheOperator commonCacheOperator;

    @Inject
    private IntegralApi integralApi;

    @Override
    public Page<UserIntegralResult> page(UserIntegralPageParam userIntegralPageParam) {

        Context context = getRequest();
        String uid = context.attrOrDefault("uid", "-");

        QueryWrapper queryWrapper = new QueryWrapper();
        if(ObjectUtil.isNotEmpty(uid)) {
            queryWrapper.eq("create_user", uid).orderBy(UserIntegral::getCreateTime,false);;
        }
        Page<UserIntegral> pageList =  this.page(CommonPageRequest.defaultPage(), queryWrapper);
        List<UserIntegral> list = this.list(queryWrapper);
        List<UserIntegralResult> resultList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (UserIntegral userIntegral : list) {
                UserIntegralResult userIntegralResult = new UserIntegralResult();
                userIntegralResult.setId(userIntegral.getId());
                userIntegralResult.setIntegral(userIntegral.getIntegral());
                userIntegralResult.setAmount(userIntegral.getAmount());
                userIntegralResult.setReason(userIntegral.getReason());
                userIntegralResult.setCreateUser(userIntegral.getCreateUser());
                userIntegralResult.setCreateTime(userIntegral.getCreateTime());
                resultList.add(userIntegralResult);
            }
        }
        Page<UserIntegralResult> pageListResult = new Page<>(pageList.getCurrent(), pageList.getPageSize(), pageList.getTotal());
        pageListResult.setRecords(resultList);
        return pageListResult;
    }

    @Tran
    @Override
    public void add(UserIntegralAddParam userIntegralAddParam) {
        Context context = getRequest();
        String uid = context.attrOrDefault("uid", "-");
        UserIntegral userIntegral = BeanUtil.toBean(userIntegralAddParam, UserIntegral.class);
        this.save(userIntegral);
        //如果状态为已通过修改积分user_info
        if(StrUtil.equals("2",userIntegralAddParam.getStatus())){
        }
        throw new CommonException("积分兑换通道正在维护");
    }
}
