package vip.xiaonuo.pay.web;

import cn.dev33.satoken.annotation.SaIgnore;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.*;
import org.noear.solon.core.handle.Context;
import vip.xiaonuo.pay.entity.WxPayV3Bean;
import vip.xiaonuo.pay.service.H5PayService;
import vip.xiaonuo.pay.service.impl.WxPayServiceImpl;

import java.io.IOException;

@Controller
@SaIgnore
@Slf4j
public class NotifyWeb {

    @Inject
    private WxPayV3Bean wxPayV3Bean;
    @Inject
    private WxPayServiceImpl wxPayService;

    @Inject
    private H5PayService h5PayService;


    @Post
    @Get
    @Mapping("/payNotify/wxNotify")
    public void wxNotify(Context ctx) throws IOException {
        wxPayService.orderNotifyResult(ctx);
    }

    @Post
    @Get
    @Mapping("/payNotify/h5Notify")
    public void h5PayNotify(Context ctx) throws IOException {
        h5PayService.h5PayNotify(ctx);
    }

}
