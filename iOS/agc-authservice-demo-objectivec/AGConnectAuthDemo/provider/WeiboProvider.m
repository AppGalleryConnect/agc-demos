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

- (void)startUp {
    [WeiboSDK registerApp:@"WEIBO_APP_KEY"];
    [WeiboSDK enableDebugMode:YES];
}

- (BOOL)application:(id)app openURL:(id)url options:(id)options {
    return [WeiboSDK handleOpenURL:url delegate:self];
}

- (void)fetchCredentialWithController:(UIViewController *)vc completion:(CredentialBlock)completion {
    self.credentialBlock = completion;
    WBAuthorizeRequest *request = [[WBAuthorizeRequest alloc] init];
    request.redirectURI = @"https://api.weibo.com/oauth2/default.html";
    request.scope = @"all";
    [WeiboSDK sendRequest:request];
}

#pragma mark - WeiboSDKDelegate
- (void)didReceiveWeiboRequest:(WBBaseRequest *)request {
    
}

- (void)didReceiveWeiboResponse:(WBBaseResponse *)response {
    WBAuthorizeResponse *weiboAuth = (WBAuthorizeResponse *)response;
    AGCAuthCredential *credential = [AGCWeiboAuthProvider credentialWithToken:weiboAuth.accessToken uid:weiboAuth.userID];
    if (self.credentialBlock) {
        self.credentialBlock(credential);
    }
}


@end
