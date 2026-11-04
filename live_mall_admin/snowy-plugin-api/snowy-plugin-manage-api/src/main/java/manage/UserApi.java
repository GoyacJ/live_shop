package manage;

import com.mybatisflex.core.paginate.Page;
import manage.dto.*;
import manage.enums.RegisterUrlStatus;

import java.time.LocalDateTime;

/**
 * Author: shaojiang
 * CreateTime: 2025/4/22 10:08
 * Description:
 */
public interface UserApi {

    /**
     * 更改用户推广注册链接状态
     */
    boolean updateRegisterUrlStatus(String userId, RegisterUrlStatus status);

    /**
     * 用户信息统计
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return 结果
     */
    UserStatisticsInfoDTO statisticsInfo(LocalDateTime beginTime, LocalDateTime endTime);

    /**
     * 获取用户
     *
     * @param userId 用户id
     * @return 用户信息
     */
    UserInfoDTO getUserById(String userId);

    /**
     * 分页查询
     *
     * @param param 查询参数
     * @return 结果
     */
    Page<RegisterManagePageDTO> registerManagePage(RegisterManagePageParamDTO param);

    /**
     * 我的推广
     *
     * @param promotionParamDTO 参数
     * @return 结果
     */
    UserPromotionDTO promotion(PromotionParamDTO promotionParamDTO);

    /**
     * 分享链接
     *
     * @param userId 用户Id
     * @return 结果
     */
    String sharingLinks(String userId);

    /**
     * 获取推广人数
     *
     * @param userId 用户Id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 结果
     */
    long getPromotionCount(String userId,LocalDateTime startTime,LocalDateTime endTime);
}
