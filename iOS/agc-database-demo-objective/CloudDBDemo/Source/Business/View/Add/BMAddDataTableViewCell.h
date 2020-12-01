//
//  BMAddTestTableViewCell.h
//  CloudDBDemo
//
//  Created by Joker on 2020/8/4.
//  Copyright © 2020 gauss. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

typedef NS_ENUM(NSInteger, BookManagerAddBookType) {
    BookManagerAddBookTypeBookNmae,
    BookManagerAddBookTypeAuthor,
    BookManagerAddBookTypePrice,
    BookManagerAddBookTypePublishingHouse,
    BookManagerAddBookTypePublishingTime,
    BookManagerAddBookTypeQueryCount
};

@class BookInfo;

@protocol BookManagerAddTableViewCellTextFieldDelegate <NSObject>

- (void)textFieldTextChange:(NSString *)text textType:(BookManagerAddBookType)textType;

@end

@interface BMAddDataTableViewCell : UITableViewCell

@property (nonatomic, weak) id<BookManagerAddTableViewCellTextFieldDelegate> delegate;

- (void)setTitleWithText:(NSString *)text textType:(BookManagerAddBookType)textType;

- (void)setContentTextWithBookInfo:(BookInfo *)bookInfo index:(NSInteger)index;

@end

NS_ASSUME_NONNULL_END
