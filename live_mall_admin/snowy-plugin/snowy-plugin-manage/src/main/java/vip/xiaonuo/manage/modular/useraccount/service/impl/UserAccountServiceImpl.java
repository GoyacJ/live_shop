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
package vip.xiaonuo.manage.modular.useraccount.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import org.noear.solon.annotation.Component;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.manage.modular.useraccount.entity.UserAccount;
import vip.xiaonuo.manage.modular.useraccount.mapper.UserAccountMapper;
import vip.xiaonuo.manage.modular.useraccount.param.*;
import vip.xiaonuo.manage.modular.useraccount.service.UserAccountService;

import java.util.List;

/**
 * 用户账号Service接口实现类
 *
 */
@Component
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements UserAccountService {

    @Override
    public Page<UserAccount> page(UserAccountPageParam userAccountPageParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.orderBy(UserAccount::getCreateTime,false);
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Override
    public void edit(UserAccountEditParam userAccountEditParam) {
        UserAccount userAccount = this.getById(userAccountEditParam.getId());
        if(ObjectUtil.isEmpty(userAccount)) {
            throw new CommonException("账号信息不存在，id值为：{}", userAccountEditParam.getId());
        }
        BeanUtil.copyProperties(userAccountEditParam, userAccount);
        this.updateById(userAccount);
    }

    @Override
    public void delete(List<UserAccountIdParam> userAccountIdParamList) {
        this.removeByIds(CollStreamUtil.toList(userAccountIdParamList, UserAccountIdParam::getId));
    }

    @Override
    public UserAccount detail(UserAccountIdParam userAccountIdParam) {
        UserAccount userAccount = this.getById(userAccountIdParam.getId());
        if(ObjectUtil.isEmpty(userAccount)) {
            throw new CommonException("账号信息不存在，id值为：{}", userAccountIdParam.getId());
        }
        return userAccount;
    }

    @Override
    public void add(UserAccountAddParam userAccountAddParam) {
        UserAccount userAccount = BeanUtil.toBean(userAccountAddParam, UserAccount.class);
        this.save(userAccount);
    }

    @Override
    public List<UserAccount> getDetailByUserId(UserAccountUserIdParam userAccountUserIdParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(ObjectUtil.isNotEmpty(userAccountUserIdParam.getUserId())) {
            queryWrapper.eq("user_id", userAccountUserIdParam.getUserId());
        }
        List<UserAccount> list = this.list(queryWrapper);
        return list;
    }
}
