//
//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved
//

#import "BaseProvider.h"
#import <TencentOpenAPI/TencentOAuth.h>

NS_ASSUME_NONNULL_BEGIN

@interface QQProvider : BaseProvider <TencentSessionDelegate>

+ (instancetype)sharedInstance;

@end

NS_ASSUME_NONNULL_END
