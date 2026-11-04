package com.sjm.plugin.adsjmsdk;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.EventChannel;

/**
 * author：hm
 * date： 2022/1/17
 */
public class AdSjmEventChannel implements EventChannel.StreamHandler , FlutterPlugin {
    private EventChannel eventChannel;

    private EventChannel.EventSink eventSink;

    private Context context;
    private String channel="0";
    public AdSjmEventChannel(){

    }
    public void setEventChannel(String channerl){
        this.channel = channerl;
    }

    public void  sendContent(Map<String,Object> maps) {
        eventSink.success(maps);
    }
    public void  endOfStream() {
        eventSink.endOfStream();
    }
    @Override
    public void onListen(Object arguments, EventChannel.EventSink events) {
        eventSink = events;
    }

    @Override
    public void onCancel(Object arguments) {
        eventSink = null;
    }

    @Override
    public void onAttachedToEngine(@NonNull  FlutterPlugin.FlutterPluginBinding binding) {
        eventChannel = new EventChannel(binding.getBinaryMessenger(),"flutter_adsjm_plugin/event_"+channel);
        eventChannel.setStreamHandler(this);
        context = binding.getApplicationContext();
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPlugin.FlutterPluginBinding binding) {
        eventChannel = null;
        eventChannel.setStreamHandler(null);
    }
}
