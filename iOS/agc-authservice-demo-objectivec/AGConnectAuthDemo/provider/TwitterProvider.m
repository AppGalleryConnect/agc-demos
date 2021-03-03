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

- (void)startUp {
    [[Twitter sharedInstance] startWithConsumerKey:@"TWITTER_CONSUMER_KEY" consumerSecret:@"TWITTER_CONSUMER_SECRET"];
}

- (BOOL)application:(id)app openURL:(id)url options:(id)options {
    return [[Twitter sharedInstance] application:app openURL:url options:options];
}

- (void)fetchCredentialWithController:(UIViewController *)vc completion:(CredentialBlock)completion {
    self.credentialBlock = completion;
    [[Twitter sharedInstance] logInWithCompletion:^(TWTRSession *session, NSError *error) {
      if (session) {
          AGCAuthCredential *credential = [AGCTwitterAuthProvider credentialWithToken:[session authToken] secret:[session authTokenSecret]];
          if (completion) {
              completion(credential);
          }
      } else {
          NSLog(@"error: %@", [error localizedDescription]);
          if (completion) {
              completion(nil);
          }
      }
    }];
}

@end
