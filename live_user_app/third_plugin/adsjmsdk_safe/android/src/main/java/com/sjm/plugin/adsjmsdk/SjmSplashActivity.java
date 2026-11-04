package com.sjm.plugin.adsjmsdk;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.sjm.sjmsdk.ad.SjmAdError;
import com.sjm.sjmsdk.ad.SjmSplashAd;
import com.sjm.sjmsdk.ad.SjmSplashAdListener;

import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.EventChannel;


public class SjmSplashActivity extends FragmentActivity implements SjmSplashAdListener {
    private static final String TAG = SjmSplashActivity.class.getSimpleName();

    private SjmSplashAd splashAd;
    private ViewGroup container;

    long fetchSplashADTime;

   public static EventChannel.EventSink eventSink_splash;
    private boolean loadAdOnly = false;
    private boolean showingAd = false;
    String adid;
    Bundle bundle = null;
    int time = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        container = (ViewGroup) this.findViewById(R.id.splash_container);
        // 刘海屏全屏展示开屏广告
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(layoutParams);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }

        //loadAdOnlyView.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
         adid = intent.getStringExtra("sjm_adId");
        time= intent.getIntExtra("time",5) ;
        loadAdOnly =false;
//        show();

        //如果启动图停留时间过短，返回未找到广告位，建议延时调用开屏广告，如下代码例子
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                show();
            }
        },1000);

    }

    Handler mHandler = new Handler();
    private void show(){
        splashAd =new SjmSplashAd(this,this,adid,time);
        if (Build.VERSION.SDK_INT >= 23) {
            if(splashAd.checkAndRequestPermission()){
                fetchSplashAD();
            }
            //请在 onRequestPermissionsResult 做相关业务处理
        } else {
            // 如果是Android6.0以下的机器，建议在manifest中配置相关权限，这里可以直接调用SDK
            fetchSplashAD();
        }
//        fetchSplashAD();
    }




    private void fetchSplashAD() {
        fetchSplashADTime = System.currentTimeMillis();

        if(loadAdOnly) {
            splashAd.fetchAdOnly();
        }else{
            splashAd.fetchAndShowIn(container);
        }
    }

    //ZjSplashAdListener


    @Override
    public void onSjmAdLoaded() {
        showStatus("onSjmAdLoaded:");
        Map<String, Object> result = new HashMap<>();
        result.put("id", "");
        result.put("event", "onSjmAdLoaded");
        if(null != eventSink_splash){
            eventSink_splash.success(result);
        }

    }

    @Override
    public void onSjmAdLoadTimeOut() {
        Map<String, Object> result = new HashMap<>();
        result.put("event", "onSjmAdError");
        result.put("code", 99998);
        result.put("id", "");
        result.put("message","onSjmAdLoadTimeOut");
        if(null != eventSink_splash){
            eventSink_splash.success(result);
            eventSink_splash.endOfStream();
        }

        jump();

    }

    @Override
    public void onSjmAdShow() {
        showStatus("onSjmAdShow:");
        Map<String, Object> result = new HashMap<>();
        result.put("id", "");
        result.put("event", "onSjmAdShow");
        if(null != eventSink_splash){
            eventSink_splash.success(result);
        }

    }

    @Override
    public void onSjmAdClicked() {
        showStatus("onSjmAdClicked:");
        Map<String, Object> result = new HashMap<>();
        result.put("id", "");
        result.put("event", "onSjmAdClicked");
        if(null != eventSink_splash){
            eventSink_splash.success(result);
        }

    }

    @Override
    public void onSjmAdTickOver() {
        Log.d("test:","onSjmAdTickOver:");
        Map<String, Object> result = new HashMap<>();
        Log.d("test","onSjmAdTickOver");
        result.put("id", "");
        result.put("event", "onSjmAdClosed");
        if(null != eventSink_splash){
            eventSink_splash.success(result);
            eventSink_splash.endOfStream();
        }

        jump();
    }

    @Override
    public void onSjmAdDismissed() {
        showStatus("onSjmAdDismissed:");
        Map<String, Object> result = new HashMap<>();
        Log.d("test","onSjmAdDismissed");
        result.put("event", "onSjmAdClosed");
        result.put("id", "");
        if(null != eventSink_splash){
            eventSink_splash.success(result);
            eventSink_splash.endOfStream();
        }

        jump();
    }


    @Override
    public void onSjmAdError(SjmAdError error) {

        showStatus("onSjmAdError:"+error.getErrorMsg());
        Map<String, Object> result = new HashMap<>();
        result.put("event", "onSjmAdError");
        result.put("id", "");
        result.put("code", error.getErrorCode());
        result.put("message", error.getErrorMsg());
        if(null != eventSink_splash){
            eventSink_splash.success(result);
            eventSink_splash.endOfStream();
        }else{

        }

        jump();
    }
    private void jump(){
        SjmSplashActivity.this.finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (splashAd.hasAllPermissionsGranted(requestCode,grantResults)) {
            fetchSplashAD();
        }else {
            Toast.makeText(this, "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//            intent.setData(Uri.parse("package:" + getPackageName()));
//            startActivity(intent);
//            finish();
            fetchSplashAD();
        }
    }

    private void showStatus(String msg){
        Log.i(TAG, msg);
    }
    /** 开屏页一定要禁止用户对返回按钮的控制，否则将可能导致用户手动退出了App而广告无法正常曝光和计费 */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK|| keyCode == KeyEvent.KEYCODE_HOME)
            return true;
        return super.onKeyDown(keyCode, event);
    }
}