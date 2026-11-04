package vip.xiaonuo.shop.modular.mallgoodsevaluate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.shop.modular.mallgoods.entity.MallGoods;
import vip.xiaonuo.shop.modular.mallgoods.param.MallGoodsIdAndTitleParam;
import vip.xiaonuo.shop.modular.mallgoods.param.MallGoodsIdParam;
import vip.xiaonuo.shop.modular.mallgoods.service.MallGoodsService;
import vip.xiaonuo.shop.modular.mallgoodsevaluate.entity.MallGoodsEvaluate;
import vip.xiaonuo.shop.modular.mallgoodsevaluate.mapper.MallGoodsEvalueMapper;
import vip.xiaonuo.shop.modular.mallgoodsevaluate.param.*;
import vip.xiaonuo.shop.modular.mallgoodsevaluate.result.MallGoodsEvaluateResult;
import vip.xiaonuo.shop.modular.mallgoodsevaluate.service.MallGoodsEvaluateService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class MallGoodsEvaluateServiceImpl extends ServiceImpl<MallGoodsEvalueMapper, MallGoodsEvaluate> implements MallGoodsEvaluateService {

    @Inject
    private MallGoodsService mallGoodsService;

    @Override
    public Page<MallGoodsEvaluateResult> page(MallGoodsEvaluatePageParam mallGoodsEvaluatePageParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(ObjectUtil.isNotEmpty(mallGoodsEvaluatePageParam.getTitle())) {
            MallGoodsIdAndTitleParam mallGoodsIdAndTitleParam = new MallGoodsIdAndTitleParam();
            mallGoodsIdAndTitleParam.setTitle(mallGoodsEvaluatePageParam.getTitle());
            List<MallGoods> mallGoodsList = mallGoodsService.mallGoodsList(mallGoodsIdAndTitleParam);
            List<String> goodsIdList = new ArrayList<>();
            if (mallGoodsList.size() == 0) {
                throw new CommonException("商城-商品信息不存在，商品名称为：{}", mallGoodsEvaluatePageParam.getTitle());
            }
            for (MallGoods mallGoods : mallGoodsList) {
                goodsIdList.add(mallGoods.getId());
            }
            queryWrapper.in(MallGoodsEvaluate::getGoodsId,goodsIdList);
        }
        if(ObjectUtil.isNotEmpty(mallGoodsEvaluatePageParam.getStartCreateTime()) && ObjectUtil.isNotEmpty(mallGoodsEvaluatePageParam.getEndCreateTime())) {
            queryWrapper.between(MallGoodsEvaluate::getCreateTime, mallGoodsEvaluatePageParam.getStartCreateTime(), mallGoodsEvaluatePageParam.getEndCreateTime());
        }
        queryWrapper.orderBy(MallGoodsEvaluate::getCreateTime,false);
        //拼接商品信息
        Page<MallGoodsEvaluate>  pageResult = this.page(CommonPageRequest.defaultPage(), queryWrapper);
        List<MallGoodsEvaluate>  list = pageResult.getRecords();
        List<MallGoodsEvaluateResult>  listResult = new ArrayList<>();
        if (list.size() > 0) {
            for (MallGoodsEvaluate  mallGoodsEvaluate : list) {
                MallGoodsEvaluateResult  result = new MallGoodsEvaluateResult();
                result = BeanUtil.toBean(mallGoodsEvaluate, MallGoodsEvaluateResult.class);
                //查询mall_goods表中的数据进行拼接
                MallGoods mallGoods  = mallGoodsService.getById(mallGoodsEvaluate.getGoodsId());
                result.setTitle(mallGoods.getTitle());
                result.setMerchant(mallGoods.getMerchant());
                listResult.add(result);
            }
        }
        /*Page<MallGoodsEvaluateResult> pageListResult = new Page<>();
        pageListResult.setRecords(listResult);
        return pageListResult;*/
        // 封装最终的分页结果
        Page<MallGoodsEvaluateResult> finalPage = new Page<>(pageResult.getCurrent(), pageResult.getSize(), pageResult.getTotal());
        finalPage.setRecords(listResult);
        return finalPage;
    }

    @Override
    public List<MallGoodsEvaluate> mallGoodsEvaluateList(MallGoodsIdForEvaluateParam mallGoodsIdForEvaluateParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(ObjectUtil.isEmpty(mallGoodsIdForEvaluateParam.getGoodsId())) {
            queryWrapper.eq("goods_id", mallGoodsIdForEvaluateParam.getGoodsId());
        }
        List<MallGoodsEvaluate> list = this.list(queryWrapper);
        return list;
    }

    @Override
    public MallGoodsEvaluate mallGoodsEvaluateOneDetail(MallGoodsIdForEvaluateParam mallGoodsIdForEvaluateParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(ObjectUtil.isEmpty(mallGoodsIdForEvaluateParam.getGoodsId())) {
            queryWrapper.eq("goods_id", mallGoodsIdForEvaluateParam.getGoodsId());
        }
        List<MallGoodsEvaluate> list = this.list(queryWrapper);
        if (list.size() > 0) {
            list.sort(new Comparator<MallGoodsEvaluate>() {
                @Override
                public int compare(MallGoodsEvaluate o1, MallGoodsEvaluate o2) {
                    if (o1.getCreateTime().getTime() > o2.getCreateTime().getTime()) {
                        return -1;
                    }else if (o1.getCreateTime().getTime() < o2.getCreateTime().getTime()) {
                        return 1;
                    }else{
                        return 0;
                    }
                }
            });
            return list.get(0);
        }else {
            return  new MallGoodsEvaluate();
        }
    }

    @Override
    public void mallGoodsEvaluateAdd(MallGoodsEvaluateAddParam mallGoodsEvaluateAddParam) {
        if(ObjectUtil.isEmpty(mallGoodsEvaluateAddParam)) {
            throw new CommonException("商品评价信息不能为空");
        }
        try {
            MallGoodsEvaluate mallGoodsEvaluate = BeanUtil.toBean(mallGoodsEvaluateAddParam, MallGoodsEvaluate.class);
            this.save(mallGoodsEvaluate);
        } catch (Exception e) {
            throw new CommonException("商品评价信息添加失败");
        }
    }

    @Override
    public void mallGoodsEvaluateEdit(MallGoodsEvaluateEditParam mallGoodsEvaluateEditParam) {
        MallGoodsEvaluate mallGoodsEvaluate = this.getById(mallGoodsEvaluateEditParam.getId());
        if(ObjectUtil.isEmpty(mallGoodsEvaluate)) {
            throw new CommonException("商品评价信息不存在，id值为：{}", mallGoodsEvaluateEditParam.getId());
        }
        try {
            BeanUtil.copyProperties(mallGoodsEvaluateEditParam, mallGoodsEvaluate);
            this.updateById(mallGoodsEvaluate);
        } catch (Exception e) {
            throw new CommonException("商品评价信息修改失败");
        }
    }

    @Override
    public void delete(List<MallGoodsEvaluateIdParam> mallGoodsEvaluateIdParamList) {
        if(mallGoodsEvaluateIdParamList.size() == 0) {
            throw new CommonException("商品评价信息不能为空");
        }
        try {
            // 执行删除
            this.removeByIds(CollStreamUtil.toList(mallGoodsEvaluateIdParamList, MallGoodsEvaluateIdParam::getId));
        } catch (Exception e) {
            throw new CommonException("商品评价信息删除失败");
        }
    }

    @Override
    public void enablestatus(MallGoodsEvaluateIdParam mallGoodsEvaluateIdParam) {
        this.updateChain().eq(MallGoodsEvaluate::getId, mallGoodsEvaluateIdParam.getId())
                .set(MallGoodsEvaluate::getStatus, "1").update();
    }

    @Override
    public void disablestatus(MallGoodsEvaluateIdParam mallGoodsEvaluateIdParam) {
        this.updateChain().eq(MallGoodsEvaluate::getId, mallGoodsEvaluateIdParam.getId())
                .set(MallGoodsEvaluate::getStatus, "2").update();
    }
}
