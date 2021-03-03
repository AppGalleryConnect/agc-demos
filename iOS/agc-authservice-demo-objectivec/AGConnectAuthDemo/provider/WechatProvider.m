/*
    Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

#import "WechatProvider.h"
#import <AGConnectCredential/AGConnectCredential.h>

@interface WechatProvider ()

@end

@implementation WechatProvider

+ (instancetype)sharedInstance {
    static WechatProvider * sharedInstance;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedInstance = [[self alloc] init];
    });
    return sharedInstance;
}

- (void)startUp {
    [WXApi registerApp:@"WEIXIN_APP_ID" universalLink:@"WEIXIN_UNIVERSAL_LINK"];
}

- (BOOL)handleOpenUniversalLink:(NSUserActivity *)userActivity {
    return [WXApi handleOpenUniversalLink:userActivity delegate:self];
}

- (BOOL)application:(id)app openURL:(id)url options:(id)options {
    return [WXApi handleOpenURL:url delegate:self];
}

- (void)fetchCredentialWithController:(UIViewController *)vc completion:(CredentialBlock)completion {
    self.credentialBlock = completion;
    SendAuthReq *req = [[SendAuthReq alloc] init];
    req.scope = @"snsapi_userinfo";
    req.state = @"none";
    [WXApi sendAuthReq:req viewController:vc delegate:self completion:nil];
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
            if (self.credentialBlock) {
                self.credentialBlock(credential);
            }
            
        }] addOnFailureCallback:^(NSError * _Nonnull error) {
            if (self.credentialBlock) {
                self.credentialBlock(nil);
            }
        }];
    }
}


@end
