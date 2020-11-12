//
//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved
//

#import "SelfBuildProvider.h"
#import <AGConnectAuth/AGConnectAuth.h>

@implementation SelfBuildProvider

+ (instancetype)sharedInstance {
    static SelfBuildProvider * sharedInstance;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedInstance = [[self alloc] init];
    });
    return sharedInstance;
}

- (void)loginWithAccount:(NSString *)account password:(NSString *)password verifyCode:(NSString *)code {
    
    // The developer sends a request to the self-built server, and the self-built server verifies the user information. After the verification is successful, the private key is used to sign the user information, and a JWT token string is generated and returned to the client.
    NSString *jwtToken = @"JWT_TOKEN";
    
    AGCAuthCredential *credential = [AGCSelfBuildAuthProvider credentialWithToken:jwtToken];
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

- (void)linkWithAccount:(NSString *)account password:(NSString *)password verifyCode:(NSString *)code {
    
    // The developer sends a request to the self-built server, and the self-built server verifies the user information. After the verification is successful, the private key is used to sign the user information, and a JWT token string is generated and returned to the client.
    NSString *jwtToken = @"JWT_TOKEN";
    
    AGCAuthCredential *credential = [AGCSelfBuildAuthProvider credentialWithToken:jwtToken];
    [[[[[AGCAuth getInstance] currentUser] link:credential] addOnSuccessCallback:^(AGCSignInResult * _Nullable result) {
            [ToastUtil showToast:@"link success"];
        }] addOnFailureCallback:^(NSError * _Nonnull error) {
            [ToastUtil showToast:@"link failed"];
        }];
}

- (void)unlink {
    [[[[[AGCAuth getInstance] currentUser] unlink:AGCAuthProviderTypeSelfBuild] addOnSuccessCallback:^(AGCSignInResult * _Nullable result) {
                [ToastUtil showToast:@"unlink success"];
            }] addOnFailureCallback:^(NSError * _Nonnull error) {
                [ToastUtil showToast:@"unlink failed"];
            }];
}

@end
