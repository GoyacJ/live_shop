package vip.xiaonuo.live.api.live;


import cn.hutool.json.JSONObject;
import org.noear.solon.validation.annotation.Validated;
import vip.xiaonuo.live.api.live.dto.CreateShoppingCarOrderDTO;
import vip.xiaonuo.live.api.live.dto.LiveBizDTO;
import vip.xiaonuo.live.api.live.dto.LiveVisitDurationDTO;
import vip.xiaonuo.live.api.live.dto.ShoppingCarDTO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface LiveApi {

    /**
     * 获取推流地址
     *
     * @return
     */
    Map<String, String> getLiveAddress();

    /**
     * 获取拉流地址
     *
     * @return
     */
    String getLivePullAddress();

    /**
     * 获取直播配置
     *
     * @param id
     * @return
     */
    JSONObject getLiveConfiguration(String id);

    /**
     * 创建营销活动
     *
     * @return
     */
    int getMarketingActivities(Integer peopleCount, double price, String seconds);

    /**
     * 获取营销活动
     */
    JSONObject getMarketing(String marketingId);

    /**
     * 更新营销活动状态
     *
     * @param id
     */
    void updateMarketingActivities(Integer id, Integer marketingState);

    /**
     * 保存营销活动记录
     *
     * @param userId
     * @param marketingId
     * @param marketingType
     * @param value
     * @return
     */
    int saveMarketingCampaignsLog(List<Integer> userId, Integer marketingId, Integer marketingType, double value);

    /**
     * 获取购物车商品
     *
     * @param id
     * @return
     */
    List<JSONObject> getShoppingCar(String id);

    /**
     * 获取购物车商品
     *
     * @param liveId
     * @return
     */
    List<ShoppingCarDTO> getShoppingCarByLiveId(String liveId);

    /**
     * 获取直播间信息
     *
     * @param liveId
     * @return
     */
    LiveBizDTO getLiveByLiveId(String liveId);

    JSONObject getShoppingCarOne(String liveId, String goodsId);

    /**
     * 更新营销活动状态
     *
     * @param id
     * @param marketingState
     * @return
     */
    int updateMarketingId(Integer id, Integer marketingState);

    /**
     * 根据直播类型获取直播信息
     *
     * @param type
     * @return
     */
    List<JSONObject> getLiveBizList(String type);

    /**
     * 获取直播预览信息根据时间排序
     *
     * @param
     * @return
     */
    List<JSONObject> getLiveBizOrderByTimeList();

    /**
     * 获取直播类型list
     *
     * @param
     * @return
     */
    List<JSONObject> getLiveTypeList();

    /**
     * 获取主播信息list
     *
     * @param
     * @return
     */
    List<JSONObject> getLiveAnchorList();

    JSONObject getLiveAnchorDetailById(String id);

    List<JSONObject> getLiveBizByIds(Collection<String> ids);

    /**
     * 扣减购物车库存
     *
     * @param shopCarId 购物车Id
     * @param stock     数量
     * @return 结果
     */
    boolean deductionCarGoodsStock(String shopCarId, Integer stock);

    /**
     * 创建订单
     *
     * @param param 创建订单参数
     * @return 订单Id
     */
    String createShoppingCarOrder(@Validated CreateShoppingCarOrderDTO param);

    /**
     * 观看时长
     *
     * @param dto dto
     */
    void viewDuration(LiveVisitDurationDTO dto);
}
