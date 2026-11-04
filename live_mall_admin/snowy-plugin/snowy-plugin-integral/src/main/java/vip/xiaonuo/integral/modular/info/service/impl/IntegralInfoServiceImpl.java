package vip.xiaonuo.integral.modular.info.service.impl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import manage.UserApi;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;
import vip.xiaonuo.common.enums.SimpleStatus;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.integral.api.exception.IntegralException;
import vip.xiaonuo.integral.modular.info.entity.IntegralInfo;
import vip.xiaonuo.integral.modular.info.mapper.IntegralInfoMapper;
import vip.xiaonuo.integral.modular.info.params.IntegralInfoPageParam;
import vip.xiaonuo.integral.modular.info.service.IntegralInfoService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/29 16:39
 */
@Tran
@Slf4j
@Component
public class IntegralInfoServiceImpl extends ServiceImpl<IntegralInfoMapper, IntegralInfo> implements IntegralInfoService {

    @Inject
    private UserApi userApi;

    @Override
    public String createIntegral(String userId, String modifyUserId, BigDecimal integral, BigDecimal frozenIntegral) {
        IntegralInfo integralInfo = new IntegralInfo();
        integralInfo.setId(IdUtil.getSnowflakeNextIdStr());
        integralInfo.setUserId(userId);
        integralInfo.setIntegral(integral);
        integralInfo.setFrozenIntegral(frozenIntegral);
        integralInfo.setExpireIntegral(BigDecimal.ZERO);
        integralInfo.setStatus(SimpleStatus.ENABLE);
        integralInfo.setLastModifyTime(LocalDateTime.now());
        integralInfo.setLastModifyUserId(modifyUserId);
        integralInfo.setCreateUser(modifyUserId);
        integralInfo.setUpdateUser(modifyUserId);
        if (this.save(integralInfo)) {
            return integralInfo.getId();
        }
        return null;
    }

    @Override
    public IntegralInfo getIntegralInfoByUserId(String userId) {
        // 获取用户积分
        return this.getOne(QueryWrapper.create().eq(IntegralInfo::getUserId, userId));
    }

    @Override
    public boolean isLockIntegralStatus(IntegralInfo integralInfo) {
        if (Objects.nonNull(integralInfo)) {
            return integralInfo.getStatus() == SimpleStatus.DISABLE;
        }
        return false;
    }

    @Override
    public boolean increaseIntegral(String userId, BigDecimal integral, String modifyUserId) {
        IntegralInfo integralInfo = getIntegralInfoByUserId(userId);
        if (Objects.nonNull(integralInfo)) {
            integralInfo.setIntegral(NumberUtil.add(integralInfo.getIntegral(), integral));
            integralInfo.setLastModifyTime(LocalDateTime.now());
            integralInfo.setLastModifyUserId(modifyUserId);
            return this.updateById(integralInfo);
        }
        return false;
    }

    @Override
    public boolean decreaseIntegral(String userId, BigDecimal integral, String modifyUserId) {
        IntegralInfo integralInfo = getIntegralInfoByUserId(userId);
        if (Objects.nonNull(integralInfo)) {
            if (integralInfo.getIntegral().compareTo(integral) < 0) {
                throw new IntegralException("积分不足");
            }
            integralInfo.setIntegral(NumberUtil.sub(integralInfo.getIntegral(), integral));
            integralInfo.setLastModifyTime(LocalDateTime.now());
            integralInfo.setLastModifyUserId(modifyUserId);
            return this.updateById(integralInfo);
        }
        return false;
    }

    @Override
    public boolean increaseFrozenIntegral(String userId, BigDecimal frozenIntegral, String modifyUserId) {
        IntegralInfo integralInfo = getIntegralInfoByUserId(userId);
        if (Objects.nonNull(integralInfo)) {
            integralInfo.setFrozenIntegral(NumberUtil.add(integralInfo.getFrozenIntegral(), frozenIntegral));
            integralInfo.setLastModifyTime(LocalDateTime.now());
            integralInfo.setLastModifyUserId(modifyUserId);
            return this.updateById(integralInfo);
        }
        return false;
    }

    @Override
    public boolean decreaseFrozenIntegral(String userId, BigDecimal frozenIntegral, String modifyUserId) {
        IntegralInfo integralInfo = getIntegralInfoByUserId(userId);
        if (Objects.nonNull(integralInfo)) {
            if (integralInfo.getFrozenIntegral().compareTo(frozenIntegral) < 0) {
                throw new IntegralException("冻结积分不足");
            }
            integralInfo.setFrozenIntegral(NumberUtil.sub(integralInfo.getFrozenIntegral(), frozenIntegral));
            integralInfo.setLastModifyTime(LocalDateTime.now());
            integralInfo.setLastModifyUserId(modifyUserId);
            return this.updateById(integralInfo);
        }
        return false;
    }

    @Override
    public boolean unLockFrozenIntegral(String userId, BigDecimal frozenIntegral, String modifyUserId) {
        IntegralInfo integralInfo = getIntegralInfoByUserId(userId);
        if (Objects.nonNull(integralInfo)) {
            if (integralInfo.getFrozenIntegral().compareTo(frozenIntegral) < 0) {
                throw new IntegralException("冻结积分不足");
            }
            integralInfo.setFrozenIntegral(NumberUtil.sub(integralInfo.getFrozenIntegral(), frozenIntegral));
            integralInfo.setIntegral(NumberUtil.add(integralInfo.getIntegral(), frozenIntegral));
            integralInfo.setLastModifyTime(LocalDateTime.now());
            integralInfo.setLastModifyUserId(modifyUserId);
            return this.updateById(integralInfo);
        }
        return false;
    }

    @Override
    public boolean updateFrozenIntegral(String userId, BigDecimal frozenIntegral, String modifyUserId) {
        IntegralInfo integralInfo = getIntegralInfoByUserId(userId);
        if (Objects.nonNull(integralInfo)) {

            if (integralInfo.getFrozenIntegral().compareTo(frozenIntegral) == 0) {
                throw new IntegralException("冻结积分未发生变化");
            }

            integralInfo.setFrozenIntegral(frozenIntegral);
            integralInfo.setLastModifyTime(LocalDateTime.now());
            integralInfo.setLastModifyUserId(modifyUserId);
            return this.updateById(integralInfo);
        }
        return false;
    }

    @Override
    public boolean updateIntegral(String userId, BigDecimal integral, String modifyUserId) {
        IntegralInfo integralInfo = getIntegralInfoByUserId(userId);
        if (Objects.nonNull(integralInfo)) {

            if (integralInfo.getIntegral().compareTo(integral) == 0) {
                throw new IntegralException("积分未发生变化");
            }

            integralInfo.setIntegral(integral);
            integralInfo.setLastModifyTime(LocalDateTime.now());
            integralInfo.setLastModifyUserId(modifyUserId);
            return this.updateById(integralInfo);
        }
        return false;
    }

    @Override
    public boolean updateExpireIntegral(String userId, BigDecimal expireIntegral, String modifyUserId) {
        IntegralInfo integralInfo = getIntegralInfoByUserId(userId);
        if (Objects.nonNull(integralInfo)) {

            if (integralInfo.getExpireIntegral().compareTo(expireIntegral) == 0) {
                throw new IntegralException("过期积分未发生变化");
            }

            integralInfo.setExpireIntegral(expireIntegral);
            integralInfo.setLastModifyTime(LocalDateTime.now());
            integralInfo.setLastModifyUserId(modifyUserId);
            return this.updateById(integralInfo);
        }
        return false;
    }

    @Override
    public boolean clearExpireIntegral(String userId, String modifyUserId) {
        IntegralInfo integralInfo = getIntegralInfoByUserId(userId);
        if (Objects.nonNull(integralInfo)) {
            integralInfo.setExpireIntegral(BigDecimal.ZERO);
            integralInfo.setLastModifyTime(LocalDateTime.now());
            integralInfo.setLastModifyUserId(modifyUserId);
            return this.updateById(integralInfo);
        }
        return false;
    }

    @Override
    public boolean increaseExpireIntegral(String userId, BigDecimal expireIntegral, String modifyUserId) {
        IntegralInfo integralInfo = getIntegralInfoByUserId(userId);
        if (Objects.nonNull(integralInfo)) {
            integralInfo.setExpireIntegral(NumberUtil.add(integralInfo.getFrozenIntegral(), expireIntegral));
            integralInfo.setLastModifyTime(LocalDateTime.now());
            integralInfo.setLastModifyUserId(modifyUserId);
            return this.updateById(integralInfo);
        }
        return false;
    }

    @Override
    public boolean decreaseExpireIntegral(String userId, BigDecimal expireIntegral, String modifyUserId) {
        IntegralInfo integralInfo = getIntegralInfoByUserId(userId);
        if (Objects.nonNull(integralInfo)) {
            integralInfo.setExpireIntegral(NumberUtil.sub(integralInfo.getFrozenIntegral(), expireIntegral));
            integralInfo.setLastModifyTime(LocalDateTime.now());
            integralInfo.setLastModifyUserId(modifyUserId);
            return this.updateById(integralInfo);
        }
        return false;
    }


    @Override
    public boolean updateIntegralStatus(String userId, SimpleStatus status, String modifyUserId) {
        IntegralInfo integralInfo = getIntegralInfoByUserId(userId);
        if (Objects.nonNull(integralInfo)) {
            integralInfo.setStatus(status);
            integralInfo.setLastModifyTime(LocalDateTime.now());
            integralInfo.setLastModifyUserId(modifyUserId);
            return this.updateById(integralInfo);
        }
        return false;
    }

    @Override
    public Page<IntegralInfo> page(IntegralInfoPageParam param) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (ObjectUtil.isNotEmpty(param.startCreateTime()) && ObjectUtil.isNotEmpty(param.endCreateTime())) {
            queryWrapper.between(IntegralInfo::getCreateTime, param.startCreateTime(), param.endCreateTime());
        }

        if (ObjectUtil.isNotEmpty(param.userId())) {
            queryWrapper.eq(IntegralInfo::getUserId, param.userId());
        }

        if (ObjectUtil.isNotEmpty(param.status())) {
            queryWrapper.eq(IntegralInfo::getStatus, param.status());
        }


        if (ObjectUtil.isAllNotEmpty(param.sortField(), param.sortOrder())) {
            CommonSortOrderEnum.validate(param.sortOrder());
            queryWrapper.orderBy(CharSequenceUtil.toUnderlineCase(param.sortField()), param.sortOrder().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(IntegralInfo::getCreateTime, false);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Override
    public IntegralInfo detail(String id) {
        return this.getById(id);
    }

    @Override
    public boolean deleteIntegralInfoByUserId(String userId) {
        return this.remove(new QueryWrapper().eq(IntegralInfo::getUserId, userId));
    }
}
