//
//  SJMFullVideoAdHelper.m
//  adsjmsdk
//
//  Created by Allen on 2023/4/25.
//

#import "SJMFullVideoAdHelper.h"

@interface SJMFullVideoAdHelper()<LUYFullScreenVideoAdDelegate>

@property(nonatomic,strong) LUYFullScreenVideoAd *fullAd;

@end

@implementation SJMFullVideoAdHelper

//@"SJM_I001",@"SJM_I101"
-(void)showAdWithAdId:(NSString *)adId{
    self.fullAd = [[LUYFullScreenVideoAd alloc]initWithSlotID:adId];
    self.fullAd.delegate = self;
    [self.fullAd loadAd];
}

///全屏视频广告加载成功
- (void)luy_fullScreenVideoAdDidLoad:(LUYFullScreenVideoAd *)fullScreenVideoAd {
    if (self.sjmCallback) {
        self.sjmCallback(SjmAd_Loaded,fullScreenVideoAd,nil);
    }
    if (self.sjmCallback) {
        self.sjmCallback(SjmAd_VideoCached,fullScreenVideoAd,nil);
    }
    [self.fullAd showAdInViewController:[self findCurrentShowingViewController]];
}
///全屏视频广告加载失败
- (void)luy_fullScreenVideoAdLoadFail:(LUYFullScreenVideoAd *)fullScreenVideoAd
                                error:(NSError * __nullable)error {
    if (self.sjmCallback) {
        self.sjmCallback(SjmAd_Error,fullScreenVideoAd,error);
    }
}
///全屏视频广告展示成功
- (void)luy_fullScreenVideoAdDidShow:(LUYFullScreenVideoAd *)fullScreenVideoAd {
    if (self.sjmCallback) {
        self.sjmCallback(SjmAd_Show,fullScreenVideoAd,nil);
    }
}
///全屏视频广告点击
- (void)luy_fullScreenVideoAdDidClick:(LUYFullScreenVideoAd *)fullScreenVideoAd {
    if (self.sjmCallback) {
        self.sjmCallback(SjmAd_Clicked,fullScreenVideoAd,nil);
    }
}
///全屏视频广告关闭
- (void)luy_fullScreenVideoAdClosed:(LUYFullScreenVideoAd *)fullScreenVideoAd {
    if (self.sjmCallback) {
        self.sjmCallback(SjmAd_Closed,fullScreenVideoAd,nil);
    }
}
///全屏视频广告详情页即将打开
- (void)luy_fullScreenVideoAdWillPresentFullScreen:(LUYFullScreenVideoAd *)fullScreenVideoAd {
    if (self.sjmCallback) {
        self.sjmCallback(SjmAd_DetailShow,fullScreenVideoAd,nil);
    }
}
///全屏视频广告详情页即将关闭
- (void)luy_fullScreenVideoAdWillDismissFullScreen:(LUYFullScreenVideoAd *)fullScreenVideoAd {
    if (self.sjmCallback) {
        self.sjmCallback(SjmAd_DetailClosed,fullScreenVideoAd,nil);
    }
}
///全屏视频广告错误
- (void)luy_fullScreenVideoAdFail:(LUYFullScreenVideoAd *)fullScreenVideoAd
                            error:(NSError * __nullable)error {
    if (self.sjmCallback) {
        self.sjmCallback(SjmAd_Error,fullScreenVideoAd,error);
    }
}



// /**
//  广告播放状态回调
//  */
//- (void)SJM_fullScreenVideoAd:(SJMFullScreenVideoAd *)ad  playerStatusChanged:(SJMMediaPlayerStatus)playerStatus {
//    if(playerStatus ==  SJMMediaPlayerStatusStoped) {
//        if (self.sjmCallback) {
//            self.sjmCallback(SjmAd_VideoComplete,ad,nil);
//        }
//    }
//}



@end
