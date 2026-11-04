package com.sjm.plugin.adsjmsdk;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.sjm.sjmsdk.ad.SjmAdError;
import com.sjm.sjmsdk.ad.SjmNativeExpressAd;
import com.sjm.sjmsdk.ad.SjmNativeExpressAdListener;
import com.sjm.sjmsdk.ad.SjmNovelContentAd;
import com.sjm.sjmsdk.ad.SjmNovelContentAdListener;
import com.sjm.sjmsdk.ad.SjmSize;

import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.platform.PlatformView;

public class ContentNovelAdView implements PlatformView{
    private static final String TAG = "adsjmsdkPlugin";

//    private FrameLayout mLinearLayout;
    private ViewGroup viewGroup;
    private View mView;
    private Activity activity;
    private EventChannel.EventSink mEventSink;

    public ContentNovelAdView(Context context, Activity activity, BinaryMessenger messenger, int id, String adId){
        Log.d("test","SjmNovelContentAd.load");
        EventChannel eventChannel = new EventChannel(messenger, "flutter_adsjm_plugin/content_novel_event_" + id);
        eventChannel.setStreamHandler(new EventChannel.StreamHandler() {
            @Override
            public void onListen(Object o, EventChannel.EventSink eventSink) {
                mEventSink = eventSink;
            }

            @Override
            public void onCancel(Object o) {
            }
        });
        Log.d("test","SjmNovelContentAd.load");
//        mLinearLayout = new FrameLayout(context);
//        ViewGroup.LayoutParams layoutParams = new FrameLayout.LayoutParams()
//        layoutParams.width = width;
//        layoutParams.height = height;
//        mLinearLayout.setLayoutParams(new FrameLayout.LayoutParams(width,height));
         View view = LayoutInflater.from(activity).inflate(R.layout.activity_novel,null,false);
        FrameLayout viewGroup = view.findViewById(R.id.novel_content);
        this.mView = view;
        this.activity = activity;
        this.viewGroup = viewGroup;
//        splashAd.fetchAndShowIn(viewGroup);
        loadSplash(adId);
    }
    private void loadSplash(String adId){
        Log.d("test","SjmNovelContentAd.load");
        SjmNovelContentAd sjmNovelContentAd = new SjmNovelContentAd(activity, new SjmNovelContentAdListener() {
            @Override
            public void onSjmAdError(SjmAdError sjmAdError) {
                Map<String, Object> result = new HashMap<>();
                result.put("event", "onSjmAdError");
                result.put("code", sjmAdError.getErrorCode());
                result.put("message", sjmAdError.getErrorMsg());
                mEventSink.success(result);
                mEventSink.endOfStream();
            }

            @Override
            public void onSjmAdClicked() {
                Map<String, Object> result = new HashMap<>();
                result.put("event", "onSjmAdClicked");
                mEventSink.success(result);
            }

            @Override
            public void onSjmAdShow() {
                Map<String, Object> result = new HashMap<>();
                result.put("event", "onSjmAdShow");
                mEventSink.success(result);
            }

            @Override
            public void onSjmNovelReadTime(long l) {

            }
        }, adId);
       sjmNovelContentAd.showAd(this.viewGroup);

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
