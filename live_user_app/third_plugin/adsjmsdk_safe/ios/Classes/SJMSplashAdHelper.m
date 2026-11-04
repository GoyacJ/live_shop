//
//  SJMSplashAdHelper.m
//  Runner
//


#import "SJMSplashAdHelper.h"

@interface SJMSplashAdHelper()<LUYSplashAdDelegate>

@end

@implementation SJMSplashAdHelper
//@"SJM_S001",@"SJM_S002",@"SJM_S003",@"SJM_S005"
-(void)showAdWithAdId:(NSString *)adId fetchDelay:(NSInteger)fetchDelay{
    _splashAd = [[LUYSplashAd alloc]initWithSlotID:adId];
    _splashAd.tolerateTimeout = fetchDelay;
    _splashAd.delegate = self;
    [_splashAd loadAd];
}

/// 开屏广告加载成功
- (void)luy_splashAdDidLoad:(LUYSplashAd *)splashAd {
    [_splashAd showAdInWindow:[UIApplication sharedApplication].keyWindow];
    if (self.splashAdCallback) {
        self.splashAdCallback(SjmAd_Loaded,splashAd,nil);
    }
}

///开屏广告展示
- (void)luy_splashAdDidShow:(LUYSplashAd *)splashAd {
    if (self.splashAdCallback) {
        self.splashAdCallback(SjmAd_Show,splashAd,nil);
    }
}

///开屏广告点击回调
- (void)luy_splashAdClicked:(LUYSplashAd *)splashAd {
    if (self.splashAdCallback) {
        self.splashAdCallback(SjmAd_Clicked,splashAd,nil);
    }
}

///开屏广告关闭回调
- (void)luy_splashAdClosed:(LUYSplashAd *)splashAd {
    if (self.splashAdCallback) {
        self.splashAdCallback(SjmAd_Closed,splashAd,nil);
    }
}

///开屏广告倒记时结束
- (void)luy_splashAdCountdownToZero:(LUYSplashAd*)splashAd {
    if (self.splashAdCallback) {
        self.splashAdCallback(@"onSjmAdCountdown",splashAd,nil);
    }
}

///开屏广告错误
- (void)luy_splashAdLoadFail:(LUYSplashAd *)splashAd error:(NSError *)error {
    if (self.splashAdCallback) {
        self.splashAdCallback(SjmAd_Error,splashAd,error);
    }
}

///开屏广告获取超时
- (void)luy_splashAdLoadFailWithTimeout:(LUYSplashAd *)splashAd {
    if (self.splashAdCallback) {
        self.splashAdCallback(SjmAd_Error,splashAd,[NSError errorWithDomain:@"SJMAdTimeout" code:19999 userInfo:@{@"info":@"开屏广告获取超时"}]);
    }
}






@end
