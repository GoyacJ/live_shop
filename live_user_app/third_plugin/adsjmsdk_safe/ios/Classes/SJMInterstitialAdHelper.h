//
//  SJMInterstitialAdHelper.h
//  Runner
//


#import "SJMBaseAdFlutter.h"

NS_ASSUME_NONNULL_BEGIN

@interface SJMInterstitialAdHelper : SJMBaseAdFlutter

-(void)showAdWithAdId:(NSString *)adId;

@property (nonatomic,copy)void(^sjmCallback)(NSString *event, LUYInterstitialAd * _Nullable ad, NSError * _Nullable error);

@end

NS_ASSUME_NONNULL_END
