package vip.xiaonuo.mobile.api.live;


import cn.hutool.json.JSONObject;

import java.util.Map;

public interface LiveConfigurationApi {

    Map<String, String> getLiveUrl();
    /**
     * 直播间信息  直播拉流地址  购物车商品
     */
    Map<String, Object> getLiveInformation(String id);

    /**
     * 创建福袋
     */
    Map<String, String> createFortune(String popleCount,String price,String seconds);

    /**
     * 领取福袋
     */
    Map<String, String> getFortune(String fortuneId);

    /**
     * 获取app分享链接
     */
    String getAppSharingLinks();

    /**
     * 获取live分享链接
     */
    Map<String, String> getLiveSharingLinks();

    /**
     * 获取app首页数据
     */
    JSONObject getHomeData();

}
