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
package vip.xiaonuo.shop.modular.mallthirdpaymen.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.shop.modular.mallorder.entity.MallOrder;
import vip.xiaonuo.shop.modular.mallorder.mapper.MallOrderMapper;
import vip.xiaonuo.shop.modular.mallorder.param.MallOrderAddParam;
import vip.xiaonuo.shop.modular.mallorder.param.MallOrderEditParam;
import vip.xiaonuo.shop.modular.mallorder.param.MallOrderIdParam;
import vip.xiaonuo.shop.modular.mallorder.param.MallOrderPageParam;
import vip.xiaonuo.shop.modular.mallorder.service.MallOrderService;
import vip.xiaonuo.shop.modular.mallthirdpaymen.entity.MallThirdPayment;
import vip.xiaonuo.shop.modular.mallthirdpaymen.mapper.MallThirdPaymentMapper;
import vip.xiaonuo.shop.modular.mallthirdpaymen.param.MallThirdPaymentAddParam;
import vip.xiaonuo.shop.modular.mallthirdpaymen.param.MallThirdPaymentEditParam;
import vip.xiaonuo.shop.modular.mallthirdpaymen.param.MallThirdPaymentIdParam;
import vip.xiaonuo.shop.modular.mallthirdpaymen.param.MallThirdPaymentPageParam;
import vip.xiaonuo.shop.modular.mallthirdpaymen.service.MallThirdPaymentService;

import java.util.List;

/**
 * 商城-支付Service接口实现类
 *
 **/



@Component
public class MallThirdPaymentServiceImpl extends ServiceImpl<MallThirdPaymentMapper, MallThirdPayment> implements MallThirdPaymentService {

    @Inject
    private MallOrderService mallOrderService;

    @Override
    public Page<MallThirdPayment> page(MallThirdPaymentPageParam mallThirdPaymentPageParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(ObjectUtil.isNotEmpty(mallThirdPaymentPageParam.getOrderNumber())) {
            MallOrderIdParam orderIdParam = new MallOrderIdParam();
            orderIdParam.setOrderNumber(mallThirdPaymentPageParam.getOrderNumber());
            MallOrder mallOrder = mallOrderService.detail(orderIdParam);
            if(ObjectUtil.isEmpty(mallOrder)) {
                throw new CommonException("商城-订单信息不存在，订单编号为：{}", mallThirdPaymentPageParam.getOrderNumber());
            }
            queryWrapper.like(MallThirdPayment::getOrderId, mallOrder.getId());
        }
        if(ObjectUtil.isNotEmpty(mallThirdPaymentPageParam.getStartCreateTime()) && ObjectUtil.isNotEmpty(mallThirdPaymentPageParam.getEndCreateTime())) {
            queryWrapper.between(MallThirdPayment::getCreateTime, mallThirdPaymentPageParam.getStartCreateTime(), mallThirdPaymentPageParam.getEndCreateTime());
        }else {
            queryWrapper.orderBy(MallThirdPayment::getCreateTime, false);
        }
         /*
            增加筛选条件: 订单状态、下单手机号
         */
        if (StringUtils.isNotEmpty(mallThirdPaymentPageParam.getStatus())){
            queryWrapper.eq(MallThirdPayment::getStatus, mallThirdPaymentPageParam.getStatus());
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Tran
    @Override
    public void add(MallThirdPaymentAddParam mallThirdPaymentAddParam) {
        if(ObjectUtil.isEmpty(mallThirdPaymentAddParam)) {
            throw new CommonException("商城-支付信息不能为空");
        }
        try {
            MallThirdPayment mallThirdPayment = BeanUtil.toBean(mallThirdPaymentAddParam, MallThirdPayment.class);
            this.save(mallThirdPayment);
        } catch (Exception e) {
            throw new CommonException("商城-支付信息添加失败");
        }
    }

    @Override
    public void edit(MallThirdPaymentEditParam mallThirdPaymentEditParam) {
        MallThirdPayment mallThirdPayment = this.getById(mallThirdPaymentEditParam.getId());
        if(ObjectUtil.isEmpty(mallThirdPayment)) {
            throw new CommonException("商城-支付信息不存在，id值为：{}", mallThirdPaymentEditParam.getId());
        }
        try {
            BeanUtil.copyProperties(mallThirdPaymentEditParam, mallThirdPayment);
            this.updateById(mallThirdPayment);
        } catch (Exception e) {
            throw new CommonException("商城-支付信息修改失败");
        }
    }
    @Tran
    @Override
    public void delete(List<MallThirdPaymentIdParam> mallThirdPaymentIdParamList) {
        try {
            // 执行删除
            this.removeByIds(CollStreamUtil.toList(mallThirdPaymentIdParamList, MallThirdPaymentIdParam::getId));
        } catch (Exception e) {
            throw new CommonException("商城-支付信息删除失败");
        }
    }

    @Override
    public MallThirdPayment detail(MallThirdPaymentIdParam mallThirdPaymentIdParam) {
        MallThirdPayment mallThirdPayment = this.getById(mallThirdPaymentIdParam.getId());
        if(ObjectUtil.isEmpty(mallThirdPayment)) {
            throw new CommonException("商城-订单不存在，id值为：{}", mallThirdPayment.getId());
        }
        return mallThirdPayment;
    }
}
