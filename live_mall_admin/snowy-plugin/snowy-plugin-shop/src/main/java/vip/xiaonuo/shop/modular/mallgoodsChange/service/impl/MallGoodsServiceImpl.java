package vip.xiaonuo.shop.modular.mallgoodsChange.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import org.noear.solon.annotation.Component;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.shop.modular.mallgoodsChange.entity.MallGoodsChange;
import vip.xiaonuo.shop.modular.mallgoodsChange.mapper.MallGoodsChangeMapper;
import vip.xiaonuo.shop.modular.mallgoodsChange.param.MallGoodsChangeAddParam;
import vip.xiaonuo.shop.modular.mallgoodsChange.param.MallGoodsIdParam;
import vip.xiaonuo.shop.modular.mallgoodsChange.service.MallGoodsChangeService;

import java.util.Comparator;
import java.util.List;

@Component
public class MallGoodsServiceImpl extends ServiceImpl<MallGoodsChangeMapper, MallGoodsChange> implements MallGoodsChangeService {
    @Override
    public List<MallGoodsChange> mallGoodsChangeList(MallGoodsIdParam mallGoodsIdParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(ObjectUtil.isEmpty(mallGoodsIdParam.getGoodsId())) {
            queryWrapper.eq("goods_id", mallGoodsIdParam.getGoodsId());
        }
        List<MallGoodsChange> list = this.list(queryWrapper);
        return list;
    }

    @Override
    public MallGoodsChange mallGoodschangeOneDetail(MallGoodsIdParam mallGoodsIdParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(ObjectUtil.isEmpty(mallGoodsIdParam.getGoodsId())) {
            queryWrapper.eq("goods_id", mallGoodsIdParam.getGoodsId());
        }
        List<MallGoodsChange> list = this.list(queryWrapper);
        if (list.size() > 0) {
            list.sort(new Comparator<MallGoodsChange>() {
                @Override
                public int compare(MallGoodsChange o1, MallGoodsChange o2) {
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
        }else{
            return new MallGoodsChange();
        }
    }

    @Override
    public void mallGoodschangeAdd(MallGoodsChangeAddParam mallGoodsChangeAddParam) {
        if(ObjectUtil.isEmpty(mallGoodsChangeAddParam)) {
            throw new CommonException("商品变更信息不能为空");
        }
        try {
            MallGoodsChange mallGoodsChange = BeanUtil.toBean(mallGoodsChangeAddParam, MallGoodsChange.class);
            this.save(mallGoodsChange);
        }catch (Exception e) {
            throw new CommonException("商品变更信息添加失败");
        }

    }
}
