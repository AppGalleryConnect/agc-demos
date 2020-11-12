//
//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved
//

#import "PhoneProvider.h"
#import <AGConnectAuth/AGConnectAuth.h>

@implementation PhoneProvider

+ (instancetype)sharedInstance {
    static PhoneProvider * sharedInstance;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedInstance = [[self alloc] init];
    });
    return sharedInstance;
}

- (void)loginWithCountryCode:(NSString *)countryCode nationNumber:(NSString *)nationNumber password:(NSString *)password verifyCode:(NSString *)code {
    AGCAuthCredential *credential;
    if (code.length == 0) {
        credential = [AGCPhoneAuthProvider credentialWithCountryCode:countryCode phoneNumber:nationNumber password:password];
    }else {
        credential = [AGCPhoneAuthProvider credentialWithCountryCode:countryCode phoneNumber:nationNumber password:password verifyCode:code];
    }
    [[[[AGCAuth getInstance] signIn:credential] addOnSuccessCallback:^(AGCSignInResult * _Nullable result) {
        NSLog(@"Phone login success : %@",[result description]);
        if (self.signInDelegate) {
            [self.signInDelegate signInSucceed];
        }
    }] addOnFailureCallback:^(NSError * _Nonnull error) {
        NSLog(@"Phone login failed : %@",error);
        if (self.signInDelegate) {
            [self.signInDelegate signInFailed];
        }
    }];
}

- (void)linkWithCountryCode:(NSString *)countryCode nationNumber:(NSString *)nationNumber password:(NSString *)password verifyCode:(NSString *)code {
    AGCAuthCredential *credential;
    if (code.length == 0) {
        credential = [AGCPhoneAuthProvider credentialWithCountryCode:countryCode phoneNumber:nationNumber password:password];
    }else {
        credential = [AGCPhoneAuthProvider credentialWithCountryCode:countryCode phoneNumber:nationNumber password:password verifyCode:code];
    }
    [[[[[AGCAuth getInstance] currentUser] link:credential] addOnSuccessCallback:^(AGCSignInResult * _Nullable result) {
            [ToastUtil showToast:@"link success"];
        }] addOnFailureCallback:^(NSError * _Nonnull error) {
            [ToastUtil showToast:@"link failed"];
        }];
}

- (void)unlink {
    [[[[[AGCAuth getInstance] currentUser] unlink:AGCAuthProviderTypePhone] addOnSuccessCallback:^(AGCSignInResult * _Nullable result) {
                [ToastUtil showToast:@"unlink success"];
            }] addOnFailureCallback:^(NSError * _Nonnull error) {
                [ToastUtil showToast:@"unlink failed"];
            }];
}

- (void)sendVerifyCodeForRegisterLogin:(NSString *)countryCode nationNumber:(NSString *)nationNumber  {
    AGCVerifyCodeSettings *setting = [[AGCVerifyCodeSettings alloc] initWithAction:AGCVerifyCodeActionRegisterLogin locale:nil sendInterval:30];
    [[[AGCPhoneAuthProvider requestVerifyCodeWithCountryCode:countryCode phoneNumber:nationNumber settings:setting] addOnSuccessCallback:^(AGCVerifyCodeResult * _Nullable result) {
        [ToastUtil showToast:@"send verification code success"];
    }] addOnFailureCallback:^(NSError * _Nonnull error) {
        [ToastUtil showToast:@"send verification code failed"];
    }];
}

- (void)sendVerifyCodeForResetPassword:(NSString *)countryCode nationNumber:(NSString *)nationNumber  {
    
    AGCVerifyCodeSettings *setting = [[AGCVerifyCodeSettings alloc] initWithAction:AGCVerifyCodeActionResetPassword locale:nil sendInterval:30];
    [[[AGCPhoneAuthProvider requestVerifyCodeWithCountryCode:countryCode phoneNumber:nationNumber settings:setting] addOnSuccessCallback:^(AGCVerifyCodeResult * _Nullable result) {
        [ToastUtil showToast:@"send verification code success"];
    }] addOnFailureCallback:^(NSError * _Nonnull error) {
        [ToastUtil showToast:@"send verification code failed"];
    }];
}

- (void)createPhoneUserWithCountryCode:(NSString *)countryCode nationNumber:(NSString *)nationNumber password:(NSString *)password verifyCode:(NSString *)code {
    [[[[AGCAuth getInstance] createUserWithCountryCode:countryCode phoneNumber:nationNumber password:password verifyCode:code] addOnSuccessCallback:^(AGCSignInResult * _Nullable result) {
            [ToastUtil showToast:@"account register success"];
        }] addOnFailureCallback:^(NSError * _Nonnull error) {
            [ToastUtil showToast:@"account register failed"];
            NSLog(@"Email create failed C %@",error);
        }];
}

- (void)updatePassword:(NSString *)password verifyCode:(NSString *)code {
    [[[[[AGCAuth getInstance] currentUser] updatePassword:password verifyCode:code provider:AGCAuthProviderTypePhone] addOnSuccessCallback:^(id  _Nullable result) {
            [ToastUtil showToast:@"password update success"];
        }] addOnFailureCallback:^(NSError * _Nonnull error) {
            [ToastUtil showToast:@"password update failed"];
            NSLog(@"password update failed : %@",error);
        }];
}

- (void)updatePhoneWithCountryCode:(NSString *)countryCode nationNumber:(NSString *)nationNumber verifyCode:(NSString *)code {
    [[[[[AGCAuth getInstance] currentUser] updatePhoneWithCountryCode:countryCode phoneNumber:nationNumber verifyCode:code] addOnSuccessCallback:^(id  _Nullable result) {
        [ToastUtil showToast:@"phone update success"];
    }] addOnFailureCallback:^(NSError * _Nonnull error) {
        NSLog(@"Phone update failed : %@",error);
        [ToastUtil showToast:@"phone update failed"];
    }];
}

@end
