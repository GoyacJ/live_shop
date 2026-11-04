package com.sjm.plugin.adsjmsdk;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.sjm.sjmsdk.SjmH5ContentListener;
import com.sjm.sjmsdk.SjmSdk;
import com.sjm.sjmsdk.SjmUser;
import com.sjm.sjmsdk.ad.SjmAdError;
import com.sjm.sjmsdk.ad.SjmFullScreenVideoAd;
import com.sjm.sjmsdk.ad.SjmFullScreenVideoAdListener;
import com.sjm.sjmsdk.ad.SjmH5Ad;
import com.sjm.sjmsdk.ad.SjmInterstitialAd;
import com.sjm.sjmsdk.ad.SjmInterstitialAdListener;
//import com.sjm.sjmsdk.ad.SjmNewsAd;
//import com.sjm.sjmsdk.ad.SjmNewsListener;
import com.sjm.sjmsdk.ad.SjmRewardVideoAd;
import com.sjm.sjmsdk.ad.SjmRewardVideoAdListener;
import com.sjm.sjmsdk.ad.SjmVoliceAd;
import com.sjm.sjmsdk.ad.SjmVoliceAdListener;
import com.sjm.sjmsdk.core.DeviceId.SjmDeviceId;
import com.sjm.sjmsdk.utils.SjmCustomController;

import java.util.HashMap;
import java.util.Map;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.flutter.plugin.platform.PlatformViewRegistry;

/** adsjmsdkPlugin */
public class AdSjmsdkPlugin implements FlutterPlugin, ActivityAware, MethodCallHandler {
    private static final String TAG = "adsjmsdkPlugin";
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private MethodChannel channel;
    //  private AdSjmEventChannel adEventChannel;
    private final static AdSjmsdkPlugin plugin = new AdSjmsdkPlugin();
    private Context applicationContext;
    private Activity activity;
    private FlutterPluginBinding pluginBinding;

    private BinaryMessenger mBinaryMessenger;
    public static void registerWith(Registrar registrar) {
        if (registrar.activity() == null) {
            // If a background Flutter view tries to register the plugin, there will be no activity from the registrar.
            // We stop the registering process immediately because the flutter_xy_plugin requires an activity.
            return;
        }
        plugin.initializePlugin(registrar.context(), registrar.activity(), registrar.messenger(), registrar.platformViewRegistry());
    }
    EventChannel.EventSink eventSink_reward,eventSink_interstital,eventSink_fullvideo,eventSink_h5content,eventSink_volice,eventSink_news,eventSink_splash;
    private void initializePlugin(
            Context applicationContext, Activity activity, BinaryMessenger messenger, PlatformViewRegistry registry) {
        this.activity = activity;
        Log.d("test","onListen===onAttachedToEngine222");
        this.applicationContext = applicationContext;

        mBinaryMessenger = messenger;
        channel = new MethodChannel(mBinaryMessenger, "flutter_adsjm_plugin/method");
        channel.setMethodCallHandler(this);
        registry.registerViewFactory("flutter_adsjm_plugin/splash", new SplashAdViewFactory(mBinaryMessenger,activity));
        registry.registerViewFactory("flutter_adsjm_plugin/ADView", new ADViewFactory(mBinaryMessenger,activity));
        registry.registerViewFactory("flutter_adsjm_plugin/banner", new BannerAdViewFactory(mBinaryMessenger,activity));
        registry.registerViewFactory("flutter_adsjm_plugin/native_express", new NativeExpressAdViewFactory(mBinaryMessenger,activity));
        registry.registerViewFactory("flutter_adsjm_plugin/contentlist", new ConentListAdViewFactory(mBinaryMessenger,activity));
        registry.registerViewFactory("flutter_adsjm_plugin/content_novel", new ContentNovelAdViewFactory(mBinaryMessenger,activity));
        setChannel();
    }
    private void setChannel(){
        EventChannel eventChannel = new EventChannel(mBinaryMessenger, "flutter_adsjm_plugin/event_rewardVideo");
        eventChannel.setStreamHandler(new EventChannel.StreamHandler() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onListen(Object o, EventChannel.EventSink es) {
                eventSink_reward = es;
            }

            @Override
            public void onCancel(Object o) {
            }
        });

        EventChannel eventChannel1 = new EventChannel(mBinaryMessenger, "flutter_adsjm_plugin/event_interstital");
        eventChannel1.setStreamHandler(new EventChannel.StreamHandler() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onListen(Object o, EventChannel.EventSink es) {
                eventSink_interstital = es;
            }

            @Override
            public void onCancel(Object o) {
            }
        });
        EventChannel eventChannel3 = new EventChannel(mBinaryMessenger, "flutter_adsjm_plugin/event_fullVideo");
        eventChannel3.setStreamHandler(new EventChannel.StreamHandler() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onListen(Object o, EventChannel.EventSink es) {
                eventSink_fullvideo = es;
            }

            @Override
            public void onCancel(Object o) {
            }
        });
        EventChannel eventChannel4 = new EventChannel(mBinaryMessenger, "flutter_adsjm_plugin/event_h5content");
        eventChannel4.setStreamHandler(new EventChannel.StreamHandler() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onListen(Object o, EventChannel.EventSink es) {
                eventSink_h5content = es;
            }

            @Override
            public void onCancel(Object o) {
            }
        });
        EventChannel eventChannel5 = new EventChannel(mBinaryMessenger, "flutter_adsjm_plugin/event_volice");
        eventChannel5.setStreamHandler(new EventChannel.StreamHandler() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onListen(Object o, EventChannel.EventSink es) {
                eventSink_volice = es;
            }

            @Override
            public void onCancel(Object o) {
            }
        });
        EventChannel eventChannel6 = new EventChannel(mBinaryMessenger, "flutter_adsjm_plugin/event_news");
        eventChannel6.setStreamHandler(new EventChannel.StreamHandler() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onListen(Object o, EventChannel.EventSink es) {
                eventSink_news = es;
            }

            @Override
            public void onCancel(Object o) {
            }
        });
        EventChannel eventChannel7 = new EventChannel(mBinaryMessenger, "flutter_adsjm_plugin/event_splash");
        eventChannel7.setStreamHandler(new EventChannel.StreamHandler() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onListen(Object o, EventChannel.EventSink es) {
                Log.d("test","setStreamHandler.eventSink_splash");
                eventSink_splash = es;
                SjmSplashActivity.eventSink_splash = es;
            }

            @Override
            public void onCancel(Object o) {
            }
        });

    }

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        Log.d("test","onListen===onAttachedToEngine111");
        pluginBinding = flutterPluginBinding;
//      adEventChannel = new AdSjmEventChannel();
//    adEventChannel.onAttachedToEngine(flutterPluginBinding);

    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        ///这里可以处理初始化等事件
        Log.e(TAG, "onMethodCall: call.method=="+call.method);
        String adId = call.argument("adId");
        switch (call.method) {

            case "showFullVideoAd":
                loadFullVideo(adId,call.argument("_channelId")+"");

                break;
            case "showRewardVideoAd":
                loadReward(adId,call.argument("_channelId")+"",call.argument("userId")+"");

                break;
            case "loadInterstitalAd":
                loadInterstitalAd(adId,call.argument("_channelId")+"");

                break;
            case "loadH5contentAd":
                loadH5contentAd(adId,call.argument("userId")+"",call.argument("username")+"",call.argument("userhead")+"",call.argument("_channelId")+"");
                break;
            case "loadVideocontentAd":
                startContent(adId);
                break;
            case "loadVoliceAd":
                loadVoliceAd(adId,call.argument("userId")+"",call.argument("rewardName")+"");
                break;
            case "init":
                String appid = call.argument("appId");
                init(appid);
                break;
            case "showSplashAd":
                showSplashAd(adId,(int)call.argument("fetchDelay"));
                break;
            default:
                result.notImplemented();
                break;

        }
        result.success(call.method);
    }

    private void init(String appId){
        SjmCustomController.getInstance().setCustomController(new SjmCustomController.Controller(){
            @Override
            public boolean canReadInstalledPackages() {
                return false;
            }

        });
        SjmSdk.init(this.activity.getApplicationContext(),appId);
    }

    private void startContent(String adId){
        Intent intent = new Intent(this.activity,ContentListActivity.class);
        intent.putExtra("sjm_adId", adId);
        this.activity.startActivity(intent);
    }
    private void showSplashAd(String adId,int fetchDelay){
        Intent intent = new Intent(this.activity,SjmSplashActivity.class);
        intent.putExtra("sjm_adId", adId);
        intent.putExtra("time", fetchDelay);
        SjmSplashActivity.eventSink_splash = eventSink_splash;
        Log.d("test","setStreamHandler.eventSink_splash1="+(SjmSplashActivity.eventSink_splash==null));

        this.activity.startActivity(intent);
    }
//    private ProgressDialog pb;
    private void showPb(){
//        if(null == pb){
//            pb = new ProgressDialog(activity);
//        }
//        pb.setMessage("加载中...");
//        pb.show();
    }
    private void cancel(){
//        if(null != pb){
//            pb.cancel();
//            pb = null;
//        }
    }
    EventChannel.EventSink eventSink;
    SjmFullScreenVideoAd sjmFullScreenVideoAd = null;
    boolean isLoadFull = false;
    private void loadFullVideo( String adId,String channelid){
        Log.e(TAG, "onSjmAdLoaded.isLoad="+isLoadFull);
        if(isLoadFull){
            return;
        }
        Log.e(TAG, "onMethodCall: call.loadReward==");
        //可以加个进度等待。。
        showPb();
        sjmFullScreenVideoAd = new SjmFullScreenVideoAd(activity, adId, new SjmFullScreenVideoAdListener() {


            @Override
            public void onSjmAdLoaded() {
                Log.e(TAG, "onSjmAdLoaded");
                isLoadFull = false;
                cancel();
                sjmFullScreenVideoAd.showAd();
                Map<String, Object> result = new HashMap<>();
                result.put("event", "onSjmAdLoaded");
                result.put("ecpm",sjmFullScreenVideoAd.getECPM());
                result.put("id", "");
                if(null == eventSink_fullvideo)
                    return;
                eventSink_fullvideo.success(result);
            }

            @Override
            public void onSjmAdVideoCached() {
//                isLoadFull = false;
//                cancel();
//                sjmFullScreenVideoAd.showAd();
                Map<String, Object> result = new HashMap<>();
                result.put("event", "onSjmAdVideoCached");
                result.put("id", "");
                if(null == eventSink_fullvideo)
                    return;
                eventSink_fullvideo.success(result);
            }

            @Override
            public void onSjmAdClosed() {
                isLoadFull = false;
                Map<String, Object> result = new HashMap<>();
                result.put("event", "onSjmAdClosed");
                result.put("id", "");
                if(null == eventSink_fullvideo)
                    return;
                eventSink_fullvideo.success(result);
                eventSink_fullvideo.endOfStream();
            }

            @Override
            public void onSjmAdShow() {
                Map<String, Object> result = new HashMap<>();
                result.put("event", "onSjmAdShow");
                result.put("ecpm",sjmFullScreenVideoAd.getECPM());
                result.put("id", "");
                if(null == eventSink_fullvideo)
                    return;
                eventSink_fullvideo.success(result);
            }

            @Override
            public void onSjmAdClicked() {
                Map<String, Object> result = new HashMap<>();
                result.put("event", "onSjmAdClicked");
                result.put("id", "");
                if(null == eventSink_fullvideo)
                    return;
                eventSink_fullvideo.success(result);
            }

            @Override
            public void onSjmAdVideoComplete() {
                Map<String, Object> result = new HashMap<>();
                result.put("event", "onSjmAdVideoComplete");
                result.put("id", "");
                if(null == eventSink_fullvideo)
                    return;
                eventSink_fullvideo.success(result);
            }

            @Override
            public void onSjmAdError(SjmAdError SjmAdError) {
                isLoad = false;
                cancel();
                Toast.makeText(activity,SjmAdError.getErrorMsg(),Toast.LENGTH_SHORT).show();
                Log.d("main","SjmAdError="+SjmAdError.getErrorCode()+",,msg=="+SjmAdError.getErrorMsg());
                Map<String, Object> result = new HashMap<>();
                result.put("id", "");
                result.put("event", "onSjmAdError");
                result.put("code", SjmAdError.getErrorCode());
                result.put("message", SjmAdError.getErrorMsg());
                if(null == eventSink_fullvideo)
                    return;
                eventSink_fullvideo.success(result);
                eventSink_fullvideo.endOfStream();
            }
        });
        sjmFullScreenVideoAd.loadAd();
    }
    SjmRewardVideoAd sjmRewardVideoAd = null;
    boolean isLoad = false;
    private void loadReward( String adId,String channelid,String userId){
        Log.d("test","onSjmloadReward.channelid="+channelid);

        Log.e(TAG, "onSjmAdLoaded.isLoad="+isLoad);
        if(isLoad){
            return;
        }
        Log.e(TAG, "onMethodCall: call.loadReward==");
        //可以加个进度等待。。
        showPb();
        sjmRewardVideoAd = new SjmRewardVideoAd(activity, adId, new SjmRewardVideoAdListener() {
            @Override
            public void onSjmAdTradeId(String s,String key,boolean isVerity) {
                Log.d("test","onSjmAdTradeId.s="+s);
                Map<String, Object> result = new HashMap<>();
                result.put("id", s);
                result.put("key", key);
                result.put("event", "onSjmAdTradeId");
                if(null == eventSink_reward)
                    return;
                eventSink_reward.success(result);
            }

            @Override
            public void onSjmAdLoaded(String s) {
                Log.e(TAG, "onSjmAdLoaded");
                isLoad = false;
                cancel();
                sjmRewardVideoAd.showAD();
                Map<String, Object> result = new HashMap<>();
                result.put("id", s);
                result.put("ecpm",sjmRewardVideoAd.getECPM());
                result.put("event", "onSjmAdLoaded");
                if(null == eventSink_reward)
                    return;
                eventSink_reward.success(result);
            }

            @Override
            public void onSjmAdVideoCached() {

//          SjmRewardVideoAd.showAD();
                Map<String, Object> result = new HashMap<>();
                result.put("id", "");
                result.put("event", "onSjmAdVideoCached");
                if(null == eventSink_reward)
                    return;
                eventSink_reward.success(result);
            }

            @Override
            public void onSjmAdShow() {
                Map<String, Object> result = new HashMap<>();
                result.put("event", "onSjmAdShow");
                result.put("id", "");
                result.put("ecpm",sjmRewardVideoAd.getECPM());
                if(null == eventSink_reward)
                    return;
                eventSink_reward.success(result);
            }

            @Override
            public void onSjmAdShowError(SjmAdError SjmAdError) {
                isLoad = false;
                cancel();
                Map<String, Object> result = new HashMap<>();
                result.put("id", "");
                result.put("event", "onSjmAdError");
                result.put("code", SjmAdError.getErrorCode());
                result.put("message", SjmAdError.getErrorMsg());
                if(null == eventSink_reward)
                    return;
                eventSink_reward.success(result);
                eventSink_reward.endOfStream();
            }

            @Override
            public void onSjmAdClick() {
                Map<String, Object> result = new HashMap<>();
                result.put("event", "onSjmAdClicked");
                result.put("id", "");
                if(null == eventSink_reward)
                    return;
                eventSink_reward.success(result);
            }

            @Override
            public void onSjmAdVideoComplete() {
                Map<String, Object> result = new HashMap<>();
                result.put("event", "onSjmAdVideoComplete");
                result.put("id", "");
                if(null == eventSink_reward)
                    return;
                eventSink_reward.success(result);
            }

            @Override
            public void onSjmAdExpose() {

            }

            @Override
            public void onSjmAdReward(String s,String key) {
                Map<String, Object> result = new HashMap<>();
                result.put("id", s);
                result.put("key", key);
                result.put("ecpm",sjmRewardVideoAd.getECPM());
                result.put("event", "onSjmAdReward");
                if(null == eventSink_reward)
                    return;
                eventSink_reward.success(result);
            }

            @Override
            public void onSjmAdClose() {
                isLoad = false;
                Map<String, Object> result = new HashMap<>();
                result.put("event", "onSjmAdClosed");
                result.put("id", "");
                if(null == eventSink_reward)
                    return;
                eventSink_reward.success(result);
                eventSink_reward.endOfStream();
            }

            @Override
            public void onSjmAdError(SjmAdError SjmAdError) {
                isLoad = false;
                cancel();
                Toast.makeText(activity,SjmAdError.getErrorMsg(),Toast.LENGTH_SHORT).show();
                Log.d("main","SjmAdError="+SjmAdError.getErrorCode()+",,msg=="+SjmAdError.getErrorMsg());
                Map<String, Object> result = new HashMap<>();
                result.put("id", "");
                result.put("event", "onSjmAdError");
                result.put("code", SjmAdError.getErrorCode());
                result.put("message", SjmAdError.getErrorMsg());
                if(null == eventSink_reward)
                    return;
                eventSink_reward.success(result);
                eventSink_reward.endOfStream();
            }
        });
        sjmRewardVideoAd.setUserId(userId);
        sjmRewardVideoAd.loadAd();

    }
    private void loadH5contentAd(String adId,String userId,String username,String userhead,String channelid){
        SjmUser Sjmuser = new SjmUser(userId, username, userhead, 1000, SjmDeviceId.getDeviceId(activity));
//      EventChannel eventChannel = new EventChannel(mBinaryMessenger, "flutter_adsjm_plugin/event_" + channelid);
//      eventChannel.setStreamHandler(new EventChannel.StreamHandler() {
//          @RequiresApi(api = Build.VERSION_CODES.N)
//          @Override
//          public void onListen(Object o, EventChannel.EventSink es) {
//              eventSink = es;
//          }
//
//          @Override
//          public void onCancel(Object o) {
//          }
//      });
        //设备号 SjmDeviceId.getDeviceId(activity)
        new SjmH5Ad(activity, Sjmuser, new SjmH5ContentListener() {
            @Override
            public void onIntegralNotEnough(SjmUser SjmUser, int i) {
                Map<String, Object> result = new HashMap<>();
                result.put("event", "onIntegralNotEnough");
                result.put("id", "");
                if(null == eventSink_h5content)
                    return;
                eventSink_h5content.success(result);
            }

            @Override
            public void onIntegralExpend(SjmUser SjmUser, int i) {
                Map<String, Object> result = new HashMap<>();
                result.put("id", "");
                result.put("event", "onIntegralExpend");
                if(null == eventSink_h5content)
                    return;
                eventSink_h5content.success(result);
            }

            @Override
            public void onFinishTasks(SjmUser SjmUser, int i) {
                Map<String, Object> result = new HashMap<>();
                result.put("id", "");
                result.put("event", "onFinishTasks");
                if(null == eventSink_h5content)
                    return;
                eventSink_h5content.success(result);
            }

            @Override
            public void onGameExit(SjmUser SjmUser) {
                Map<String, Object> result = new HashMap<>();
                result.put("id", "");
                result.put("event", "onGameExit");
                if(null == eventSink_h5content)
                    return;
                eventSink_h5content.success(result);
                eventSink_h5content.endOfStream();
            }

            @Override
            public void onSjmAdReward(SjmUser SjmUser, int i) {
                Map<String, Object> result = new HashMap<>();
                result.put("id", "");
                result.put("event", "onSjmAdRewardFinish");
                if(null == eventSink_h5content)
                    return;
                eventSink_h5content.success(result);
            }

            @Override
            public void onSjmAdReward(String s) {
                Map<String, Object> result = new HashMap<>();
                result.put("id", "");
                result.put("event", "onSjmAdReward");
                if(null == eventSink_h5content)
                    return;
                eventSink_h5content.success(result);
            }

            @Override
            public void onSjmAdLoaded(String s) {
                Map<String, Object> result = new HashMap<>();
                result.put("event", "onSjmAdLoaded");
                result.put("id", "");
                if(null == eventSink_h5content)
                    return;
                eventSink_h5content.success(result);
            }

            @Override
            public void onSjmAdTradeId(String s) {
                Map<String, Object> result = new HashMap<>();
                result.put("id",s);
                result.put("event", "onSjmAdTradeId");
                if(null == eventSink_h5content)
                    return;
                eventSink_h5content.success(result);
            }

            @Override
            public void onSjmAdClick() {
                Map<String, Object> result = new HashMap<>();
                result.put("id", "");
                result.put("event", "onSjmAdClick");
                if(null == eventSink_h5content)
                    return;
                eventSink_h5content.success(result);
            }

            @Override
            public void onSjmUserBehavior(String s) {
                Map<String, Object> result = new HashMap<>();
                result.put("id",s);
                result.put("event", "onSjmUserBehavior");
                if(null == eventSink_h5content)
                    return;
                eventSink_h5content.success(result);
            }
        }, adId);//H333888
    }
    SjmInterstitialAd SjmInterstitialAd = null;
    private void loadInterstitalAd(String adId,String channelid){
//      EventChannel eventChannel = new EventChannel(mBinaryMessenger, "flutter_adsjm_plugin/event_" + channelid);
//      eventChannel.setStreamHandler(new EventChannel.StreamHandler() {
//          @RequiresApi(api = Build.VERSION_CODES.N)
//          @Override
//          public void onListen(Object o, EventChannel.EventSink es) {
//              eventSink = es;
//          }
//
//          @Override
//          public void onCancel(Object o) {
//          }
//      });
        showPb();
        SjmInterstitialAd =  new SjmInterstitialAd(activity, adId, new SjmInterstitialAdListener() {
            @Override
            public void onSjmAdClosed() {
                Map<String, Object> result = new HashMap<>();
                result.put("id", "");
                result.put("event", "onSjmAdClosed");
                if(null == eventSink_interstital)
                    return;
                eventSink_interstital.success(result);
                eventSink_interstital.endOfStream();
            }

            @Override
            public void onSjmAdLoaded() {
                cancel();
                SjmInterstitialAd.showAd();
                Map<String, Object> result = new HashMap<>();
                result.put("id", "");
                result.put("ecpm",SjmInterstitialAd.getECPM());
                result.put("event", "onSjmAdLoaded");
                if(null == eventSink_interstital)
                    return;
                eventSink_interstital.success(result);

            }

            @Override
            public void onSjmAdShow() {
                Map<String, Object> result = new HashMap<>();
                result.put("id", "");
                result.put("ecpm",SjmInterstitialAd.getECPM());
                result.put("event", "onSjmAdShow");
                if(null == eventSink_interstital)
                    return;
                eventSink_interstital.success(result);

            }

            @Override
            public void onSjmAdClicked() {
                Map<String, Object> result = new HashMap<>();
                result.put("id", "");
                result.put("event", "onSjmAdClicked");
                if(null == eventSink_interstital)
                    return;
                eventSink_interstital.success(result);

            }

            @Override
            public void onSjmAdError(SjmAdError SjmAdError) {
                cancel();
                Map<String, Object> result = new HashMap<>();
                result.put("id", "");
                result.put("event", "onSjmAdError");
                result.put("code", SjmAdError.getErrorCode());
                result.put("message", SjmAdError.getErrorMsg());
                if(null == eventSink_interstital)
                    return;
                eventSink_interstital.success(result);

                eventSink_interstital.endOfStream();
            }
        });
        SjmInterstitialAd.loadAd();
    }

    SjmVoliceAd sjmVoliceAd = null;
    private void loadVoliceAd(String adId,String userId,String rewardName){
        sjmVoliceAd = new SjmVoliceAd(activity, new SjmVoliceAdListener() {
            @Override
            public void onSjmAdLoad() {
                Map<String, Object> result = new HashMap<>();
                result.put("id", "");
                result.put("event", "onSjmAdLoad");
                if(null == eventSink_volice)
                    return;
                eventSink_volice.success(result);
                sjmVoliceAd.showVoliceAd();
            }

            @Override
            public void onSjmAdShow() {
                Map<String, Object> result = new HashMap<>();
                result.put("id", "");
                result.put("event", "onSjmAdShow");
                if(null == eventSink_volice)
                    return;
                eventSink_volice.success(result);
            }

            @Override
            public void onSjmAdSuccess(int reward, int stepNum,boolean isMultipleReward) {
                Map<String, Object> result = new HashMap<>();
                result.put("id", "");
                result.put("reward", reward);
                result.put("stepNum", stepNum);
                result.put("isMultipleReward", isMultipleReward);
                result.put("event", "onSjmAdSuccess");
                if(null == eventSink_volice)
                    return;
                eventSink_volice.success(result);

            }

            @Override
            public void onSjmAdClose() {
                Map<String, Object> result = new HashMap<>();
                result.put("id", "");
                result.put("event", "onSjmAdClose");
                if(null == eventSink_volice)
                    return;
                eventSink_volice.success(result);
            }

            @Override
            public void onSjmVoiceComplete() {
                Map<String, Object> result = new HashMap<>();
                result.put("id", "");
                result.put("event", "onSjmVoiceComplete");
                if(null == eventSink_volice)
                    return;
                eventSink_volice.success(result);
            }

            @Override
            public void onSjmAdError(SjmAdError sjmAdError) {
                cancel();
                Map<String, Object> result = new HashMap<>();
                result.put("id", "");
                result.put("event", "onSjmAdError");
                result.put("code", sjmAdError.getErrorCode());
                result.put("message", sjmAdError.getErrorMsg());
                if(null == eventSink_volice)
                    return;
                eventSink_volice.success(result);

                eventSink_volice.endOfStream();
            }

            @Override
            public float onGetRewardInfo(int i, int i1) {
                return 0.05f;
            }
        }, adId);
        if(!TextUtils.isEmpty(userId)){
            sjmVoliceAd.setUserId(userId);
        }
        if(!TextUtils.isEmpty(rewardName)){
            sjmVoliceAd.setRewardName(rewardName);
        }

        sjmVoliceAd.loadVoliceAd();
    }
//    SjmNewsAd sjmNewsAd = null;
//    private void loadNewsAd(String adId,String userId){
//        sjmNewsAd = new SjmNewsAd(activity,adId, new SjmNewsListener() {
//
//
//            @Override
//            public void onSjmAdClicked(String s) {
//                Map<String, Object> result = new HashMap<>();
//                result.put("id", "");
//                result.put("event", "onSjmAdClicked");
//                if(null == eventSink_news)
//                    return;
//                eventSink_news.success(result);
//            }
//
//            @Override
//            public void onSjmAdError(SjmAdError sjmAdError) {
//                cancel();
//                Map<String, Object> result = new HashMap<>();
//                result.put("id", "");
//                result.put("event", "onSjmAdError");
//                result.put("code", sjmAdError.getErrorCode());
//                result.put("message", sjmAdError.getErrorMsg());
//                if(null == eventSink_news)
//                    return;
//                eventSink_news.success(result);
//
//                eventSink_news.endOfStream();
//            }
//
//            @Override
//            public void onSuccess() {
//                Map<String, Object> result = new HashMap<>();
//                result.put("id", "");
//                result.put("event", "onSuccess");
//                if(null == eventSink_news)
//                    return;
//                eventSink_news.success(result);
//                eventSink_news.endOfStream();
//            }
//
//
//        });
//        if(!TextUtils.isEmpty(userId)){
//            sjmNewsAd.setUserId(userId);
//        }
//        sjmNewsAd.loadAd();
//    }
    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        pluginBinding = null;
//    adEventChannel.onDetachedFromEngine(binding);
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        initializePlugin(
                pluginBinding.getApplicationContext(),
                binding.getActivity(),
                pluginBinding.getBinaryMessenger(),
                pluginBinding.getPlatformViewRegistry());
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        activity = null;
    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
        initializePlugin(
                pluginBinding.getApplicationContext(),
                binding.getActivity(),
                pluginBinding.getBinaryMessenger(),
                pluginBinding.getPlatformViewRegistry());
    }

    @Override
    public void onDetachedFromActivity() {
        activity = null;
    }
}
