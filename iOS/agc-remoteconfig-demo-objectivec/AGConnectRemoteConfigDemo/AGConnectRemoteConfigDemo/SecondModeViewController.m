//
//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved
//

#import "SecondModeViewController.h"
#import <AGConnectRemoteConfig/AGConnectRemoteConfig.h>

@interface SecondModeViewController ()

@end

@implementation SecondModeViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    NSDictionary *defaultConfig = @{@"test1":@"value1", @"test2":@(2)};
    [[AGCRemoteConfig sharedInstance] applyDefaults:defaultConfig];
    
    AGCConfigValues *lastFetchedConfig = [[AGCRemoteConfig sharedInstance] loadLastFetched];
    [[AGCRemoteConfig sharedInstance] apply:lastFetchedConfig];
    [self showAllValue];
    
    [[[[AGCRemoteConfig sharedInstance] fetch] addOnSuccessCallback:^(AGCConfigValues * _Nullable result) {
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
