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
package vip.xiaonuo.manage.modular.userinfo.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import org.noear.solon.core.handle.UploadedFile;
import vip.xiaonuo.manage.modular.userinfo.entity.UserInfo;
import vip.xiaonuo.manage.modular.userinfo.param.UserInfoAddParam;
import vip.xiaonuo.manage.modular.userinfo.param.UserInfoEditParam;
import vip.xiaonuo.manage.modular.userinfo.param.UserInfoIdParam;
import vip.xiaonuo.manage.modular.userinfo.param.UserInfoPageParam;
import vip.xiaonuo.manage.modular.userinfo.result.UserInfoResult;

import java.util.List;

/**
 * 用户列表Service接口
 *
 * @author zhaishaojiang
 * @date  2025/02/17 17:28
 **/
public interface UserInfoService extends IService<UserInfo> {

    /**
     * 获取用户列表分页
     *
     * @author zhaishaojiang
     * @date  2025/02/17 17:28
     */
    Page<UserInfo> page(UserInfoPageParam userInfoPageParam);

    /**
     * 添加用户列表
     *
     * @author zhaishaojiang
     * @date  2025/02/17 17:28
     */
    void add(UserInfoAddParam userInfoAddParam);

    /**
     * 编辑用户列表
     *
     * @author zhaishaojiang
     * @date  2025/02/17 17:28
     */
    void edit(UserInfoEditParam userInfoEditParam);

    /**
     * 删除用户列表
     *
     * @author zhaishaojiang
     * @date  2025/02/17 17:28
     */
    void delete(List<UserInfoIdParam> userInfoIdParamList);

    /**
     * 获取用户列表详情
     *
     * @author zhaishaojiang
     * @date  2025/02/17 17:28
     */
    UserInfo detail(UserInfoIdParam userInfoIdParam);

    /**
     * 获取用户列表详情
     *
     * @author zhaishaojiang
     * @date  2025/02/17 17:28
     **/
    UserInfo queryEntity(String id);

    /**
     * 根据手机号获取用户详情
     *
     **/
    UserInfoResult getUserInfoByMobile(String mobile);

    /**
     * 修改用户头像信息
     *
     **/
    String editUserAvatar(String mobile,UploadedFile file);

    /**
     * 变更用户积分余额
     *
     **/
    void editUserIntegral(UserInfoEditParam userInfoEditParam);
}
