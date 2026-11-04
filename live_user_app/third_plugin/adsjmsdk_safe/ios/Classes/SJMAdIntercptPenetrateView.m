//
//  SJMAdIntercptPenetrateView.m
//  adsjmsdk
//
//  Created by Mickey on 2024/4/23.
//

#import "SJMAdIntercptPenetrateView.h"
#import <WebKit/WebKit.h>


@interface SJMAdIntercptPenetrateView ()

@property (nonatomic,strong) UIView *shelterView;

/// 存在穿透问题？
@property (nonatomic, assign) BOOL isPermeable;
///广告是否被覆盖
@property (nonatomic, assign) BOOL isCovered;

///广告的可见区域
@property (nonatomic, assign) CGRect coveredBounds;

@property (nonatomic, assign) BOOL enableDebug;

@end

@implementation SJMAdIntercptPenetrateView

- (BOOL)pointInside:(CGPoint)point withEvent:(UIEvent *)event {
    //NSLog(@"%d,%d,%f,%f,%f,%f",self.isPermeable,self.isCovered,self.coveredBounds.origin.x,self.coveredBounds.origin.y,self.coveredBounds.size.width,self.coveredBounds.size.height);
    if (self.isPermeable) {
        BOOL isContain = CGRectContainsPoint(self.coveredBounds, point);
        //NSLog(@"pointInside:%f,%f (%d)",point.x,point.y, isContain);
        if(_isCovered && isContain) {
            return false;
        }
    }
    return true;
}



- (instancetype)initWithFrame:(CGRect)frame registrar:(NSObject<FlutterPluginRegistrar> *)registrar{
    if (self = [super initWithFrame:frame]) {
        _isPermeable = NO;
        _isCovered = NO;
        _enableDebug = NO;
        
        //Channel
        FlutterMethodChannel *channel = [FlutterMethodChannel methodChannelWithName:@"flutter_adsjm_plugin/webAd_method" binaryMessenger:registrar.messenger];
        __weak typeof(self) weakSelf = self;
        [channel setMethodCallHandler:^(FlutterMethodCall * _Nonnull call, FlutterResult  _Nonnull result) {
            __strong typeof(weakSelf) strongSelf = weakSelf;
            if ([call.method isEqualToString:@"updateCoverBounds"]) {
                //是否被覆盖
                strongSelf.isCovered = ((NSNumber *)[call.arguments valueForKey:@"isCovered"]).boolValue;
                double x = ((NSNumber *)[call.arguments valueForKey:@"x"]).doubleValue;
                double y = ((NSNumber *)[call.arguments valueForKey:@"y"]).doubleValue;
                double w = ((NSNumber *)[call.arguments valueForKey:@"w"]).doubleValue;
                double h = ((NSNumber *)[call.arguments valueForKey:@"h"]).doubleValue;
                strongSelf.coveredBounds = CGRectMake(x, y, w, h);
            }else if ([call.method isEqualToString:@"enableDebug"]) {
                strongSelf.enableDebug = ((NSNumber *)[call.arguments valueForKey:@"enable"]).boolValue;
            }
            [strongSelf debugIntercptPenetrateView];
        }];
    }
    return self;
}


- (void)updateIsPermeableWithAd:(UIView *)adView forcePermeable:(BOOL)forcePermeable{
    if (forcePermeable) {
        self.isPermeable = YES;
    }else {
        if ([adView isKindOfClass:WKWebView.class]) {
            self.isPermeable = YES;
        }else if (adView.subviews.count > 0) {
            UIView *subFirst = adView.subviews.firstObject;
            if ([subFirst isKindOfClass:WKWebView.class]) {
                self.isPermeable = YES;
            }else if (subFirst.subviews.count > 0 && [subFirst.subviews.firstObject isKindOfClass:WKWebView.class]){
                self.isPermeable = YES;
            }else {
                self.isPermeable = NO;
            }
        }else {
            self.isPermeable = NO;
        }
    }
    [self debugIntercptPenetrateView];
}


- (void)debugIntercptPenetrateView {
#if DEBUG
    if (self.enableDebug) {
        [self updateIntercptPenetrateView];
    }else {
        if (_shelterView) {
            [_shelterView removeFromSuperview];
            _shelterView = nil;
        }
    }
#endif
}

- (void)updateIntercptPenetrateView {
    if (self.isPermeable) {
        if(_isCovered) {
            self.shelterView.frame = self.coveredBounds;
            return ;
        }
    }
    [_shelterView removeFromSuperview];
    _shelterView = nil;
}


- (UIView *)shelterView {
    if (!_shelterView) {
        _shelterView = [[UIView alloc] init];
        _shelterView.userInteractionEnabled = NO;
        _shelterView.backgroundColor = [[UIColor blackColor] colorWithAlphaComponent:0.6];
        [self addSubview:_shelterView];
        
    }
    return _shelterView;
}
@end
