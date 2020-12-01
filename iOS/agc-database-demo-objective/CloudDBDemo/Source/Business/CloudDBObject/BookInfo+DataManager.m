//
//  BookInfo+DataManager.m
//  CloudDBDemo
//
//  Created by David Lo on 2020/10/15.
//  Copyright © 2020 gauss. All rights reserved.
//

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
