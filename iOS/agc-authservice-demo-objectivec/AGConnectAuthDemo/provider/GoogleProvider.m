//
//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved
//

#import "GoogleProvider.h"

@implementation GoogleProvider

+ (instancetype)sharedInstance {
    static GoogleProvider * sharedInstance;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedInstance = [[self alloc] init];
    });
    return sharedInstance;
}

- (instancetype)init {
    if (self = [super init]) {
        
    }
    return self;
}

- (void)registerApp:(UIApplication *)application options:(NSDictionary *)launchOptions {
    [GIDSignIn sharedInstance].clientID = @"GOOGLE_CLIENT_ID";
    [GIDSignIn sharedInstance].delegate = self;
}

- (BOOL)application:(id)app openURL:(id)url options:(id)options {
    return [[GIDSignIn sharedInstance] handleURL:url];
}

- (void)loginWithViewController:(id<SignInDelegate>)delegate {
    self.signInDelegate = delegate;
    [GIDSignIn sharedInstance].presentingViewController = (UIViewController *)delegate;
    [[GIDSignIn sharedInstance] signIn];
}

- (void)linkWithViewController:(UIViewController *)viewController {
    
    self.isLink = YES;
    [GIDSignIn sharedInstance].presentingViewController = viewController;
    [[GIDSignIn sharedInstance] signIn];
}

- (void)unlink {
    [[[[[AGCAuth getInstance] currentUser] unlink:AGCAuthProviderTypeGoogle] addOnSuccessCallback:^(AGCSignInResult * _Nullable result) {
                [ToastUtil showToast:@"unlink success"];
            }] addOnFailureCallback:^(NSError * _Nonnull error) {
                [ToastUtil showToast:@"unlink failed"];
            }];
}

#pragma mark - GIDSignInDelegate
- (void)signIn:(GIDSignIn *)signIn didSignInForUser:(GIDGoogleUser *)user withError:(NSError *)error {
    if (error != nil) {
      if (error.code == kGIDSignInErrorCodeHasNoAuthInKeychain) {
        NSLog(@"The user has not signed in before or they have since signed out.");
      } else {
        NSLog(@"%@", error.localizedDescription);
      }
      return;
    }
    
    AGCAuthCredential *credential = [AGCGoogleAuthProvider credentialWithToken:user.authentication.idToken];
    if (self.isLink) {
        [self linkWithCredential:credential];
        self.isLink = NO;
    }else {
        [self signInWithCredential:credential];
    }
}

@end
