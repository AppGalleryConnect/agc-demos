//
//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved
//

#import "BaseProvider.h"

NS_ASSUME_NONNULL_BEGIN

@interface EmailProvider : BaseProvider

+ (instancetype)sharedInstance;

- (void)loginWithEmail:(NSString *)email password:(NSString *)password verifyCode:(NSString *)code;

- (void)linkWithEmail:(NSString *)email password:(NSString *)password verifyCode:(NSString *)code;

- (void)sendVerifyCodeForRegisterLogin:(NSString *)email;

- (void)sendVerifyCodeForResetPassword:(NSString *)email;

- (void)createUserWithEmail:(NSString *)email password:(NSString *)password verifyCode:(NSString *)code;

- (void)updatePassword:(NSString *)password verifyCode:(NSString *)code;

- (void)updateEmail:(NSString *)email verifyCode:(NSString *)code;

@end

NS_ASSUME_NONNULL_END
