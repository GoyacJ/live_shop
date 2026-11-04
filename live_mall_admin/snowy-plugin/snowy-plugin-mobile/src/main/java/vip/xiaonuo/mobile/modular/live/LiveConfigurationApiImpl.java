package vip.xiaonuo.mobile.modular.live;

import api.shop.MallGoodsApi;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import com.google.zxing.WriterException;
import lombok.extern.slf4j.Slf4j;
import manage.UserApi;
import org.apache.commons.lang3.StringUtils;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.handle.UploadedFile;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.common.cache.CommonCacheOperator;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.common.util.QRCodeGeneratorUtil;
import vip.xiaonuo.common.util.ResponseUtil;
import vip.xiaonuo.common.util.SerializeUtil;
import vip.xiaonuo.dev.api.DevConfigApi;
import vip.xiaonuo.dev.api.DevFileApi;
import vip.xiaonuo.dev.api.DevSlideshowApi;
import vip.xiaonuo.dev.modular.slideshow.entity.DevSlideshow;
import vip.xiaonuo.live.api.live.LiveApi;
import vip.xiaonuo.manage.modular.userinfo.mapper.UserInfoMapper;
import vip.xiaonuo.mobile.api.live.LiveConfigurationApi;
import vip.xiaonuo.mobile.modular.live.result.Banner;
import vip.xiaonuo.mobile.modular.live.result.HomeDataResult;
import vip.xiaonuo.mobile.modular.live.result.LivePreview;
import vip.xiaonuo.mobile.modular.live.result.LiveType;
import vip.xiaonuo.task.api.TaskApi;
import vip.xiaonuo.task.api.enums.TaskBusinessCategory;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static vip.xiaonuo.common.util.CommonServletUtil.getRequest;

@Slf4j
@Component
public class LiveConfigurationApiImpl implements LiveConfigurationApi {

    @Inject
    private LiveApi liveApi;
    @Inject
    private MallGoodsApi mallGoodsApi;
    @Inject
    private DevConfigApi devConfigApi;
    @Inject
    private UserInfoMapper userInfoMapper;
    @Inject
    private UserApi userApi;
    @Inject
    private CommonCacheOperator commonCacheOperator;
    @Inject("devFileApi")
    private DevFileApi devFileApi;

    @Inject("${appQr.appCodeContent}")
    private String app_codeContent;
    @Inject("${appQr.width}")
    private int codeWidth;
    @Inject("${appQr.height}")
    private int codeHeight;
    @Inject("${liveQr.liveCodeContent}")
    private String live_codeContent;

    @Inject
    private DevSlideshowApi devSlideshowApi;

    @Inject
    private TaskApi taskApi;

    @Override
    public Map<String, String> getLiveUrl() {
        return liveApi.getLiveAddress();
    }

    @Override
    @Tran(readOnly = true)
    public Map<String, Object> getLiveInformation(String liveId) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        //获取推流地址
        //查询直播信息
        JSONObject liveConfiguration = liveApi.getLiveConfiguration(liveId);
        if (liveConfiguration.isEmpty()) {
            throw new CommonException("直播信息信息不存在");
        } else {
            JSONObject anchorDetail = liveApi.getLiveAnchorDetailById(liveConfiguration.getStr("anchorId"));

            hashMap.put("id", liveConfiguration.getStr("id"));
            hashMap.put("cover", liveConfiguration.getStr("coverUrl"));
            hashMap.put("url", liveConfiguration.getStr("liveUrl"));
            hashMap.put("anchor", anchorDetail.getStr("username"));
            hashMap.put("avatar", anchorDetail.getStr("avatar"));
            // hashMap.put("viewership", liveConfiguration.getStr("viewership"));
            BigDecimal viewership = RandomUtil.randomBigDecimal(Convert.toBigDecimal(1), Convert.toBigDecimal(6)).setScale(2, RoundingMode.HALF_UP);
            hashMap.put("viewership", Convert.toStr(viewership) + "万人");
        }
        //获取商城信息
        List<JSONObject> shoppingCar = liveApi.getShoppingCar(liveId);
        if (shoppingCar.isEmpty()) {
            hashMap.put("goods", new ArrayList<>());
            return hashMap;
        }

        List<JSONObject> goodsList = mallGoodsApi.getGoodsByIds(shoppingCar.stream().map(obj -> ((JSONObject) obj).getStr("goods_id")).collect(Collectors.toList()));
        Map<String, JSONObject> map = new HashMap<>();
        for (JSONObject jsonObject : goodsList) {
            map.put(jsonObject.getStr("id"), jsonObject);
        }

        System.out.println("商品信息map");
        System.out.println(map);

        for (JSONObject jsonObject : shoppingCar) {
            JSONObject goodsDetail = map.get(jsonObject.getStr("goods_id"));
            if (!ObjectUtil.isEmpty(goodsDetail)) {
                jsonObject.set("goods_title", goodsDetail.getStr("title"));
                jsonObject.set("goods_cover", goodsDetail.getStr("cover"));
                jsonObject.set("sales", goodsDetail.getStr("sales"));
                jsonObject.set("stock", goodsDetail.getStr("stock"));
            }
        }

        if (!shoppingCar.isEmpty()) {
            hashMap.put("goods", shoppingCar);
            // hashMap.put("COMMODITY_ID",shoppingCar.getStr("commodityId"));
            // hashMap.put("QUANTITY",shoppingCar.getStr("quantity"));
            // hashMap.put("PRICE",shoppingCar.getStr("price"));
        }
        return hashMap;
    }

    @Tran
    @Override
    public Map<String, String> createFortune(String peopleCount, String price, String seconds) {
        if (Integer.parseInt(peopleCount) < 0) {
            throw new IllegalArgumentException("中奖人数无效");
        }
        // 检查福袋价格是否有效
        if (Double.parseDouble(price) < 0) {
            throw new IllegalArgumentException("福袋价格无效");
        }
        commonCacheOperator.put("LUCK_EXPIR_TIME", price, Long.parseLong(seconds));
        return ResponseUtil.successResponse(liveApi.getMarketingActivities(Integer.parseInt(peopleCount), Double.parseDouble(price), seconds));
    }

    @Tran
    @Override
    public Map<String, String> getFortune(String fortuneId) {
        Object addUserToGroup = commonCacheOperator.get("openim_user_count");
        //获取在线userID
        List<String> listStrings = SerializeUtil.castList(addUserToGroup, String.class);
        JSONObject marketingActivities = liveApi.getMarketing(fortuneId);
        if (!marketingActivities.isEmpty() && marketingActivities.getStr("marketingId").equals(fortuneId)) {
            //金额
            double marketingValue = Double.parseDouble(marketingActivities.getStr("marketingValue"));
            //人数
            int numberWinners = marketingActivities.getInt("numberWinners");
            //修改福袋状态
            liveApi.updateMarketingId(Integer.parseInt(fortuneId), 1);
            //随机中奖人数
            List randomThreeInfoList = SerializeUtil.getRandomThreeInfoList(listStrings, numberWinners);
            //中奖信息记录
            return ResponseUtil.successResponse(liveApi.saveMarketingCampaignsLog(randomThreeInfoList, Integer.parseInt(fortuneId), 1, marketingValue));
        }
        return ResponseUtil.errorResponse(-1, "福袋领取失败", null);
    }


    //app分享海报  直播间分享海报
    @Override
    public String getAppSharingLinks() {
        Object uid = getRequest().attr("uid");
        if (ObjectUtil.isEmpty(uid)) {
            throw new CommonException("用户未登录");
        }
        return userApi.sharingLinks(Convert.toStr(uid));
    }

    @Override
    public Map<String, String> getLiveSharingLinks() {
        String liveLogo = devConfigApi.getValueByKey("LIVE_LOGO");
        UploadedFile file = null;
        if (!liveLogo.isEmpty()) {
            try {
                InputStream inputStream = QRCodeGeneratorUtil.generateQRCodePoster(liveLogo, live_codeContent, codeWidth, codeHeight);
                file = new UploadedFile("image/png", inputStream, "live.png");
            } catch (IOException | WriterException e) {
                throw new RuntimeException(e);
            }
        }

        String res = devFileApi.storageFileWithReturnUrlMinio(file);

        String userId = Convert.toStr(getRequest().attr("uid"));
        if (StringUtils.isNotEmpty(userId)) {
            taskApi.task(userId, TaskBusinessCategory.LIVE_SHARE);
        }

        return ResponseUtil.successResponse(res);
    }

    @Override
    public JSONObject getHomeData() {
        HomeDataResult homeDataResult = new HomeDataResult();
        //根据place获取轮播图信息dev_slideshow 默认为地址为首页["CLIENT_MOBILE"]
        List<JSONObject> Objectlist = devSlideshowApi.getListByPlace("[\"CLIENT_MOBILE\"]");
        List<Banner> BannerList = new ArrayList<>();
        List<LiveType> liveTypeList = new ArrayList<>();
        List<LivePreview> livePreviewList = new ArrayList<>();
        if (Objectlist.size() > 0) {
            /*List<DevSlideshow> DevSlideshowList = SerializeUtil.castList(Objectlist, DevSlideshow.class);*/
            List<DevSlideshow> DevSlideshowList = new ArrayList<>();
            for (int i = 0; i < Objectlist.size(); i++) {
                DevSlideshowList.add(BeanUtil.toBean(Objectlist.get(i), DevSlideshow.class));
            }
            //排序
            DevSlideshowList.sort(new Comparator<DevSlideshow>() {
                @Override
                public int compare(DevSlideshow o1, DevSlideshow o2) {
                    if (o1.getSortCode() > o2.getSortCode()) {
                        return -1;
                    } else if ((o1.getSortCode() < o2.getSortCode())) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });
            //添加轮播图信息
            for (int i = 0; i < DevSlideshowList.size(); i++) {
                if (i <= 4) {
                    Banner Banner = new Banner();
                    Banner.setTitle(DevSlideshowList.get(i).getTitle());
                    Banner.setImage(DevSlideshowList.get(i).getImage());
                    BannerList.add(Banner);
                }
            }
        }
        homeDataResult.setBannerList(BannerList);
        //查询现有直播类型
        List<JSONObject> liveTypeList1 = liveApi.getLiveTypeList();
        if (liveTypeList1.size() > 0) {
            for (JSONObject JSonObject : liveTypeList1) {
                LiveType liveType = new LiveType();
                liveType.setId(JSonObject.get("id").toString());
                liveType.setTitle(JSonObject.get("title").toString());
                //根据类型获取直播信息
                List<JSONObject> liveList = liveApi.getLiveBizList(JSonObject.get("id").toString());
                List<LivePreview> livePreviewList1 = new ArrayList<>();
                for (JSONObject JSonObject1 : liveList) {
                    if (livePreviewList1.size() < 3) {
                        LivePreview livePreview = new LivePreview();
                        livePreview.setId(JSonObject1.get("id").toString());
                        livePreview.setTitle(JSonObject1.get("mainTitle").toString());
                        livePreview.setImage(JSonObject1.get("coverUrl").toString());
                        livePreviewList1.add(livePreview);
                    }
                }
                liveType.setList(livePreviewList1);
                liveTypeList.add(liveType);
            }
        }
        homeDataResult.setLiveTypeList(liveTypeList);
        //根据时间获取直播预览信息
        List<JSONObject> liveBizOrderByTimeList = liveApi.getLiveBizOrderByTimeList();
        for (JSONObject JSonObject : liveBizOrderByTimeList) {
            if (livePreviewList.size() < 3) {
                LivePreview livePreview = new LivePreview();
                livePreview.setId(JSonObject.get("id").toString());
                livePreview.setTitle(JSonObject.get("mainTitle").toString());
                livePreview.setImage(JSonObject.get("coverUrl").toString());
                livePreviewList.add(livePreview);
            }
        }
        homeDataResult.setLivePreviewList(livePreviewList);
        JSONObject jsonObject = new JSONObject(homeDataResult);
        return jsonObject;
    }
}
