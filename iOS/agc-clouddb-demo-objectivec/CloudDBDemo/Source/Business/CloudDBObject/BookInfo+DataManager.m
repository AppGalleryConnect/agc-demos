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

#import "BookInfo+DataManager.h"

@implementation BookInfo (DataManager)

- (id)copyWithZone:(NSZone *)zone {
    BookInfo *info = [[BookInfo alloc] init];
    info.id = self.id;
    info.bookName = self.bookName;
    info.author = self.author;
    info.price = self.price;
    info.publisher = self.publisher;
    info.publishTime = self.publishTime;
    info.shadowFlag = self.shadowFlag;
    return info;
}


- (void)textFieldTextChange:(NSString *)text textType:(BookManagerAddBookType)textType {
    
    self.shadowFlag = [NSNumber numberWithBool:YES];
    switch (textType) {
        case BookManagerAddBookTypeBookNmae: {
            self.bookName = text;
        }
            break;
            
        case BookManagerAddBookTypeAuthor: {
            self.author = text;
        }
            break;
            
        case BookManagerAddBookTypePrice: {
            self.price = [NSNumber numberWithDouble:text.doubleValue];
        }
            break;
            
        case BookManagerAddBookTypePublishingHouse: {
            self.publisher = text;
        }
            break;
            
        case BookManagerAddBookTypePublishingTime: {
            
            NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
            formatter.dateFormat = @"yyyy-MM-dd";
            
            NSDate *date = [formatter dateFromString:text];
            self.publishTime = date;
        }
            break;
            
        default:
            break;
    }
}

@end
