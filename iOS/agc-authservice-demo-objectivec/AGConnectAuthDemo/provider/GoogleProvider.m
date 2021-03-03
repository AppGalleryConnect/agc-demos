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

- (void)startUp {
    [GIDSignIn sharedInstance].clientID = @"GOOGLE_CLIENT_ID";
    [GIDSignIn sharedInstance].delegate = self;
}

- (BOOL)application:(id)app openURL:(id)url options:(id)options {
    return [[GIDSignIn sharedInstance] handleURL:url];
}

- (void)fetchCredentialWithController:(UIViewController *)vc completion:(CredentialBlock)completion {
    self.credentialBlock = completion;
    [GIDSignIn sharedInstance].presentingViewController = vc;
    [[GIDSignIn sharedInstance] signIn];
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
    if (self.credentialBlock) {
        self.credentialBlock(credential);
    }
}

@end
