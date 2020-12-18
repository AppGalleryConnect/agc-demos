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

#import "BookManagerMainHeaderViewButton.h"

@interface BookManagerMainHeaderViewButton ()

@property (nonatomic, strong) UIImageView *upImageView;

@property (nonatomic, strong) UIImageView *downImageView;

@end

@implementation BookManagerMainHeaderViewButton

- (instancetype)initWithFrame:(CGRect)frame title:(NSString *)title {
    self = [super initWithFrame:frame];
    if (self) {
        [self setupUIWithTitle:title];
    }
    return self;
}

- (void)layoutSubviews {
    [super layoutSubviews];
    self.upImageView.frame = CGRectMake(self.titleLabel.frame.size.width + self.titleLabel.frame.origin.x - 2, self.frame.size.height / 2 - 10, 10, 10);
    self.downImageView.frame = CGRectMake(self.titleLabel.frame.size.width + self.titleLabel.frame.origin.x - 2, self.frame.size.height / 2, 10, 10);
}

- (void)setupUIWithTitle:(NSString *)title {
    [self setTitle:title.length ? title : @"" forState:UIControlStateNormal];
    UIColor *textColor = [UIColor colorWithRed:116.0/256.0 green:116.0/256.0 blue:116.0/256.0 alpha:1];
    [self setTitleColor:textColor forState:UIControlStateNormal];
    self.titleLabel.text = title.length ? title : @"";
    [self.titleLabel setFont:[UIFont systemFontOfSize:13.f]];
    [self.titleLabel sizeToFit];
    
    self.upImageView = [[UIImageView alloc] init];
    self.upImageView.image = [UIImage imageNamed:@"icon-up-normal"];
    [self addSubview:self.upImageView];
    
    self.downImageView = [[UIImageView alloc] init];
    self.downImageView.image = [UIImage imageNamed:@"icon-down-normal"];
    [self addSubview:self.downImageView];
    
    [self layoutIfNeeded];
}

- (void)refreshIconImage {
    self.iconState = !self.iconState;
    if (self.iconState == YES) {
        self.upImageView.image = [UIImage imageNamed:@"icon-up-selected"];
        self.downImageView.image = [UIImage imageNamed:@"icon-down-normal"];
    } else {
        self.upImageView.image = [UIImage imageNamed:@"icon-up-normal"];
        self.downImageView.image = [UIImage imageNamed:@"icon-down-selected"];
    }
}

- (void)cleanSelectedState {
    self.upImageView.image = [UIImage imageNamed:@"icon-up-normal"];
    self.downImageView.image = [UIImage imageNamed:@"icon-down-normal"];
}

- (BOOL)getRefreshDirectionState {
    return self.iconState;
}

@end
