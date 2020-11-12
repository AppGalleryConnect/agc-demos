//
//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved
//

#import "ViewController.h"
#import <AGConnectCrash/AGConnectCrash.h>

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (IBAction)enableCrash:(id)sender {
    [[AGCCrash sharedInstance] enableCrashCollection:YES];
}

- (IBAction)disableCrash:(id)sender {
    [[AGCCrash sharedInstance] enableCrashCollection:NO];
}

- (IBAction)testNSException:(id)sender {
    [[AGCCrash sharedInstance] testIt];
}

- (IBAction)testSignal:(id)sender {
    abort();
}

@end
