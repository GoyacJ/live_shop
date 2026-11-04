package vip.xiaonuo.manage.modular.provider;

import api.shop.OrderApi;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.zxing.WriterException;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import manage.UserApi;
import manage.dto.*;
import manage.enums.RegisterUrlStatus;
import org.apache.commons.lang3.StringUtils;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.handle.UploadedFile;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.common.util.QRCodeGeneratorUtil;
import vip.xiaonuo.dev.api.DevConfigApi;
import vip.xiaonuo.dev.api.DevFileApi;
import vip.xiaonuo.distribution.api.DistributionApi;
import vip.xiaonuo.distribution.api.dto.UserOwnPromotionDTO;
import vip.xiaonuo.distribution.api.dto.UserOwnPromotionParamDTO;
import vip.xiaonuo.manage.modular.deviceinfo.service.MobileDeviceInfoService;
import vip.xiaonuo.manage.modular.userbalancelog.mapper.UserBalanceLogMapper;
import vip.xiaonuo.manage.modular.userinfo.entity.UserInfo;
import vip.xiaonuo.manage.modular.userinfo.mapper.UserInfoMapper;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

/**
 * Author: shaojiang
 * CreateTime: 2025/4/21 21:23
 * Description:
 */
@Tran
@Slf4j
@Component
public class UserApiImpl implements UserApi {
    @Inject
    private UserInfoMapper userInfoMapper;
    @Inject
    private UserBalanceLogMapper userBalanceLogMapper;

    @Inject
    private MobileDeviceInfoService mobileDeviceInfoService;
    @Inject
    private OrderApi orderApi;
    @Inject
    private DevConfigApi devConfigApi;
    @Inject("devFileApi")
    private DevFileApi devFileApi;
    @Inject("${appQr.appCodeContent}")
    private String app_codeContent;

    @Inject
    private DistributionApi distributionApi;

    @Override
    public boolean updateRegisterUrlStatus(String userId, RegisterUrlStatus status) {
        UserInfo userInfo = userInfoMapper.selectOneById(userId);
        if (ObjectUtil.isEmpty(userInfo)) {
            throw new CommonException("用户不存在");
        }
        userInfo.setRegisterUrlStatus(status);
        return userInfoMapper.update(userInfo) > 0;
    }

    @Override
    public UserStatisticsInfoDTO statisticsInfo(LocalDateTime beginTime, LocalDateTime endTime) {
        UserStatisticsInfoDTO dto = this.userInfoMapper.statisticsInfo(beginTime, endTime);
        long count = mobileDeviceInfoService.count();
        dto.setAppVisitsNo((int) count);
        return dto;
    }

    @Override
    public UserInfoDTO getUserById(String userId) {
        UserInfo userInfo = this.userInfoMapper.selectOneById(userId);
        if (Objects.nonNull(userInfo)) {
            return BeanUtil.toBean(userInfo, UserInfoDTO.class);
        }
        return null;
    }

    @Override
    public Page<RegisterManagePageDTO> registerManagePage(RegisterManagePageParamDTO param) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (ObjectUtil.isNotEmpty(param.userName())) {
            queryWrapper.like(UserInfo::getNickname, param.userName());
        }
        if (ObjectUtil.isNotEmpty(param.phone())) {
            queryWrapper.like(UserInfo::getMobile, param.phone());
        }
        if (ObjectUtil.isNotEmpty(param.invitationCode())) {
            queryWrapper.like(UserInfo::getRcode, param.invitationCode());
        }
        if (ObjectUtil.isNotEmpty(param.startCreateTime()) && ObjectUtil.isNotEmpty(param.endCreateTime())) {
            queryWrapper.between(UserInfo::getCreateTime, param.startCreateTime(), param.endCreateTime());
        }
        queryWrapper.and(wq -> {
            wq.ne(UserInfo::getPId, "").isNotNull(UserInfo::getPId);
        });
        if (ObjectUtil.isAllNotEmpty(param.sortField(), param.sortOrder())) {
            CommonSortOrderEnum.validate(param.sortOrder());
            queryWrapper.orderBy(StrUtil.toUnderlineCase(param.sortField()), param.sortOrder().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(UserInfo::getCreateTime, false);
        }
        return this.userInfoMapper.paginateAs(CommonPageRequest.defaultPage(), queryWrapper, UserInfo.class)
                .map(data -> {
                    RegisterManagePageDTO dto = BeanUtil.toBean(data, RegisterManagePageDTO.class);
                    dto.setRegisterTime(data.getCreateTime().toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime());
                    dto.setUuid(data.getId());
                    dto.setAccount(data.getMobile());
                    dto.setPhone(data.getMobile());
                    dto.setUserId(data.getId());
                    UserInfo parent = this.userInfoMapper.selectOneById(data.getPId());
                    if (Objects.nonNull(parent)) {
                        dto.setParentAccount(parent.getMobile());
                    }
                    dto.setParentId(data.getPId());
                    dto.setRegisterUrl("");
                    dto.setNickName(data.getNickname());
                    dto.setInvitationCode(data.getRcode());
                    dto.setCreateTime(dto.getRegisterTime());
                    dto.setRegisterUrlStatus(data.getRegisterUrlStatus());
                    return dto;
                });
    }

    @Override
    public UserPromotionDTO promotion(PromotionParamDTO dto) {

        UserOwnPromotionDTO promotion = distributionApi.userOwnPromotion(new UserOwnPromotionParamDTO(
                dto.userId(),
                dto.current(),
                dto.size(),
                dto.startTime(),
                dto.endTime()
        ));

        Page<UserPromotionDTO.UserPromotionDetailDTO> map = promotion.promotionDetails().map(detail -> new UserPromotionDTO.UserPromotionDetailDTO(
                detail.orderId(),
                detail.orderNumber(),
                detail.promotionName(),
                detail.integral(),
                detail.settleTime(),
                detail.settlementStatus()
        ));

        QueryWrapper inviteNumQuery = new QueryWrapper().eq(UserInfo::getPId, dto.userId());
        if (Objects.nonNull(dto.startTime()) && Objects.nonNull(dto.endTime())) {
            inviteNumQuery.between(UserInfo::getCreateTime, dto.startTime(), dto.endTime());
        }
        // 邀请人数
        long inviteNum = this.userInfoMapper.selectCountByQuery(inviteNumQuery);
        return new UserPromotionDTO((int) inviteNum, promotion.promotionProfit(), map);
    }

    @Override
    public String sharingLinks(String userId) {
        UserInfoDTO userInfo = this.getUserById(userId);
        if (ObjectUtil.isEmpty(userInfo)) {
            throw new CommonException("用户不存在");
        }
        if (RegisterUrlStatus.DISABLE.equals(userInfo.getRegisterUrlStatus())) {
            throw new CommonException("海报生成失败,您已经被停用此功能");
        }
        String appLogo = devConfigApi.getValueByKey("APP_POSTER");
        String qrcode = devConfigApi.getValueByKey("APP_POSTER_QRCODE_RATIO");
        double x = 0;
        double y = 0;
        double width = 0;
        double height = 0;
        if (StringUtils.isNotEmpty(qrcode)) {
            JSONObject qrcodeJson = JSONUtil.parseObj(qrcode);
            width = qrcodeJson.getDouble("width");
            height = qrcodeJson.getDouble("height");
            x = qrcodeJson.getDouble("x");
            y = qrcodeJson.getDouble("y");
        }
        String shareUrl = app_codeContent + "?r=" + userInfo.getRcode();
        UploadedFile file = null;
        if (!appLogo.isEmpty()) {
            try {
                InputStream inputStream = QRCodeGeneratorUtil.generateQRCodePoster(appLogo, shareUrl, width, height, x, y);
                file = new UploadedFile("image/png", inputStream, "app.png");
            } catch (IOException | WriterException e) {
                log.error(">>> 生成二维码异常：", e);
                throw new CommonException("海报生成失败");
            }
        }
        //分享海报  输出到minio服务器上
        return devFileApi.storageFileWithReturnUrlMinio(file);
    }

    @Override
    public long getPromotionCount(String userId, LocalDateTime startTime, LocalDateTime endTime) {
        return this.userInfoMapper.selectCountByQuery(new QueryWrapper().eq(UserInfo::getPId, userId)
                .between(UserInfo::getCreateTime, startTime, endTime));
    }

}
