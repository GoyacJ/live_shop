package vip.xiaonuo.shop.modular.specialtopic.service;

import api.shop.dto.MallGoodsSearchResDTO;
import api.shop.dto.MallSpecialTopicGoodsSearchDTO;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import vip.xiaonuo.common.pojo.CommonValidList;
import vip.xiaonuo.shop.modular.specialtopic.entity.MallGoodsSpecialTopic;
import vip.xiaonuo.shop.modular.specialtopic.entity.MallGoodsSpecialTopicDetail;
import vip.xiaonuo.shop.modular.specialtopic.param.*;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/9 16:37
 */
public interface MallGoodsSpecialTopicService extends IService<MallGoodsSpecialTopic> {
    /**
     * 分页条件查询
     * @param param 条件
     * @return 结果
     */
    Page<MallGoodsSpecialTopic> page(MallGoodsSpecialTopicPageQueryParam param);

    /**
     * 新增商品专题
     * @param param 参数
     */
    void add(MallGoodsSpecialTopicAddParam param);

    /**
     * 编辑商品专题
     * @param param 参数
     */
    void edit(MallGoodsSpecialTopicEditParam param);

    /**
     * 删除商品专题
     * @param param 参数
     */
    void delete(CommonValidList<MallGoodsSpecialTopicIdParam> param);

    /**
     * 绑定商品
     * @param param 参数
     */
    void bind(MallGoodsSpecialTopicBindParam param);

    /**
     * 专题详情商品分页
     * @param param 参数
     * @return 结果
     */
    Page<MallGoodsSpecialTopicDetail> detailPage(MallGoodsSpecialTopicDetailPageQueryParam param);

    /**
     * 删除专题绑定商品
     * @param param 参数
     */
    void detailDelete(CommonValidList<MallGoodsSpecialTopicIdParam> param);

    /**
     * 专题商品列表
     * @param dto 参数
     * @return 专题商品列表
     */
    Page<MallGoodsSearchResDTO> specialTopicGoodsPage(MallSpecialTopicGoodsSearchDTO dto);
}
