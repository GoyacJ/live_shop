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
package vip.xiaonuo.shop.modular.mallaftersales.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import vip.xiaonuo.shop.modular.mallaftersales.entity.MallAfterSales;
import vip.xiaonuo.shop.modular.mallaftersales.param.MallAfterSalesAddParam;
import vip.xiaonuo.shop.modular.mallaftersales.param.MallAfterSalesEditParam;
import vip.xiaonuo.shop.modular.mallaftersales.param.MallAfterSalesIdParam;
import vip.xiaonuo.shop.modular.mallaftersales.param.MallAfterSalesPageParam;
import vip.xiaonuo.shop.modular.mallaftersales.result.MallAfterSalesPageResult;
import vip.xiaonuo.shop.modular.mallorder.param.MallOrderAddParam;

import java.util.List;

/**
 * 商城-售后Service接口
 *
 **/
public interface MallAfterSalesService extends IService<MallAfterSales> {

    /**
     * 售后分页
     *
     */
    Page<MallAfterSalesPageResult> page(MallAfterSalesPageParam mallAfterSalesPageParam);

    /**
     * 添加售后
     *
     */
    void add(MallAfterSalesAddParam mallAfterSalesAddParam);

    /**
     * 编辑售后
     *
     */
    void edit(MallAfterSalesEditParam mallAfterSalesEditParam);

    /**
     * 删除售后
     *
     */
    void delete(List<MallAfterSalesIdParam> mallAfterSalesIdParam);

    /**
     * 获取售后详情
     *
     */
    MallAfterSales detail(MallAfterSalesIdParam mallAfterSalesIdParam);

}
