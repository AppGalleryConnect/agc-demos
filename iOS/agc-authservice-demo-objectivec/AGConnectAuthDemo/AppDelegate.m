//
//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved
//

#import "AppDelegate.h"
#import "WeixinProvider.h"
#import "QQProvider.h"
#import "WeiboProvider.h"
#import "GoogleProvider.h"
#import "TwitterProvider.h"
#import "FacebookProvider.h"

@interface AppDelegate ()

@property (nonatomic) UIViewController *mainViewController;

@end

@implementation AppDelegate


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    // Override point for customization after application launch.
    
    [[FacebookProvider sharedInstance] registerApp:application options:launchOptions];
    [[TwitterProvider sharedInstance] registerApp:application options:launchOptions];
    [[GoogleProvider sharedInstance] registerApp:application options:launchOptions];
    [[WeixinProvider sharedInstance] registerApp:application options:launchOptions];
    [[WeiboProvider sharedInstance] registerApp:application options:launchOptions];
    [[QQProvider sharedInstance] registerApp:application options:launchOptions];
    
    return YES;
}

- (BOOL)application:(UIApplication *)application continueUserActivity:(NSUserActivity *)userActivity restorationHandler:(void (^)(NSArray<id<UIUserActivityRestoring>> * _Nullable))restorationHandler {
    return [[WeixinProvider sharedInstance] handleOpenUniversalLink:userActivity];
}

- (BOOL)application:(UIApplication *)app openURL:(NSURL *)url options:(NSDictionary<UIApplicationOpenURLOptionsKey,id> *)options {
    BOOL isWeChatSuccess = [[WeixinProvider sharedInstance] application:app openURL:url options:options];
    BOOL isWeiboSuccess = [[WeiboProvider sharedInstance] application:app openURL:url options:options];
    BOOL isQQSuccess = [[QQProvider sharedInstance] application:app openURL:url options:options];
    BOOL isGoogleSuccess = [[GoogleProvider sharedInstance] application:app openURL:url options:options];
    BOOL isTwitterSuccess = [[TwitterProvider sharedInstance] application:app openURL:url options:options];
    BOOL isFacebookSuccess = [[FacebookProvider sharedInstance] application:app openURL:url options:options];
    return isWeiboSuccess | isQQSuccess | isWeChatSuccess | isGoogleSuccess | isTwitterSuccess | isFacebookSuccess;
}



@end
