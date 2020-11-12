//
//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved
//

#import "BaseProvider.h"

NS_ASSUME_NONNULL_BEGIN

@interface PhoneProvider : BaseProvider

+ (instancetype)sharedInstance;

- (void)loginWithCountryCode:(NSString *)countryCode nationNumber:(NSString *)nationNumber password:(NSString *)password verifyCode:(NSString *)code;

- (void)linkWithCountryCode:(NSString *)countryCode nationNumber:(NSString *)nationNumber password:(NSString *)password verifyCode:(NSString *)code;

- (void)sendVerifyCodeForRegisterLogin:(NSString *)countryCode nationNumber:(NSString *)nationNumber;

- (void)sendVerifyCodeForResetPassword:(NSString *)countryCode nationNumber:(NSString *)nationNumber;

- (void)createPhoneUserWithCountryCode:(NSString *)countryCode nationNumber:(NSString *)nationNumber password:(NSString *)password verifyCode:(NSString *)code;

- (void)updatePassword:(NSString *)password verifyCode:(NSString *)code;

- (void)updatePhoneWithCountryCode:(NSString *)countryCode nationNumber:(NSString *)nationNumber verifyCode:(NSString *)code;

@end

NS_ASSUME_NONNULL_END
