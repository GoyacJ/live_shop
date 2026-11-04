//
//  SJMRewardVideoHelper.h
//  Runner
//


#import "SJMBaseAdFlutter.h"

NS_ASSUME_NONNULL_BEGIN

@interface SJMRewardVideoHelper : SJMBaseAdFlutter

-(void)showAdWithAdId:(NSString *)adId userId:(NSString *)userId;

@property (nonatomic,copy)void(^sjmCallback)(NSString *event, LUYRewardVideoAd * _Nullable ad, NSError * _Nullable error);



@end

NS_ASSUME_NONNULL_END
