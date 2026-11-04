//
//  NSError+SJMError.h
//  adsjmsdk
//
//  Created by Allen on 2023/4/24.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NSError (SJMError)

- (NSString *)getErrorString;

@end

NS_ASSUME_NONNULL_END
