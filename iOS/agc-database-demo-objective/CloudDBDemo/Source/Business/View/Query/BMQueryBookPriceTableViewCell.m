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

#import "BMQueryBookPriceTableViewCell.h"

@interface BMQueryBookPriceTableViewCell () <UITextFieldDelegate>

@property (weak, nonatomic) IBOutlet UILabel *nameTitleLabel;
@property (weak, nonatomic) IBOutlet UITextField *contentTextFieldOne;

@property (weak, nonatomic) IBOutlet UITextField *contentTextFieldTwo;

@property (weak, nonatomic) IBOutlet UIView *lineViewOne;

@property (weak, nonatomic) IBOutlet UIView *lineViewTwo;

@end

@implementation BMQueryBookPriceTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    
    self.nameTitleLabel.text = NSLocalizedString(@"MainViewHeaderPrice", nil);
    // Initialization code
    [self.contentTextFieldOne addTarget:self action:@selector(editingDidBegin:) forControlEvents:UIControlEventEditingDidBegin];
    [self.contentTextFieldOne addTarget:self action:@selector(editingDidChange:) forControlEvents:UIControlEventEditingChanged];
    [self.contentTextFieldOne addTarget:self action:@selector(editingDidEnd:) forControlEvents:UIControlEventEditingDidEnd];
    
    [self.contentTextFieldTwo addTarget:self action:@selector(editingDidBegin:) forControlEvents:UIControlEventEditingDidBegin];
    [self.contentTextFieldTwo addTarget:self action:@selector(editingDidChange:) forControlEvents:UIControlEventEditingChanged];
    [self.contentTextFieldTwo addTarget:self action:@selector(editingDidEnd:) forControlEvents:UIControlEventEditingDidEnd];
}

- (void)editingDidBegin:(UITextField *)textField {
    if ([textField isEqual:self.contentTextFieldOne]) {
        self.lineViewOne.backgroundColor = [UIColor redColor];
    } else {
        self.lineViewTwo.backgroundColor = [UIColor redColor];
    }
}

- (void)editingDidChange:(UITextField *)textField {
    if ([self.delegate respondsToSelector:@selector(textFieldTextChange:tag:)]) {
        [self.delegate textFieldTextChange:textField.text tag:textField.tag];
    }
}

- (void)editingDidEnd:(UITextField *)textField {
    //    self.lineView.backgroundColor = [UIColor lightGrayColor];
    if ([textField isEqual:self.contentTextFieldOne]) {
        self.lineViewOne.backgroundColor = [UIColor lightGrayColor];
    } else {
        self.lineViewTwo.backgroundColor = [UIColor lightGrayColor];
    }
}

@end
