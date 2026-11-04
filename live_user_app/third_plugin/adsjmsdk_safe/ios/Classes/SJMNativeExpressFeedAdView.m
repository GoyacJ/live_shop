//
//  SJMNativeExpressFeedAdView.m
//  sjmadplugin
//


#import "SJMNativeExpressFeedAdView.h"
#import <LUYSDK/LUYSDK.h>
#import "SJMAdIntercptPenetrateView.h"
@interface SJMNativeExpressFeedAdView()<FlutterStreamHandler,LUYNativeExpressAdManagerDelegate,LUYNativeExpressAdDelegate>
@property (nonatomic,strong)LUYNativeExpressAdManager *feedAd;

@property (nonatomic, strong) SJMAdIntercptPenetrateView *containerView;

@property (nonatomic,strong)NSArray <LUYNativeExpressAd *>*adArray;

@end

@implementation SJMNativeExpressFeedAdView
- (instancetype)initWithFrame:(CGRect)frame
               viewIdentifier:(int64_t)viewId
                    arguments:(id _Nullable)args
                    registrar:(NSObject<FlutterPluginRegistrar> *)registrar{
    if (self = [super init]) {
        
        // 事件通道
        NSString *channelName = [NSString stringWithFormat:@"flutter_adsjm_plugin/native_express_event_%lld", viewId];
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
        _feedAd = [[LUYNativeExpressAdManager alloc] initWithSlotID:adId adSize:CGSizeMake(adWidth, adHeight)];
        _feedAd.delegate = self;
        [_feedAd loadAdWithCount:1];
       

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



#pragma mark - LUYNativeExpressAdManagerDelegate

//信息流广告加载成功
- (void)luy_nativeExpressAdDidLoad:(LUYNativeExpressAdManager *)nativeExpressAd
                  nativeExpressAds:(NSArray<LUYNativeExpressAd *> *_Nullable)nativeExpressAds {
    self.adArray = nativeExpressAds;
    [self callbackWithEvent:SjmAd_Loaded otherDic:nil error:nil];
    if (self.adArray.count > 0) {
        LUYNativeExpressAd *feedAd = self.adArray.firstObject;
        feedAd.rootViewController = [self findCurrentShowingViewController];
        feedAd.delegate = self;
        [feedAd render];
    }
}

///信息流广告加载失败
- (void)luy_nativeExpressAdLoadFail:(LUYNativeExpressAdManager *)nativeExpressAd
                              error:(NSError *_Nullable)error {
    if (self.containerView.subviews.count > 0) {
        [self.containerView.subviews makeObjectsPerformSelector:@selector(removeFromSuperview)];
    }
//    [self.containerView removeFromSuperview];
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.25 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        [self callbackWithEvent:SjmAd_Error otherDic:nil error:error];
    });
}



#pragma mark - LUYNativeExpressAdDelegate
/// 广告渲染成功
- (void)luy_nativeExpressAdRenderSuccess:(LUYNativeExpressAd *)expressAd {
    expressAd.nativeExpressAdView.frame = CGRectMake(0, 0, expressAd.nativeExpressAdView.frame.size.width, expressAd.nativeExpressAdView.frame.size.height);
    
    [self.containerView addSubview:expressAd.nativeExpressAdView];
    [self callbackWithEvent:SjmAd_RenderSuccess otherDic:nil error:nil];
    
    [self.containerView updateIsPermeableWithAd:expressAd.nativeExpressAdView forcePermeable:(self.feedAd.pType == LUYPlatformType_KS||self.feedAd.pType == LUYPlatformType_TT)];
}

/// 广告渲染失败
- (void)luy_nativeExpressAdRenderFail:(LUYNativeExpressAd *)expressAd {
    if (self.containerView.subviews.count > 0) {
        [self.containerView.subviews makeObjectsPerformSelector:@selector(removeFromSuperview)];
    }
//    [self.containerView removeFromSuperview];
    [self callbackWithEvent:SjmAd_Error otherDic:nil error:[NSError errorWithDomain:@"SJMAdError" code:12000 userInfo:@{@"info":@"广告渲染失败"}]];
}

/// 广告即将展示
- (void)luy_nativeExpressAdWillShow:(LUYNativeExpressAd *)expressAd {
    [self callbackWithEvent:SjmAd_Show otherDic:nil error:nil];
}

///广告点击
- (void)luy_nativeExpressAdDidClick:(LUYNativeExpressAd *)expressAd {
    [self callbackWithEvent:SjmAd_Clicked otherDic:nil error:nil];
}

///关闭该广告
- (void)luy_nativeExpressAdDislike:(LUYNativeExpressAd *)expressAd {
    [self callbackWithEvent:SjmAd_Closed otherDic:nil error:nil];
}

///展示广告详情页
- (void)luy_nativeExpressAdDidPresentFullScreen:(LUYNativeExpressAd *)expressAd {
    [self callbackWithEvent:SjmAd_DetailShow otherDic:nil error:nil];
}

///关闭广告详情页
- (void)luy_nativeExpressAdDidDismissFullScreen:(LUYNativeExpressAd *)expressAd {
    [self callbackWithEvent:SjmAd_DetailClosed otherDic:nil error:nil];
}

@end


@interface SJMNativeExpressFeedAdViewFactory()
@property (nonatomic, strong) NSObject<FlutterPluginRegistrar> *registrar;
@end

@implementation SJMNativeExpressFeedAdViewFactory
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
    return [[SJMNativeExpressFeedAdView alloc] initWithFrame:frame viewIdentifier:viewId arguments:args registrar:_registrar];
}

@end
