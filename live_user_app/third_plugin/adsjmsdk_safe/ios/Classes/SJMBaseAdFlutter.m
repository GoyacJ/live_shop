//
//  SJMBaseAdFlutter.m
//  adsjmsdk
//
//  Created by Allen on 2023/4/24.
//

#import "SJMBaseAdFlutter.h"
#import "SJMAdHeader.h"
@implementation SJMBaseAdFlutter

/**回调事件*/
-(void)callbackWithEvent:(NSString *)event otherDic:(NSDictionary *)otherDic error:(NSError *)error{
    if (self.adCallback) {
        NSMutableDictionary *result = [NSMutableDictionary dictionary];
        
        [result setValue:event.length > 0 ?event :@"未知事件" forKey:sjmevent];
        [result setValue:self.adId?:@"错误Id" forKey:sjmid];
        if (otherDic) {
            [result addEntriesFromDictionary:otherDic];
        }
        if (error) {
            [result setObject:@(error.code) forKey:sjmcode];
            [result setObject:[error getErrorString] forKey:sjmmessage];
        }
        self.adCallback(result);
    }else{
        if(error){
            NSLog(@"SJMAdError:%@",error);
        }
    }
}


// 获取当前显示的 UIViewController
- (UIViewController *)findCurrentShowingViewController {
    //获得当前活动窗口的根视图
    UIViewController *vc = [UIApplication sharedApplication].keyWindow.rootViewController;
    UIViewController *currentShowingVC = [self findCurrentShowingViewControllerFrom:vc];
    return currentShowingVC;
}


- (UIViewController *)findCurrentShowingViewControllerFrom:(UIViewController *)vc {
    // 递归方法 Recursive method
    UIViewController *currentShowingVC;
    if ([vc presentedViewController]) {
        // 当前视图是被presented出来的
        UIViewController *nextRootVC = [vc presentedViewController];
        currentShowingVC = [self findCurrentShowingViewControllerFrom:nextRootVC];

    } else if ([vc isKindOfClass:[UITabBarController class]]) {
        // 根视图为UITabBarController
        UIViewController *nextRootVC = [(UITabBarController *)vc selectedViewController];
        currentShowingVC = [self findCurrentShowingViewControllerFrom:nextRootVC];

    } else if ([vc isKindOfClass:[UINavigationController class]]){
        // 根视图为UINavigationController
        UIViewController *nextRootVC = [(UINavigationController *)vc visibleViewController];
        currentShowingVC = [self findCurrentShowingViewControllerFrom:nextRootVC];

    } else {
        // 根视图为非导航类
        currentShowingVC = vc;
    }

    return currentShowingVC;
}

@end
