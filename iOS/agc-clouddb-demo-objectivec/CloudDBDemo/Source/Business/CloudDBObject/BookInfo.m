/**
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2020. All rights reserved.
 * Generated by the CloudDB ObjectType compiler.  DO NOT EDIT!
 */

#import "BookInfo.h"

@implementation BookInfo

- (instancetype)init {
    self = [super init];
    if (self) {
        _shadowFlag = @YES;
    }
    return self;
}

+ (NSArray<NSString *> *)primaryKeyProperties {
    return @[@"id"];
}

+ (NSDictionary<NSString *, NSArray *> *)indexProperties { 
    return @{@"bookName" : @[@"bookName"]};
}

+ (NSDictionary *)defaultValueProperties {
    return @{@"shadowFlag" : @YES};
}

+ (NSArray<NSString *> *)notNullProperties {
    return @[@"id"];
}

@end
