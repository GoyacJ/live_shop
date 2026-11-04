//
//  SJMSplashAdView.m
//  adsjmsdk
//
//  Created by Allen on 2023/4/24.
//

#import "SJMSplashAdView.h"
#import <LUYSDK/LUYSDK.h>

@interface SJMSplashAdView ()<FlutterStreamHandler,LUYSplashAdDelegate>
@property (nonatomic,strong)LUYSplashAd *splashAd;

@property (nonatomic, strong) UIView *containerView;


@end

@implementation SJMSplashAdView

- (instancetype)initWithFrame:(CGRect)frame
               viewIdentifier:(int64_t)viewId
                    arguments:(id _Nullable)args
                    registrar:(NSObject<FlutterPluginRegistrar> *)registrar{
    if (self = [super init]) {
        // 事件通道
        NSString *channelName = [NSString stringWithFormat:@"flutter_adsjm_plugin/splash_event_%lld", viewId];
        FlutterEventChannel *eventChannel = [FlutterEventChannel eventChannelWithName:channelName binaryMessenger:[registrar messenger]];
        [eventChannel setStreamHandler:self];
        
        
        // 获取参数
        NSString *adId;
        CGFloat adWidth = 0, adHeight = 0;
        if ([args isKindOfClass:[NSDictionary class]]) {
            adId = args[@"adId"];
            adWidth = [args[@"width"] floatValue];
            adHeight = [args[@"height"] floatValue];
        }
        
        if (adWidth <= 0.0) {
            adWidth = [UIScreen mainScreen].bounds.size.width;
        }
        
        self.adId = adId;
        _splashAd = [[LUYSplashAd alloc] initWithSlotID:adId];
        _splashAd.delegate = self;
        _splashAd.tolerateTimeout = 5;
        _splashAd.rootViewController = [self findCurrentShowingViewController];
        [_splashAd loadAd];
       
        // 容器view
        _containerView = [[UIView alloc] initWithFrame:frame];
        _containerView.backgroundColor = [UIColor clearColor];
        
    }
    return self;
}

- (nonnull UIView *)view {
    return _containerView;
}

- (FlutterError* _Nullable)onListenWithArguments:(NSString *_Nullable)arguments
                                       eventSink:(FlutterEventSink)events {
    if (events) {
        self.adCallback = events;
    }
    return nil;
}

- (FlutterError *)onCancelWithArguments:(id)arguments {
    NSLog(@"event -> cancel listen");
    return nil;
}


#pragma mark - LUYSplashAdDelegate
/// 开屏广告加载成功
- (void)luy_splashAdDidLoad:(LUYSplashAd *)splashAd {
    if (self.containerView.window) {
        [self.splashAd showAdInWindow:self.containerView.window];
    }else{
        [self.splashAd showAdInWindow:[UIApplication sharedApplication].keyWindow];
    }
    [self callbackWithEvent:SjmAd_Loaded otherDic:nil error:nil];
}

///开屏广告展示
- (void)luy_splashAdDidShow:(LUYSplashAd *)splashAd {
    [self callbackWithEvent:SjmAd_Show otherDic:nil error:nil];
}

///开屏广告点击回调
- (void)luy_splashAdClicked:(LUYSplashAd *)splashAd {
    [self callbackWithEvent:SjmAd_Clicked otherDic:nil error:nil];
}

///开屏广告关闭回调
- (void)luy_splashAdClosed:(LUYSplashAd *)splashAd {
    [self.containerView removeFromSuperview];
    [self callbackWithEvent:SjmAd_Closed otherDic:nil error:nil];
}

///开屏广告倒记时结束
- (void)luy_splashAdCountdownToZero:(LUYSplashAd*)splashAd {
    [self.containerView removeFromSuperview];
    [self callbackWithEvent:@"onSjmAdCountdown" otherDic:nil error:nil];
}

///开屏广告错误
- (void)luy_splashAdLoadFail:(LUYSplashAd *)splashAd error:(NSError *)error {
    [self.containerView removeFromSuperview];
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.25 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        [self callbackWithEvent:SjmAd_Error otherDic:nil error:error];
    });
}

///开屏广告获取超时
- (void)luy_splashAdLoadFailWithTimeout:(LUYSplashAd *)splashAd {
    [self.containerView removeFromSuperview];
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.25 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        [self callbackWithEvent:SjmAd_Error otherDic:nil error:[NSError errorWithDomain:@"SJMAdTimeout" code:19999 userInfo:@{@"info":@"开屏广告获取超时"}]];
    });
}

@end


@interface SJMSplashAdFactory()
@property (nonatomic, strong) NSObject<FlutterPluginRegistrar> *registrar;
@end

@implementation SJMSplashAdFactory
- (instancetype)initWithRegistrar:(NSObject<FlutterPluginRegistrar> *)registrar {
    self = [super init];
    if (self) {
        _registrar = registrar;
    }
    return self;
}

- (NSObject<FlutterMessageCodec>*)createArgsCodec {
    return [FlutterStandardMessageCodec sharedInstance];
}

- (NSObject<FlutterPlatformView> *)createWithFrame:(CGRect)frame viewIdentifier:(int64_t)viewId arguments:(id)args {
    return [[SJMSplashAdView alloc] initWithFrame:frame viewIdentifier:viewId arguments:args registrar:_registrar];
}
@end
