//
//  SJMSplashAdHelper.h
//  Runner
//


#import "SJMBaseAdFlutter.h"

NS_ASSUME_NONNULL_BEGIN

@interface SJMSplashAdHelper : SJMBaseAdFlutter

@property (nonatomic,strong)LUYSplashAd *splashAd;

-(void)showAdWithAdId:(NSString *)adId fetchDelay:(NSInteger)fetchDelay;

@property (nonatomic,copy)void(^splashAdCallback)(NSString *event, LUYSplashAd * _Nullable ad, NSError * _Nullable error);


@end

NS_ASSUME_NONNULL_END
