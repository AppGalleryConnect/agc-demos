//
//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved
//

#import "QQProvider.h"

@implementation QQProvider
{
    TencentOAuth *qq;
}

+ (instancetype)sharedInstance {
    static QQProvider * sharedInstance;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedInstance = [[self alloc] init];
    });
    return sharedInstance;
}

- (void)registerApp:(UIApplication *)application options:(NSDictionary *)launchOptions {
    qq = [[TencentOAuth alloc] initWithAppId:@"QQ_APP_ID" andDelegate:self];
}

- (BOOL)application:(id)app openURL:(id)url options:(id)options {
    return [TencentOAuth HandleOpenURL:url];
}

- (void)loginWithViewController:(id<SignInDelegate>)delegate {
    self.signInDelegate = delegate;
    [qq authorize:@[@"all"]];
}

- (void)linkWithViewController:(UIViewController *)viewController {
    self.isLink = YES;
    [qq authorize:@[@"all"]];
}

- (void)unlink {
    [[[[[AGCAuth getInstance] currentUser] unlink:AGCAuthProviderTypeQQ] addOnSuccessCallback:^(AGCSignInResult * _Nullable result) {
            [ToastUtil showToast:@"unlink success"];
        }] addOnFailureCallback:^(NSError * _Nonnull error) {
            [ToastUtil showToast:@"unlink failed"];
        }];
}

#pragma mark - TencentSessionDelegate

- (void)tencentDidLogin {
    AGCAuthCredential *credential = [AGCQQAuthProvider credentialWithToken:[qq accessToken] openId:[qq openId]];
    if (self.isLink) {
        [self linkWithCredential:credential];
        self.isLink = NO;
    }else {
        [self signInWithCredential:credential];
    }
}

- (void)tencentDidNotLogin:(BOOL)cancelled {
    
}

- (void)tencentDidNotNetWork {
    
}





@end
