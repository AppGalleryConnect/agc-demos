//
//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved
//

#import "WeiboProvider.h"

@implementation WeiboProvider

+ (instancetype)sharedInstance {
    static WeiboProvider * sharedInstance;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedInstance = [[self alloc] init];
    });
    return sharedInstance;
}

- (void)registerApp:(UIApplication *)application options:(NSDictionary *)launchOptions {
    [WeiboSDK registerApp:@"WEIBO_APP_KEY"];
    [WeiboSDK enableDebugMode:YES];
}

- (BOOL)application:(id)app openURL:(id)url options:(id)options {
    return [WeiboSDK handleOpenURL:url delegate:self];
}

- (void)loginWithViewController:(id<SignInDelegate>)delegate {
    self.signInDelegate = delegate;
    
    WBAuthorizeRequest *request = [[WBAuthorizeRequest alloc] init];
    request.redirectURI = @"https://api.weibo.com/oauth2/default.html";
    request.scope = @"all";
    [WeiboSDK sendRequest:request];
}

- (void)linkWithViewController:(UIViewController *)viewController {
    self.isLink = YES;
    
    WBAuthorizeRequest *request = [[WBAuthorizeRequest alloc] init];
    request.redirectURI = @"https://api.weibo.com/oauth2/default.html";
    request.scope = @"all";
    [WeiboSDK sendRequest:request];
}

- (void)unlink {
    [[[[[AGCAuth getInstance] currentUser] unlink:AGCAuthProviderTypeWeiBo] addOnSuccessCallback:^(AGCSignInResult * _Nullable result) {
                [ToastUtil showToast:@"unlink success"];
            }] addOnFailureCallback:^(NSError * _Nonnull error) {
                [ToastUtil showToast:@"unlink failed"];
            }];
}

#pragma mark - WeiboSDKDelegate
- (void)didReceiveWeiboRequest:(WBBaseRequest *)request {
    
}

- (void)didReceiveWeiboResponse:(WBBaseResponse *)response {
    
    WBAuthorizeResponse *weiboAuth = (WBAuthorizeResponse *)response;
    AGCAuthCredential *credential = [AGCWeiboAuthProvider credentialWithToken:weiboAuth.accessToken uid:weiboAuth.userID];
    if (self.isLink) {
        [self linkWithCredential:credential];
        self.isLink = NO;
    }else {
        [self signInWithCredential:credential];
    }
}


@end
