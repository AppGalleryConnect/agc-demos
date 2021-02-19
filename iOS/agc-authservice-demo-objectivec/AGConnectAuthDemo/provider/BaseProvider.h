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

#import <UIKit/UIKit.h>
#import <AGConnectAuth/AGConnectAuth.h>

typedef void (^CredentialBlock)(AGCAuthCredential * _Nullable credential);

NS_ASSUME_NONNULL_BEGIN

@interface BaseProvider : NSObject

@property (nonatomic, copy) CredentialBlock credentialBlock;

// Startup the third party auth sdk
- (void)startUp;

// Handle URL for the third party auth sdk
- (BOOL)application:app openURL:url options:options;

// Fetch credential of the third party auth sdk
- (void)fetchCredentialWithController:(UIViewController *)vc completion:(CredentialBlock)completion;

// Send phone verification code
+ (void)sendVerifyCodeWithCountryCode:(NSString *)countryCode phoneNumber:(NSString *)phoneNumber action:(AGCVerifyCodeAction)action;

// Send email verification code
+ (void)sendVerifyCodeWithEmail:(NSString *)email action:(AGCVerifyCodeAction)action;

@end

NS_ASSUME_NONNULL_END
