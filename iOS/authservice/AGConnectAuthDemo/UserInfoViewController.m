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

#import "UserInfoViewController.h"
#import "ViewController.h"
#import "UserSettingsViewController.h"
#import "UserLinkViewController.h"
#import <AGConnectAuth/AGConnectAuth.h>

@implementation UserInfoViewController

+ (instancetype)instantiate {
    UIStoryboard *story = [UIStoryboard storyboardWithName:@"Main" bundle:[NSBundle mainBundle]];
    UserInfoViewController *vc = [story instantiateViewControllerWithIdentifier:@"UserInfoVC"];
    return vc;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.title = @"User Info";
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    
    // Get the currently logged-in user
    AGCUser *user = [[AGCAuth getInstance] currentUser];
    [self refreshViewWithUser:user];
}

- (void)refreshViewWithUser:(AGCUser *)user {
    _nameLabel.text = user.displayName;
    _idLabel.text = user.uid;
}

- (IBAction)signOut:(id)sender {
    // sign out
    [[AGCAuth getInstance] signOut];
    
    [self.navigationController setViewControllers:@[[ViewController instantiate]]];
}

- (IBAction)showSettings:(id)sender {
    [self.navigationController pushViewController:[UserSettingsViewController new] animated:YES];
}

- (IBAction)linkAccounts:(id)sender {
    [self.navigationController pushViewController:[UserLinkViewController new] animated:YES];
}

@end
