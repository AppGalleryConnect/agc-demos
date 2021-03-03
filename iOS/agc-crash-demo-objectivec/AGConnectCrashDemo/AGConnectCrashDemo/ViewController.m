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
#import <AGConnectCrash/AGConnectCrash.h>

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
}

- (IBAction)enableCrash:(id)sender {
    // Enable crash collection and reporting
    [[AGCCrash sharedInstance] enableCrashCollection:YES];
}

- (IBAction)disableCrash:(id)sender {
    // Disable crash collection and reporting
    [[AGCCrash sharedInstance] enableCrashCollection:NO];
}

- (IBAction)testException:(id)sender {
    // Create a crash, you can view the crash information on the appgallery connect website after restarting
    [[AGCCrash sharedInstance] testIt];
}

- (IBAction)recordNonfatalException:(id)sender {
    // catch the error thrown by your function
    // or generate the error as needed.
    NSString *yourErrorDomain = @"your_error_domain";
    NSInteger yourErrorCode = 0;
    NSDictionary *yourErrorInfo = nil;
    NSError *error = [NSError errorWithDomain:yourErrorDomain code:yourErrorCode userInfo:yourErrorInfo];
    // record error.
    [[AGCCrash sharedInstance] recordError:error];
}

@end
