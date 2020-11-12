//
//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved
//

#import "FacebookProvider.h"

@implementation FacebookProvider
{
    FBSDKLoginManager *manager;
}

+ (instancetype)sharedInstance {
    static FacebookProvider * sharedInstance;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedInstance = [[self alloc] init];
    });
    return sharedInstance;
}

- (void)registerApp:(UIApplication *)application options:(NSDictionary *)launchOptions {
    [[FBSDKApplicationDelegate sharedInstance] application:application didFinishLaunchingWithOptions:launchOptions];
}

- (instancetype)init {
    if (self = [super init]) {
        manager = [FBSDKLoginManager new];
    }
    return self;
}

- (BOOL)application:(id)app openURL:(id)url options:(id)options {
    return [[FBSDKApplicationDelegate sharedInstance] application:app openURL:url sourceApplication:options[UIApplicationOpenURLOptionsSourceApplicationKey] annotation:options[UIApplicationOpenURLOptionsAnnotationKey] ];
}

- (void)loginWithViewController:(id<SignInDelegate>)delegate {
    self.signInDelegate = delegate;
    
    [manager logInWithPermissions:@[@"public_profile",@"email"]
               fromViewController:(UIViewController *)self
                          handler:^(FBSDKLoginManagerLoginResult * _Nullable result, NSError * _Nullable error) {
        if (!error) {
            AGCAuthCredential *credential = [AGCFacebookAuthProvider credentialWithToken:result.token.tokenString];
            [self signInWithCredential:credential];
        }
        
    }];
}

- (void)linkWithViewController:(UIViewController *)viewController {
    
    [manager logInWithPermissions:@[@"public_profile",@"email"]
               fromViewController:(UIViewController *)self
                          handler:^(FBSDKLoginManagerLoginResult * _Nullable result, NSError * _Nullable error) {
        if (!error) {
            AGCAuthCredential *credential = [AGCFacebookAuthProvider credentialWithToken:result.token.tokenString];
            [self linkWithCredential:credential];
        }
    }];
}

- (void)unlink {
    [[[[[AGCAuth getInstance] currentUser] unlink:AGCAuthProviderTypeFacebook] addOnSuccessCallback:^(AGCSignInResult * _Nullable result) {
                [ToastUtil showToast:@"unlink success"];
            }] addOnFailureCallback:^(NSError * _Nonnull error) {
                [ToastUtil showToast:@"unlink failed"];
            }];
}


@end
