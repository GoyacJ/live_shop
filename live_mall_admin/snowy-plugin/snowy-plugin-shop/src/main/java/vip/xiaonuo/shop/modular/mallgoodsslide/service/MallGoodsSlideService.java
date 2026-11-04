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
package vip.xiaonuo.shop.modular.mallgoodsslide.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import vip.xiaonuo.shop.modular.mallgoodsChange.param.MallGoodsIdParam;
import vip.xiaonuo.shop.modular.mallgoodsslide.entity.MallGoodsSlide;
import vip.xiaonuo.shop.modular.mallgoodsslide.param.MallGoodsSlideAddParam;
import vip.xiaonuo.shop.modular.mallgoodsslide.param.MallGoodsSlideEditParam;
import vip.xiaonuo.shop.modular.mallgoodsslide.param.MallGoodsSlideIdParam;
import vip.xiaonuo.shop.modular.mallgoodsslide.param.MallGoodsSlidePageParam;

import java.util.List;

/**
 * 商品轮播图Service接口
 *
 * @author yuezisheng
 * @date  2025/02/20 15:30
 **/
public interface MallGoodsSlideService extends IService<MallGoodsSlide> {

    /**
     * 获取商品轮播图分页
     *
     * @author yuezisheng
     * @date  2025/02/20 15:30
     */
    Page<MallGoodsSlide> page(MallGoodsSlidePageParam mallGoodsSlidePageParam);

    /**
     * 添加商品轮播图
     *
     * @author yuezisheng
     * @date  2025/02/20 15:30
     */
    void add(MallGoodsSlideAddParam mallGoodsSlideAddParam);

    /**
     * 编辑商品轮播图
     *
     * @author yuezisheng
     * @date  2025/02/20 15:30
     */
    void edit(MallGoodsSlideEditParam mallGoodsSlideEditParam);

    /**
     * 删除商品轮播图
     *
     * @author yuezisheng
     * @date  2025/02/20 15:30
     */
    void delete(List<MallGoodsSlideIdParam> mallGoodsSlideIdParamList);

    /**
     * 获取商品轮播图详情
     *
     * @author yuezisheng
     * @date  2025/02/20 15:30
     */
    MallGoodsSlide detail(MallGoodsSlideIdParam mallGoodsSlideIdParam);

    /**
     * 获取商品轮播图详情
     *
     * @author yuezisheng
     * @date  2025/02/20 15:30
     **/
    MallGoodsSlide queryEntity(String id);


    /**
     * 根据商品id查询图片信息
     *
     **/
    List<MallGoodsSlide> queryEntityListByGoodsId(MallGoodsSlideIdParam mallGoodsSlideIdParam);

    /**
     * 根据商品id删除图片信息
     *
     **/
    void deleteEntityListByGoodsId(MallGoodsSlideIdParam mallGoodsSlideIdParam);
}
