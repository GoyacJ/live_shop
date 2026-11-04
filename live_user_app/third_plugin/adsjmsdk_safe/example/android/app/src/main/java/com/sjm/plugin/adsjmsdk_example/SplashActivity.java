package com.sjm.plugin.adsjmsdk_example;

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


public class SplashActivity extends FragmentActivity implements SjmSplashAdListener {
    private static final String TAG = SplashActivity.class.getSimpleName();

    private SjmSplashAd splashAd;
    private ViewGroup container;

    long fetchSplashADTime;


    private boolean loadAdOnly = false;
    private boolean showingAd = false;
    private LinearLayout loadAdOnlyView;
    private Button loadAdOnlyCloseButton;
    private Button loadAdOnlyDisplayButton;
    private Button loadAdOnlyRefreshButton;
    private TextView loadAdOnlyStatusTextView;
    String zj_adId;
    Bundle bundle = null;
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

        zj_adId = "sjmad_test002";
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
        splashAd =new SjmSplashAd(this,this,zj_adId,3);
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
    }

    @Override
    public void onSjmAdLoadTimeOut() {
        jump();
    }

    @Override
    public void onSjmAdShow() {
        showStatus("onSjmAdShow:");
    }

    @Override
    public void onSjmAdClicked() {
        showStatus("onSjmAdClicked:");
    }

    @Override
    public void onSjmAdTickOver() {
        Log.d("test:","onSjmAdTickOver:");
        jump();
    }

    @Override
    public void onSjmAdDismissed() {
        showStatus("onSjmAdDismissed:");
        Log.d("test:","onSjmAdDismissed:");
        jump();
    }


    @Override
    public void onSjmAdError(SjmAdError error) {

        showStatus("onSjmAdError:"+error.getErrorMsg());
        startActivity(new Intent(this,MainActivity.class));
      SplashActivity.this.finish();
    }
    private void jump(){
        startActivity(new Intent(this,MainActivity.class));
       SplashActivity.this.finish();
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