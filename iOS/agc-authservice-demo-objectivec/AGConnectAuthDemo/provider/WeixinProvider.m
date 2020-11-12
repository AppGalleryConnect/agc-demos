//
//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved
//

#import "WeixinProvider.h"
#import <AGConnectAuth/AGConnectAuth.h>
#import <AGConnectCredential/AGConnectCredential.h>
#import "UserInfoViewController.h"

@interface WeixinProvider ()

@end

@implementation WeixinProvider

+ (instancetype)sharedInstance {
    static WeixinProvider * sharedInstance;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedInstance = [[self alloc] init];
    });
    return sharedInstance;
}

- (void)registerApp:(UIApplication *)application options:(NSDictionary *)launchOptions {
    [WXApi registerApp:@"WEIXIN_APP_ID" universalLink:@"WEIXIN_UNIVERSAL_LINK"];
}

- (BOOL)handleOpenUniversalLink:(NSUserActivity *)userActivity {
    return [WXApi handleOpenUniversalLink:userActivity delegate:self];
}

- (BOOL)application:(id)app openURL:(id)url options:(id)options {
    return [WXApi handleOpenURL:url delegate:self];
}

- (void)loginWithViewController:(id<SignInDelegate>)delegate {
    self.signInDelegate = delegate;
    SendAuthReq *req = [[SendAuthReq alloc] init];
    req.scope = @"snsapi_userinfo";
    req.state = @"none";
    [WXApi sendAuthReq:req viewController:(UIViewController *)delegate delegate:self completion:^(BOOL success) {
        
    }];
}

- (void)linkWithViewController:(UIViewController *)viewController{
    self.isLink = YES;
    SendAuthReq *req = [[SendAuthReq alloc] init];
    req.scope = @"snsapi_userinfo";
    req.state = @"none";
    [WXApi sendAuthReq:req viewController:viewController delegate:self completion:^(BOOL success) {
        
    }];
}

- (void)unlink {
    [[[[[AGCAuth getInstance] currentUser] unlink:AGCAuthProviderTypeWeiXin] addOnSuccessCallback:^(AGCSignInResult * _Nullable result) {
        [ToastUtil showToast:@"unlink success"];
    }] addOnFailureCallback:^(NSError * _Nonnull error) {
        [ToastUtil showToast:@"unlink failed"];
    }];
}

#pragma mark - WXApiDelegate
- (void)onReq:(BaseReq*)req {
    
}

- (void)onResp:(BaseResp*)resp {
    if ([resp isKindOfClass:[SendAuthResp class]]) {
        SendAuthResp *authResp = (SendAuthResp *)resp;
        NSString *url = [NSString stringWithFormat:@"https://api.weixin.qq.com/sns/oauth2/access_token?appid=wxee9b4cbba326bd96&secret=abc2ea29ec6a0d0f4f48e6d22f5e7d42&code=%@&grant_type=authorization_code",authResp.code];
        NSURLRequest *request = [[NSURLRequest alloc] initWithURL:[NSURL URLWithString:url]];
        [[[[AGCBackend sharedInstance] dataTaskWithRequest:request] addOnSuccessCallback:^(id  _Nullable result) {
            NSError *error = nil;
            NSDictionary *body = [NSJSONSerialization JSONObjectWithData:result options:0 error:&error];
            if (error || !body || ![body isKindOfClass:[NSDictionary class]]) {
                NSLog(@"WeiXin faied");
                return ;
            }
            NSString *accessToken = body[@"access_token"];
            NSString *openid = body[@"openid"];
            if (!accessToken || !openid) {
                NSLog(@"WeiXin faied");
                return ;
            }
            
            AGCAuthCredential *credential = [AGCWeiXinAuthProvider credentialWithToken:accessToken openId:openid];
            if (self.isLink) {
                [self linkWithCredential:credential];
                self.isLink = NO;
            }else {
                [self signInWithCredential:credential];
            }
            
        }] addOnFailureCallback:^(NSError * _Nonnull error) {
            
        }];
    }
}


@end
