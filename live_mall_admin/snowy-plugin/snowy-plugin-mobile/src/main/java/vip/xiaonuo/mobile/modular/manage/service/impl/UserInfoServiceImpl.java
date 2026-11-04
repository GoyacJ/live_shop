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
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.*;
import cn.hutool.crypto.digest.DigestUtil;
import com.github.houbb.heaven.util.lang.StringUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import manage.UserApi;
import manage.dto.PromotionParamDTO;
import org.apache.commons.lang3.StringUtils;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.UploadedFile;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.common.cache.CommonCacheOperator;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.dev.api.DevFileApi;
import vip.xiaonuo.distribution.api.DistributionApi;
import vip.xiaonuo.distribution.api.enums.DistributionType;
import vip.xiaonuo.integral.api.IntegralApi;
import vip.xiaonuo.integral.api.dto.IntegralInfoDTO;
import vip.xiaonuo.live.api.openim.OpenImApi;
import vip.xiaonuo.manage.modular.userinfo.entity.UserInfo;
import vip.xiaonuo.manage.modular.userinfo.mapper.UserInfoMapper;
import vip.xiaonuo.manage.modular.userinfo.param.*;
import vip.xiaonuo.mobile.core.conts.MobileRedisConstants;
import vip.xiaonuo.mobile.modular.manage.param.PromotionParam;
import vip.xiaonuo.mobile.modular.manage.param.UserInfoRegisterParam;
import vip.xiaonuo.mobile.modular.manage.result.UserInfoResult;
import vip.xiaonuo.mobile.modular.manage.service.UserInfoService;
import vip.xiaonuo.mobile.modular.manage.vo.UserPromotionVO;
import vip.xiaonuo.task.api.TaskApi;
import vip.xiaonuo.task.api.enums.TaskBusinessCategory;

import java.util.List;
import java.util.Objects;

import static vip.xiaonuo.common.util.CommonServletUtil.getRequest;

/**
 * 用户列表Service接口实现类
 *
 * @author zhaishaojiang
 * @date 2025/02/17 17:28
 **/
@Slf4j
@Component
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    @Inject("${mobile.user.token-expire-time}")
    private int tokenExpireTime;


    @Inject
    private CommonCacheOperator commonCacheOperator;

    @Inject("devFileApi")
    private DevFileApi devFileApi;

    @Inject
    private UserApi userApi;

    @Inject
    private IntegralApi integralApi;

    @Inject
    private OpenImApi openImApi;

    @Inject
    private TaskApi taskApi;

    @Inject
    private DistributionApi distributionApi;

    @Override
    public Page<UserInfo> page(UserInfoPageParam userInfoPageParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (ObjectUtil.isNotEmpty(userInfoPageParam.getMobile())) {
            queryWrapper.like(UserInfo::getMobile, userInfoPageParam.getMobile());
        }
        if (ObjectUtil.isNotEmpty(userInfoPageParam.getLevelId())) {
            queryWrapper.eq(UserInfo::getLevelId, userInfoPageParam.getLevelId());
        }
        if (ObjectUtil.isNotEmpty(userInfoPageParam.getNickname())) {
            queryWrapper.like(UserInfo::getNickname, userInfoPageParam.getNickname());
        }
        if (ObjectUtil.isNotEmpty(userInfoPageParam.getStartCreateTime()) && ObjectUtil.isNotEmpty(userInfoPageParam.getEndCreateTime())) {
            queryWrapper.between(UserInfo::getCreateTime, userInfoPageParam.getStartCreateTime(), userInfoPageParam.getEndCreateTime());
        }
        if (ObjectUtil.isAllNotEmpty(userInfoPageParam.getSortField(), userInfoPageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(userInfoPageParam.getSortOrder());
            queryWrapper.orderBy(StrUtil.toUnderlineCase(userInfoPageParam.getSortField()), userInfoPageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(UserInfo::getId);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Tran
    @Override
    public void add(UserInfoAddParam userInfoAddParam) {
        UserInfo userInfo = BeanUtil.toBean(userInfoAddParam, UserInfo.class);
        this.save(userInfo);
    }

    @Tran
    @Override
    public void edit(UserInfoEditParam userInfoEditParam) {
        UserInfo userInfo = this.queryEntity(userInfoEditParam.getId());
        BeanUtil.copyProperties(userInfoEditParam, userInfo);
        this.updateById(userInfo);
    }

    @Tran
    @Override
    public void delete(List<UserInfoIdParam> userInfoIdParamList) {
        // 执行删除
        this.removeByIds(CollStreamUtil.toList(userInfoIdParamList, UserInfoIdParam::getId));
    }

    @Override
    public UserInfo detail(UserInfoIdParam userInfoIdParam) {
        return this.queryEntity(userInfoIdParam.getId());
    }

    @Override
    public UserInfo queryEntity(String id) {
        UserInfo userInfo = this.getById(id);
        if (ObjectUtil.isEmpty(userInfo)) {
            throw new CommonException("用户不存在", id);
        }
        return userInfo;
    }

    @Override
    public UserInfoResult getUserinfo(String uid) {
        UserInfo userinfo = this.getById(uid);
        if (ObjectUtil.isEmpty(userinfo)) {
            throw new CommonException("用户不存在");
        }

        UserInfoResult userInfoResult = BeanUtil.copyProperties(userinfo, UserInfoResult.class);

        String token = DigestUtil.md5Hex(RandomUtil.randomString(10) + userinfo.getId());
        commonCacheOperator.put(MobileRedisConstants.USER_TOKEN_PREFIX + token, userinfo.getId(), tokenExpireTime);
        userInfoResult.setToken(token);

        IntegralInfoDTO integralInfoDTO = integralApi.getIntegralInfoByUserId(uid);
        userInfoResult.setIntegral(integralInfoDTO.integral());
        userInfoResult.setFrozenIntegral(integralInfoDTO.frozenIntegral());

        return userInfoResult;
    }

    @Override
    public String editUserAvatar(String uid, UploadedFile file) {
        // 获取用户信息
        UserInfo userInfo = this.queryEntity(uid);
        //上传文件信息
        String url = devFileApi.storageFileWithReturnUrlMinio(file);
        //修改userInfo表中用户头像
        userInfo.setAvatar(url);
        this.updateById(userInfo);
        return url;
    }

    @Override
    public void editUserName(UserNameAndMobileEditParam userNameAndMobileEditParam) {
        Context request = getRequest();
        String userId = request.attr("uid");
        if (StringUtil.isEmpty(userId)) {
            throw new CommonException("用户id不能为空");
        }
        UserInfo userInfo = this.queryEntity(userId);
        if (ObjectUtil.isEmpty(userInfo)) {
            throw new CommonException("用户不存在");
        }
        BeanUtil.copyProperties(userNameAndMobileEditParam, userInfo);
        this.updateById(userInfo);
    }

    @Override
    public UserInfo getUserInfoByRcode(String rcode) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (ObjectUtil.isNotEmpty(rcode)) {
            queryWrapper.eq(UserInfo::getRcode, rcode);
        }
        return this.getOne(queryWrapper);
    }

    @Override
    public Page<UserInfo> getPromotionList(PromotionParam promotionParam) {
        Object uid = getRequest().attr("uid");
        if (ObjectUtil.isEmpty(uid)) {
            throw new CommonException("用户id不能为空");
        }

        QueryWrapper queryWrapper = QueryWrapper.create();
        queryWrapper.eq(UserInfo::getPId, Convert.toStr(uid));
        queryWrapper.select(UserInfo::getNickname, UserInfo::getCreateTime);
        queryWrapper.orderBy(UserInfo::getCreateTime, false);

        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Override
    public UserPromotionVO promotion(PromotionParam promotionParam) {
        Object uid = getRequest().attr("uid");
        if (ObjectUtil.isEmpty(uid)) {
            throw new CommonException("用户id不能为空");
        }
        return UserPromotionVO.of(userApi.promotion(new PromotionParamDTO(promotionParam.getCurrent(), promotionParam.getSize(), (String) uid, promotionParam.getStartTime(), promotionParam.getEndTime())));
    }

    @Tran
    @Override
    public UserInfo register(UserInfoRegisterParam param) {
        //注册
        UserInfo userInfo = new UserInfo();
        userInfo.setId(IdUtil.getSnowflakeNextIdStr());
        userInfo.setNickname(PhoneUtil.hideBetween(param.mobile()).toString());
        userInfo.setMobile(param.mobile());

        //如果用户邀请码不为空则根据邀请码查询用户di
        if (StringUtils.isNotEmpty(param.rCode())) {
            UserInfo user = this.getUserInfoByRcode(param.rCode());
            if (Objects.nonNull(user)) {
                userInfo.setPId(user.getPId());
            } else {
                throw new CommonException("用户邀请码不存在");
            }
        }

        //注册时生成用户邀请码
        String rcodeStr = RandomUtil.randomString("abcdefghijklmnopqrstuvwxyz0123456789", 6).toUpperCase();
        userInfo.setRcode(rcodeStr);
        userInfo.setLevelId("0");

        // 先注册opemim用户
        openImApi.registerUser(userInfo.getId(), userInfo.getNickname(), "");

        String integralId = integralApi.createIntegral(userInfo.getId());
        userInfo.setIntegralId(integralId);
        userInfo.setAvatar("");
        boolean result = this.save(userInfo);
        if (result) {
            taskApi.task(userInfo.getPId(), TaskBusinessCategory.AUTH_REGISTER);
            // 注册给积分
            registerIntegral(userInfo);
            // 注册返佣
            recommendedRebate(userInfo.getId());
        }
        return userInfo;
    }

    /**
     * 注册送积分
     *
     * @param muserInfo 用户信息
     */
    private void registerIntegral(UserInfo muserInfo) {
        taskApi.task(muserInfo.getId(), TaskBusinessCategory.AUTH_REGISTER);
    }

    /**
     * 推荐注册反佣
     *
     * @param userId userId
     */
    private void recommendedRebate(String userId) {
        distributionApi.promotion(userId, DistributionType.ONCE_REGISTER, userId, "推荐注册送积分");
    }
}
