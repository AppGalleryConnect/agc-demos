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

#import "BMAddBookViewController.h"

#import "UIView+Toast.h"

#import "BMAddDataTableViewCell.h"
#import "CloudDBManager.h"

@interface BMAddBookViewController () <UITableViewDelegate, UITableViewDataSource>

@property (nonatomic, strong) UITableView *addBookTableView;

@property (nonatomic, copy) NSArray *titleArray;

@property (nonatomic, strong) UIView *footerView;

@property (nonatomic, strong) BookInfo *dataModel;

@end

@implementation BMAddBookViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.title = NSLocalizedString(@"AddBookAddTitle", nil);
    
    if (self.bookModel) {
        self.title = NSLocalizedString(@"AddBookEditTitle", nil);
        self.dataModel = self.bookModel.copy;
    }
    
    [self.view addSubview:self.addBookTableView];
    [self.view addSubview:self.footerView];
    
    UITapGestureRecognizer *tapGestureRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(keyboardHide:)];
    tapGestureRecognizer.cancelsTouchesInView = NO;
    [self.view addGestureRecognizer:tapGestureRecognizer];
}

- (void)keyboardHide:(UITapGestureRecognizer *)tap {
    [self.view endEditing:YES];
}

#pragma mark - UITableView delegate && dataSource
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.titleArray.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 50;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *cellIdentifier = @"BMAddTestTableViewCellIdentifier";
    BMAddDataTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier];
    if (!cell) {
        cell = [[BMAddDataTableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
    }
    NSDictionary *dic = (NSDictionary *)[self.titleArray objectAtIndex:indexPath.row];
    NSString *title = dic[@"title"];
    NSNumber *typeNumber = dic[@"textType"];
    BookManagerAddBookType type = (BookManagerAddBookType)typeNumber.integerValue;
    cell.delegate = self.dataModel;
    [cell setTitleWithText:title textType:type];
    
    if (self.bookModel) {
        [cell setContentTextWithBookInfo:self.bookModel index:indexPath.row];
    }
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    return cell;
}

#pragma mark - private method
- (void)addBookAction {
    if ([CloudDBManager shareInsatnce].isLogin == NO) {
        [self.view makeToast:@"Please login first"];
        return;
    }
    __weak typeof(self) weakSelf = self;
    if (!self.bookModel) { // insert book
        [[CloudDBManager shareInsatnce] executeUpsertWithBook:self.dataModel complete:^(BOOL success, NSError *error) {
            if (success) {
                [[NSNotificationCenter defaultCenter] postNotificationName:@"KNotificationRefreshBookList" object:nil];
                dispatch_async(dispatch_get_main_queue(), ^{
                    [weakSelf.navigationController popViewControllerAnimated:YES];
                });
            } else {
                NSString *errorString = [NSString stringWithFormat:@"Add failed with error：%@", error];
                dispatch_async(dispatch_get_main_queue(), ^{
                    [weakSelf.view makeToast:errorString];
                });
            }
        }];
    } else { // update book
        [[CloudDBManager shareInsatnce] executeUpdateWithBook:self.dataModel complete:^(BOOL success, NSError *error) {
            if (success) {
                [[NSNotificationCenter defaultCenter] postNotificationName:@"KNotificationRefreshBookList" object:nil];
                dispatch_async(dispatch_get_main_queue(), ^{
                    [weakSelf.navigationController popViewControllerAnimated:YES];
                });
            } else {
                NSString *errorString = [NSString stringWithFormat:@"Add failure：%@", error];
                
                dispatch_async(dispatch_get_main_queue(), ^{
                    [weakSelf.view makeToast:errorString];
                });
            }
        }];
    }
}

- (void)cancelAction {
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark - setters && getters
- (UITableView *)addBookTableView {
    if (!_addBookTableView) {
        _addBookTableView = [[UITableView alloc] initWithFrame:self.view.bounds style:UITableViewStylePlain];
        _addBookTableView.delegate = self;
        _addBookTableView.dataSource = self;
        _addBookTableView.separatorColor = [UIColor whiteColor];
    }
    return _addBookTableView;
}

- (UIView *)footerView {
    if (!_footerView) {
        UIView *footerView = [[UIView alloc] init];
        
        CGFloat navigationHeigth = 44 + 20;
        CGFloat bottomSafeMargin = 0;
        if ([UIScreen mainScreen].bounds.size.width >= 375.0f && [UIScreen mainScreen].bounds.size.height >= 812.0f) {
            navigationHeigth = 44 + 44;
            bottomSafeMargin = 34;
        }
        
        footerView.frame = CGRectMake(0, [UIScreen mainScreen].bounds.size.height - 60 - navigationHeigth - bottomSafeMargin,
                                      [UIScreen mainScreen].bounds.size.width, 60);
        _footerView = footerView;
        
        UIButton *addButton = [[UIButton alloc] init];
        [addButton setTitle:NSLocalizedString(@"AddBookAddButtonTitle", nil) forState:UIControlStateNormal];
        if (self.bookModel) {
            [addButton setTitle:NSLocalizedString(@"AddBookEditButtonTitle", nil) forState:UIControlStateNormal];
        }
        addButton.frame = CGRectMake([UIScreen mainScreen].bounds.size.width / 2 - 80, 0, 80, 40);
        [addButton setTitleColor:[UIColor redColor] forState:UIControlStateNormal];
        [addButton addTarget:self action:@selector(addBookAction) forControlEvents:UIControlEventTouchUpInside];
        [footerView addSubview:addButton];
        
        UIButton *cancelButton = [[UIButton alloc] init];
        cancelButton.frame = CGRectMake([UIScreen mainScreen].bounds.size.width / 2, 0, 80, 40);
        [cancelButton setTitle:NSLocalizedString(@"AddBookCancelButtonTitle", nil) forState:UIControlStateNormal];
        [cancelButton setTitleColor:[UIColor redColor] forState:UIControlStateNormal];
        [cancelButton addTarget:self action:@selector(cancelAction) forControlEvents:UIControlEventTouchUpInside];
        [footerView addSubview:cancelButton];
    }
    return _footerView;
}

- (NSArray *)titleArray {
    return @[ @{ @"title": NSLocalizedString(@"MainViewHeaderName", nil),
                 @"textType": [NSNumber numberWithInteger:BookManagerAddBookTypeBookNmae] },
              @{ @"title": NSLocalizedString(@"MainViewHeaderAuthor", nil),
                 @"textType": [NSNumber numberWithInteger:BookManagerAddBookTypeAuthor] },
              @{ @"title": NSLocalizedString(@"MainViewHeaderPrice", nil),
                 @"textType": [NSNumber numberWithInteger:BookManagerAddBookTypePrice] },
              @{ @"title": NSLocalizedString(@"MainViewHeaderPublisher", nil),
                 @"textType": [NSNumber numberWithInteger:BookManagerAddBookTypePublishingHouse] },
              @{ @"title": NSLocalizedString(@"MainViewHeaderTime", nil),
                 @"textType": [NSNumber numberWithInteger:BookManagerAddBookTypePublishingTime] }];
}

- (BookInfo *)dataModel {
    if (!_dataModel) {
        _dataModel = [[BookInfo alloc] init];
    }
    return _dataModel;
}

- (void)dealloc {
    NSLog(@"%s", __func__);
}

@end
