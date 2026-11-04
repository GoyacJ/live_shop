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
package vip.xiaonuo.mobile.modular.live.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.common.cache.CommonCacheOperator;
import vip.xiaonuo.dev.api.DevSlideshowApi;
import vip.xiaonuo.dev.modular.slideshow.entity.DevSlideshow;
import vip.xiaonuo.live.api.live.LiveApi;
import vip.xiaonuo.live.api.live.dto.LiveVisitDurationDTO;
import vip.xiaonuo.mobile.modular.live.param.UserVisitLiveTimeParam;
import vip.xiaonuo.mobile.modular.live.result.*;
import vip.xiaonuo.mobile.modular.live.service.LiveService;
import vip.xiaonuo.mobile.modular.manage.service.UserAddressService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * 直播Service接口实现类
 */
@Component
public class LiveServiceImpl implements LiveService {

    @Inject
    private DevSlideshowApi devSlideshowApi;
    @Inject
    private LiveApi liveApi;
    @Inject
    private UserAddressService userAddressService;

    @Inject
    private CommonCacheOperator commonCacheOperator;

    @Override
    public HomeDataResult getHomeData() {
        HomeDataResult homeDataResult = new HomeDataResult();
        List<Banner> bannerList;
        List<LivePreview> livePreviewList;

        //根据place获取轮播图信息dev_slideshow 默认为地址为首页["CLIENT_MOBILE"]
        List<JSONObject> objectlist = devSlideshowApi.getListByPlace("CLIENT_MOBILE");
        //查询现有直播类型
        //查询直播信息
        List<JSONObject> liveBizOrderByTimeList = liveApi.getLiveBizOrderByTimeList();
        //查询主播信息

        //处理轮播图数据
        bannerList = madeBannerList(objectlist);
        homeDataResult.setBannerList(bannerList);

        //处理直播类型直播数据

        //根据时间获取直播预览信息
        livePreviewList = madeLivePreviewList(liveBizOrderByTimeList);
        homeDataResult.setLivePreviewList(livePreviewList);

        return homeDataResult;
    }

    @Override
    public JSONObject getOrderBaseInfo() {
        List<Payment> paymentList = new ArrayList<>();
        Payment payment = new Payment();
        payment.setId("111");
        payment.setType("wx");
        payment.setTitle("微信支付");
        payment.setIcon("https://file.live.weilaizhihuigu.cn/live/2025/4/8/1909546239861530624.png");
        paymentList.add(payment);

        JSONObject jsonObject = new JSONObject();
        jsonObject.set("default_address", userAddressService.getDefaultAddress());
        jsonObject.set("payment_list", paymentList);
        return jsonObject;
    }

    @Override
    public boolean userVisitLiveTime(UserVisitLiveTimeParam param) {
        if (param.visitTime() <= 0) {
            throw new IllegalArgumentException("观看时长必须为正值");
        }

        LocalDate today = LocalDate.now();
        String dayKey = "live:watch:time:" + param.userId() + ":" + param.liveId() + "D" + today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // 日维度
        commonCacheOperator.incrBy(dayKey, param.visitTime());

        // 保留90天数据
        commonCacheOperator.expire(dayKey, 3600 * 24 * 90);

        // 周维度
        WeekFields weekFields = WeekFields.of(Locale.CHINA);
        int weekNum = today.get(weekFields.weekOfWeekBasedYear());
        String weekKey = "live:watch:time:" + param.userId() + ":" + param.liveId() + "W" + weekNum;
        commonCacheOperator.incrBy(weekKey, param.visitTime());

        // 保留90天数据
        commonCacheOperator.expire(weekKey, 3600 * 24 * 90);

        // 月维度
        String monthKey = "live:watch:time:" + param.userId() + ":" + param.liveId() + "M" + today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        commonCacheOperator.incrBy(monthKey, param.visitTime());
        // 保留3个月
        commonCacheOperator.expire(monthKey, 3600 * 24 * 90);

        return true;
    }

    @Override
    public void viewDuration(LiveVisitDurationDTO dto) {
        liveApi.viewDuration(dto);
    }

    //处理轮播图数据
    public List<Banner> madeBannerList(List<JSONObject> objectlist) {
        List<Banner> bannerList = new ArrayList<>();
        if (IterUtil.isNotEmpty(objectlist)) {
            List<DevSlideshow> devSlideshowList = new ArrayList<>();
            for (JSONObject jsonObject : objectlist) {
                devSlideshowList.add(BeanUtil.toBean(jsonObject, DevSlideshow.class));
            }
            //添加轮播图信息
            bannerList = devSlideshowList.stream()
                    .sorted(Comparator.comparingInt(DevSlideshow::getSortCode))
                    .map(slide -> {
                        Banner banner = new Banner();
                        banner.setTitle(slide.getTitle());
                        banner.setImage(slide.getImage());
                        JSON parse = JSONUtil.parse(slide.getPathDetails());
                        if (parse instanceof JSONArray array) {
                            array.forEach((detail) -> {
                                JSONObject detailObj = JSONUtil.parseObj(detail);
                                if ("CLIENT_MOBILE".equals(detailObj.getStr("key"))) {
                                    banner.setWhetherToClick(detailObj.getStr("whetherToClick"));
                                    banner.setSkipMode(detailObj.getStr("skipMode"));
                                    banner.setUrl(detailObj.getStr("url"));
                                }
                            });
                        } else if (parse instanceof JSONObject jsonObject) {
                            banner.setWhetherToClick(jsonObject.getStr("whetherToClick"));
                            banner.setSkipMode(jsonObject.getStr("skipMode"));
                            banner.setUrl(jsonObject.getStr("url"));
                        }

                        return banner;
                    }).collect(Collectors.toList());
        }
        return bannerList;
    }

    //处理直播类型直播数据
    public List<LiveType> madeLiveTypeList(List<JSONObject> liveTypeList1, List<JSONObject> liveBizOrderByTimeList, List<JSONObject> liveAnchorList) {
        List<LiveType> liveTypeList = new ArrayList<>();
        //根据类型进行分组，每组放两条信息
        if (liveTypeList1.size() > 0) {
            for (JSONObject JSonObject : liveTypeList1) {
                LiveType liveType = new LiveType();
                liveType.setId(JSonObject.get("id").toString());
                liveType.setTitle(JSonObject.get("title").toString());
                List<LivePreview> livePreviewList1 = new ArrayList<>();
                for (JSONObject JSonObject1 : liveBizOrderByTimeList) {
                    if (JSonObject1.get("liveState") == null) {
                        continue;
                    }
                    if (JSonObject.get("id").toString().equals(JSonObject1.get("liveState").toString())) {
                        if (livePreviewList1.size() < 3) {
                            LivePreview livePreview = new LivePreview();
                            //设置直播信息
                            livePreview.setId(JSonObject1.get("id").toString());
                            livePreview.setTitle(JSonObject1.get("mainTitle").toString());
                            livePreview.setImage(JSonObject1.get("coverUrl").toString());
                            //设置主播信息
                            for (JSONObject jSONObject : liveAnchorList) {
                                if (JSonObject1.get("anchorId") == null || jSONObject.get("id") == null) {
                                    continue;
                                }
                                if (JSonObject1.get("anchorId").toString().equals(jSONObject.get("id").toString())) {
                                    livePreview.setRealName(jSONObject.get("realName").toString());
                                    livePreview.setPictureUrl(jSONObject.get("pictureUrl").toString());
                                }
                            }
                            livePreviewList1.add(livePreview);
                        }
                    }
                }
                liveType.setList(livePreviewList1);
                liveTypeList.add(liveType);
            }
        }
        return liveTypeList;
    }

    //处理直播预览信息
    public List<LivePreview> madeLivePreviewList(List<JSONObject> liveBizOrderByTimeList) {
        List<LivePreview> livePreviewList = new ArrayList<>();
        if (!liveBizOrderByTimeList.isEmpty()) {
            // int i = 0;
            for (JSONObject jsonObject : liveBizOrderByTimeList) {
                // if (i > 1) {
                //     break;
                // }
                if (jsonObject.get("liveRoomState") == null) {
                    continue;
                }
                String liveRoomState = jsonObject.get("liveRoomState").toString();
                if (StrUtil.equals(liveRoomState, "1")) {
                    LivePreview livePreview = new LivePreview();
                    //添加直播预览信息
                    livePreview.setId(jsonObject.getStr("id"));
                    livePreview.setTitle(jsonObject.getStr("mainTitle"));
                    livePreview.setImage(jsonObject.getStr("coverUrl"));
/*                    //添加主播信息
                    for (JSONObject jSONObject : liveAnchorList) {
                        if (JSonObject.get("anchorId") == null || jSONObject.get("id") == null) {
                            continue;
                        }
                        if (JSonObject.get("anchorId").toString().equals(jSONObject.get("id").toString())) {
                            livePreview.setRealName(jSONObject.get("realName").toString());
                            livePreview.setPictureUrl(jSONObject.get("pictureUrl").toString());
                        }
                    }*/
                    livePreviewList.add(livePreview);
                }
                // i++;
            }
        }
        return livePreviewList;
    }
}