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

#import "BMMainHeaderView.h"

#import "BMMainHeaderViewButton.h"

@interface BMMainHeaderView ()<UIScrollViewDelegate>

@property (nonatomic, copy) NSArray *titles;

@property (nonatomic, strong) UIScrollView *scrollView;

@property (nonatomic, strong) NSMutableArray *buttonArray;

@end

@implementation BMMainHeaderView

- (instancetype)initWithFrame:(CGRect)frame titles:(NSArray *_Nonnull)titles {
    self = [super initWithFrame:frame];
    if (self) {
        _titles = titles;
        [self setupUI];
    }
    return self;
}

- (void)setupUI {
    self.buttonArray = [NSMutableArray array];
    
    CGFloat buttonWidth = self.frame.size.width / self.titles.count;
    for (int i = 0; i < self.titles.count; ++i) {
        CGRect frame = CGRectMake(i * buttonWidth, 0, buttonWidth, self.frame.size.height);
        NSString *title = self.titles[i];
        BMMainHeaderViewButton *titleButton = [[BMMainHeaderViewButton alloc] initWithFrame:frame title:title];
        titleButton.tag = i * 10;
        [titleButton addTarget:self action:@selector(titleButtonClickAction:) forControlEvents:UIControlEventTouchUpInside];
        [self addSubview:titleButton];
        //save button
        [self.buttonArray addObject:titleButton];
    }
}

#pragma mark - button click action
- (void)titleButtonClickAction:(BMMainHeaderViewButton *)button {
    [button refreshIconImage];
    // deal current button state and other button state
    for (UIButton *btn in self.buttonArray) {
        if ([btn isKindOfClass:[BMMainHeaderViewButton class]]) {
            if (btn.tag != button.tag) {
                BMMainHeaderViewButton *headerButton = (BMMainHeaderViewButton *)btn;
                [headerButton cleanSelectedState];
            }
        }
    }
    
    // deal current button click action
    if ([self.delegate respondsToSelector:@selector(headerButtonClickWithTag:state:)]) {
        [self.delegate headerButtonClickWithTag:button.tag state:button.iconState];
    }
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
