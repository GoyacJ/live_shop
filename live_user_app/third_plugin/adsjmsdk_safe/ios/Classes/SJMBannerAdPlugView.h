//
//  SJMBannerAdPlugView.h
//  adsjmsdk
//
//  Created by Allen on 2023/4/25.
//

#import "SJMBaseAdFlutter.h"
#import <Flutter/Flutter.h>

NS_ASSUME_NONNULL_BEGIN

@interface SJMBannerAdPlugView : SJMBaseAdFlutter<FlutterPlatformView>

@end

@interface SJMBannerAdViewFactory : NSObject<FlutterPlatformViewFactory>
- (instancetype)initWithRegistrar:(NSObject<FlutterPluginRegistrar> *)registrar;

@end

NS_ASSUME_NONNULL_END
