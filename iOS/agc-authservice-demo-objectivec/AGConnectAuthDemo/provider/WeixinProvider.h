//
//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved
//

#import "BaseProvider.h"
#import <WXApi.h>

NS_ASSUME_NONNULL_BEGIN

@interface WeixinProvider : BaseProvider <WXApiDelegate>

+ (instancetype)sharedInstance;

@end

NS_ASSUME_NONNULL_END
