package vip.xiaonuo.shop.modular.specialtopic.service.impl;

import api.shop.dto.MallGoodsSearchResDTO;
import api.shop.dto.MallSpecialTopicGoodsSearchDTO;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.common.pojo.CommonValidList;
import vip.xiaonuo.shop.modular.mallgoods.entity.MallGoods;
import vip.xiaonuo.shop.modular.mallgoods.service.MallGoodsService;
import vip.xiaonuo.shop.modular.specialtopic.entity.MallGoodsSpecialTopic;
import vip.xiaonuo.shop.modular.specialtopic.entity.MallGoodsSpecialTopicDetail;
import vip.xiaonuo.shop.modular.specialtopic.mapper.MallGoodsSpecialTopicMapper;
import vip.xiaonuo.shop.modular.specialtopic.param.*;
import vip.xiaonuo.shop.modular.specialtopic.service.MallGoodsSpecialTopicDetailService;
import vip.xiaonuo.shop.modular.specialtopic.service.MallGoodsSpecialTopicService;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/9 16:37
 */
@Slf4j
@Component
public class MallGoodsSpecialTopicServiceImpl extends ServiceImpl<MallGoodsSpecialTopicMapper, MallGoodsSpecialTopic> implements MallGoodsSpecialTopicService {

    @Inject
    private MallGoodsSpecialTopicDetailService mallGoodsSpecialTopicDetailService;
    @Inject
    private MallGoodsService mallGoodsService;

    @Override
    public Page<MallGoodsSpecialTopic> page(MallGoodsSpecialTopicPageQueryParam param) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (ObjectUtil.isNotEmpty(param.name())) {
            queryWrapper.like(MallGoodsSpecialTopic::getName, param.name());
        }
        if (ObjectUtil.isNotEmpty(param.startCreateTime()) && ObjectUtil.isNotEmpty(param.endCreateTime())) {
            queryWrapper.between(MallGoodsSpecialTopic::getCreateTime, param.startCreateTime(), param.endCreateTime());
        }

        // 关键词查询
        if (ObjectUtil.isNotEmpty(param.searchKey())) {
            // sql注入处理
            queryWrapper.and(wq -> {
                wq.where(wq1 -> {
                    wq1.or(MallGoodsSpecialTopic::getName).like(param.searchKey());
                });
            });
        }

        if (ObjectUtil.isAllNotEmpty(param.sortField(), param.sortOrder())) {
            CommonSortOrderEnum.validate(param.sortOrder());
            queryWrapper.orderBy(CharSequenceUtil.toUnderlineCase(param.sortField()), param.sortOrder().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(MallGoodsSpecialTopic::getCreateTime, false);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Override
    public void add(MallGoodsSpecialTopicAddParam param) {
        MallGoodsSpecialTopic topic = new MallGoodsSpecialTopic();
        topic.setName(param.name());
        topic.setSort(param.sort());
        topic.setCarousel(param.carousel());
        topic.setBackground(param.background());
        this.save(topic);
    }

    @Override
    public void edit(MallGoodsSpecialTopicEditParam param) {
        UpdateChain.of(MallGoodsSpecialTopic.class)
                .set(MallGoodsSpecialTopic::getName, param.name())
                .set(MallGoodsSpecialTopic::getSort, param.sort())
                .set(MallGoodsSpecialTopic::getCarousel, param.carousel())
                .set(MallGoodsSpecialTopic::getBackground, param.background())
                .where(MallGoodsSpecialTopic::getId).eq(param.id())
                .update();
    }

    @Override
    public void delete(CommonValidList<MallGoodsSpecialTopicIdParam> param) {
        this.removeByIds(param.getList().stream().map(MallGoodsSpecialTopicIdParam::id).toList());
    }

    @Override
    @Tran
    public void bind(MallGoodsSpecialTopicBindParam param) {
        boolean res = mallGoodsService.deductionMallGoodsStock(param.goodsId(), param.goodsNum());
        if (res) {
            MallGoodsSpecialTopicDetail detail = new MallGoodsSpecialTopicDetail();
            detail.setSpecialTopicId(param.specialTopicId());
            detail.setGoodsId(param.goodsId());
            detail.setGoodsNum(param.goodsNum());
            detail.setGoodsPrice(param.goodsPrice());
            this.mallGoodsSpecialTopicDetailService.save(detail);
            return;
        }
        throw new CommonException("商品库存不足");
    }

    @Override
    public Page<MallGoodsSpecialTopicDetail> detailPage(MallGoodsSpecialTopicDetailPageQueryParam param) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(MallGoodsSpecialTopicDetail::getSpecialTopicId, param.specialTopicId());
        if (ObjectUtil.isNotEmpty(param.startCreateTime()) && ObjectUtil.isNotEmpty(param.endCreateTime())) {
            queryWrapper.between(MallGoodsSpecialTopicDetail::getCreateTime, param.startCreateTime(), param.endCreateTime());
        }

        if (ObjectUtil.isAllNotEmpty(param.sortField(), param.sortOrder())) {
            CommonSortOrderEnum.validate(param.sortOrder());
            queryWrapper.orderBy(CharSequenceUtil.toUnderlineCase(param.sortField()), param.sortOrder().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(MallGoodsSpecialTopicDetail::getCreateTime, false);
        }
        Page<MallGoodsSpecialTopicDetail> page = this.mallGoodsSpecialTopicDetailService.page(CommonPageRequest.defaultPage(), queryWrapper);

        page.getRecords().forEach(p -> {
            MallGoods mallGoods = mallGoodsService.getById(p.getGoodsId());
            if (Objects.nonNull(mallGoods)) {
                p.setTitle(mallGoods.getTitle());
                p.setHostImage(mallGoods.getHostImage());
                p.setPrice(BigDecimal.valueOf(mallGoods.getPrice()));
            }
        });

        return page;
    }

    @Override
    @Tran
    public void detailDelete(CommonValidList<MallGoodsSpecialTopicIdParam> param) {
        param.getList().forEach(p -> {
            MallGoodsSpecialTopicDetail detail = this.mallGoodsSpecialTopicDetailService.getById(p.id());
            mallGoodsService.supplementGoodsStock(detail.getGoodsId(), detail.getGoodsNum());
        });
        this.mallGoodsSpecialTopicDetailService.removeByIds(param.stream().map(MallGoodsSpecialTopicIdParam::id).toList());
    }

    @Override
    public Page<MallGoodsSearchResDTO> specialTopicGoodsPage(MallSpecialTopicGoodsSearchDTO dto) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(MallGoodsSpecialTopicDetail::getSpecialTopicId, dto.specialTopicId());

        if (Objects.nonNull(dto.price())) {
            CommonSortOrderEnum.validate(dto.price().getValue());
            queryWrapper.orderBy(MallGoodsSpecialTopicDetail::getGoodsPrice, dto.price().getValue().equals(CommonSortOrderEnum.ASC.getValue()));
        } else if (Objects.nonNull(dto.sort())) {
            CommonSortOrderEnum.validate(dto.sort().getValue());
            queryWrapper.orderBy(MallGoodsSpecialTopicDetail::getId, dto.sort().getValue().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(MallGoodsSpecialTopicDetail::getCreateTime, false);
        }


        Page<MallGoodsSpecialTopicDetail> page = this.mallGoodsSpecialTopicDetailService.page(CommonPageRequest.defaultPage(), queryWrapper);

        return page.map(p -> {
            MallGoods mallGoods = mallGoodsService.getById(p.getGoodsId());
            if (Objects.nonNull(mallGoods)) {
                MallGoodsSearchResDTO bean = BeanUtil.toBean(mallGoods, MallGoodsSearchResDTO.class);
                if (Objects.nonNull(p.getGoodsPrice())) {
                    bean.setPrice(p.getGoodsPrice().doubleValue());
                }
                bean.setStock(p.getGoodsNum());
                return bean;
            } else {
                return null;
            }
        });
    }
}
