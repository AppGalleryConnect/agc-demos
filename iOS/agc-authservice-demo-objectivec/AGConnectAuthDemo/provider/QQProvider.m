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

- (void)startUp {
    qq = [[TencentOAuth alloc] initWithAppId:@"QQ_APP_ID" andDelegate:self];
}

- (BOOL)application:(id)app openURL:(id)url options:(id)options {
    return [TencentOAuth HandleOpenURL:url];
}

- (void)fetchCredentialWithController:(UIViewController *)vc completion:(CredentialBlock)completion {
    self.credentialBlock = completion;
    [qq authorize:@[@"all"]];
}

#pragma mark - TencentSessionDelegate

- (void)tencentDidLogin {
    AGCAuthCredential *credential = [AGCQQAuthProvider credentialWithToken:[qq accessToken] openId:[qq openId]];
    if (self.credentialBlock) {
        self.credentialBlock(credential);
    }
}

- (void)tencentDidNotLogin:(BOOL)cancelled {
    
}

- (void)tencentDidNotNetWork {
    
}





@end
