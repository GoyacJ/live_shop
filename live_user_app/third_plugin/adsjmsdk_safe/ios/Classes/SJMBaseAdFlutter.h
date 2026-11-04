//
//  SJMBaseAdFlutter.h
//  adsjmsdk
//
//  Created by Allen on 2023/4/24.
//

#import <Foundation/Foundation.h>
#import "NSError+SJMError.h"
#import <Flutter/Flutter.h>
#import <LUYSDK/LUYSDK.h>

NS_ASSUME_NONNULL_BEGIN

#define SjmAd_Loaded @"onSjmAdLoaded"
#define SjmAd_Show @"onSjmAdShow"
#define SjmAd_Clicked @"onSjmAdClicked"
#define SjmAd_Closed @"onSjmAdClosed"
#define SjmAd_Error @"onSjmAdError"
#define SjmAd_DetailClosed @"onSjmAdDetailClosed"
#define SjmAd_DetailShow @"onSjmAdDetailShow"

#define SjmAd_VideoCached @"onSjmAdVideoCached"

#define SjmAd_Reward @"onSjmAdReward"
#define SjmAd_TradeId @"onSjmAdTradeId"
#define SjmAd_VideoComplete @"onSjmAdVideoComplete"

#define SjmAd_RenderSuccess @"onSjmAdRenderSuccess"
#define SjmAd_RenderFail @"onSjmAdRenderFail"



@interface SJMBaseAdFlutter : NSObject

- (UIViewController *)findCurrentShowingViewController;

@property (nonatomic, strong) FlutterResult adCallback;

@property (nonatomic, copy) NSString *adId;

/**回调事件*/
-(void)callbackWithEvent:(NSString *)event otherDic:(nullable NSDictionary *)otherDic error:(nullable NSError *)error;
@end

NS_ASSUME_NONNULL_END
