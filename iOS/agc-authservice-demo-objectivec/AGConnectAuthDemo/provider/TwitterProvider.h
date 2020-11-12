//
//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved
//

#import "BaseProvider.h"
#import <TwitterKit/TWTRKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface TwitterProvider : BaseProvider

+ (instancetype)sharedInstance;

@end

NS_ASSUME_NONNULL_END
