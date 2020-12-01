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

#import "BMMainBookListTableViewCell.h"

@interface BMMainBookListTableViewCell ()

@property (weak, nonatomic) IBOutlet UILabel *bookName;
@property (weak, nonatomic) IBOutlet UILabel *bookAuthor;
@property (weak, nonatomic) IBOutlet UILabel *bookPrice;
@property (weak, nonatomic) IBOutlet UILabel *bookPublishHoue;
@property (weak, nonatomic) IBOutlet UILabel *bookPublishTime;

@end

@implementation BMMainBookListTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)paddingDataWithBookModel:(BookInfo *)bookModel {
    self.bookName.text = bookModel.bookName;
    self.bookAuthor.text = bookModel.author;
    self.bookPrice.text = [NSString stringWithFormat:@"%.2f", [bookModel.price doubleValue]];
    self.bookPublishHoue.text = bookModel.publisher;
    
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    formatter.dateFormat = @"yyyy-MM-dd";
    
    NSString *dateString = [formatter stringFromDate:bookModel.publishTime];
    
    self.bookPublishTime.text = dateString;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];
    
    // Configure the view for the selected state
}

@end
