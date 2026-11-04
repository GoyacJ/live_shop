package api.evaluate;

import cn.hutool.json.JSONObject;

import java.util.List;

public interface MallEvaluateApi {

    /**
     * 添加评论
     * @param userId
     * @param evaluationContent
     * @return
     */
    int addEvaluate(String userId,String productId,String evaluationContent);

    /**
     * 获取商品的评价
     * @param productId
     * @return
     */
    List<JSONObject> getSingleProductEvaluateList(String productId);

    /**
     * 隐藏或显示评论  默认是显示
     * @param evaluationId
     * @return
     */
    int updateEvaluate(String evaluationId);

}
