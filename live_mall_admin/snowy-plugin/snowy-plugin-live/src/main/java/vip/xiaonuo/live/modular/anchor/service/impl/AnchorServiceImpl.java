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
package vip.xiaonuo.live.modular.anchor.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import org.noear.solon.annotation.Component;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.live.modular.anchor.entity.Anchor;
import vip.xiaonuo.live.modular.anchor.mapper.AnchorMapper;
import vip.xiaonuo.live.modular.anchor.param.AnchorAddParam;
import vip.xiaonuo.live.modular.anchor.param.AnchorEditParam;
import vip.xiaonuo.live.modular.anchor.param.AnchorIdParam;
import vip.xiaonuo.live.modular.anchor.param.AnchorPageParam;
import vip.xiaonuo.live.modular.anchor.service.AnchorService;

import java.util.List;

/**
 * 主播信息表Service接口实现类
 *
 * @author lgq
 * @date  2025/03/21 14:22
 **/
@Component
public class AnchorServiceImpl extends ServiceImpl<AnchorMapper, Anchor> implements AnchorService {

    @Override
    public Page<Anchor> page(AnchorPageParam anchorPageParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(ObjectUtil.isNotEmpty(anchorPageParam.getRealName())) {
            queryWrapper.eq(Anchor::getRealName, anchorPageParam.getRealName());
        }
        if(ObjectUtil.isNotEmpty(anchorPageParam.getAccount())) {
            queryWrapper.eq(Anchor::getAccount, anchorPageParam.getAccount());
        }
        if(ObjectUtil.isAllNotEmpty(anchorPageParam.getSortField(), anchorPageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(anchorPageParam.getSortOrder());
            queryWrapper.orderBy(StrUtil.toUnderlineCase(anchorPageParam.getSortField()),anchorPageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(Anchor::getId);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Tran
    @Override
    public void add(AnchorAddParam anchorAddParam) {
        Anchor anchor = BeanUtil.toBean(anchorAddParam, Anchor.class);
        this.save(anchor);
    }

    @Tran
    @Override
    public void edit(AnchorEditParam anchorEditParam) {
        Anchor anchor = this.queryEntity(String.valueOf(anchorEditParam.getId()));
        BeanUtil.copyProperties(anchorEditParam, anchor);
        this.updateById(anchor);
    }

    @Tran
    @Override
    public void delete(List<AnchorIdParam> anchorIdParamList) {
        // 执行删除
        this.removeByIds(CollStreamUtil.toList(anchorIdParamList, AnchorIdParam::getId));
    }

    @Override
    public Anchor detail(AnchorIdParam anchorIdParam) {
        return this.queryEntity(String.valueOf(anchorIdParam.getId()));
    }

    @Override
    public Anchor queryEntity(String id) {
        Anchor anchor = this.getById(id);
        if(ObjectUtil.isEmpty(anchor)) {
            throw new CommonException("主播信息表不存在，id值为：{}", id);
        }
        return anchor;
    }

    @Override
    public List<Anchor> getLiveAnchorList() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.orderBy(Anchor::getId);
        List<Anchor> anchorList = this.list(queryWrapper);
        return anchorList;
    }
}
