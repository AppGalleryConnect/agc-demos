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

#import "BMNavigationViewController.h"


@interface BMNavigationViewController ()

@end

@implementation BMNavigationViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.navigationBar.translucent = NO;
    self.navigationBar.barTintColor = [UIColor colorWithRed:247.0/256.f green:205.0/256.0 blue:160.0/256.0 alpha:1.f];
    self.navigationBar.titleTextAttributes = @{NSForegroundColorAttributeName: [UIColor whiteColor],
                                               NSFontAttributeName: [UIFont fontWithName:@"Helvetica-Bold" size:20.f]};
    
    self.view.backgroundColor = [UIColor whiteColor];
    
    [[UINavigationBar appearance] setTintColor:[UIColor whiteColor]];
    
}

@end
