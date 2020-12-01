//
//  BookInfo+DataManager.h
//  CloudDBDemo
//
//  Created by David Lo on 2020/10/15.
//  Copyright © 2020 gauss. All rights reserved.
//

#import "BookInfo.h"
#import "BookInfo+DataManager.h"
#import "BMAddDataTableViewCell.h"

NS_ASSUME_NONNULL_BEGIN

@interface BookInfo (DataManager) <BookManagerAddTableViewCellTextFieldDelegate>

@end

NS_ASSUME_NONNULL_END
