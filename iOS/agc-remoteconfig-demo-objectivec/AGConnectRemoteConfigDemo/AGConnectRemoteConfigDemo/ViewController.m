//
//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved
//

#import "ViewController.h"
#import "FirstModeViewController.h"
#import "SecondModeViewController.h"
#import <AGConnectRemoteConfig/AGConnectRemoteConfig.h>

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (IBAction)FirstModeButtonTapped:(id)sender {
    [self.navigationController pushViewController:[FirstModeViewController new] animated:YES];
}

- (IBAction)SecondModeButtonTapped:(id)sender {
    [self.navigationController pushViewController:[SecondModeViewController new] animated:YES];
}

- (IBAction)ClearDataButtonTapped:(id)sender {
    [[AGCRemoteConfig sharedIntance] clearAll];
    NSLog(@"clear all config.");
}


@end
