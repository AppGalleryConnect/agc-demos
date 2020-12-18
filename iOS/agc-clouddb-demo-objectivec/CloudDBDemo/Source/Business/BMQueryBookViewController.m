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

#import "BMQueryBookViewController.h"

#import "UIView+Toast.h"

#import "BMAddDataTableViewCell.h"
#import "BMQueryBookPriceTableViewCell.h"
#import "BMQueryBookDataModel.h"
#import "CloudDBManager.h"
#import "BMMainViewController.h"

@interface BMQueryBookViewController () <UITableViewDelegate, UITableViewDataSource>

@property (nonatomic, strong) UITableView *searchTableView;

@property (nonatomic, strong) NSArray *titleArray;

@property (nonatomic, strong) UIView *footerView;

@property (nonatomic, strong) BMQueryBookDataModel *dataModel;

@end

@implementation BMQueryBookViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.title = NSLocalizedString(@"QueryBookTitle", nil);
    
    [self.view addSubview:self.searchTableView];
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
    if (indexPath.row == 1) {
        BMQueryBookPriceTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"BMQueryBookPriceTableViewCellInde"];
        cell.delegate = self.dataModel;
        return cell;
    }
    
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
    
    return cell;
}

- (void)tableView:(UITableView *)tableView willPerformPreviewActionForMenuWithConfiguration:(UIContextMenuConfiguration *)configuration animator:(id)animator {
}

#pragma mark - private method
- (void)queryBookAction {
    if ([CloudDBManager shareInsatnce].isLogin == NO) {
        [self.view makeToast:@"Please login first"];
        return;
    }
    
    __weak typeof(self) wself = self;
    [[CloudDBManager shareInsatnce] fuzzyQueryAGCDataWithBookInfo:self.dataModel results:^(NSArray *_Nonnull bookList, NSError *error) {
        if (error) {
            NSString *errorString = [NSString stringWithFormat:@"Query failed with error： %@", error];
            dispatch_async(dispatch_get_main_queue(), ^{
                [wself.view makeToast:errorString];
            });
        } else {
            if (bookList.count > 0 ) {
                dispatch_async(dispatch_get_main_queue(), ^{
                    BMMainViewController *listCtr = [[BMMainViewController alloc] init];
                    listCtr.queryList = bookList;
                    [wself.navigationController pushViewController:listCtr animated:YES];
                });
            } else {
                dispatch_async(dispatch_get_main_queue(), ^{
                    [wself.view makeToast:@"No data was queried"];
                });
            }
        }
    }];
}

- (void)cancelAction {
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark - setters && getters
- (UITableView *)searchTableView {
    if (!_searchTableView) {
        _searchTableView = [[UITableView alloc] initWithFrame:self.view.bounds style:UITableViewStylePlain];
        _searchTableView.delegate = self;
        _searchTableView.dataSource = self;
        _searchTableView.separatorColor = [UIColor whiteColor];
        [_searchTableView registerNib:[UINib nibWithNibName:@"BMQueryBookPriceTableViewCell" bundle:nil] forCellReuseIdentifier:@"BMQueryBookPriceTableViewCellInde"];
    }
    return _searchTableView;
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
        
        footerView.frame = CGRectMake(0, [UIScreen mainScreen].bounds.size.height - 60 - navigationHeigth - bottomSafeMargin, [UIScreen mainScreen].bounds.size.width, 60);
        _footerView = footerView;
        
        UIButton *queryButton = [[UIButton alloc] init];
        [queryButton setTitle:NSLocalizedString(@"QueryBookQueryButtonTitle", nil) forState:UIControlStateNormal];
        queryButton.frame = CGRectMake([UIScreen mainScreen].bounds.size.width / 2 - 80, 0, 80, 40);
        [queryButton setTitleColor:[UIColor redColor] forState:UIControlStateNormal];
        [queryButton addTarget:self action:@selector(queryBookAction) forControlEvents:UIControlEventTouchUpInside];
        [footerView addSubview:queryButton];
        
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
              @{ @"title": NSLocalizedString(@"MainViewHeaderPrice", nil),
                 @"textType": [NSNumber numberWithInteger:BookManagerAddBookTypePrice] },
              @{ @"title": NSLocalizedString(@"QueryBookShowCount", nil),
                 @"textType": [NSNumber numberWithInteger:BookManagerAddBookTypeQueryCount] }];
}

- (BMQueryBookDataModel *)dataModel {
    if (!_dataModel) {
        _dataModel = [[BMQueryBookDataModel alloc] init];
    }
    return _dataModel;
}

- (void)dealloc {
    NSLog(@"%s", __func__);
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
