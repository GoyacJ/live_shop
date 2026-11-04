//
//  SJMAdIntercptPenetrateView.h
//  adsjmsdk
//
//  Created by Mickey on 2024/4/23.
//

#import <UIKit/UIKit.h>
#import <Flutter/Flutter.h>

NS_ASSUME_NONNULL_BEGIN

@interface SJMAdIntercptPenetrateView : UIView

- (instancetype)initWithFrame:(CGRect)frame registrar:(NSObject<FlutterPluginRegistrar> *)registrar;


- (void)updateIsPermeableWithAd:(UIView *)adView forcePermeable:(BOOL)forcePermeable;


@end

NS_ASSUME_NONNULL_END
