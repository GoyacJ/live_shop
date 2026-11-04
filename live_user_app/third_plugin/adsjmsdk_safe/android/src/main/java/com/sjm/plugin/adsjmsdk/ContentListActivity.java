package com.sjm.plugin.adsjmsdk;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.fragment.app.FragmentActivity;

import com.sjm.plugin.adsjmsdk.R;
import com.kwad.sdk.api.KsContentPage;
import com.sjm.sjmsdk.ad.SjmAdError;
import com.sjm.sjmsdk.ad.SjmContentAd;
import com.sjm.sjmsdk.ad.SjmContentAdListener;


public class ContentListActivity extends FragmentActivity {

//
    SjmContentAd contentAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_list);
        // 刘海屏全屏展示开屏广告
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(layoutParams);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
        Intent intent = getIntent();
        String adid = intent.getStringExtra("sjm_adId");
        Log.d("test","adid="+adid);
        contentAd= new SjmContentAd(this, new SjmContentAdListener() {
            @Override
            public void onSjmAdError(SjmAdError error) {
                Log.d("test","onSjmAdError.error="+error.getErrorMsg());
            }

            @Override
            public void onPageEnter(KsContentPage.ContentItem contentItem) {

            }

            @Override
            public void onPageResume(KsContentPage.ContentItem contentItem) {

            }

            @Override
            public void onPagePause(KsContentPage.ContentItem contentItem) {

            }

            @Override
            public void onPageLeave(KsContentPage.ContentItem contentItem) {

            }

            @Override
            public void onVideoPlayStart(KsContentPage.ContentItem contentItem) {

            }

            @Override
            public void onVideoPlayPaused(KsContentPage.ContentItem contentItem) {

            }

            @Override
            public void onVideoPlayResume(KsContentPage.ContentItem contentItem) {

            }

            @Override
            public void onVideoPlayCompleted(KsContentPage.ContentItem contentItem) {

            }

            @Override
            public void onVideoPlayError(KsContentPage.ContentItem contentItem, int i, int i1) {

            }


        }, adid);//ee6156e7  SjmAd_241218 SjmAd_5420000012
//        contentAd.regContentAd(this,"SjmAd_5090131");

       contentAd.showAd(R.id.main_frame_layout);
    }



}
