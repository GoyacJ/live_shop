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
package vip.xiaonuo.manage.modular.userlevel.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import vip.xiaonuo.manage.modular.userlevel.entity.UserLevel;
import vip.xiaonuo.manage.modular.userlevel.param.UserLevelAddParam;
import vip.xiaonuo.manage.modular.userlevel.param.UserLevelEditParam;
import vip.xiaonuo.manage.modular.userlevel.param.UserLevelIdParam;
import vip.xiaonuo.manage.modular.userlevel.param.UserLevelPageParam;

import java.util.List;

/**
 * 平台-用户等级Service接口
 *
 * @author yuezisheng
 * @date  2025/02/18 09:16
 **/
public interface UserLevelService extends IService<UserLevel> {

    /**
     * 获取平台-用户等级分页
     *
     * @author yuezisheng
     * @date  2025/02/18 09:16
     */
    Page<UserLevel> page(UserLevelPageParam userLevelPageParam);

    /**
     * 添加平台-用户等级
     *
     * @author yuezisheng
     * @date  2025/02/18 09:16
     */
    void add(UserLevelAddParam userLevelAddParam);

    /**
     * 编辑平台-用户等级
     *
     * @author yuezisheng
     * @date  2025/02/18 09:16
     */
    void edit(UserLevelEditParam userLevelEditParam);

    /**
     * 删除平台-用户等级
     *
     * @author yuezisheng
     * @date  2025/02/18 09:16
     */
    void delete(List<UserLevelIdParam> userLevelIdParamList);

    /**
     * 获取平台-用户等级详情
     *
     * @author yuezisheng
     * @date  2025/02/18 09:16
     */
    UserLevel detail(UserLevelIdParam userLevelIdParam);

    /**
     * 获取平台-用户等级详情
     *
     * @author yuezisheng
     * @date  2025/02/18 09:16
     **/
    UserLevel queryEntity(String id);
}
