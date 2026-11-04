package com.sjm.plugin.adsjmsdk_example

import com.sjm.plugin.adsjmsdk.AdSjmsdkPlugin
import io.flutter.embedding.android.FlutterFragmentActivity
import io.flutter.embedding.engine.FlutterEngine

class testAc : FlutterFragmentActivity() {
    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        flutterEngine.plugins.add(AdSjmsdkPlugin())
    }
}