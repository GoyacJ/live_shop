package com.sjm.plugin.adsjmsdk;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;


import com.sjm.plugin.adsjmsdk.R;
import com.sjm.sjmsdk.ad.SjmAdError;
import com.sjm.sjmsdk.ad.SjmBannerAd;
import com.sjm.sjmsdk.ad.SjmBannerAdListener;
//import com.sjm.sjmsdk.ad.SjmContentAd;
//import com.sjm.sjmsdk.ad.SjmContentAdListener;

import java.util.Map;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.platform.PlatformView;

public class AdView implements PlatformView, MethodChannel.MethodCallHandler {
    private static final String TAG = "adsjmsdkPlugin";
    private final MethodChannel channel;
    private FrameLayout mLinearLayout;
    private ViewGroup viewGroup;
    private View mView;
    SjmBannerAd bannerAd;
    private Activity activity;
    public  AdView(Context context, Activity activity, BinaryMessenger messenger, int id, Map<String, Object> params){
        channel = new MethodChannel(messenger, "flutter_adsjm_plugin/ADView_" + id);
        channel.setMethodCallHandler(this);

//        FrameLayout mLinearLayout = new FrameLayout(context);
//        mLinearLayout.setBackgroundColor(Color.rgb(100, 200, 100));
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(900, 1900);
//        mLinearLayout.setLayoutParams(lp);
//        this.mLinearLayout = mLinearLayout;


        this.activity = activity;
//        splashAd.fetchAndShowIn(viewGroup);
    }
    private void loadSplash(String adid){
        bannerAd = new SjmBannerAd(activity, adid, new SjmBannerAdListener() {
            @Override
            public void onSjmAdLoaded() {

            }

            @Override
            public void onSjmAdShow() {

            }

            @Override
            public void onSjmAdClicked() {

            }

            @Override
            public void onSjmAdClosed() {

            }

            @Override
            public void onSjmAdError(SjmAdError SjmAdError) {

            }
        });
        bannerAd.setBannerContainer(viewGroup);
        bannerAd.setRefresh(60);
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
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Log.e(TAG, "onMethodCall:call.method="+call.method);
        switch (call.method) {
            case "load":
                ///这里处理加载消息
                String id = call.argument("id");
                Log.e(TAG, "onMethodCall: 广告ID"+id);
                loadSplash(id);
//                showAd();
                result.success(null);
                break;
            case "show":
                ///这里处理显示消息
                result.success(null);
                break;
            case "play":
                ///这里处理播放消息
                result.success(null);
                break;
            case "pause":
                ///这里处理暂停消息
                result.success(null);
                break;
            case "getPlatformVersion":
                result.success("Android " + android.os.Build.VERSION.RELEASE);
                break;
            default:
                result.notImplemented();
                break;
        }
    }


    @Override
    public View getView() {
        if(this.mView != null)
            return mView;
        View view = LayoutInflater.from(activity).inflate(R.layout.activity_splash,null,false);
        FrameLayout viewGroup = view.findViewById(R.id.splash_container);
        this.mView = view;
        this.viewGroup = viewGroup;
        Log.e(TAG, "onMethodCall:call.method111=getView");
//        this.mLinearLayout = viewGroup;
        return view;
    }

    @Override
    public void dispose() {
        Log.e(TAG, "onMethodCall.dispose");
    }
}
