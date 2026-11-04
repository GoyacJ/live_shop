//
//  SJMRewardVideoHelper.m
//  Runner
//


#import "SJMRewardVideoHelper.h"

@interface SJMRewardVideoHelper()<LUYRewardVideoAdDelegate>
@property(nonatomic,strong) LUYRewardVideoAd *rewardVideoAd;

@end

@implementation SJMRewardVideoHelper
//@"SJM_R001",@"SJM_R002",@"SJM_R003",@"SJM_R004"
-(void)showAdWithAdId:(NSString *)adId userId:(NSString *)userId{
    if ([userId isKindOfClass:NSNumber.class]) {
        userId = ((NSNumber *)userId).stringValue;
    }
    self.rewardVideoAd = [[LUYRewardVideoAd alloc] initWithSlotID:adId userId:userId];
    self.rewardVideoAd.userId = userId;
    self.rewardVideoAd.delegate = self;
    [self.rewardVideoAd loadAd];
}

///激励视频广告加载成功
- (void)luy_rewardVideoAdDidLoad:(LUYRewardVideoAd *)rewardedVideoAd {
    if(self.sjmCallback) {
        self.sjmCallback(SjmAd_Loaded, rewardedVideoAd, nil);
    }
}
///激励视频广告下载成功
- (void)luy_rewardVideoAdVideoDidLoad:(LUYRewardVideoAd *)rewardedVideoAd {
    if(self.sjmCallback) {
        self.sjmCallback(SjmAd_VideoCached, rewardedVideoAd, nil);
    }
    [self.rewardVideoAd showAdInViewController:[self findCurrentShowingViewController]];
}
///激励视频广告展示
- (void)luy_rewardVideoAdDidShow:(LUYRewardVideoAd *)rewardedVideoAd {
    if(self.sjmCallback) {
        self.sjmCallback(SjmAd_Show, rewardedVideoAd, nil);
    }
}
///激励视频广告关闭
- (void)luy_rewardVideoAdClosed:(LUYRewardVideoAd *)rewardedVideoAd {
    if(self.sjmCallback) {
        self.sjmCallback(SjmAd_Closed, rewardedVideoAd, nil);
    }
}
///激励视频广告点击
- (void)luy_rewardVideoAdDidClick:(LUYRewardVideoAd *)rewardedVideoAd {
    if(self.sjmCallback) {
        self.sjmCallback(SjmAd_Clicked, rewardedVideoAd, nil);
    }
}
///激励视频广告触发奖励
- (void)luy_rewardVideoAdDidRewardEffective:(LUYRewardVideoAd *)rewardedVideoAd {
    if(self.sjmCallback) {
        self.sjmCallback(SjmAd_Reward, rewardedVideoAd, nil);
    }
    if(self.sjmCallback) {
        self.sjmCallback(SjmAd_TradeId, rewardedVideoAd, nil);
    }
}

///激励视频广告播放完成
- (void)luy_rewardVideoAdDidPlayFinish:(LUYRewardVideoAd *)rewardedVideoAd {
    if(self.sjmCallback) {
        self.sjmCallback(SjmAd_VideoComplete, rewardedVideoAd, nil);
    }
}

///激励视频广告错误
- (void)luy_rewardVideoAdFail:(LUYRewardVideoAd *)rewardedVideoAd
                        error:(NSError *)error {
    if(self.sjmCallback) {
        self.sjmCallback(SjmAd_Error, rewardedVideoAd, error);
    }
}





@end
