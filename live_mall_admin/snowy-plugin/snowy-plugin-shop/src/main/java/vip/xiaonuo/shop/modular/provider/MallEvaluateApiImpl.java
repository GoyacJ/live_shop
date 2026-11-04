package vip.xiaonuo.shop.modular.provider;

import api.evaluate.MallEvaluateApi;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.mybatisflex.core.query.QueryWrapper;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.common.util.RandomCountUtil;
import vip.xiaonuo.shop.modular.mallgoodsevaluate.entity.MallGoodsEvaluate;
import vip.xiaonuo.shop.modular.mallgoodsevaluate.mapper.MallGoodsEvalueMapper;
import vip.xiaonuo.shop.modular.mallgoodsevaluate.param.MallGoodsEvaluateAddParam;
import vip.xiaonuo.shop.modular.mallgoodsevaluate.service.MallGoodsEvaluateService;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MallEvaluateApiImpl implements MallEvaluateApi {

    @Inject
    private MallGoodsEvaluateService mallGoodsEvaluateService;
    @Inject
    private MallGoodsEvalueMapper MallGoodsEvaluateMappper;

    @Override
    public int addEvaluate(String userId, String productId, String evaluationContent) {
        MallGoodsEvaluateAddParam addParam = new MallGoodsEvaluateAddParam();
        addParam.setCreateUser(userId);
        addParam.setGoodsId(productId);
        addParam.setEvaluateId(RandomCountUtil.getRandomCount());
        addParam.setEvaluateInformation(evaluationContent);
        addParam.setEvaluateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
        addParam.setStatus(String.valueOf(1));//默认显示
        mallGoodsEvaluateService.mallGoodsEvaluateAdd(addParam);
        return 0;
    }

    @Override
    public List<JSONObject> getSingleProductEvaluateList(String productId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("goods_id",productId);
        return mallGoodsEvaluateService.list(queryWrapper).stream().map(JSONUtil::parseObj).collect(Collectors.toList());
    }

    @Override
    public int updateEvaluate(String evaluationId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("evaluate_id",evaluationId);
        MallGoodsEvaluate mallGoodsEvaluate = new MallGoodsEvaluate();
        return MallGoodsEvaluateMappper.updateByQuery(mallGoodsEvaluate,queryWrapper.eq("status",2));
    }
}
