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

#import "BMMeViewController.h"

#import "UIView+Toast.h"

#import "BMLoginView.h"
#import "CloudDBManager.h"

@interface BMMeViewController ()

@property (nonatomic, strong) BMLoginView *loginView;

@end

@implementation BMMeViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.view.backgroundColor = [UIColor whiteColor];
    self.navigationItem.title = NSLocalizedString(@"MeViewTitle", nil);
    
    self.loginView.hidden = NO;
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    
    if ([CloudDBManager shareInsatnce].isLogin) {
        [self.loginView loginSuccess];
    }
}

#pragma mark - setter && getter
- (BMLoginView *)loginView {
    if (!_loginView) {
        _loginView = [[BMLoginView alloc] initWithFrame:CGRectMake(20, 20, [UIScreen mainScreen].bounds.size.width - 40, 120)];
        [self.view addSubview:_loginView];
        
        UITapGestureRecognizer *tapGesture = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapGestureEvent:)];
        [_loginView addGestureRecognizer:tapGesture];
    }
    return _loginView;
}

#pragma mark - tapGestureEvent
- (void)tapGestureEvent:(UITapGestureRecognizer *)gesture {
    // Anonymous logins
    __weak typeof(self) weakSelf = self;
    [[CloudDBManager shareInsatnce] loginAGCWithComplete:^(BOOL success, NSError *error) {
        if (success) {
            dispatch_async(dispatch_get_main_queue(), ^{
                [weakSelf.view makeToast:@"Login Success"];
                [weakSelf.loginView loginSuccess];
            });
            // Login suceess post notification refresh mainpage
            [[NSNotificationCenter defaultCenter] postNotificationName:@"KNotificationRefreshBookList" object:nil];
            // Login suceess add AGC listener to listen for data changes
            [weakSelf addListener];
        } else {
            dispatch_async(dispatch_get_main_queue(), ^{
                NSString *errorString = [NSString stringWithFormat:@"AGC login failed with error: %@", error];
                [weakSelf.view makeToast:errorString];
            });
        }
    }];
}

#pragma mark - Add listener
/**
 Add data change listener
 */
- (void)addListener {
    sleep(2);
    __weak typeof(self) weakSelf = self;
    [[CloudDBManager shareInsatnce] subscribeSnapshotComplete:^(NSArray *_Nonnull bookList, NSError *error) {
        if (error) {
            dispatch_async(dispatch_get_main_queue(), ^{
                NSString *errorString = [NSString stringWithFormat:@"add listener failed with error: %@", error];
                [weakSelf.view makeToast:errorString];
            });
        } else {
            [[NSNotificationCenter defaultCenter] postNotificationName:@"KNotificationRefreshBookList" object:nil];
        }
    }];
}

@end
