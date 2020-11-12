//
//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved
//

#import <UIKit/UIKit.h>
#import "ToastUtil.h"
#import <AGConnectAuth/AGConnectAuth.h>

NS_ASSUME_NONNULL_BEGIN

@protocol SignInDelegate <NSObject>

- (void)signInSucceed;

- (void)signInFailed;

@end

@interface BaseProvider : NSObject

@property (nonatomic, weak) id<SignInDelegate> signInDelegate;

@property (nonatomic) BOOL isLink;

- (void)registerApp:(UIApplication *)application options:(NSDictionary *)launchOptions;

- (BOOL)handleOpenUniversalLink:(NSUserActivity *)userActivity;

- (BOOL)application:app openURL:url options:options;

- (void)loginWithViewController:(id<SignInDelegate>)delegate;

+ (void)logout;

- (void)linkWithViewController:(UIViewController *)viewController;

- (void)unlink;

- (void)signInWithCredential:(AGCAuthCredential *)credential;

- (void)linkWithCredential:(AGCAuthCredential *)credential;

+ (void)updateProfileWithDisplayName:(NSString *)name photoUrl:(NSString *)photo;

@end

NS_ASSUME_NONNULL_END
