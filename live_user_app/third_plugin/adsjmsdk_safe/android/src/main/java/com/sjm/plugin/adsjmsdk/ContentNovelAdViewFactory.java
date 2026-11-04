package com.sjm.plugin.adsjmsdk;

import android.app.Activity;
import android.content.Context;

import java.util.Map;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;

public class ContentNovelAdViewFactory extends PlatformViewFactory {

    private final BinaryMessenger messenger;
    public Activity mActivity;
    ContentNovelAdView novelAdView;
    public ContentNovelAdViewFactory(BinaryMessenger messenger, Activity activity) {
        super(StandardMessageCodec.INSTANCE);
        this.messenger = messenger;
        this.mActivity = activity;
    }

    @SuppressWarnings("unchecked")
    @Override
    public PlatformView create(Context context, int viewId, Object args) {
        Map<String, Object> map = (Map<String, Object>) args;
        String adId = (String) map.get("adId");
        if(null == novelAdView){
            novelAdView = new ContentNovelAdView(context,mActivity, messenger, viewId,  adId);
        }
        return novelAdView;
    }
}
