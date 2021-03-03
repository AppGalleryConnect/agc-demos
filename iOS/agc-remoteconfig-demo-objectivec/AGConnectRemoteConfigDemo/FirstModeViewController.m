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

#import "FirstModeViewController.h"
#import <AGConnectRemoteConfig/AGConnectRemoteConfig.h>

@implementation FirstModeViewController

- (void)viewDidLoad {
    [super viewDidLoad];

    // the following code demonstrates how to fetch config and apply immediately.
    
    // apply default config if you want
    NSDictionary *defaultConfig = @{@"test1":@"value1", @"test2":@(2)};
    [[AGCRemoteConfig sharedInstance] applyDefaults:defaultConfig];
    
    // fetch config
    [[[[AGCRemoteConfig sharedInstance] fetch] addOnSuccessCallback:^(AGCConfigValues * _Nullable result) {
        NSLog(@"fetch successfully");
        // apply the config when fetch is successful
        [[AGCRemoteConfig sharedInstance] apply:result];
        
        // get all applied config and show it in label
        NSDictionary *appliedConfig = [[AGCRemoteConfig sharedInstance] getMergedAll];
        self.label.text = [appliedConfig description];
    }] addOnFailureCallback:^(NSError * _Nonnull error) {
        NSLog(@"fetch failed");
    }];
}

@end
