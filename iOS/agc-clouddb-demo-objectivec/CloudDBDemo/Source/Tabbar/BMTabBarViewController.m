/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#import "BMTabBarViewController.h"

#import "BMNavigationViewController.h"
#import "BMMeViewController.h"
#import "BMMainViewController.h"

@interface BMTabBarViewController ()

@property (nonatomic, strong) BMNavigationViewController *meVC;
@property (nonatomic, strong) BMNavigationViewController *mainVC;

@end

@implementation BMTabBarViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [self combinationViewController];
}

- (void)combinationViewController {
    
    BMMainViewController *mainVc = [[BMMainViewController alloc] init];
    self.mainVC = [[BMNavigationViewController alloc] initWithRootViewController:mainVc];
    self.mainVC.title = NSLocalizedString(@"TabBarMainTitle",nil);
    self.mainVC.tabBarItem.image = [UIImage imageNamed:@"icon_home_normal"];
    UIImage *mainSelectImage = [UIImage imageNamed:@"icon_home_selected"];
    mainSelectImage = [mainSelectImage imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
    self.mainVC.tabBarItem.selectedImage = mainSelectImage;
    
    BMMeViewController *meVc = [[BMMeViewController alloc] init];
    self.meVC = [[BMNavigationViewController alloc] initWithRootViewController:meVc];
    self.meVC.title = NSLocalizedString(@"TabBarMeTitle",nil);
    self.meVC.tabBarItem.image = [UIImage imageNamed:@"icon_me_normal"];
    UIImage *meSelectImage = [UIImage imageNamed:@"icon_me_selected"];
    meSelectImage = [meSelectImage imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
    self.meVC.tabBarItem.selectedImage = meSelectImage;
    
    UIColor *selectColor = [UIColor colorWithRed:247.0/256.f green:205.0/256.0 blue:160.0/256.0 alpha:1.f];
    self.tabBar.tintColor = selectColor;
    
    self.viewControllers = @[self.mainVC,self.meVC];
    
    [self setSelectedIndex:1];
}

@end
