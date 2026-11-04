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
import com.sjm.sjmsdk.ad.SjmSplashAd;
import com.sjm.sjmsdk.ad.SjmSplashAdListener;

import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.platform.PlatformView;

public class SplashAdView implements PlatformView{
    private static final String TAG = "adsjmsdkPlugin";

    private FrameLayout mLinearLayout;
    private ViewGroup viewGroup;
    private View mView;
    SjmSplashAd SjmSplashAd;
    private Activity activity;
    private EventChannel.EventSink mEventSink;

    public SplashAdView(Context context, Activity activity, BinaryMessenger messenger, int id, final String adId, int width, int height){

        EventChannel eventChannel = new EventChannel(messenger, "flutter_adsjm_plugin/splash_event_" + id);
        eventChannel.setStreamHandler(new EventChannel.StreamHandler() {
            @Override
            public void onListen(Object o, EventChannel.EventSink eventSink) {
                Log.d("test","Sjm.onlistener");
                mEventSink = eventSink;
            }

            @Override
            public void onCancel(Object o) {
                Log.d("test","Sjm.onlistener,onCancel");

            }
        });
//        FrameLayout mLinearLayout = new FrameLayout(context);
//        mLinearLayout.setBackgroundColor(Color.rgb(100, 200, 100));
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(900, 1900);
//        mLinearLayout.setLayoutParams(lp);
//        this.mLinearLayout = mLinearLayout;


        this.activity = activity;
        View view = LayoutInflater.from(activity).inflate(R.layout.activity_splash,null,false);
        FrameLayout viewGroup = view.findViewById(R.id.splash_container);
        this.mView = view;
        this.activity = activity;
        this.viewGroup = viewGroup;
        this.viewGroup.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadSplash(adId);
            }
        },1000);

    }
    private void loadSplash(String adId ){
        SjmSplashAd = new SjmSplashAd(activity,new SjmSplashAdListener(){

            @Override
            public void onSjmAdLoaded() {
                Map<String, Object> result = new HashMap<>();
                result.put("id", "");
                result.put("ecpm",SjmSplashAd.getECPM());
                result.put("event", "onSjmAdLoaded");
                if(null!=mEventSink){
                    mEventSink.success(result);
                }

            }

            @Override
            public void onSjmAdLoadTimeOut() {
                Map<String, Object> result = new HashMap<>();
                result.put("event", "onSjmAdError");
                result.put("code", 99998);
                result.put("id", "");
                result.put("message","onSjmAdLoadTimeOut");
                if(null!=mEventSink){
                    mEventSink.success(result);
                    mEventSink.endOfStream();
                }

            }

            @Override
            public void onSjmAdShow() {
                Map<String, Object> result = new HashMap<>();
                result.put("id", "");
                result.put("event", "onSjmAdShow");
                if(null!=mEventSink){
                    mEventSink.success(result);
                }

            }

            @Override
            public void onSjmAdClicked() {
                Map<String, Object> result = new HashMap<>();
                result.put("id", "");
                result.put("event", "onSjmAdClicked");
                if(null!=mEventSink){
                    mEventSink.success(result);
                }

            }

            @Override
            public void onSjmAdTickOver() {
                Map<String, Object> result = new HashMap<>();
                Log.d("test","onSjmAdTickOver");
                result.put("id", "");
                result.put("event", "onSjmAdClosed");
                if(null!=mEventSink){
                    mEventSink.success(result);
                    mEventSink.endOfStream();
                }

            }


            @Override
            public void onSjmAdDismissed() {
                Map<String, Object> result = new HashMap<>();
                Log.d("test","onSjmAdDismissed");
                result.put("event", "onSjmAdClosed");
                result.put("id", "");
                if(null!=mEventSink){
                    mEventSink.success(result);
                    mEventSink.endOfStream();
                }

            }

            @Override
            public void onSjmAdError(final SjmAdError SjmAdError) {
                Log.d("test","onSjmAdError");
                viewGroup.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, Object> result = new HashMap<>();
                        result.put("event", "onSjmAdError");
                        result.put("id", "");
                        result.put("code", SjmAdError.getErrorCode());
                        result.put("message", SjmAdError.getErrorMsg());
                        if(null!=mEventSink){
                            mEventSink.success(result);
                            mEventSink.endOfStream();
                        }

                    }
                },1000);

            }
        }, adId,3);
        SjmSplashAd.checkAndRequestPermission();
        SjmSplashAd.fetchAndShowIn(viewGroup);
    }

    //结束或错误
    private void finish(){

    }


    @Override
    public View getView() {
        if(this.mView != null)
            return mView;

        Log.e(TAG, "onMethodCall:call.method111=getView");
//        this.mLinearLayout = viewGroup;
        return mView;
    }

    @Override
    public void dispose() {
        Log.e(TAG, "onMethodCall.dispose");
    }
}
