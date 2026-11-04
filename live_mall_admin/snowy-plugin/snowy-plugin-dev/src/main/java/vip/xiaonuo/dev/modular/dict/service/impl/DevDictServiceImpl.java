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
package vip.xiaonuo.dev.modular.dict.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.lang.tree.parser.DefaultNodeParser;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Init;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.dev.modular.dict.entity.DevDict;
import vip.xiaonuo.dev.modular.dict.enums.DevDictCategoryEnum;
import vip.xiaonuo.dev.modular.dict.mapper.DevDictMapper;
import vip.xiaonuo.dev.modular.dict.param.*;
import vip.xiaonuo.dev.modular.dict.service.DevDictService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 字典Service接口实现类
 *
 * @author xuyuxiang
 * @date 2022/4/22 10:41
 **/
@Component
public class DevDictServiceImpl extends ServiceImpl<DevDictMapper, DevDict> implements DevDictService  {

    private static final String ROOT_PARENT_ID = "0";

    @Override
    public Page<DevDict> page(DevDictPageParam devDictPageParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        // 查询部分字段
        queryWrapper.select(DevDict::getId, DevDict::getParentId, DevDict::getCategory, DevDict::getDictLabel,
                DevDict::getDictValue, DevDict::getSortCode);
        if (ObjectUtil.isNotEmpty(devDictPageParam.getParentId())) {
            queryWrapper.and(q->{
                q.eq(DevDict::getParentId, devDictPageParam.getParentId());
                q.or(q1->{
                    q1.eq(DevDict::getId, devDictPageParam.getParentId());
                });
            });
        }
        if (ObjectUtil.isNotEmpty(devDictPageParam.getCategory())) {
            queryWrapper.eq(DevDict::getCategory, devDictPageParam.getCategory());
        }
        if (ObjectUtil.isNotEmpty(devDictPageParam.getSearchKey())) {
            queryWrapper.like(DevDict::getDictLabel, devDictPageParam.getSearchKey());
        }
        if (ObjectUtil.isAllNotEmpty(devDictPageParam.getSortField(), devDictPageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(devDictPageParam.getSortOrder());
            queryWrapper.orderBy(StrUtil.toUnderlineCase(devDictPageParam.getSortField()),devDictPageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(DevDict::getSortCode);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Override
    public List<DevDict> list(DevDictListParam devDictListParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (ObjectUtil.isNotEmpty(devDictListParam.getParentId())) {
            queryWrapper.eq(DevDict::getParentId, devDictListParam.getParentId());
        }
        if (ObjectUtil.isNotEmpty(devDictListParam.getCategory())) {
            queryWrapper.eq(DevDict::getCategory, devDictListParam.getCategory());
        }
        return this.list(queryWrapper);
    }

    @Override
    public List<Tree<String>> tree(DevDictTreeParam devDictTreeParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.orderBy(DevDict::getSortCode);
        if (ObjectUtil.isNotEmpty(devDictTreeParam.getCategory())) {
            queryWrapper.eq(DevDict::getCategory, devDictTreeParam.getCategory());
        }
        // 查询部分字段
        queryWrapper.select(DevDict::getId, DevDict::getParentId, DevDict::getCategory, DevDict::getDictLabel, DevDict::getDictValue, DevDict::getSortCode);
        List<DevDict> devDictList = this.list(queryWrapper);
        List<TreeNode<String>> treeNodeList = devDictList.stream().map(devDict ->
                        new TreeNode<>(devDict.getId(), devDict.getParentId(),
                                devDict.getDictLabel(), devDict.getSortCode()).setExtra(JSONUtil.parseObj(devDict)))
                .collect(Collectors.toList());
        // 精简冗余字段(sortCode、weight字段合并)
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setWeightKey("sortCode");
        return TreeUtil.build(treeNodeList, "0", treeNodeConfig, new DefaultNodeParser<>());
    }

    @Override
    public void add(DevDictAddParam devDictAddParam) {
        checkParam(devDictAddParam);
        DevDict devDict = BeanUtil.toBean(devDictAddParam, DevDict.class);
        this.save(devDict);
    }

    private void checkParam(DevDictAddParam devDictAddParam) {
        DevDictCategoryEnum.validate(devDictAddParam.getCategory());
        boolean hasSameDictLabel = this.count(new QueryWrapper()
                .eq(DevDict::getParentId, devDictAddParam.getParentId())
                .eq(DevDict::getCategory, devDictAddParam.getCategory())
                .eq(DevDict::getDictLabel, devDictAddParam.getDictLabel())) > 0;
        if (hasSameDictLabel) {
            throw new CommonException("存在重复的字典文字，名称为：{}", devDictAddParam.getDictLabel());
        }

        boolean hasSameDictValue = this.count(new QueryWrapper()
                .eq(DevDict::getParentId, devDictAddParam.getParentId())
                .eq(DevDict::getDictValue, devDictAddParam.getDictValue())) > 0;
        if (hasSameDictValue) {
            throw new CommonException("存在重复的字典值，名称为：{}", devDictAddParam.getDictValue());
        }
    }

    @Override
    public void edit(DevDictEditParam devDictEditParam) {
        DevDict devDict = this.queryEntity(devDictEditParam.getId());
        checkParam(devDictEditParam);
        BeanUtil.copyProperties(devDictEditParam, devDict);
        this.updateById(devDict);
//        refreshTransCache();
    }

    private void checkParam(DevDictEditParam devDictEditParam) {
        DevDictCategoryEnum.validate(devDictEditParam.getCategory());
        boolean hasSameDictLabel = this.count(new QueryWrapper()
                .eq(DevDict::getParentId, devDictEditParam.getParentId())
                .eq(DevDict::getCategory, devDictEditParam.getCategory())
                .eq(DevDict::getDictLabel, devDictEditParam.getDictLabel())
                .ne(DevDict::getId, devDictEditParam.getId())) > 0;
        if (hasSameDictLabel) {
            throw new CommonException("存在重复的字典文字，名称为：{}", devDictEditParam.getDictLabel());
        }

        boolean hasSameDictValue = this.count(new QueryWrapper()
                .eq(DevDict::getParentId, devDictEditParam.getParentId())
                .eq(DevDict::getDictValue, devDictEditParam.getDictValue())
                .ne(DevDict::getId, devDictEditParam.getId())) > 0;
        if (hasSameDictValue) {
            throw new CommonException("存在重复的字典值，名称为：{}", devDictEditParam.getDictValue());
        }
    }

    @Override
    public void delete(List<DevDictIdParam> devDictIdParamList) {
        List<String> devDictIdList = CollStreamUtil.toList(devDictIdParamList, DevDictIdParam::getId);
        if (ObjectUtil.isNotEmpty(devDictIdList)) {
            boolean systemDict = this.listByIds(devDictIdList).stream().map(DevDict::getCategory)
                    .collect(Collectors.toSet()).contains(DevDictCategoryEnum.FRM.getValue());
            if (systemDict) {
                throw new CommonException("不可删除系统内置字典");
            }
            // 判断字典是否有子集 同时删除子集
            boolean hasChildren = this.count(new QueryWrapper().in(DevDict::getParentId, devDictIdList)) > 0;
            if (hasChildren) {
                List<DevDict> list = this.list(new QueryWrapper().in(DevDict::getParentId, devDictIdList));
                devDictIdList.addAll(list.stream().map(DevDict::getId).collect(Collectors.toList()));
            }
            // 删除
            this.removeByIds(devDictIdList);
        }
    }

    @Override
    public DevDict detail(DevDictIdParam devDictIdParam) {
        return this.queryEntity(devDictIdParam.getId());
    }

    @Override
    public DevDict queryEntity(String id) {
        DevDict devDict = this.getById(id);
        if (ObjectUtil.isEmpty(devDict)) {
            throw new CommonException("字典不存在，id值为：{}", id);
        }
        return devDict;
    }

    @Init
    public void afterInjection() throws Throwable {
        //@Init:: 容器初始化完成后，再运行
//        refreshTransCache();
    }
}
