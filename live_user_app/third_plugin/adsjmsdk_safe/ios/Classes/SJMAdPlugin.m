//
//  SJMAdPlugin.m
//  Runner
//


#import "SJMAdPlugin.h"

#import "SJMSplashAdHelper.h"
#import "SJMRewardVideoHelper.h"
#import "SJMInterstitialAdHelper.h"
#import "SJMFullVideoAdHelper.h"
#import "SJMNativeExpressFeedAdView.h"
#import "SJMSplashAdView.h"
#import "SJMBannerAdPlugView.h"

#import "SJMAdHeader.h"

#import <LUYSDKRely/LUYAdDebugLog.h>

@interface SJMAdPlugin()<FlutterStreamHandler>
@property(nonatomic, retain) FlutterMethodChannel *channel;

@property (nonatomic,strong) SJMSplashAdHelper *splashAd;
@property (nonatomic,strong) SJMRewardVideoHelper *rewardVideoAd;
@property (nonatomic,strong) SJMInterstitialAdHelper *interstitialAd;
@property (nonatomic,strong) SJMFullVideoAdHelper *fullVideoAd;

@property (nonatomic, weak) NSObject<FlutterPluginRegistrar> *registrar;

@property (nonatomic, strong) FlutterResult callback;
@end

@implementation SJMAdPlugin


+ (void)registerWithRegistrar:(NSObject <FlutterPluginRegistrar> *)registrar {
    FlutterMethodChannel *channel = [FlutterMethodChannel methodChannelWithName:@"flutter_adsjm_plugin/method" binaryMessenger:registrar.messenger];
    SJMAdPlugin *instance = [[SJMAdPlugin alloc] init];
    instance.registrar = registrar;
    [registrar addMethodCallDelegate:instance channel:channel];
    
    
    SJMSplashAdFactory *splashF = [[SJMSplashAdFactory alloc] initWithRegistrar:registrar];
    [registrar registerViewFactory:splashF withId:@"flutter_adsjm_plugin/splash"];
    
    SJMNativeExpressFeedAdViewFactory *feedF = [[SJMNativeExpressFeedAdViewFactory alloc] initWithRegistrar:registrar];
    [registrar registerViewFactory:feedF withId:@"flutter_adsjm_plugin/native_express"];
    
    SJMBannerAdViewFactory *bannerF = [[SJMBannerAdViewFactory alloc] initWithRegistrar:registrar];
    [registrar registerViewFactory:bannerF withId:@"flutter_adsjm_plugin/banner"];
}


- (void)showSplashAd:(FlutterMethodCall *)call result:(FlutterResult)result{
    NSString *adId = [call.arguments valueForKey:@"adId"];
    NSNumber *fetchDelay = [call.arguments valueForKey:@"fetchDelay"];
    self.splashAd =[[SJMSplashAdHelper alloc]init];
    __weak typeof(self) weakSelf = self;
    self.splashAd.splashAdCallback = ^(NSString * _Nonnull event, LUYSplashAd * _Nullable ad, NSError * _Nullable error) {
        [weakSelf callbackWithEvent:event adId:ad.slotID otherDic:nil error:error];
    };
    [self.splashAd showAdWithAdId:adId fetchDelay:fetchDelay.integerValue];
    
}


- (void)showRewardVideoAd:(FlutterMethodCall *)call result:(FlutterResult)result{
    NSString *adId = [call.arguments valueForKey:@"adId"];
    id userId = [call.arguments valueForKey:@"userId"];
    if ([userId isKindOfClass:[NSNumber class]]) {
        userId = ((NSNumber*)userId).stringValue;
    }
    self.rewardVideoAd =[[SJMRewardVideoHelper alloc]init];
    __weak typeof(self) weakSelf = self;
    self.rewardVideoAd.sjmCallback = ^(NSString * _Nonnull event, LUYRewardVideoAd * _Nullable ad, NSError * _Nullable error) {
        if([event isEqualToString:SjmAd_TradeId]) {
            [weakSelf callbackWithEvent:event adId:ad.slotID otherDic:@{@"key":@""} error:error];
        }else{
            [weakSelf callbackWithEvent:event adId:ad.slotID otherDic:nil error:error];
        }
    };
    [self.rewardVideoAd showAdWithAdId:adId userId:userId];
    
}

- (void)showInterstitialAd:(FlutterMethodCall *)call result:(FlutterResult)result{
    NSString *adId = [call.arguments valueForKey:@"adId"];
    self.interstitialAd = [[SJMInterstitialAdHelper alloc]init];
    __weak typeof(self) weakSelf = self;
    self.interstitialAd.sjmCallback = ^(NSString * _Nonnull event, LUYInterstitialAd * _Nullable ad, NSError * _Nullable error) {
        [weakSelf callbackWithEvent:event adId:ad.slotID otherDic:nil error:error];
    };
    [self.interstitialAd showAdWithAdId:adId];
}

- (void)showFullVideoAd:(FlutterMethodCall *)call result:(FlutterResult)result{
    NSString *adId = [call.arguments valueForKey:@"adId"];
    self.fullVideoAd = [[SJMFullVideoAdHelper alloc]init];
    __weak typeof(self) weakSelf = self;
    self.fullVideoAd.sjmCallback = ^(NSString * _Nonnull event, LUYFullScreenVideoAd * _Nullable ad, NSError * _Nullable error) {
        [weakSelf callbackWithEvent:event adId:ad.slotID otherDic:nil error:error];
    };
    [self.fullVideoAd showAdWithAdId:adId];
}


- (void)init:(FlutterMethodCall *)call result:(FlutterResult)result {
    NSString *appId = [call.arguments valueForKey:@"appId"];
    [LUYSDKConfig registerAppId:appId callback:^(BOOL finished, NSDictionary * _Nonnull info) {
        LUYLogDebug(@"finished:%@,info:%@",finished?@"YES":@"NO",info);
    }];
}


- (void)handleMethodCall:(FlutterMethodCall *)call result:(FlutterResult)result{
    
    if ([call.method isEqualToString:@"showSplashAd"]) {
        [self channelEvent:@"flutter_adsjm_plugin/event_splash"];
        // 加载开屏广告
        [self showSplashAd:call result:result];
    }else if (([call.method isEqualToString:@"showRewardVideoAd"])) {
        [self channelEvent:@"flutter_adsjm_plugin/event_rewardVideo"];
        // 加载激励视频
        [self showRewardVideoAd:call result:result];
    }else if ([call.method isEqualToString:@"loadInterstitalAd"]) {
        [self channelEvent:@"flutter_adsjm_plugin/event_interstital"];
        // 加载插屏广告
        [self showInterstitialAd:call result:result];
    }else if ([call.method isEqualToString:@"showFullVideoAd"]) {
        [self channelEvent:@"flutter_adsjm_plugin/event_fullVideo"];
        // 加载全屏广告
        [self showFullVideoAd:call result:result];
    }else if ([call.method isEqualToString:@"init"]) {
        // 初始化
        [self init:call result:result];
    }else {
        result(FlutterMethodNotImplemented);
    }
}





- (void)channelEvent:(NSString *)event {
    NSString *channel = [NSString stringWithFormat:@"%@", event];
    FlutterEventChannel *eventChannel = [FlutterEventChannel eventChannelWithName:channel binaryMessenger:[_registrar messenger]];
    [eventChannel setStreamHandler:self];
}


// // 这个onListen是Flutter端开始监听这个channel时的回调，第二个参数 EventSink是用来传数据的载体。
- (FlutterError* _Nullable)onListenWithArguments:(id _Nullable)arguments
                                       eventSink:(FlutterEventSink)events {
    // arguments flutter给native的参数
    // 回调给flutter， 建议使用实例指向，因为该block可以使用多次
    NSLog(@"onListenWithArguments:%@", arguments);
    if (events) {
        self.callback = events;
    }
    return nil;
}

/// flutter不再接收
- (FlutterError* _Nullable)onCancelWithArguments:(id _Nullable)arguments {
    // arguments flutter给native的参数
    NSLog(@"onCancelListen:%@", arguments);
    return nil;
}


/**回调事件*/
-(void)callbackWithEvent:(NSString *)event adId:(NSString *)adId otherDic:(NSDictionary *)otherDic error:(NSError *)error{
    if (self.callback) {
        NSMutableDictionary *result = [NSMutableDictionary dictionary];
        [result setValue:event.length > 0 ?event :@"未知事件" forKey:sjmevent];
        [result setValue:adId?:@"错误Id" forKey:sjmid];
        if (otherDic) {
            [result addEntriesFromDictionary:otherDic];
        }
        if (error) {
            [result setObject:@(error.code) forKey:sjmcode];
            [result setObject:[error getErrorString] forKey:sjmmessage];
        }
        self.callback(result);
    }
}





@end
