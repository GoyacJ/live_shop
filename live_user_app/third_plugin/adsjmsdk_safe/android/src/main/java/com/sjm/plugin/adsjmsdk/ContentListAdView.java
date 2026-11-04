package com.sjm.plugin.adsjmsdk;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.fragment.app.FragmentActivity;

import com.kwad.sdk.api.KsContentPage;
import com.sjm.sjmsdk.ad.SjmAdError;
import com.sjm.sjmsdk.ad.SjmContentAdListener;

import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.platform.PlatformView;

public class ContentListAdView implements PlatformView{
    private static final String TAG = "AdSjmsdkPlugin";

//    private FrameLayout mLinearLayout;
    private ViewGroup viewGroup;
    private View mView;
    private Activity activity;
    String adid;
    private EventChannel.EventSink mEventSink;
    public ContentListAdView(Context context, Activity activity, BinaryMessenger messenger, int id, final String adId){
        Log.d("test","ContentListAdView==");
        EventChannel eventChannel = new EventChannel(messenger, "flutter_adSjm_plugin/contentlist_event_" + id);
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
         View view = LayoutInflater.from(activity).inflate(R.layout.activity_content,null,false);
        FrameLayout viewGroup = view.findViewById(R.id.main_frame_layout);
        this.mView = view;
        this.activity = activity;
        this.viewGroup = viewGroup;
//        splashAd.fetchAndShowIn(viewGroup);
        adid = adId;
        final ContentFragment contentFragment =(ContentFragment)((FragmentActivity) activity).getSupportFragmentManager().findFragmentById(R.id.main_frame);
        contentFragment.loadContent(adId, new SjmContentAdListener() {
            @Override
            public void onSjmAdError(SjmAdError sjmAdError) {
                Log.d("test","onSjmAdError="+sjmAdError.getErrorCode());
                Map<String, Object> result = new HashMap<>();
                result.put("event", "onSjmAdError");
                result.put("code", sjmAdError.getErrorCode());
                result.put("message", sjmAdError.getErrorMsg());
                if(null != mEventSink){
                    mEventSink.success(result);
                    mEventSink.endOfStream();
                }

            }

            @Override
            public void onPageEnter(KsContentPage.ContentItem contentItem) {
                Map<String, Object> result = new HashMap<>();
//                result.put("contentItem",contentItem);
                result.put("event", "onPageEnter");
                result.put("id",contentItem.id);
                if(null != mEventSink){
                    mEventSink.success(result);
                }

            }

            @Override
            public void onPageResume(KsContentPage.ContentItem contentItem) {
                Map<String, Object> result = new HashMap<>();
//                result.put("contentItem",contentItem);
                result.put("event", "onPageResume");
                result.put("id",contentItem.id);
                if(null != mEventSink){
                    mEventSink.success(result);
                }
            }

            @Override
            public void onPagePause(KsContentPage.ContentItem contentItem) {
                Map<String, Object> result = new HashMap<>();
//                result.put("contentItem",contentItem);
                result.put("event", "onPagePause");
                result.put("id",contentItem.id);
                if(null != mEventSink){
                    mEventSink.success(result);
                }
            }

            @Override
            public void onPageLeave(KsContentPage.ContentItem contentItem) {
                Map<String, Object> result = new HashMap<>();
//                result.put("contentItem",contentItem);
                result.put("event", "onPageLeave");
                result.put("id",contentItem.id);
                if(null != mEventSink){
                    mEventSink.success(result);
                }
            }

            @Override
            public void onVideoPlayStart(KsContentPage.ContentItem contentItem) {
                Map<String, Object> result = new HashMap<>();
//                result.put("contentItem",contentItem);
                result.put("event", "onVideoPlayStart");
                result.put("id",contentItem.id);
                if(null != mEventSink){
                    mEventSink.success(result);
                }
            }

            @Override
            public void onVideoPlayPaused(KsContentPage.ContentItem contentItem) {
                Map<String, Object> result = new HashMap<>();
//                result.put("contentItem",contentItem);
                result.put("event", "onVideoPlayPaused");
                result.put("id",contentItem.id);
                if(null != mEventSink){
                    mEventSink.success(result);
                }
            }

            @Override
            public void onVideoPlayResume(KsContentPage.ContentItem contentItem) {
                Map<String, Object> result = new HashMap<>();
//                result.put("contentItem",contentItem);
                result.put("event", "onVideoPlayResume");
                result.put("id",contentItem.id);
                if(null != mEventSink){
                    mEventSink.success(result);
                }
            }

            @Override
            public void onVideoPlayCompleted(KsContentPage.ContentItem contentItem) {
                Map<String, Object> result = new HashMap<>();
//                result.put("contentItem",contentItem);
                result.put("event", "onVideoPlayCompleted");
                result.put("id",contentItem.id);
                if(null != mEventSink){
                    mEventSink.success(result);
                }
            }

            @Override
            public void onVideoPlayError(KsContentPage.ContentItem contentItem, int i, int i1) {
                Map<String, Object> result = new HashMap<>();
//                result.put("contentItem",contentItem);
                result.put("event", "onVideoPlayError");
                result.put("id",contentItem.id);
                if(null != mEventSink){
                    mEventSink.success(result);
                }
            }
        });

    }
    //结束或错误
    private void finish(){

    }


    boolean isFrist = false;
    @Override
    public View getView() {
        if(this.mView != null){
            Log.e(TAG, "onMethodCall:call.method111=getView22");
        }

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
