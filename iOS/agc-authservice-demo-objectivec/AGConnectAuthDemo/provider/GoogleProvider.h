//
//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved
//

#import "BaseProvider.h"
@import GoogleSignIn;

NS_ASSUME_NONNULL_BEGIN

@interface GoogleProvider : BaseProvider <GIDSignInDelegate>

+ (instancetype)sharedInstance;

@end

NS_ASSUME_NONNULL_END
