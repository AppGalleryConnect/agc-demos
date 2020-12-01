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

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@protocol BMMainHeaderViewDelegate <NSObject>

/**
 * @brief button click  delegate  action
 * @param tag Button identification
 * @param state up or down
 */
- (void)headerButtonClickWithTag:(NSInteger)tag state:(BOOL)state;

@end

@interface BMMainHeaderView : UIView

@property (nonatomic, weak) id<BMMainHeaderViewDelegate> delegate;

- (instancetype)initWithFrame:(CGRect)frame titles:(NSArray *_Nonnull)titles;

@end

NS_ASSUME_NONNULL_END
