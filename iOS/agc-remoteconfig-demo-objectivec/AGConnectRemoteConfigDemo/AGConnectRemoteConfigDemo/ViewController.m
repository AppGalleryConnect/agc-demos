//
//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved
//

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
    // Do any additional setup after loading the view.
    [HiAnalytics setUserProfile:@"red" setValue:@"favorite_color"];
    [HiAnalytics setUserProfile:@"five" setValue:@"favorite_number"];
    [HiAnalytics setUserProfile:@"banana" setValue:@"favorite_food"];

}

- (IBAction)FirstModeButtonTapped:(id)sender {
    [self.navigationController pushViewController:[FirstModeViewController new] animated:YES];
}

- (IBAction)SecondModeButtonTapped:(id)sender {
    [self.navigationController pushViewController:[SecondModeViewController new] animated:YES];
}

- (IBAction)ClearDataButtonTapped:(id)sender {
    [[AGCRemoteConfig sharedInstance] clearAll];
    NSLog(@"clear all config.");
}


@end
