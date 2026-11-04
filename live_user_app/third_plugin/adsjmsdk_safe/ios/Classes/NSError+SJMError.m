//
//  NSError+SJMError.m
//  adsjmsdk
//
//  Created by Allen on 2023/4/24.
//

#import "NSError+SJMError.h"

@implementation NSError (SJMError)


- (NSString *)getErrorString{
    NSMutableDictionary *dic = [NSMutableDictionary dictionary];
    [dic setValue:self.domain forKey:@"domain"];
    [dic setValue:[NSNumber numberWithInteger:self.code] forKey:@"code"];
    if (self.userInfo) {
        [dic setValue:self.userInfo forKey:@"userInfo"];
    }
    return [NSString stringWithFormat:@"错误信息:(%@)",dic];
}

@end
