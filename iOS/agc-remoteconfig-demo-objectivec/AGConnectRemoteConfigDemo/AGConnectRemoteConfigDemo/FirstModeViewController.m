//
//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved
//

#import "FirstModeViewController.h"
#import <AGConnectRemoteConfig/AGConnectRemoteConfig.h>

@interface FirstModeViewController ()

@end

@implementation FirstModeViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    NSDictionary *defaultConfig = @{@"test1":@"value1", @"test2":@(2)};
    [[AGCRemoteConfig sharedIntance] applyDefaults:defaultConfig];
    
    [[[[AGCRemoteConfig sharedIntance] fetch] addOnSuccessCallback:^(AGCConfigValues * _Nullable result) {
        [[AGCRemoteConfig sharedIntance] apply:result];
        [self showAllValue];
        NSLog(@"fetch successful");
    }] addOnFailureCallback:^(NSError * _Nonnull error) {
        NSLog(@"fetch failed");
    }];
}

- (void)showAllValue {
    NSDictionary *val = [[AGCRemoteConfig sharedIntance] getMergedAll];
    _label.text = [val description];
}


@end
