//
//  SJMBannerAdPlugView.m
//  adsjmsdk
//
//  Created by Allen on 2023/4/25.
//

#import "SJMBannerAdPlugView.h"
#import "SJMAdIntercptPenetrateView.h"

@interface SJMBannerAdPlugView()<FlutterStreamHandler,LUYBannerViewDelegate>
@property (nonatomic,strong)LUYBannerView *bannerView;

@property (nonatomic, strong) SJMAdIntercptPenetrateView *containerView;

@end

@implementation SJMBannerAdPlugView
- (instancetype)initWithFrame:(CGRect)frame
               viewIdentifier:(int64_t)viewId
                    arguments:(id _Nullable)args
                    registrar:(NSObject<FlutterPluginRegistrar> *)registrar{
    if (self = [super init]) {
        // 事件通道
        NSString *channelName = [NSString stringWithFormat:@"flutter_adsjm_plugin/banner_event_%lld", viewId];
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
        _bannerView = [[LUYBannerView alloc] initWithSlotID:adId viewController:[self findCurrentShowingViewController] adSize:CGSizeMake(adWidth, adHeight) autoInterval:60];
        _bannerView.delegate = self;
        [_bannerView loadAdAndShow];
       

        // 容器view
        _containerView = [[SJMAdIntercptPenetrateView alloc] initWithFrame:frame registrar:registrar];
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

#pragma mark - LUYBannerAdViewDelegate

///banner 广告加载成功
- (void)luy_bannerAdDidLoad:(LUYBannerView *)bannerView {
    if(bannerView&&![self.containerView.subviews containsObject:bannerView]){
        CGRect frame = CGRectMake(0, 0, bannerView.adSize.width, bannerView.adSize.height);
        self.bannerView.frame = frame;
        [self.containerView addSubview:self.bannerView];
        
        if (self.bannerView.subviews.count > 0) {
            
            [self.containerView updateIsPermeableWithAd:self.bannerView.subviews.firstObject forcePermeable:(bannerView.pType == LUYPlatformType_KS||bannerView.pType == LUYPlatformType_TT)];
        }
    }
    [self callbackWithEvent:SjmAd_Loaded otherDic:nil error:nil];
}
///banner 广告加载失败
- (void)luy_bannerAdLoadFail:(LUYBannerView *)bannerView
                       error:(NSError *_Nullable)error {
    [self.bannerView removeFromSuperview];
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.25 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        [self callbackWithEvent:SjmAd_Error otherDic:nil error:error];
    });
}
///banner 广告展示成功
- (void)luy_bannerAdWillExpose:(LUYBannerView *)bannerView {
    [self callbackWithEvent:SjmAd_Show otherDic:nil error:nil];
}

///banner 广告关闭回调
- (void)luy_bannerAdWillClose:(LUYBannerView *)bannerView {
    [self.bannerView removeFromSuperview];
    [self callbackWithEvent:SjmAd_Closed otherDic:nil error:nil];
}

///banner 广告点击回调
- (void)luy_bannerAdDidClick:(LUYBannerView *)bannerView {
    [self callbackWithEvent:SjmAd_Clicked otherDic:nil error:nil];
}

///banner广告即将关闭详情页
- (void)luy_bannerAdWillDismissFullScreen:(LUYBannerView *)bannerView {
    [self callbackWithEvent:SjmAd_DetailClosed otherDic:nil error:nil];
}


@end


@interface SJMBannerAdViewFactory()
@property (nonatomic, strong) NSObject<FlutterPluginRegistrar> *registrar;
@end

@implementation SJMBannerAdViewFactory
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
    return [[SJMBannerAdPlugView alloc] initWithFrame:frame viewIdentifier:viewId arguments:args registrar:_registrar];
}

@end
