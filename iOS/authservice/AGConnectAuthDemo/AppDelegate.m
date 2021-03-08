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

#import "AppDelegate.h"
#import "WechatProvider.h"
#import "QQProvider.h"
#import "WeiboProvider.h"
#import "GoogleProvider.h"
#import "TwitterProvider.h"
#import "FacebookProvider.h"

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    // initialize agconnet sdk
    [AGCInstance startUp];
    
    // initialize social accounts sdk
    [[FacebookProvider sharedInstance] startUpWithApp:application options:launchOptions];
    [[TwitterProvider sharedInstance] startUp];
    [[GoogleProvider sharedInstance] startUp];
    [[WechatProvider sharedInstance] startUp];
    [[WeiboProvider sharedInstance] startUp];
    [[QQProvider sharedInstance] startUp];
    
    return YES;
}

- (BOOL)application:(UIApplication *)application continueUserActivity:(NSUserActivity *)userActivity restorationHandler:(void (^)(NSArray<id<UIUserActivityRestoring>> * _Nullable))restorationHandler {
    // handle url for social accounts auth sdk
    return [[WechatProvider sharedInstance] handleOpenUniversalLink:userActivity];
}

- (BOOL)application:(UIApplication *)app openURL:(NSURL *)url options:(NSDictionary<UIApplicationOpenURLOptionsKey,id> *)options {
    // handle url for social accounts auth sdk
    BOOL isWeChatSuccess = [[WechatProvider sharedInstance] application:app openURL:url options:options];
    BOOL isWeiboSuccess = [[WeiboProvider sharedInstance] application:app openURL:url options:options];
    BOOL isQQSuccess = [[QQProvider sharedInstance] application:app openURL:url options:options];
    BOOL isGoogleSuccess = [[GoogleProvider sharedInstance] application:app openURL:url options:options];
    BOOL isTwitterSuccess = [[TwitterProvider sharedInstance] application:app openURL:url options:options];
    BOOL isFacebookSuccess = [[FacebookProvider sharedInstance] application:app openURL:url options:options];
    return isWeiboSuccess | isQQSuccess | isWeChatSuccess | isGoogleSuccess | isTwitterSuccess | isFacebookSuccess;
}

@end
