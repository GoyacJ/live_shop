package vip.xiaonuo.live.modular;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.mybatisflex.core.query.QueryWrapper;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.common.cache.CommonCacheOperator;
import vip.xiaonuo.live.api.live.LiveApi;
import vip.xiaonuo.live.api.live.dto.CreateShoppingCarOrderDTO;
import vip.xiaonuo.live.api.live.dto.LiveBizDTO;
import vip.xiaonuo.live.api.live.dto.LiveVisitDurationDTO;
import vip.xiaonuo.live.api.live.dto.ShoppingCarDTO;
import vip.xiaonuo.live.core.config.LiveConfig;
import vip.xiaonuo.live.core.util.LiveUtil;
import vip.xiaonuo.live.modular.anchor.entity.Anchor;
import vip.xiaonuo.live.modular.anchor.service.AnchorService;
import vip.xiaonuo.live.modular.livebiz.entity.LiveBiz;
import vip.xiaonuo.live.modular.livebiz.service.LiveBizService;
import vip.xiaonuo.live.modular.marketingcampaigns.entity.MarketingCampaigns;
import vip.xiaonuo.live.modular.marketingcampaigns.param.MarketingCampaignsAddParam;
import vip.xiaonuo.live.modular.marketingcampaigns.param.MarketingCampaignsEditParam;
import vip.xiaonuo.live.modular.marketingcampaigns.service.MarketingCampaignsService;
import vip.xiaonuo.live.modular.marketingcampaignslog.param.LiveMarketingCampaignsLogAddParam;
import vip.xiaonuo.live.modular.marketingcampaignslog.service.LiveMarketingCampaignsLogService;
import vip.xiaonuo.live.modular.shoppingcar.entity.ShoppingCar;
import vip.xiaonuo.live.modular.shoppingcar.service.ShoppingCarService;
import vip.xiaonuo.live.modular.streamingtype.entity.LiveStreamingType;
import vip.xiaonuo.live.modular.streamingtype.service.LiveStreamingTypeService;
import vip.xiaonuo.task.api.TaskApi;
import vip.xiaonuo.task.api.enums.TaskBusinessCategory;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class LiveApiIml implements LiveApi {
    @Inject
    private LiveConfig liveConfig;
    @Inject
    private LiveBizService liveBizService;
    @Inject
    private ShoppingCarService shoppingCarService;
    @Inject
    private MarketingCampaignsService marketingCampaignsService;
    @Inject
    private LiveMarketingCampaignsLogService liveMarketingCampaignsLogService;
    @Inject
    private LiveStreamingTypeService liveStreamingTypeService;
    @Inject
    private AnchorService anchorService;

    @Inject
    private TaskApi taskApi;

    @Inject
    private CommonCacheOperator commonCacheOperator;

    public static final String LIVE_VIEW_DURATION_PREFIX = "live:view_duration:";

    @Override
    public Map<String, String> getLiveAddress() {
        if (liveConfig == null) {
            throw new IllegalArgumentException("liveConfig 未初始化");
        }
        return LiveUtil.createUrl(liveConfig);
    }

    @Override
    public String getLivePullAddress() {
        if (liveConfig == null) {
            throw new IllegalStateException("liveConfig 未被初始化");
        }
        return LiveUtil.createPullUrl(liveConfig);
    }

    @Tran(readOnly = true)
    @Override
    public JSONObject getLiveConfiguration(String id) {
        return new JSONObject(liveBizService.queryEntity(id));
    }

    @Tran
    @Override
    public int getMarketingActivities(Integer popleCount, double price, String seconds) {
        MarketingCampaignsAddParam addParam = new MarketingCampaignsAddParam();
        addParam.setMarketingId(Math.toIntExact(System.currentTimeMillis() / 1000));
        addParam.setMarketingType(0); //0红包 1优惠券
        addParam.setMarketingState(0);  //0进行中 1已结束
        addParam.setNumberWinners(popleCount);
        addParam.setMarketingValue(String.valueOf(price));
        addParam.setCreateTime(new Timestamp(System.currentTimeMillis()));
        boolean add = marketingCampaignsService.add(addParam);
        return add ? 1 : 0;
    }

    @Tran(readOnly = true)
    @Override
    public JSONObject getMarketing(String marketingId) {
        MarketingCampaigns marketingCampaigns = marketingCampaignsService.getOne(marketingId);
        return new JSONObject(marketingCampaigns);
    }

    @Tran
    @Override
    public void updateMarketingActivities(Integer id, Integer marketingState) {
        MarketingCampaignsEditParam editParam = new MarketingCampaignsEditParam();
        editParam.setMarketingId(id);
        editParam.setMarketingState(marketingState);
        marketingCampaignsService.edit(editParam);
    }

    @Tran
    @Override
    public int saveMarketingCampaignsLog(List<Integer> userId, Integer marketingId, Integer marketingType, double value) {
        for (int i = 0; i < userId.size(); i++) {
            LiveMarketingCampaignsLogAddParam addParam = new LiveMarketingCampaignsLogAddParam();
            addParam.setMarketingId(marketingId);
            addParam.setUserId(userId.get(i));
            addParam.setMarketingType(marketingType);
            addParam.setMarketingValue(Double.toString(value));
            liveMarketingCampaignsLogService.add(addParam);
        }
        return 0;
    }

    @Tran(readOnly = true)
    @Override
    public List<JSONObject> getShoppingCar(String liveId) {
        QueryWrapper queryWrapper = QueryWrapper.create();
        queryWrapper.eq("LIVE_ID", liveId);
        return shoppingCarService.list(queryWrapper).stream()
                .map(item -> JSONUtil.createObj().set("live_id", item.getLiveId()).set("goods_id", item.getGoodsId()).set("quantity", item.getQuantity()).set("price", item.getPrice()))
                .collect(Collectors.toList());


        // return new JSONObject(shoppingCarService.queryEntity(liveId));
    }

    @Override
    public List<ShoppingCarDTO> getShoppingCarByLiveId(String liveId) {
        if (StringUtils.isEmpty(liveId)) {
            return Lists.newArrayList();
        }
        return this.shoppingCarService.list(new QueryWrapper().eq(ShoppingCar::getLiveId, liveId))
                .stream()
                .map(shoppingCar -> BeanUtil.toBean(shoppingCar, ShoppingCarDTO.class))
                .toList();
    }

    @Override
    public LiveBizDTO getLiveByLiveId(String liveId) {
        if (StringUtils.isEmpty(liveId)) {
            return null;
        }
        LiveBiz liveBiz = this.liveBizService.getById(liveId);
        if (Objects.isNull(liveBiz)) {
            return null;
        }
        return BeanUtil.toBean(liveBiz, LiveBizDTO.class);
    }

    @Override
    public JSONObject getShoppingCarOne(String liveId, String goodsId) {
        QueryWrapper queryWrapper = QueryWrapper.create();
        queryWrapper.eq(ShoppingCar::getLiveId, liveId);
        queryWrapper.eq(ShoppingCar::getGoodsId, goodsId);
        ShoppingCar shoppingCar = shoppingCarService.getOne(queryWrapper);
        return new JSONObject(shoppingCar);
    }

    @Tran
    @Override
    public int updateMarketingId(Integer id, Integer marketingState) {
        return marketingCampaignsService.updateMarketingId(String.valueOf(id), marketingState.toString());
    }

    @Override
    public List<JSONObject> getLiveBizList(String type) {
        QueryWrapper queryWrapper = QueryWrapper.create();
        queryWrapper.eq("LIVE_STATE", type);
        List<JSONObject> JSONList = new ArrayList<>();
        List<LiveBiz> liveBizList = liveBizService.list(queryWrapper);
        if (!liveBizList.isEmpty()) {
            for (LiveBiz LiveBiz : liveBizList) {
                JSONList.add(new JSONObject(LiveBiz));
            }
        }
        return JSONList;
    }

    @Override
    public List<JSONObject> getLiveBizOrderByTimeList() {
        List<JSONObject> JSONList = new ArrayList<>();
        // 根据时间排序
        QueryWrapper queryWrapper = QueryWrapper.create();
        queryWrapper.orderBy("CREATE_TIME DESC");
        List<LiveBiz> liveBizList = liveBizService.list(queryWrapper);
        if (!liveBizList.isEmpty()) {
            for (LiveBiz LiveBiz : liveBizList) {
                JSONList.add(new JSONObject(LiveBiz));
            }
        }
        return JSONList;
    }

    @Override
    public List<JSONObject> getLiveTypeList() {
        List<JSONObject> JSONList = new ArrayList<>();
        List<LiveStreamingType> liveStreamingTypeList = liveStreamingTypeService.getLiveTypeList();
        if (!liveStreamingTypeList.isEmpty()) {
            for (LiveStreamingType liveStreamingType : liveStreamingTypeList) {
                JSONList.add(new JSONObject(liveStreamingType));
            }
        }
        return JSONList;
    }

    @Override
    public List<JSONObject> getLiveAnchorList() {
        List<JSONObject> JSONList = new ArrayList<>();
        List<Anchor> liveAnchorList = anchorService.getLiveAnchorList();

        if (!liveAnchorList.isEmpty()) {
            for (Anchor anchor : liveAnchorList) {
                JSONList.add(new JSONObject(anchor));
            }
        }
        return JSONList;
    }

    @Override
    public JSONObject getLiveAnchorDetailById(String id) {
        JSONObject result = new JSONObject();
        Anchor anchor = anchorService.getById(id);
        result.set("id", String.valueOf(anchor.getId()));
        result.set("username", anchor.getUsername());
        result.set("avatar", anchor.getPictureUrl());

        return result;
    }

    @Override
    public List<JSONObject> getLiveBizByIds(Collection<String> ids) {
        List<LiveBiz> liveBizs = liveBizService.listByIds(ids);
        if (ObjectUtil.isEmpty(liveBizs)) {
            return Lists.newArrayList();
        }
        return liveBizs.stream().map(item -> JSONUtil.parseObj(item)).collect(Collectors.toList());
    }

    @Override
    public boolean deductionCarGoodsStock(String shopCarId, Integer stock) {
        return shoppingCarService.deductionCarGoodsStock(shopCarId, stock);
    }

    @Override
    public String createShoppingCarOrder(CreateShoppingCarOrderDTO param) {
        return shoppingCarService.createShoppingCarOrder(param);
    }

    @Override
    public void viewDuration(LiveVisitDurationDTO dto) {
        commonCacheOperator.put(LIVE_VIEW_DURATION_PREFIX + dto.getUserId(), dto, 7776000);
        taskApi.task(dto.getUserId(), dto.getLiveId(), TaskBusinessCategory.LIVE_VISIT);
    }
}
