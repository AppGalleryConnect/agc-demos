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
    [[AGCRemoteConfig sharedInstance] applyDefaults:defaultConfig];
    
    [[[[AGCRemoteConfig sharedInstance] fetch] addOnSuccessCallback:^(AGCConfigValues * _Nullable result) {
        [[AGCRemoteConfig sharedInstance] apply:result];
        [self showAllValue];
        NSLog(@"fetch successful");
    }] addOnFailureCallback:^(NSError * _Nonnull error) {
        NSLog(@"fetch failed");
    }];
}

- (void)showAllValue {
    NSDictionary *val = [[AGCRemoteConfig sharedInstance] getMergedAll];
    _label.text = [val description];
}


@end
