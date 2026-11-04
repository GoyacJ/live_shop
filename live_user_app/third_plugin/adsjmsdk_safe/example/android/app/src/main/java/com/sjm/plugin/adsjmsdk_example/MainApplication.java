package com.sjm.plugin.adsjmsdk_example;

import com.sjm.sjmsdk.SjmSdk;

import io.flutter.app.FlutterApplication;


public class MainApplication extends FlutterApplication {

    @Override
    public void onCreate()

    {
        super.onCreate();

//        com.kwad.sdk.glide.c.b(this);
        SjmSdk.init(this,"test0001");
//        SpeechVoiceSdk.init(this, new VoiceConfig.Builder()
//                .appId("43514512")
//                .appSecret("TOAu3PHgYPBJ5W36hmU46X78yERa2yIe")
//                .debug(false)
//                .showToast(false)//是否展示Toast默认为ture
//                .build());

    }


}
