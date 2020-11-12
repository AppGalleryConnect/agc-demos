//
//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved
//

#import "BaseProvider.h"

NS_ASSUME_NONNULL_BEGIN

@interface SelfBuildProvider : BaseProvider

+ (instancetype)sharedInstance;

- (void)loginWithAccount:(NSString *)account password:(NSString *)password verifyCode:(NSString *)code;

- (void)linkWithAccount:(NSString *)account password:(NSString *)password verifyCode:(NSString *)code;

@end

NS_ASSUME_NONNULL_END
