//
//  SJMInterstitialAdHelper.m
//  Runner
//


#import "SJMInterstitialAdHelper.h"

@interface SJMInterstitialAdHelper()<LUYInterstitialAdDelegate>

@property(nonatomic,strong) LUYInterstitialAd *interstitialAd;

@end

@implementation SJMInterstitialAdHelper
//@"SJM_I001",@"SJM_I101"
-(void)showAdWithAdId:(NSString *)adId{
    self.interstitialAd = [[LUYInterstitialAd alloc]initWithSlotID:adId];
    self.interstitialAd.delegate = self;
    [self.interstitialAd loadAd];
}

///插屏广告加载成功
- (void)luy_interstitialAdDidLoad:(LUYInterstitialAd *)interstitialAd {
    if (self.sjmCallback) {
        self.sjmCallback(SjmAd_Loaded,interstitialAd,nil);
    }
    [self.interstitialAd showAdInViewController:[self findCurrentShowingViewController]];
}
///插屏广告加载失败
- (void)luy_interstitialAdLoadFail:(LUYInterstitialAd *)interstitialAd
                             error:(NSError * __nullable)error {
    if (self.sjmCallback) {
        self.sjmCallback(SjmAd_Error,interstitialAd,error);
    }
}
///插屏广告展示成功
- (void)luy_interstitialAdDidShow:(LUYInterstitialAd *)interstitialAd {
    if (self.sjmCallback) {
        self.sjmCallback(SjmAd_Show,interstitialAd,nil);
    }
}
///插屏广告点击
- (void)luy_interstitialAdDidClick:(LUYInterstitialAd *)interstitialAd {
    if (self.sjmCallback) {
        self.sjmCallback(SjmAd_Clicked,interstitialAd,nil);
    }
}
///插屏广告关闭
- (void)luy_interstitialAdClosed:(LUYInterstitialAd *)interstitialAd {
    if (self.sjmCallback) {
        self.sjmCallback(SjmAd_Closed,interstitialAd,nil);
    }
}
///插屏广告详情页即将打开
- (void)luy_interstitialAdWillPresentFullScreen:(LUYInterstitialAd *)interstitialAd {
    if (self.sjmCallback) {
        self.sjmCallback(SjmAd_DetailShow,interstitialAd,nil);
    }
}
///插屏广告详情页即将关闭
- (void)luy_interstitialAdWillDismissFullScreen:(LUYInterstitialAd *)interstitialAd {
    if (self.sjmCallback) {
        self.sjmCallback(SjmAd_DetailClosed,interstitialAd,nil);
    }
}
///插屏广告错误
- (void)luy_interstitialAdFail:(LUYInterstitialAd *)interstitialAd
                         error:(NSError * __nullable)error {
    if (self.sjmCallback) {
        self.sjmCallback(SjmAd_Error,interstitialAd,error);
    }
}

@end
