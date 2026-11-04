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
package vip.xiaonuo.shop.modular.mallintegralgoods.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.common.cache.CommonCacheOperator;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.shop.modular.mallintegralgoods.entity.MallIntegralGoods;
import vip.xiaonuo.shop.modular.mallintegralgoods.mapper.MallIntegralGoodsMapper;
import vip.xiaonuo.shop.modular.mallintegralgoods.param.MallIntegralGoodsAddParam;
import vip.xiaonuo.shop.modular.mallintegralgoods.param.MallIntegralGoodsEditParam;
import vip.xiaonuo.shop.modular.mallintegralgoods.param.MallIntegralGoodsIdParam;
import vip.xiaonuo.shop.modular.mallintegralgoods.param.MallIntegralGoodsPageParam;
import vip.xiaonuo.shop.modular.mallintegralgoods.service.MallIntegralGoodsService;

import java.util.List;

/**
 * 积分商城Service接口实现类
 *
 * @author 陈金坤
 * @date  2025/04/08 14:04
 **/
@Slf4j
@Component
public class MallIntegralGoodsServiceImpl extends ServiceImpl<MallIntegralGoodsMapper, MallIntegralGoods> implements MallIntegralGoodsService {

    @Inject
    private MallIntegralGoodsMapper mallIntegralGoodsMapper;

    @Inject
    private CommonCacheOperator commonCacheOperator;

    @Override
    public Page<MallIntegralGoods> page(MallIntegralGoodsPageParam mallIntegralGoodsPageParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(ObjectUtil.isNotEmpty(mallIntegralGoodsPageParam.getTitle())) {
            queryWrapper.like(MallIntegralGoods::getTitle, mallIntegralGoodsPageParam.getTitle());
        }
        if(ObjectUtil.isNotEmpty(mallIntegralGoodsPageParam.getMarketPrice())) {
            queryWrapper.eq(MallIntegralGoods::getMarketPrice, mallIntegralGoodsPageParam.getMarketPrice());
        }

        if(ObjectUtil.isNotEmpty(mallIntegralGoodsPageParam.getMinMarketPrice())) {
            queryWrapper.ge(MallIntegralGoods::getMarketPrice, mallIntegralGoodsPageParam.getMinMarketPrice());
        }

        if(ObjectUtil.isNotEmpty(mallIntegralGoodsPageParam.getMaxMarketPrice())) {
            queryWrapper.le(MallIntegralGoods::getMarketPrice, mallIntegralGoodsPageParam.getMaxMarketPrice());
        }

        if(ObjectUtil.isNotEmpty(mallIntegralGoodsPageParam.getIntegral())) {
            queryWrapper.eq(MallIntegralGoods::getIntegral, mallIntegralGoodsPageParam.getIntegral());
        }
        if(ObjectUtil.isNotEmpty(mallIntegralGoodsPageParam.getDetail())) {
            queryWrapper.like(MallIntegralGoods::getDetail, mallIntegralGoodsPageParam.getDetail());
        }
        if(ObjectUtil.isNotEmpty(mallIntegralGoodsPageParam.getStatus())) {
            queryWrapper.eq(MallIntegralGoods::getStatus, mallIntegralGoodsPageParam.getStatus());
        }
        if(ObjectUtil.isAllNotEmpty(mallIntegralGoodsPageParam.getSortField(), mallIntegralGoodsPageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(mallIntegralGoodsPageParam.getSortOrder());
            queryWrapper.orderBy(StrUtil.toUnderlineCase(mallIntegralGoodsPageParam.getSortField()),mallIntegralGoodsPageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(MallIntegralGoods::getId);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Tran
    @Override
    public void add(MallIntegralGoodsAddParam mallIntegralGoodsAddParam) {
        MallIntegralGoods mallIntegralGoods = BeanUtil.toBean(mallIntegralGoodsAddParam, MallIntegralGoods.class);
        this.save(mallIntegralGoods);
    }

    @Tran
    @Override
    public void edit(MallIntegralGoodsEditParam mallIntegralGoodsEditParam) {
        MallIntegralGoods mallIntegralGoods = this.queryEntity(mallIntegralGoodsEditParam.getId());
        BeanUtil.copyProperties(mallIntegralGoodsEditParam, mallIntegralGoods);
        this.updateById(mallIntegralGoods);
    }

    @Tran
    @Override
    public void delete(List<MallIntegralGoodsIdParam> mallIntegralGoodsIdParamList) {
        // 执行删除
        this.removeByIds(CollStreamUtil.toList(mallIntegralGoodsIdParamList, MallIntegralGoodsIdParam::getId));
    }

    @Override
    public MallIntegralGoods detail(MallIntegralGoodsIdParam mallIntegralGoodsIdParam) {
        return this.queryEntity(mallIntegralGoodsIdParam.getId());
    }

    @Override
    public MallIntegralGoods queryEntity(String id) {
        MallIntegralGoods mallIntegralGoods = this.getById(id);
        if(ObjectUtil.isEmpty(mallIntegralGoods)) {
            throw new CommonException("积分商城不存在，id值为：{}", id);
        }
        return mallIntegralGoods;
    }

    @Override
    @Tran
    public boolean deductionMallGoodsStock(String goodsId, Integer stock) {
        if (stock <= 0) {
            return true;
        }
        String lockKey = "integral_goods:stock:" + goodsId;

        try {
            boolean locked = commonCacheOperator.tryLock(lockKey);
            if (!locked) {
                return false;
            }
            // 后续库存扣减逻辑
            return mallIntegralGoodsMapper.deductionStock(goodsId, stock) > 0;
        } catch (Exception e) {
            log.error("扣减库存异常", e);
            return false;
        } finally {
            commonCacheOperator.unLock(lockKey);
        }

    }
}
