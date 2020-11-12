//
//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved
//

#import "AnonymousProvider.h"

@implementation AnonymousProvider

+ (instancetype)sharedInstance {
    static AnonymousProvider * sharedInstance;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedInstance = [[self alloc] init];
    });
    return sharedInstance;
}

- (void)loginWithViewController:(id<SignInDelegate>)delegate {
    self.signInDelegate = delegate;
    [[[[AGCAuth getInstance] signInAnonymously] addOnSuccessCallback:^(AGCSignInResult * _Nullable result) {
        [self.signInDelegate signInSucceed];
    }] addOnFailureCallback:^(NSError * _Nonnull error) {
        [self.signInDelegate signInFailed];
    }];
}

@end
