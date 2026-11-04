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
package vip.xiaonuo.shop.modular.mallsalestype.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.util.ObjectUtil;
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
import vip.xiaonuo.shop.modular.mallsalestype.entity.MallSalesType;
import vip.xiaonuo.shop.modular.mallsalestype.mapper.MallSalesTypeMapper;
import vip.xiaonuo.shop.modular.mallsalestype.param.MallSalesTypeAddParam;
import vip.xiaonuo.shop.modular.mallsalestype.param.MallSalesTypeEditParam;
import vip.xiaonuo.shop.modular.mallsalestype.param.MallSalesTypeIdParam;
import vip.xiaonuo.shop.modular.mallsalestype.param.MallSalesTypeSelectorParam;
import vip.xiaonuo.shop.modular.mallsalestype.service.MallSalesTypeService;

import java.util.List;

/**
 * 商城-售后类型Service接口实现类
 *
 **/
@Component
public class MallSalesTypeServiceImpl extends ServiceImpl<MallSalesTypeMapper, MallSalesType> implements MallSalesTypeService {


    @Tran
    @Override
    public void add(MallSalesTypeAddParam mallSalesTypeAddParam) {
        if(ObjectUtil.isEmpty(mallSalesTypeAddParam)) {
            throw new CommonException("商城-售后类型信息不能为空");
        }
        try {
            MallSalesType mallSalesType = BeanUtil.toBean(mallSalesTypeAddParam, MallSalesType.class);
            this.save(mallSalesType);
        } catch (Exception e) {
            throw new  CommonException("商城-售后类型信息添加失败");
        }
    }

    @Override
    public void edit(MallSalesTypeEditParam mallSalesTypeEditParam) {
        MallSalesType mallSalesType = this.getById(mallSalesTypeEditParam.getId());
        if(ObjectUtil.isEmpty(mallSalesType)) {
            throw new CommonException("商城-售后类型不存在，id值为：{}", mallSalesTypeEditParam.getId());
        }
        try {
            BeanUtil.copyProperties(mallSalesTypeEditParam, mallSalesType);
            this.updateById(mallSalesType);
        } catch (Exception e) {
            throw new CommonException("商城-售后类型信息修改失败");
        }
    }
    @Tran
    @Override
    public void delete(List<MallSalesTypeIdParam> mallSalesTypeIdParamList) {
        try {
            // 执行删除
            this.removeByIds(CollStreamUtil.toList(mallSalesTypeIdParamList, MallSalesTypeIdParam::getId));
        } catch (Exception e) {
            throw new CommonException("商城-售后类型信息删除失败");
        }
    }

    @Override
    public MallSalesType detail(MallSalesTypeIdParam mallSalesTypeIdParam) {
        MallSalesType mallSalesType = this.getById(mallSalesTypeIdParam.getId());
       if(ObjectUtil.isEmpty(mallSalesType)) {
            throw new CommonException("商城-售后类型不存在，id值为：{}", mallSalesTypeIdParam.getId());
        }
        return mallSalesType;
    }

    @Override
    public List<MallSalesType> getMallSalesTypeList(MallSalesTypeIdParam mallSalesTypeIdParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(ObjectUtil.isNotEmpty(mallSalesTypeIdParam.getId())) {
            queryWrapper.eq("id", mallSalesTypeIdParam.getId());
        }
        List<MallSalesType> list = this.list(queryWrapper);
        return list;
    }

    @Override
    public Page<MallSalesType> salesTypeSelector(MallSalesTypeSelectorParam mallSalesTypeSelectorParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        // 只查询部分字段
        queryWrapper.select(MallSalesType::getId, MallSalesType::getTitle, MallSalesType::getSort);
            queryWrapper.orderBy(MallSalesType::getSort);
            return this.page(CommonPageRequest.defaultPage(), queryWrapper);
        }
}
