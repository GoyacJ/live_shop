package com.sjm.plugin.adsjmsdk;

import android.app.Activity;
import android.content.Context;

import java.util.Map;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;

public class BannerAdViewFactory extends PlatformViewFactory {

    private final BinaryMessenger messenger;
    public Activity mActivity;

    public BannerAdViewFactory(BinaryMessenger messenger, Activity activity) {
        super(StandardMessageCodec.INSTANCE);
        this.messenger = messenger;
        this.mActivity = activity;
    }

    @SuppressWarnings("unchecked")
    @Override
    public PlatformView create(Context context, int viewId, Object args) {
        Map<String, Object> map = (Map<String, Object>) args;
        String adId = (String) map.get("adId");
        float width = ((Double) map.get("width")).floatValue();
        float height = ((Double) map.get("height")).floatValue();
        return new BannerAdView(context,mActivity, messenger, viewId,  adId, (int)width, (int)height);
    }
}
