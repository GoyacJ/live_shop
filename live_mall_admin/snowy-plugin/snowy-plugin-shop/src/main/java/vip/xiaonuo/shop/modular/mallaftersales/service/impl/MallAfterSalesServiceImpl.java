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
package vip.xiaonuo.shop.modular.mallaftersales.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.shop.modular.mallaftersales.entity.MallAfterSales;
import vip.xiaonuo.shop.modular.mallaftersales.mapper.MallAfterSalesMapper;
import vip.xiaonuo.shop.modular.mallaftersales.param.MallAfterSalesAddParam;
import vip.xiaonuo.shop.modular.mallaftersales.param.MallAfterSalesEditParam;
import vip.xiaonuo.shop.modular.mallaftersales.param.MallAfterSalesIdParam;
import vip.xiaonuo.shop.modular.mallaftersales.param.MallAfterSalesPageParam;
import vip.xiaonuo.shop.modular.mallaftersales.result.MallAfterSalesPageResult;
import vip.xiaonuo.shop.modular.mallaftersales.service.MallAfterSalesService;
import vip.xiaonuo.shop.modular.mallorder.entity.MallOrder;
import vip.xiaonuo.shop.modular.mallorder.mapper.MallOrderMapper;
import vip.xiaonuo.shop.modular.mallorder.param.MallOrderAddParam;
import vip.xiaonuo.shop.modular.mallorder.param.MallOrderEditParam;
import vip.xiaonuo.shop.modular.mallorder.param.MallOrderIdParam;
import vip.xiaonuo.shop.modular.mallorder.param.MallOrderPageParam;
import vip.xiaonuo.shop.modular.mallorder.service.MallOrderService;
import vip.xiaonuo.shop.modular.mallthirdpaymen.entity.MallThirdPayment;

import java.util.ArrayList;
import java.util.List;

/**
 * 商城-售后Service接口实现类
 *
 **/
@Component
public class MallAfterSalesServiceImpl extends ServiceImpl<MallAfterSalesMapper, MallAfterSales> implements MallAfterSalesService {

    @Inject
    private MallOrderService mallOrderService;

    @Override
    public Page<MallAfterSalesPageResult> page(MallAfterSalesPageParam mallAfterSalesPageParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(ObjectUtil.isNotEmpty(mallAfterSalesPageParam.getOrderNumber())) {
            MallOrderIdParam orderIdParam = new MallOrderIdParam();
            orderIdParam.setOrderNumber(mallAfterSalesPageParam.getOrderNumber());
            MallOrder mallOrder = mallOrderService.detail(orderIdParam);
            if(ObjectUtil.isEmpty(mallOrder)) {
                throw new CommonException("商城-订单信息不存在，订单编号为：{}", mallAfterSalesPageParam.getOrderNumber());
            }
            queryWrapper.like(MallAfterSales::getOrderId, mallOrder.getId());
        }
        if(ObjectUtil.isNotEmpty(mallAfterSalesPageParam.getStartCreateTime()) && ObjectUtil.isNotEmpty(mallAfterSalesPageParam.getEndCreateTime())) {
            queryWrapper.between(MallAfterSales::getCreateTime, mallAfterSalesPageParam.getStartCreateTime(), mallAfterSalesPageParam.getEndCreateTime());
        }else {
            queryWrapper.orderBy(MallAfterSales::getCreateTime, false);
        }
        //拼接订单信息
        Page<MallAfterSales> pageList =  this.page(CommonPageRequest.defaultPage(), queryWrapper);
        Page<MallAfterSalesPageResult> pageListResult = new Page<>(pageList.getCurrent(), pageList.getSize(), pageList.getTotal());
        List<MallAfterSalesPageResult> results = new ArrayList<>();
        if (pageList.getRecords().size() > 0) {
            for (MallAfterSales mallAfterSales : pageList.getRecords()){
                MallAfterSalesPageResult result = BeanUtil.toBean(mallAfterSales, MallAfterSalesPageResult.class);
                //根据订单id查询订单编号
                MallOrder mallOrder = mallOrderService.getById(mallAfterSales.getOrderId());
                if (!ObjectUtil.isEmpty(mallOrder)){
//                    throw new CommonException("商城-订单信息不存在，订单id为：{}", mallAfterSales.getOrderId());
                    result.setOrderNumber(mallOrder.getOrderNumber());
                    result.setOrderType(mallOrder.getOrderType());
                }
                results.add(result);
            }
        }
        pageListResult.setRecords(results);
        return pageListResult;
    }

    @Tran
    @Override
    public void add(MallAfterSalesAddParam mallAfterSalesAddParam) {
        if(ObjectUtil.isEmpty(mallAfterSalesAddParam)) {
            throw new CommonException("售后信息不能为空");
        }
        try {
            MallAfterSales mallAfterSales = BeanUtil.toBean(mallAfterSalesAddParam, MallAfterSales.class);
            this.save(mallAfterSales);
        }catch (Exception e) {
            throw new CommonException("售后信息添加失败");
        }
    }

    @Override
    public void edit(MallAfterSalesEditParam mallAfterSalesEditParam) {
        MallAfterSales mallAfterSales = this.getById(mallAfterSalesEditParam.getId());
        if(ObjectUtil.isEmpty(mallAfterSales)) {
            throw new CommonException("商城-售后信息不存在，id值为：{}", mallAfterSalesEditParam.getId());
        }
        try {
            BeanUtil.copyProperties(mallAfterSalesEditParam, mallAfterSales);
            this.updateById(mallAfterSales);
        }catch (Exception e) {
            throw new CommonException("商城-售后信息修改失败");
        }
    }
    @Tran
    @Override
    public void delete(List<MallAfterSalesIdParam> mallAfterSalesIdParamList) {
        try {
            // 执行删除
            this.removeByIds(CollStreamUtil.toList(mallAfterSalesIdParamList, MallAfterSalesIdParam::getId));
        } catch (Exception e) {
            throw new CommonException("商城-售后信息删除失败");
        }
    }

    @Override
    public MallAfterSales detail(MallAfterSalesIdParam mallAfterSalesIdParam) {
        MallAfterSales mallAfterSales = this.getById(mallAfterSalesIdParam.getId());
        return mallAfterSales;
    }
}
