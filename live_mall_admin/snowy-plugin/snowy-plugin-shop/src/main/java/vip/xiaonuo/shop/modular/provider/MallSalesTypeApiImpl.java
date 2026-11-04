package vip.xiaonuo.shop.modular.provider;

import api.sales.MallSalesTypeApi;
import com.mybatisflex.core.query.QueryWrapper;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.shop.modular.mallsalestype.entity.MallSalesType;
import vip.xiaonuo.shop.modular.mallsalestype.mapper.MallSalesTypeMapper;
import vip.xiaonuo.shop.modular.mallsalestype.param.MallSalesTypeAddParam;
import vip.xiaonuo.shop.modular.mallsalestype.service.MallSalesTypeService;

import java.text.SimpleDateFormat;

@Component
public class MallSalesTypeApiImpl implements MallSalesTypeApi {

    @Inject
    private MallSalesTypeService mallSalesTypeService;
    @Inject
    private MallSalesTypeMapper mallSalesTypeMapper;

    @Override
    public int addMallSalesType(String orderId, String salesLabel, String salesValue) {
        MallSalesTypeAddParam addParam = new MallSalesTypeAddParam();
        addParam.setDictLabel(salesLabel);
        addParam.setDictValue(salesValue);
        addParam.setOrderId(orderId);
        //售后时间
        addParam.setExtJson(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
        mallSalesTypeService.add(addParam);
        return 1;
    }

    @Override
    public Boolean updateMallSalesType(String orderId, String salesLabel, String salesValue) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("ext_json",orderId);
        MallSalesType mallSalesType = new MallSalesType();
//        mallSalesType.setDictLabel(salesLabel);
//        mallSalesType.setDictValue(salesValue);
        return  mallSalesTypeService.update(mallSalesType, queryWrapper);
    }
}
