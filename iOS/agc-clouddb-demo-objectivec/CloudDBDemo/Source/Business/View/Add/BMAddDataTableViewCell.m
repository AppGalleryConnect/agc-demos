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

#import "BMAddDataTableViewCell.h"

#import "BookInfo.h"

@interface BMAddDataTableViewCell () <UITextFieldDelegate>

@property (nonatomic, strong) UILabel *nameLable;

@property (nonatomic, strong) UITextField *contentTextField;

@property (nonatomic, strong) UIView *lineView;

@property (nonatomic, assign) BookManagerAddBookType textType;

@property (nonatomic, strong) UIDatePicker *datePicker;

@property (nonatomic, strong) NSDateFormatter *dateFormatter;

@end

@implementation BMAddDataTableViewCell

- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        [self setupUI];
    }
    return self;
}

- (void)layoutSubviews {
    [super layoutSubviews];
    self.nameLable.frame = CGRectMake(20, (self.frame.size.height - 30) / 2, 70, 30);
    
    CGFloat lineWidth = self.frame.size.width - 20 - 30 - CGRectGetMaxX(self.nameLable.frame);
    
    self.contentTextField.frame = CGRectMake(CGRectGetMaxX(self.nameLable.frame) + 30, (self.frame.size.height - 30) / 2, lineWidth, 30);
    self.lineView.frame = CGRectMake(CGRectGetMinX(self.contentTextField.frame), CGRectGetMaxY(self.contentTextField.frame), lineWidth, 1);
}

- (void)setupUI {
    UILabel *nameLable = [[UILabel alloc] init];
    nameLable.textColor = [UIColor lightGrayColor];
    nameLable.font = [UIFont systemFontOfSize:16.f];
    _nameLable = nameLable;
    [self.contentView addSubview:nameLable];
    
    UITextField *contentTextField = [[UITextField alloc] init];
    contentTextField.tintColor = [UIColor redColor];
    contentTextField.font = [UIFont systemFontOfSize:17.f];
    contentTextField.delegate = self;
    _contentTextField = contentTextField;
    [self.contentView addSubview:contentTextField];
    
    UIView *lineView = [[UIView alloc] init];
    lineView.backgroundColor = [UIColor lightGrayColor];
    _lineView = lineView;
    [self.contentView addSubview:lineView];
    
    [self.contentTextField addTarget:self action:@selector(editingDidChange:) forControlEvents:UIControlEventEditingChanged];
}

#pragma mark - publice method
- (void)setTitleWithText:(NSString *)text textType:(BookManagerAddBookType)textType {
    self.textType = textType;
    self.nameLable.text = text;
    switch (textType) {
        case BookManagerAddBookTypeBookNmae:
        case BookManagerAddBookTypeAuthor:
        case BookManagerAddBookTypePublishingHouse: {
            self.contentTextField.keyboardType = UIKeyboardTypeDefault;
        }
            break;
            
        case BookManagerAddBookTypePrice: {
            self.contentTextField.keyboardType = UIKeyboardTypeNumberPad;
        }
            break;
            
        case BookManagerAddBookTypePublishingTime: {
            NSDate *today = [NSDate date];
            NSString *todayString = [self.dateFormatter stringFromDate:today];
            self.contentTextField.text = todayString;
            if ([self.delegate respondsToSelector:@selector(textFieldTextChange:textType:)]) {
                [self.delegate textFieldTextChange:todayString textType:textType];
            }
        }
            break;
            
        case BookManagerAddBookTypeQueryCount: {
            self.contentTextField.keyboardType = UIKeyboardTypeNumberPad;
        }
            break;
            
        default: {
            self.contentTextField.keyboardType = UIKeyboardTypeDefault;
        }
            break;
    }
}

- (void)setContentTextWithBookInfo:(BookInfo *)bookInfo index:(NSInteger)index {
    if (!bookInfo) {
        return;
    }
    if (index == 0) {
        [self.contentTextField setText:bookInfo.bookName];
    }
    if (index == 1) {
        [self.contentTextField setText:bookInfo.author];
    }
    if (index == 2) {
        [self.contentTextField setText:[NSString stringWithFormat:@"%.2f", bookInfo.price.doubleValue]];
    }
    if (index == 3) {
        [self.contentTextField setText:bookInfo.publisher];
    }
    if (index == 4) {
        NSString *dateString = [self.dateFormatter stringFromDate:bookInfo.publishTime];
        [self.contentTextField setText:dateString];
    }
}

#pragma mark - TextField delegate
- (void)textFieldDidBeginEditing:(UITextField *)textField {
    self.lineView.backgroundColor = [UIColor redColor];
    
    if (self.textType == BookManagerAddBookTypePublishingTime) {
        textField.inputView = self.datePicker;
    }
}

- (void)editingDidChange:(UITextField *)textField {
    if ([self.delegate respondsToSelector:@selector(textFieldTextChange:textType:)]) {
        [self.delegate textFieldTextChange:textField.text textType:self.textType];
    }
}

- (void)textFieldDidEndEditing:(UITextField *)textField {
    self.lineView.backgroundColor = [UIColor lightGrayColor];
}

#pragma mark - private Action
- (void)sureAction {
}

- (void)cancleAction {
    self.datePicker.hidden = YES;
}

#pragma mark - setter && getter
- (UIDatePicker *)datePicker {
    if (!_datePicker) {
        _datePicker = [[UIDatePicker alloc] init];
        [_datePicker setLocale:[[NSLocale alloc] initWithLocaleIdentifier:@"EN"]];
        [_datePicker setDatePickerMode:UIDatePickerModeDate];
        [_datePicker setDate:[NSDate date] animated:YES];
        [_datePicker addTarget:self action:@selector(onDatePickerChanged:) forControlEvents:UIControlEventValueChanged];
    }
    return _datePicker;
}

- (NSDateFormatter *)dateFormatter {
    if (!_dateFormatter) {
        _dateFormatter = [[NSDateFormatter alloc] init];
        _dateFormatter.dateFormat = @"yyyy-MM-dd";
    }
    return _dateFormatter;
}

- (void)onDatePickerChanged:(UIDatePicker *)picker {
    NSDate *date = picker.date;
    NSString *dateString = [self.dateFormatter stringFromDate:date];
    self.contentTextField.text = dateString;
    if ([self.delegate respondsToSelector:@selector(textFieldTextChange:textType:)]) {
        [self.delegate textFieldTextChange:dateString textType:self.textType];
    }
}

@end
