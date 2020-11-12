//
//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved
//

#import "BaseProvider.h"
#import <WeiboSDK.h>

NS_ASSUME_NONNULL_BEGIN

@interface WeiboProvider : BaseProvider <WeiboSDKDelegate>

+ (instancetype)sharedInstance;

@end

NS_ASSUME_NONNULL_END
