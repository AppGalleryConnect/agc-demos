//
//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved
//

#import "ViewController.h"
#import "AGConnectAPM/AGConnectAPM.H"

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    UIButton* sendNetworkRequestBtn = [UIButton buttonWithType:UIButtonTypeRoundedRect];
    sendNetworkRequestBtn.frame = CGRectMake(80, 100, 200, 40);
    [sendNetworkRequestBtn setTitle:@"Send Network Request" forState:UIControlStateNormal];
    [sendNetworkRequestBtn addTarget:self action:@selector(sendNetworkRequest) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:sendNetworkRequestBtn];
    
    UIButton* disableBtn = [UIButton buttonWithType:UIButtonTypeRoundedRect];
    disableBtn.frame = CGRectMake(80, 150, 200, 40);
    [disableBtn setTitle:@"APM Collection Off" forState:UIControlStateNormal];
    [disableBtn addTarget:self action:@selector(disableCollection) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:disableBtn];
    
    UIButton* enableBtn = [UIButton buttonWithType:UIButtonTypeRoundedRect];
    enableBtn.frame = CGRectMake(80, 200, 200, 40);
    [enableBtn setTitle:@"APM Collection On" forState:UIControlStateNormal];
    [enableBtn addTarget:self action:@selector(enableCollection) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:enableBtn];
}

- (void)sendNetworkRequest {
    NSURL *url = [NSURL URLWithString:@"https://developer.huawei.com/consumer/cn/"];
    
    
    NSURLSession *session = [NSURLSession sharedSession];
    NSURLSessionDataTask *task = [session dataTaskWithURL:url completionHandler:^(NSData * _Nullable data, NSURLResponse * _Nullable response, NSError * _Nullable error) {
        NSString* str = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
        NSLog(@"result: %@", str);
    }];
    
    [task resume];
}

- (void)disableCollection {
    [[AGCAPM sharedInstance] enableCollection:NO];
}

- (void)enableCollection {
    [[AGCAPM sharedInstance] enableCollection:YES];
}

@end
