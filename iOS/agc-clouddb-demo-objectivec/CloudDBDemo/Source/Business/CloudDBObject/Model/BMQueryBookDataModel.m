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

#import "BMQueryBookDataModel.h"

@implementation BMQueryBookDataModel

- (void)textFieldTextChange:(NSString *)text tag:(NSInteger)tag {
    if (tag == 11) { //left textField
        self.minBookPrice = [NSNumber numberWithDouble:text.doubleValue];
    } else { //right textField
        self.maxBookPrice = [NSNumber numberWithDouble:text.doubleValue];
    }
}

- (void)textFieldTextChange:(NSString *)text textType:(BookManagerAddBookType)textType {
    switch (textType) {
        case BookManagerAddBookTypeBookNmae: {
            self.bookName = text;
        }
            break;
            
        case BookManagerAddBookTypeQueryCount: {
            self.count = text.integerValue;
        }
            break;
            
        default:
            break;
    }
}

@end
