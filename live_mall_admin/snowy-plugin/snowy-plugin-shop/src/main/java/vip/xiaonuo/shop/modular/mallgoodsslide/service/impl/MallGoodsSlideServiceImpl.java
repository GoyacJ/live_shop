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
package vip.xiaonuo.shop.modular.mallgoodsslide.service.impl;

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
import vip.xiaonuo.shop.modular.mallgoodsChange.entity.MallGoodsChange;
import vip.xiaonuo.shop.modular.mallgoodsslide.entity.MallGoodsSlide;
import vip.xiaonuo.shop.modular.mallgoodsslide.mapper.MallGoodsSlideMapper;
import vip.xiaonuo.shop.modular.mallgoodsslide.param.MallGoodsSlideAddParam;
import vip.xiaonuo.shop.modular.mallgoodsslide.param.MallGoodsSlideEditParam;
import vip.xiaonuo.shop.modular.mallgoodsslide.param.MallGoodsSlideIdParam;
import vip.xiaonuo.shop.modular.mallgoodsslide.param.MallGoodsSlidePageParam;
import vip.xiaonuo.shop.modular.mallgoodsslide.service.MallGoodsSlideService;

import java.util.List;

/**
 * 商品轮播图Service接口实现类
 *
 * @author yuezisheng
 * @date  2025/02/20 15:30
 **/
@Component
public class MallGoodsSlideServiceImpl extends ServiceImpl<MallGoodsSlideMapper, MallGoodsSlide> implements MallGoodsSlideService {

    @Override
    public Page<MallGoodsSlide> page(MallGoodsSlidePageParam mallGoodsSlidePageParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(ObjectUtil.isNotEmpty(mallGoodsSlidePageParam.getStartCreateTime()) && ObjectUtil.isNotEmpty(mallGoodsSlidePageParam.getEndCreateTime())) {
            queryWrapper.between(MallGoodsSlide::getCreateTime, mallGoodsSlidePageParam.getStartCreateTime(), mallGoodsSlidePageParam.getEndCreateTime());
        }
        if(ObjectUtil.isAllNotEmpty(mallGoodsSlidePageParam.getSortField(), mallGoodsSlidePageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(mallGoodsSlidePageParam.getSortOrder());
            queryWrapper.orderBy(StrUtil.toUnderlineCase(mallGoodsSlidePageParam.getSortField()),mallGoodsSlidePageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(MallGoodsSlide::getCreateTime, false);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Tran
    @Override
    public void add(MallGoodsSlideAddParam mallGoodsSlideAddParam) {
        MallGoodsSlide mallGoodsSlide = BeanUtil.toBean(mallGoodsSlideAddParam, MallGoodsSlide.class);
        this.save(mallGoodsSlide);
    }

    @Tran
    @Override
    public void edit(MallGoodsSlideEditParam mallGoodsSlideEditParam) {
        MallGoodsSlide mallGoodsSlide = this.queryEntity(mallGoodsSlideEditParam.getId());
        BeanUtil.copyProperties(mallGoodsSlideEditParam, mallGoodsSlide);
        this.updateById(mallGoodsSlide);
    }

    @Tran
    @Override
    public void delete(List<MallGoodsSlideIdParam> mallGoodsSlideIdParamList) {
        // 执行删除
        this.removeByIds(CollStreamUtil.toList(mallGoodsSlideIdParamList, MallGoodsSlideIdParam::getId));
    }

    @Override
    public MallGoodsSlide detail(MallGoodsSlideIdParam mallGoodsSlideIdParam) {
        return this.queryEntity(mallGoodsSlideIdParam.getId());
    }

    @Override
    public MallGoodsSlide queryEntity(String id) {
        MallGoodsSlide mallGoodsSlide = this.getById(id);
        if(ObjectUtil.isEmpty(mallGoodsSlide)) {
            throw new CommonException("商品轮播图不存在，id值为：{}", id);
        }
        return mallGoodsSlide;
    }

    @Override
    public List<MallGoodsSlide> queryEntityListByGoodsId(MallGoodsSlideIdParam mallGoodsSlideIdParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("goods_id", mallGoodsSlideIdParam.getGoodsId());
        queryWrapper.orderBy("sort",true);
        List<MallGoodsSlide> list = this.list(queryWrapper);
        return list;
    }

    @Override
    public void deleteEntityListByGoodsId(MallGoodsSlideIdParam mallGoodsSlideIdParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("goods_id", mallGoodsSlideIdParam.getGoodsId());
        this.remove(queryWrapper);
    }
}
