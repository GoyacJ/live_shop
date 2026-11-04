package com.sjm.plugin.adsjmsdk_example;

import androidx.annotation.NonNull;

import com.sjm.plugin.adsjmsdk.AdSjmsdkPlugin;

import io.flutter.embedding.android.FlutterFragmentActivity;
import io.flutter.embedding.engine.FlutterEngine;

public class MainActivity extends FlutterFragmentActivity {
    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);
        flutterEngine.getPlugins().add(new AdSjmsdkPlugin());
    }
}
