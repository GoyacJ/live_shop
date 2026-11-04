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
package vip.xiaonuo.biz.modular.group.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.util.CollectionUtil;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.auth.core.util.StpLoginUserUtil;
import vip.xiaonuo.biz.modular.group.entity.BizGroup;
import vip.xiaonuo.biz.modular.group.mapper.BizGroupMapper;
import vip.xiaonuo.biz.modular.group.param.*;
import vip.xiaonuo.biz.modular.group.service.BizGroupService;
import vip.xiaonuo.biz.modular.org.entity.BizOrg;
import vip.xiaonuo.biz.modular.org.service.BizOrgService;
import vip.xiaonuo.biz.modular.user.entity.BizUser;
import vip.xiaonuo.biz.modular.user.service.BizUserService;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.sys.api.SysGroupApi;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户组Service接口实现类
 *
 * @author chengchuanyao
 * @date 2025/1/6 11:29
 */
@Component
public class BizGroupServiceImpl extends ServiceImpl<BizGroupMapper, BizGroup> implements BizGroupService {

    @Inject
    private SysGroupApi sysGroupApi;

    @Inject
    private BizUserService bizUserService;

    @Inject
    private BizOrgService bizOrgService;

    @Override
    public Page<BizGroup> page(BizGroupPageParam bizGroupPageParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(ObjectUtil.isNotEmpty(bizGroupPageParam.getName())) {
            queryWrapper.like(BizGroup::getName, bizGroupPageParam.getName());
        }
        if(ObjectUtil.isAllNotEmpty(bizGroupPageParam.getSortField(), bizGroupPageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(bizGroupPageParam.getSortOrder());
            queryWrapper.orderBy(StrUtil.toUnderlineCase(bizGroupPageParam.getSortField()), bizGroupPageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(BizGroup::getSortCode).asc();
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Tran
    @Override
    public void add(BizGroupAddParam bizGroupAddParam) {
        BizGroup bizGroup = BeanUtil.toBean(bizGroupAddParam, BizGroup.class);
        this.save(bizGroup);
    }

    @Tran
    @Override
    public void edit(BizGroupEditParam bizGroupEditParam) {
        BizGroup bizGroup = this.queryEntity(bizGroupEditParam.getId());
        BeanUtil.copyProperties(bizGroupEditParam, bizGroup);
        this.updateById(bizGroup);
    }

    @Tran
    @Override
    public void delete(List<BizGroupIdParam> bizGroupIdParamList) {
        // 执行删除
        this.removeByIds(CollStreamUtil.toList(bizGroupIdParamList, BizGroupIdParam::getId));
    }

    @Override
    public BizGroup detail(BizGroupIdParam bizGroupIdParam) {
        return this.queryEntity(bizGroupIdParam.getId());
    }

    @Override
    public BizGroup queryEntity(String id) {
        BizGroup bizGroup = this.getById(id);
        if(ObjectUtil.isEmpty(bizGroup)) {
            throw new CommonException("用户组不存在，id值为：{}", id);
        }
        return bizGroup;
    }

    @Override
    public List<String> ownUser(BizGroupIdParam sysGroupIdParam) {
        return sysGroupApi.ownUser(sysGroupIdParam.getId());
    }

    @Override
    public List<Tree<String>> orgTreeSelector() {
        // 获取所有机构
        List<BizOrg> allOrgList = bizOrgService.list();
        // 定义机构集合
        Set<BizOrg> bizOrgSet = CollectionUtil.newHashSet();
        // 校验数据范围
        List<String> loginUserDataScope = StpLoginUserUtil.getLoginUserDataScope();
        if(ObjectUtil.isNotEmpty(loginUserDataScope)) {
            loginUserDataScope.forEach(orgId -> bizOrgSet.addAll(bizOrgService.getParentListById(allOrgList, orgId, true)));
        } else {
            return CollectionUtil.newArrayList();
        }
        List<TreeNode<String>> treeNodeList = bizOrgSet.stream().map(bizOrg ->
                        new TreeNode<>(bizOrg.getId(), bizOrg.getParentId(),
                                bizOrg.getName(), bizOrg.getSortCode()).setExtra(JSONUtil.parseObj(bizOrg)))
                .collect(Collectors.toList());
        return TreeUtil.build(treeNodeList, "0");
    }

    @Override
    public Page<BizUser> userSelector(BizGroupSelectorUserParam bizGroupSelectorUserParam) {
        QueryWrapper lambdaQueryWrapper = new QueryWrapper();
        // 校验数据范围
        List<String> loginUserDataScope = StpLoginUserUtil.getLoginUserDataScope();
        if(ObjectUtil.isNotEmpty(loginUserDataScope)) {
            lambdaQueryWrapper.in(BizUser::getOrgId, loginUserDataScope);
        } else {
            return new Page<>();
        }
        // 只查询部分字段
        lambdaQueryWrapper.select(BizUser::getId, BizUser::getAvatar, BizUser::getOrgId, BizUser::getPositionId, BizUser::getAccount,
                BizUser::getName, BizUser::getSortCode, BizUser::getGender, BizUser::getEntryDate);
        if (ObjectUtil.isNotEmpty(bizGroupSelectorUserParam.getOrgId())) {
            // 如果机构id不为空，则查询该机构及其子机构下的所有人
            List<String> childOrgIdList = CollStreamUtil.toList(bizOrgService.getChildListById(bizOrgService
                    .getAllOrgList(), bizGroupSelectorUserParam.getOrgId(), true), BizOrg::getId);
            if (ObjectUtil.isNotEmpty(childOrgIdList)) {
                lambdaQueryWrapper.in(BizUser::getOrgId, childOrgIdList);
            } else {
                return new Page<>();
            }
        }
        if(ObjectUtil.isNotEmpty(bizGroupSelectorUserParam.getSearchKey())) {
            lambdaQueryWrapper.like(BizUser::getName, bizGroupSelectorUserParam.getSearchKey());
        }
        lambdaQueryWrapper.orderBy(BizUser::getSortCode).asc();
        return bizUserService.page(CommonPageRequest.defaultPage(), lambdaQueryWrapper);
    }

    @Override
    public void grantUser(BizGroupGrantUserParam bizGroupGrantUserParam) {
        sysGroupApi.grantUser(bizGroupGrantUserParam.getId(), bizGroupGrantUserParam.getGrantInfoList());
    }
}
