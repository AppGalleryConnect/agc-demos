//
//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved
//

#import "ViewController.h"
#import <AGConnectAppLinking/AGConnectAppLinking.h>

@interface ViewController ()

@property (weak, nonatomic) IBOutlet UILabel *shortLink;
@property (weak, nonatomic) IBOutlet UILabel *longLink;

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (IBAction)create:(id)sender {
    AGCAppLinkingComponents *components = [[AGCAppLinkingComponents alloc] init];
    components.uriPrefix = @"https://example.drcn.agconnect.link";
    components.deepLink = @"https://www.example.com";
    components.iosBundleId = [[NSBundle mainBundle] bundleIdentifier];
    components.iosDeepLink = @"example://ios/detail";
    components.androidDeepLink = @"example://android/detail";
    components.androidPackageName = @"com.android.demo";
    components.campaignName = @"name";
    components.campaignMedium = @"App";
    components.campaignSource = @"AGC";
    components.socialTitle = @"Title";
    components.socialImageUrl = @"https://example.com/1.png";
    components.socialDescription = @"Description";
    
    self.longLink.text = [components buildLongLink].absoluteString;
    __weak typeof(self) weakSelf = self;
    [components buildShortLink:^(AGCShortAppLinking * _Nullable shortLinking, NSError * _Nullable error) {
        if (shortLinking) {
            weakSelf.shortLink.text = shortLinking.url.absoluteString;
        }
    }];
}

- (IBAction)openShortLink:(id)sender {
    NSURL *url = [NSURL URLWithString:self.shortLink.text];
    if (url) {
        [UIApplication.sharedApplication openURL:url options:[NSDictionary dictionary] completionHandler:nil];
    }
}

- (IBAction)openLongLink:(id)sender {
    NSURL *url = [NSURL URLWithString:self.longLink.text];
    if (url) {
        [UIApplication.sharedApplication openURL:url options:[NSDictionary dictionary] completionHandler:nil];
    }
}


@end
