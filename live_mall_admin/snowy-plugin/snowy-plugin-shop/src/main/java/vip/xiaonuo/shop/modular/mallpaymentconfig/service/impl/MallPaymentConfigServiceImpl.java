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
package vip.xiaonuo.shop.modular.mallpaymentconfig.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import org.noear.solon.annotation.Component;
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
import vip.xiaonuo.shop.modular.mallpaymentconfig.entity.MallPaymentConfig;
import vip.xiaonuo.shop.modular.mallpaymentconfig.mapper.MallPaymentConfigMapper;
import vip.xiaonuo.shop.modular.mallpaymentconfig.param.MallPaymentConfigAddParam;
import vip.xiaonuo.shop.modular.mallpaymentconfig.param.MallPaymentConfigEditParam;
import vip.xiaonuo.shop.modular.mallpaymentconfig.param.MallPaymentConfigIdParam;
import vip.xiaonuo.shop.modular.mallpaymentconfig.param.MallPaymentConfigPageParam;
import vip.xiaonuo.shop.modular.mallpaymentconfig.service.MallPaymentConfigService;

import java.util.List;

/**
 * 商城-支付配置Service接口实现类
 *
 **/
@Component
public class MallPaymentConfigServiceImpl extends ServiceImpl<MallPaymentConfigMapper, MallPaymentConfig> implements MallPaymentConfigService {
    @Override
    public Page<MallPaymentConfig> page(MallPaymentConfigPageParam mallPaymentConfigPageParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(ObjectUtil.isNotEmpty(mallPaymentConfigPageParam.getPaymentName())) {
            queryWrapper.like(MallPaymentConfig::getPaymentName, mallPaymentConfigPageParam.getPaymentName());
        }
        if(ObjectUtil.isNotEmpty(mallPaymentConfigPageParam.getStartCreateTime()) && ObjectUtil.isNotEmpty(mallPaymentConfigPageParam.getEndCreateTime())) {
            queryWrapper.between(MallPaymentConfig::getCreateTime, mallPaymentConfigPageParam.getStartCreateTime(), mallPaymentConfigPageParam.getEndCreateTime());
        }else {
            queryWrapper.orderBy(MallPaymentConfig::getCreateTime, false);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Tran
    @Override
    public void add(MallPaymentConfigAddParam mallPaymentConfigAddParam) {
        if(ObjectUtil.isEmpty(mallPaymentConfigAddParam)) {
            throw new CommonException("商城-支付配置信息不能为空");
        }
        try {
            MallPaymentConfig mallPaymentConfig = BeanUtil.toBean(mallPaymentConfigAddParam, MallPaymentConfig.class);
            this.save(mallPaymentConfig);
        } catch (Exception e) {
            throw new CommonException("商城-支付配置信息添加失败");
        }
    }

    @Override
    public void edit(MallPaymentConfigEditParam mallPaymentConfigEditParam) {
        MallPaymentConfig mallPaymentConfig = this.getById(mallPaymentConfigEditParam.getId());
        if(ObjectUtil.isEmpty(mallPaymentConfig)) {
            throw new CommonException("商城-支付配置信息不存在，id值为：{}", mallPaymentConfigEditParam.getId());
        }
        try {
            BeanUtil.copyProperties(mallPaymentConfigEditParam, mallPaymentConfig);
            this.updateById(mallPaymentConfig);
        } catch (Exception e) {
            throw new CommonException("商城-支付配置信息修改失败");
        }
    }
    @Tran
    @Override
    public void delete(List<MallPaymentConfigIdParam> mallPaymentConfigIdParamList) {
        try {
            // 执行删除
            this.removeByIds(CollStreamUtil.toList(mallPaymentConfigIdParamList, MallPaymentConfigIdParam::getId));
        } catch (Exception e) {
            throw new CommonException("商城-支付配置信息删除失败");
        }
    }

    @Override
    public MallPaymentConfig detail(MallPaymentConfigIdParam mallPaymentConfigIdParam) {
        MallPaymentConfig mallPaymentConfig = this.getById(mallPaymentConfigIdParam.getId());
        if(ObjectUtil.isEmpty(mallPaymentConfig)) {
            throw new CommonException("商城-支付配置信息不存在，id值为：{}", mallPaymentConfigIdParam.getId());
        }
        return mallPaymentConfig;
    }
}
