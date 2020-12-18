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

#import "BMLoginView.h"

#import "CloudDBManager.h"

@interface BMLoginView ()

@property (nonatomic, strong) UIImageView *iconImageView;

@property (nonatomic, strong) UILabel *tipLabel;

@property (nonatomic, strong) CAGradientLayer *gradientLayer;

@end

@implementation BMLoginView

/*
 // Only override drawRect: if you perform custom drawing.
 // An empty implementation adversely affects performance during animation.
 - (void)drawRect:(CGRect)rect {
 // Drawing code
 }
 */

- (instancetype)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        [self coreGradientLayer];
        [self setupUI];
    }
    return self;
}

- (void)layoutSubviews {
    [super layoutSubviews];
    
    self.iconImageView.frame = CGRectMake((self.frame.size.width - 40) / 2, 20, 40, 40);
    self.tipLabel.frame = CGRectMake(0, CGRectGetMaxY(self.iconImageView.frame) + 18, self.frame.size.width, 20);
}

- (void)setupUI {
    UIImageView *iconImageView = [[UIImageView alloc] init];
    iconImageView.image = [UIImage imageNamed:@"icon_account"];
    [self addSubview:iconImageView];
    
    self.iconImageView = iconImageView;
    
    UILabel *tipLabel = [[UILabel alloc] init];
    tipLabel.textAlignment = NSTextAlignmentCenter;
    tipLabel.textColor = [UIColor darkGrayColor];
    tipLabel.text = NSLocalizedString(@"LoginAdminText", nil);
    [self addSubview:tipLabel];
    
    self.tipLabel = tipLabel;
    
    self.layer.cornerRadius = 5.f;
    self.layer.masksToBounds = YES;
}

/**
 * @brief Paint a gradient background
 */
- (void)coreGradientLayer {
    CAGradientLayer *gradientLayer = [CAGradientLayer layer];
    gradientLayer.colors = @[(__bridge id)[UIColor grayColor].CGColor, (__bridge id)[UIColor whiteColor].CGColor, (__bridge id)[UIColor grayColor].CGColor];
    gradientLayer.startPoint = CGPointMake(0, 0);
    gradientLayer.endPoint = CGPointMake(1.0, 0);
    gradientLayer.frame = self.bounds;
    [self.layer insertSublayer:gradientLayer below:self.layer];
    self.gradientLayer = gradientLayer;
}

#pragma mark - publice Method
- (void)loginSuccess {
    self.tipLabel.text = @"Administrator";
}

@end
