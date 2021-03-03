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

#import "ViewController.h"
#import "FirstModeViewController.h"
#import "SecondModeViewController.h"
#import <AGConnectRemoteConfig/AGConnectRemoteConfig.h>
#import <HiAnalytics/HiAnalytics.h>

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    // if you add user attributes condition in remote configuration, you can set user attributes like this.
    [HiAnalytics setUserProfile:@"favorite_color" setValue:@"red"];
}

- (IBAction)navigateToMode1:(id)sender {
    [self.navigationController pushViewController:[FirstModeViewController new] animated:YES];
}

- (IBAction)navigateToMode2:(id)sender {
    [self.navigationController pushViewController:[SecondModeViewController new] animated:YES];
}

- (IBAction)clearData:(id)sender {
    NSLog(@"clear all config");
    // clear all config
    [[AGCRemoteConfig sharedInstance] clearAll];
}

@end
