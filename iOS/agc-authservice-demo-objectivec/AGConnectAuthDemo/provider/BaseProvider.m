//
//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved
//

#import "BaseProvider.h"

@implementation BaseProvider

- (void)loginWithViewController:(id<SignInDelegate>)delegate {
    @throw [NSException exceptionWithName:NSGenericException reason:@"This method should be overrided by subclass" userInfo:nil];
}

- (void)linkWithViewController:(UIViewController *)viewController {
    @throw [NSException exceptionWithName:NSGenericException reason:@"This method should be overrided by subclass" userInfo:nil];
}

- (void)unlink {
    @throw [NSException exceptionWithName:NSGenericException reason:@"This method should be overrided by subclass" userInfo:nil];
}

- (BOOL)handleOpenUniversalLink:(NSUserActivity *)userActivity {
    @throw [NSException exceptionWithName:NSGenericException reason:@"This method should be overrided by subclass" userInfo:nil];
}

- (BOOL)application:(id)app openURL:(id)url options:(id)options {
    @throw [NSException exceptionWithName:NSGenericException reason:@"This method should be overrided by subclass" userInfo:nil];
}

- (void)registerApp:(UIApplication *)application options:(NSDictionary *)launchOptions {
    @throw [NSException exceptionWithName:NSGenericException reason:@"This method should be overrided by subclass" userInfo:nil];
}


+ (void)logout {
    [[AGCAuth getInstance] signOut];
}

- (void)linkWithCredential:(AGCAuthCredential *)credential {
    [[[[[AGCAuth getInstance] currentUser] link:credential] addOnSuccessCallback:^(AGCSignInResult * _Nullable result) {
        [ToastUtil showToast:@"link success"];
    }] addOnFailureCallback:^(NSError * _Nonnull error) {
        NSLog(@" link failure : %@",error);
        [ToastUtil showToast:@"link failure"];
    }];
}

- (void)signInWithCredential:(AGCAuthCredential *)credential {
    [[[[AGCAuth getInstance] signIn:credential] addOnSuccessCallback:^(AGCSignInResult * _Nullable result) {
        [self.signInDelegate signInSucceed];
    }] addOnFailureCallback:^(NSError * _Nonnull error) {
        NSLog(@" login failed : %@",error);
        [self.signInDelegate signInFailed];
    }];
}

+ (void)updateProfileWithDisplayName:(NSString *)name photoUrl:(NSString *)photo {
    AGCProfileRequest *request = [AGCProfileRequest new];
    request.displayName = name;
    request.photoUrl = photo;
    [[[[[AGCAuth getInstance] currentUser] updateProfile:request] addOnSuccessCallback:^(id  _Nullable result) {
        [ToastUtil showToast:@"profile update success"];
    }] addOnFailureCallback:^(NSError * _Nonnull error) {
        NSLog(@"Profile update failed : %@",error);
        [ToastUtil showToast:@"profile update failed"];
    }];
}





@end
