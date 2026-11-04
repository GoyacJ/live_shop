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

import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import org.noear.solon.annotation.Component;
import vip.xiaonuo.manage.modular.useraccount.entity.UserAccount;
import vip.xiaonuo.manage.modular.useraccount.mapper.UserAccountMapper;
import vip.xiaonuo.mobile.modular.manage.result.UserAccountResult;
import vip.xiaonuo.mobile.modular.manage.service.UserAccountService;

import java.util.ArrayList;
import java.util.List;


/**
 * 用户账号Service接口实现类
 *
 */
@Component
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements UserAccountService {

    @Override
    public List<UserAccountResult> getDetailByUserId(String uid) {

        QueryWrapper queryWrapper = new QueryWrapper();
        if(ObjectUtil.isNotEmpty(uid)) {
            queryWrapper.eq("user_id", uid).orderBy(UserAccount::getCreateTime,false);
        }
        List<UserAccount> oldlist = this.list(queryWrapper);
        List<UserAccountResult> list = new ArrayList<>();
        if (oldlist != null && oldlist.size() > 0) {
            if (oldlist != null && oldlist.size() > 0) {
                for (UserAccount userAccount : oldlist) {
                    UserAccountResult userAccountResult = new UserAccountResult();
                    userAccountResult.setAccount(userAccount.getAccount());
                    userAccountResult.setId(userAccount.getId());
                    userAccountResult.setAccountType(userAccount.getAccountType());
                    userAccountResult.setDefaultStatus(userAccount.getDefalutStatus());
                    list.add(userAccountResult);
                }
            }
        }
        return list;
    }
}
