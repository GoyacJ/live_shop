package com.sjm.plugin.adsjmsdk;

import android.app.Activity;
import android.content.Context;

import java.util.Map;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;
import io.flutter.plugin.common.StandardMessageCodec;

public class ADViewFactory extends PlatformViewFactory {

    private final BinaryMessenger messenger;
    public Activity mActivity;

    public ADViewFactory(BinaryMessenger messenger,Activity activity) {
        super(StandardMessageCodec.INSTANCE);
        this.messenger = messenger;
        this.mActivity = activity;
    }

    @SuppressWarnings("unchecked")
    @Override
    public PlatformView create(Context context, int viewId, Object args) {
        Map<String, Object> params = (Map<String, Object>) args;
        return new AdView(context,mActivity, messenger, viewId, params);
    }
}
