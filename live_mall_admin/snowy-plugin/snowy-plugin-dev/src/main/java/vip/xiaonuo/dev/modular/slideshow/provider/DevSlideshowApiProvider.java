package vip.xiaonuo.dev.modular.slideshow.provider;

import cn.hutool.json.JSONObject;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.dev.api.DevSlideshowApi;
import vip.xiaonuo.dev.modular.slideshow.service.DevSlideshowService;

import java.util.List;

/**
 * 轮播图API接口实现类
 *
 * @author yubaoshan
 * @date  2024/07/13 00:31
 */
@Component
public class DevSlideshowApiProvider implements DevSlideshowApi {

    @Inject
    private DevSlideshowService devSlideshowService;

    @Override
    public List<JSONObject> getListByPlace(String place) {
        return devSlideshowService.getListByPlace(place);
    }
}