//
//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved
//

#import "EmailProvider.h"
#import <AGConnectAuth/AGConnectAuth.h>

@implementation EmailProvider

+ (instancetype)sharedInstance {
    static EmailProvider * sharedInstance;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedInstance = [[self alloc] init];
    });
    return sharedInstance;
}

- (void)loginWithEmail:(NSString *)email password:(NSString *)password verifyCode:(NSString *)code {
    AGCAuthCredential *credential;
    if (code.length > 0) {
        credential = [AGCEmailAuthProvider credentialWithEmail:email password:password verifyCode:code];
    }else {
        credential = [AGCEmailAuthProvider credentialWithEmail:email password:password];
    }
    [[[[AGCAuth getInstance] signIn:credential] addOnSuccessCallback:^(AGCSignInResult * _Nullable result) {
        if (self.signInDelegate) {
            [self.signInDelegate signInSucceed];
        }
    }] addOnFailureCallback:^(NSError * _Nonnull error) {
        if (self.signInDelegate) {
            [self.signInDelegate signInFailed];
        }
    }];
}

- (void)linkWithEmail:(NSString *)email password:(NSString *)password verifyCode:(NSString *)code {
    AGCAuthCredential *credential;
    if (code.length > 0) {
        credential = [AGCEmailAuthProvider credentialWithEmail:email password:password verifyCode:code];
    }else {
        credential = [AGCEmailAuthProvider credentialWithEmail:email password:password];
    }
    [[[[[AGCAuth getInstance] currentUser] link:credential] addOnSuccessCallback:^(AGCSignInResult * _Nullable result) {
            [ToastUtil showToast:@"link success"];
        }] addOnFailureCallback:^(NSError * _Nonnull error) {
            [ToastUtil showToast:@"link failed"];
        }];
}

- (void)unlink {
    [[[[[AGCAuth getInstance] currentUser] unlink:AGCAuthProviderTypeEmail] addOnSuccessCallback:^(AGCSignInResult * _Nullable result) {
                [ToastUtil showToast:@"unlink success"];
            }] addOnFailureCallback:^(NSError * _Nonnull error) {
                [ToastUtil showToast:@"unlink failed"];
            }];
}

- (void)sendVerifyCodeForRegisterLogin:(NSString *)email {
    AGCVerifyCodeSettings *setting = [[AGCVerifyCodeSettings alloc] initWithAction:AGCVerifyCodeActionRegisterLogin locale:nil sendInterval:30];
    [[[AGCEmailAuthProvider requestVerifyCodeWithEmail:email settings:setting] addOnSuccessCallback:^(AGCVerifyCodeResult * _Nullable result) {
        [ToastUtil showToast:@"send verification code success"];
    }] addOnFailureCallback:^(NSError * _Nonnull error) {
        [ToastUtil showToast:@"send verification code failed"];
        NSLog(@"send verification code failed : %@",error);
    }];
}

- (void)sendVerifyCodeForResetPassword:(NSString *)email {
    AGCVerifyCodeSettings *setting = [[AGCVerifyCodeSettings alloc] initWithAction:AGCVerifyCodeActionResetPassword locale:nil sendInterval:30];
    [[[AGCEmailAuthProvider requestVerifyCodeWithEmail:email settings:setting] addOnSuccessCallback:^(AGCVerifyCodeResult * _Nullable result) {
        [ToastUtil showToast:@"send verification code success"];
    }] addOnFailureCallback:^(NSError * _Nonnull error) {
        [ToastUtil showToast:@"send verification code failed"];
        NSLog(@"send verification code failed : %@",error);
    }];
}

- (void)createUserWithEmail:(NSString *)email password:(NSString *)password verifyCode:(NSString *)code {
    [[[[AGCAuth getInstance] createUserWithEmail:email password:password verifyCode:code] addOnSuccessCallback:^(AGCSignInResult * _Nullable result) {
        [ToastUtil showToast:@"account register success"];
    }] addOnFailureCallback:^(NSError * _Nonnull error) {
        [ToastUtil showToast:@"account register failed"];
        NSLog(@"Email create failed : %@",error);
    }];
}

- (void)updatePassword:(NSString *)password verifyCode:(NSString *)code {
    [[[[[AGCAuth getInstance] currentUser] updatePassword:password verifyCode:code provider:AGCAuthProviderTypeEmail] addOnSuccessCallback:^(id  _Nullable result) {
        [ToastUtil showToast:@"password update success"];
    }] addOnFailureCallback:^(NSError * _Nonnull error) {
        [ToastUtil showToast:@"password update failed"];
        NSLog(@"password update failed : %@",error);
    }];
}

- (void)updateEmail:(NSString *)email verifyCode:(NSString *)code {
    [[[[[AGCAuth getInstance] currentUser] updateEmail:email verifyCode:code] addOnSuccessCallback:^(id  _Nullable result) {
        [ToastUtil showToast:@"email update success"];
    }] addOnFailureCallback:^(NSError * _Nonnull error) {
        NSLog(@"Email update failed : %@",error);
        [ToastUtil showToast:@"email update failed"];
    }];
}

@end
