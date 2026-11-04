//
//  SJMFullVideoAdHelper.h
//  adsjmsdk
//
//  Created by Allen on 2023/4/25.
//

#import "SJMBaseAdFlutter.h"

NS_ASSUME_NONNULL_BEGIN

@interface SJMFullVideoAdHelper : SJMBaseAdFlutter

-(void)showAdWithAdId:(NSString *)adId;

@property (nonatomic,copy)void(^sjmCallback)(NSString *event, LUYFullScreenVideoAd * _Nullable ad, NSError * _Nullable error);

@end

NS_ASSUME_NONNULL_END
