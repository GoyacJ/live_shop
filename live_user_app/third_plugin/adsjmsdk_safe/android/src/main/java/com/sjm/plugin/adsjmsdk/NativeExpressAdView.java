package com.sjm.plugin.adsjmsdk;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.sjm.plugin.adsjmsdk.R;
import com.sjm.sjmsdk.ad.SjmAdError;
import com.sjm.sjmsdk.ad.SjmNativeExpressAd;
import com.sjm.sjmsdk.ad.SjmNativeExpressAdListener;
import com.sjm.sjmsdk.ad.SjmSize;

import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.platform.PlatformView;

public class NativeExpressAdView implements PlatformView{
    private static final String TAG = "adsjmsdkPlugin";

//    private FrameLayout mLinearLayout;
    private ViewGroup viewGroup;
    private View mView;
    private Activity activity;
    private EventChannel.EventSink mEventSink;

    public NativeExpressAdView(Context context, Activity activity, BinaryMessenger messenger, int id, String adId, int width, int height){

        EventChannel eventChannel = new EventChannel(messenger, "flutter_adsjm_plugin/native_express_event_" + id);
        eventChannel.setStreamHandler(new EventChannel.StreamHandler() {
            @Override
            public void onListen(Object o, EventChannel.EventSink eventSink) {
                mEventSink = eventSink;
            }

            @Override
            public void onCancel(Object o) {
            }
        });
//        mLinearLayout = new FrameLayout(context);
//        ViewGroup.LayoutParams layoutParams = new FrameLayout.LayoutParams()
//        layoutParams.width = width;
//        layoutParams.height = height;
//        mLinearLayout.setLayoutParams(new FrameLayout.LayoutParams(width,height));
         View view = LayoutInflater.from(activity).inflate(R.layout.activity_banner,null,false);
        FrameLayout viewGroup = view.findViewById(R.id.bannerContainer);
        this.mView = view;
        this.activity = activity;
        this.viewGroup = viewGroup;
//        splashAd.fetchAndShowIn(viewGroup);
        loadSplash(adId);
    }
    private void loadSplash(String adId){
        mView.setVisibility(View.VISIBLE);
        SjmNativeExpressAd expressAd = new SjmNativeExpressAd(activity,adId, new SjmNativeExpressAdListener() {
            @Override
            public void onSjmAdLoaded() {
                Map<String, Object> result = new HashMap<>();
                result.put("id", "id");
                result.put("event", "onSjmAdLoaded");
                if(null != mEventSink){
                    mEventSink.success(result);
                }

            }

            @Override
            public void onSjmAdShow() {
                Map<String, Object> result = new HashMap<>();
                result.put("id", "id");
                result.put("event", "onSjmAdShow");
                if(null != mEventSink){
                    mEventSink.success(result);
                }
            }

            @Override
            public void onSjmAdClicked() {
                Map<String, Object> result = new HashMap<>();
                result.put("id", "id");
                result.put("event", "onSjmAdClicked");
                if(null != mEventSink){
                    mEventSink.success(result);
                }
            }

            @Override
            public void onSjmAdClosed() {
                Map<String, Object> result = new HashMap<>();
                result.put("id", "id");
                result.put("event", "onSjmAdClosed");
                if(null != mEventSink){
                    mEventSink.success(result);
                    mEventSink.endOfStream();
                }

                mView.setVisibility(View.GONE);
            }

            @Override
            public void onSjmAdError(SjmAdError SjmAdError) {
                Map<String, Object> result = new HashMap<>();
                result.put("id", "id");
                result.put("event", "onSjmAdError");
                result.put("code", SjmAdError.getErrorCode());
                result.put("message", SjmAdError.getErrorMsg());
                if(null != mEventSink){
                    mEventSink.success(result);
                    mEventSink.endOfStream();
                }

            }
        },this.viewGroup);
        expressAd.setSize(new SjmSize(340,0));
        expressAd.loadAd();

    }
    //显示
//    private void showAd(){
//        splashAd.fetchAndShowIn(viewGroup);
//    }
    //结束或错误
    private void finish(){

    }



    @Override
    public View getView() {
//        if(this.mView != null)
//            return mView;
//        View view = LayoutInflater.from(activity).inflate(R.layout.activity_splash,null,false);
//        FrameLayout viewGroup = view.findViewById(R.id.splash_container);
//        this.mView = view;
//        this.viewGroup = viewGroup;
        Log.e(TAG, "onMethodCall:call.method111=getView");
//        this.mLinearLayout = viewGroup;
        return mView;
    }

    @Override
    public void dispose() {
        Log.e(TAG, "onMethodCall.dispose");
    }
}
