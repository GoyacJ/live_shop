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
package vip.xiaonuo.manage.modular.userinfo.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.handle.UploadedFile;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.common.cache.CommonCacheOperator;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.dev.api.DevFileApi;
import vip.xiaonuo.integral.api.IntegralApi;
import vip.xiaonuo.integral.api.dto.ChangeIntegralDTO;
import vip.xiaonuo.integral.api.enums.IntegralOperationSource;
import vip.xiaonuo.integral.api.enums.IntegralSource;
import vip.xiaonuo.manage.modular.userinfo.entity.UserInfo;
import vip.xiaonuo.manage.modular.userinfo.mapper.UserInfoMapper;
import vip.xiaonuo.manage.modular.userinfo.param.UserInfoAddParam;
import vip.xiaonuo.manage.modular.userinfo.param.UserInfoEditParam;
import vip.xiaonuo.manage.modular.userinfo.param.UserInfoIdParam;
import vip.xiaonuo.manage.modular.userinfo.param.UserInfoPageParam;
import vip.xiaonuo.manage.modular.userinfo.result.UserInfoResult;
import vip.xiaonuo.manage.modular.userinfo.service.UserInfoService;

import java.util.List;

/**
 * 用户列表Service接口实现类
 *
 * @author zhaishaojiang
 * @date 2025/02/17 17:28
 **/
@Component
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Inject
    private CommonCacheOperator commonCacheOperator;

    @Inject("devFileApi")
    private DevFileApi devFileApi;

    @Inject
    private IntegralApi integralApi;

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
            queryWrapper.orderBy(UserInfo::getCreateTime, false);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Tran
    @Override
    public void add(UserInfoAddParam userInfoAddParam) {
        UserInfo userInfo = BeanUtil.toBean(userInfoAddParam, UserInfo.class);
        userInfo.setId(IdUtil.getSnowflakeNextIdStr());
        if (this.save(userInfo)) {
            integralApi.createIntegral(userInfo.getId(), (String) StpUtil.getLoginId(), userInfoAddParam.getIntegral(), userInfoAddParam.getFrozenIntegral());
        }
    }

    @Tran
    @Override
    public void edit(UserInfoEditParam userInfoEditParam) {
        UserInfo userInfo = this.queryEntity(userInfoEditParam.getId());
        BeanUtil.copyProperties(userInfoEditParam, userInfo);
        if (this.updateById(userInfo)) {
            integralApi.changeIntegral(ChangeIntegralDTO.adjustmentIntegral(
                    userInfo.getId(),
                    userInfoEditParam.getIntegral(),
                    (String) StpUtil.getLoginId(),
                    IntegralSource.SYSTEM,
                    (String) StpUtil.getLoginId(),
                    IntegralOperationSource.ADMIN,
                    "管理员编辑用户积分"
            ));

            integralApi.changeIntegral(ChangeIntegralDTO.adjustmentFrozenIntegral(
                    userInfo.getId(),
                    userInfoEditParam.getFrozenIntegral(),
                    (String) StpUtil.getLoginId(),
                    IntegralSource.SYSTEM,
                    (String) StpUtil.getLoginId(),
                    IntegralOperationSource.ADMIN,
                    "管理员编辑用户冻结积分"
            ));
        }
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
            throw new CommonException("用户列表不存在，id值为：{}", id);
        }
        return userInfo;
    }

    @Override
    public UserInfoResult getUserInfoByMobile(String mobile) {
        QueryWrapper queryWrapper = new QueryWrapper();
        UserInfoResult userInfoResult = new UserInfoResult();
        if (ObjectUtil.isNotEmpty(mobile)) {
            queryWrapper.eq(UserInfo::getMobile, mobile);
            List<UserInfo> list = this.list(queryWrapper);
            if (list != null && list.size() > 0) {
                //生成token作为唯一标识
                String token = RandomUtil.randomString(10) + list.get(0).getId() + System.currentTimeMillis();
                commonCacheOperator.put("Authorization", token, 7 * 24 * 60 * 60);
                JSONObject jsonObject = JSONUtil.parseObj(list.get(0));
                userInfoResult = jsonObject.toBean(UserInfoResult.class);
                userInfoResult.setToken(token);
            }
        }
        return userInfoResult;
    }

    @Override
    public String editUserAvatar(String mobile, UploadedFile file) {
        //上传文件信息
        String url = devFileApi.storageFileWithReturnUrlMinio(file);
        //修改userInfo表中用户头像
        UserInfo userInfo = this.queryEntity(mobile);
        UserInfoEditParam userInfoEditParam = new UserInfoEditParam();
        userInfoEditParam.setAvatar(url);
        BeanUtil.copyProperties(userInfoEditParam, userInfo);
        this.updateById(userInfo);
        return url;
    }

    @Override
    public void editUserIntegral(UserInfoEditParam userInfoEditParam) {
        UserInfo userInfo = this.queryEntity(userInfoEditParam.getId());
        BeanUtil.copyProperties(userInfoEditParam, userInfo);
        this.updateById(userInfo);
    }
}
