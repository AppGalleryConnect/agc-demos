//
//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved
//

#import "TwitterProvider.h"

@implementation TwitterProvider

+ (instancetype)sharedInstance {
    static TwitterProvider * sharedInstance;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedInstance = [[self alloc] init];
    });
    return sharedInstance;
}

- (void)registerApp:(UIApplication *)application options:(NSDictionary *)launchOptions {
    [[Twitter sharedInstance] startWithConsumerKey:@"TWITTER_CONSUMER_KEY" consumerSecret:@"TWITTER_CONSUMER_SECRET"];
}

- (BOOL)application:(id)app openURL:(id)url options:(id)options {
    return [[Twitter sharedInstance] application:app openURL:url options:options];
}


- (void)loginWithViewController:(id<SignInDelegate>)delegate {
    
    [[Twitter sharedInstance] logInWithCompletion:^(TWTRSession *session, NSError *error) {
      if (session) {
          NSLog(@"signed in as %@", [session userName]);
          AGCAuthCredential *credential = [AGCTwitterAuthProvider credentialWithToken:[session authToken] secret:[session authTokenSecret]];
          [self signInWithCredential:credential];
      } else {
          NSLog(@"error: %@", [error localizedDescription]);
      }
    }];
}

- (void)unlink {
    [[[[[AGCAuth getInstance] currentUser] unlink:AGCAuthProviderTypeTwitter] addOnSuccessCallback:^(AGCSignInResult * _Nullable result) {
                [ToastUtil showToast:@"unlink success"];
            }] addOnFailureCallback:^(NSError * _Nonnull error) {
                [ToastUtil showToast:@"unlink failed"];
            }];
}

- (void)linkWithViewController:(UIViewController *)viewController {

    [[Twitter sharedInstance] logInWithCompletion:^(TWTRSession *session, NSError *error) {
      if (session) {
          NSLog(@"signed in as %@", [session userName]);
          AGCAuthCredential *credential = [AGCTwitterAuthProvider credentialWithToken:[session authToken] secret:[session authTokenSecret]];
          [self linkWithCredential:credential];
      } else {
          NSLog(@"error: %@", [error localizedDescription]);
      }
    }];
}


@end
