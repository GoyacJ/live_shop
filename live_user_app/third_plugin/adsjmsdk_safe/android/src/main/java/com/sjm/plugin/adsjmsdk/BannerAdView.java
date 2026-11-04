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
import com.sjm.sjmsdk.ad.SjmBannerAd;
import com.sjm.sjmsdk.ad.SjmBannerAdListener;

import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.platform.PlatformView;

public class BannerAdView implements PlatformView{
    private static final String TAG = "adsjmsdkPlugin";

//    private FrameLayout mLinearLayout;
    private ViewGroup viewGroup;
    private View mView;
    SjmBannerAd bannerAd;
    private Activity activity;
    private EventChannel.EventSink mEventSink;
    public BannerAdView(Context context, Activity activity, BinaryMessenger messenger, int id,String adId, int width, int height){

        EventChannel eventChannel = new EventChannel(messenger, "flutter_adsjm_plugin/banner_event_" + id);
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
    private void loadSplash(String adid){
        bannerAd = new SjmBannerAd(activity, adid, new SjmBannerAdListener() {
            @Override
            public void onSjmAdLoaded() {
                Map<String, Object> result = new HashMap<>();

                result.put("event", "onSjmAdLoaded");
                result.put("id", "id");
                if(null != mEventSink){
                    mEventSink.success(result);
                }
            }

            @Override
            public void onSjmAdShow() {
                Map<String, Object> result = new HashMap<>();

                result.put("event", "onSjmAdShow");
                result.put("id", "id");
                if(null != mEventSink){
                    mEventSink.success(result);
                }
            }

            @Override
            public void onSjmAdClicked() {
                Map<String, Object> result = new HashMap<>();

                result.put("event", "onSjmAdClicked");
                result.put("id", "id");
                if(null != mEventSink){
                    mEventSink.success(result);
                }
            }

            @Override
            public void onSjmAdClosed() {
                Map<String, Object> result = new HashMap<>();
                result.put("event", "onSjmAdClosed");
                result.put("id", "id");
                if(null != mEventSink){
                    mEventSink.success(result);
                    mEventSink.endOfStream();
                }

            }

            @Override
            public void onSjmAdError(SjmAdError SjmAdError) {
                Map<String, Object> result = new HashMap<>();
                result.put("event", "onSjmAdError");
                result.put("code", SjmAdError.getErrorCode());
                result.put("message", SjmAdError.getErrorMsg());
                result.put("id", "id");
                if(null != mEventSink){
                    mEventSink.success(result);
                    mEventSink.endOfStream();
                }

            }
        });
        bannerAd.setBannerContainer(viewGroup);
        bannerAd.setRefresh(0);
        bannerAd.loadAD();
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
