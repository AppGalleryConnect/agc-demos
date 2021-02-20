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

#import "BMMainViewController.h"

#import "UIView+Toast.h"

#import "BMMainHeaderView.h"
#import "BMQueryBookViewController.h"
#import "BMAddBookViewController.h"
#import "BMMainBookListTableViewCell.h"
#import "CloudDBManager.h"

@interface BMMainViewController () <UITableViewDelegate, UITableViewDataSource, BMMainHeaderViewDelegate>

@property (nonatomic, strong) BMMainHeaderView *titleHeaderView;

@property (nonatomic, strong) UITableView *bookTableView;

@property (nonatomic, strong) NSMutableArray *bookList;

@end

@implementation BMMainViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.navigationItem.title = NSLocalizedString(@"MainViewTitle", nil);
    
    if (self.queryList.count) { // Query page
        [self.bookList addObjectsFromArray:self.queryList];
    } else { // Main page
        UIButton *addBookButton = [[UIButton alloc] init];
        [addBookButton setImage:[UIImage imageNamed:@"icon-add"] forState:UIControlStateNormal];
        [addBookButton.widthAnchor constraintEqualToConstant:25].active = YES;
        [addBookButton.heightAnchor constraintEqualToConstant:25].active = YES;
        [addBookButton addTarget:self action:@selector(addDataAction) forControlEvents:UIControlEventTouchUpInside];
        
        UIButton *queryBookButton = [[UIButton alloc] init];
        [queryBookButton setImage:[UIImage imageNamed:@"icon-search"] forState:UIControlStateNormal];
        [queryBookButton.widthAnchor constraintEqualToConstant:25].active = YES;
        [queryBookButton.heightAnchor constraintEqualToConstant:25].active = YES;
        [queryBookButton addTarget:self action:@selector(queryDataAction) forControlEvents:UIControlEventTouchUpInside];
        
        UIBarButtonItem *addItem = [[UIBarButtonItem alloc] initWithCustomView:addBookButton];
        UIBarButtonItem *queryItem = [[UIBarButtonItem alloc] initWithCustomView:queryBookButton];
        
        self.navigationItem.rightBarButtonItems = @[addItem, queryItem];
        
        // Add notification observer
        [[NSNotificationCenter defaultCenter] addObserver:self
                                                 selector:@selector(refreshBookList)
                                                     name:@"KNotificationRefreshBookList" object:nil];
    }
    [self.view addSubview:self.bookTableView];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    if(!self.queryList) {
        [self refreshBookList];
    }
}

#pragma mark - refresh book list
- (void)refreshBookList {
    
    __weak typeof(self) weakSelf = self;
    [[CloudDBManager shareInsatnce] queryAllBooksWithResults:^(NSArray *_Nonnull bookList, NSError *error) {
        [weakSelf.bookList removeAllObjects];
        [weakSelf.bookList addObjectsFromArray:bookList];
        dispatch_async(dispatch_get_main_queue(), ^{
            [weakSelf.bookTableView reloadData];
        });
    }];
}

#pragma mark - UIBarButtonItem Action
- (void)addDataAction {
    BMAddBookViewController *addVC = [[BMAddBookViewController alloc] init];
    addVC.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:addVC animated:YES];
}

- (void)queryDataAction {
    BMQueryBookViewController *queryVC = [[BMQueryBookViewController alloc] init];
    queryVC.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:queryVC animated:YES];
}

#pragma mark - UITableView delegate && dataSource
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.bookList.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 80;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    BMMainBookListTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"BMMainBookListTableViewCell"];
    
    BookInfo *model = [self.bookList objectAtIndex:indexPath.row];
    [cell paddingDataWithBookModel:model];
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    BMAddBookViewController *ctr = [[BMAddBookViewController alloc] init];
    BookInfo *model = [self.bookList objectAtIndex:indexPath.row];
    ctr.bookModel = model;
    ctr.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:ctr animated:YES];
}

- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath {
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        [self deleteSelectIndexPath:indexPath];
    }
}

- (void)deleteSelectIndexPath:(NSIndexPath *)indexPath {
    BookInfo *model = [self.bookList objectAtIndex:indexPath.row];
    
    [self.bookList removeObjectAtIndex:indexPath.row];
    
    __weak typeof(self) weakSelf = self;
    [[CloudDBManager shareInsatnce] deleteAGCDataWithBookID:model.id.stringValue complete:^(BOOL success, NSError *error) {
        if (success) {
            dispatch_async(dispatch_get_main_queue(), ^{
                [weakSelf.view makeToast:@"Dlete success"];
                [weakSelf.bookTableView deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationFade];
                [weakSelf.bookTableView reloadData];
            });
        } else {
            dispatch_async(dispatch_get_main_queue(), ^{
                NSString *errorStrong = [NSString stringWithFormat:@"Dlete falied with error: %@",error];
                [weakSelf.view makeToast:errorStrong];
            });
        }
    }];
}

#pragma mark UITbaleView Edit

- (void)setEditing:(BOOL)editing animated:(BOOL)animated {
    [super setEditing:editing animated:animated];
    [self.bookTableView setEditing:!self.bookTableView.isEditing animated:YES];
}

#pragma mark set can edit

- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath {
    return YES;
}

#pragma mark set edit style

- (UITableViewCellEditingStyle)tableView:(UITableView *)tableView editingStyleForRowAtIndexPath:(NSIndexPath *)indexPath {
    return UITableViewCellEditingStyleDelete;
}

- (NSString *)tableView:(UITableView *)tableView titleForDeleteConfirmationButtonForRowAtIndexPath:(NSIndexPath *)indexPath {
    return @"Delete";
}

#pragma mark - CloudDBDemoMainHeaderViewDelegate
- (void)headerButtonClickWithTag:(NSInteger)tag state:(BOOL)state {
    NSString *fieldName = @"bookName";
    
    switch (tag) {
        case 0: {
            fieldName = @"bookName";
        }
            break;
            
        case 10: {
            fieldName = @"author";
        }
            break;
            
        case 20: {
            fieldName = @"price";
        }
            break;
            
        case 30: {
            fieldName = @"publisher";
        }
            break;
            
        case 40: {
            fieldName = @"publishTime";
        }
            break;
            
        default:
            break;
    }
    // Perform sorting
    CloudDBManagerSortType sortType = state ? CloudDBManagerSortTypeAsc : CloudDBManagerSortTypeDesc;
    
    __weak typeof(self) weakSelf = self;
    [[CloudDBManager shareInsatnce] queryAGCDataWithFieldName:fieldName
                                                     sortType:sortType
                                                      results:^(NSArray *_Nonnull bookList, NSError *error) {
        if (bookList.count) {
            dispatch_async(dispatch_get_main_queue(), ^{
                [weakSelf.bookList removeAllObjects];
                [weakSelf.bookList addObjectsFromArray:bookList];
                [weakSelf.bookTableView reloadData];
            });
        } else {
            dispatch_async(dispatch_get_main_queue(), ^{
                NSString *errorStrig = [NSString stringWithFormat:@"Query failed with error :%@", error];
                [weakSelf.view makeToast:errorStrig];
            });
        }
    }];
}

#pragma mark - getters && setters
- (BMMainHeaderView *)titleHeaderView {
    if (!_titleHeaderView) {
        CGRect frame = CGRectMake(0, 0, [UIScreen mainScreen].bounds.size.width, 50);
        
        NSArray *titleArray = @[NSLocalizedString(@"MainViewHeaderName", nil),
                                NSLocalizedString(@"MainViewHeaderAuthor", nil),
                                NSLocalizedString(@"MainViewHeaderPrice", nil),
                                NSLocalizedString(@"MainViewHeaderPublisher", nil),
                                NSLocalizedString(@"MainViewHeaderTime", nil)];
        
        _titleHeaderView = [[BMMainHeaderView alloc] initWithFrame:frame titles:titleArray];
        _titleHeaderView.delegate = self;
    }
    return _titleHeaderView;
}

- (UITableView *)bookTableView {
    if (!_bookTableView) {
        CGRect frame = CGRectMake(0, self.view.bounds.origin.y, self.view.bounds.size.width,
                                  self.view.bounds.size.height - self.tabBarController.tabBar.bounds.size.height);
        _bookTableView = [[UITableView alloc] initWithFrame:frame style:UITableViewStyleGrouped];
        _bookTableView.backgroundColor = [UIColor whiteColor];
        _bookTableView.delegate = self;
        _bookTableView.dataSource = self;
        _bookTableView.tableHeaderView = self.titleHeaderView;
        [_bookTableView registerNib:[UINib nibWithNibName:@"BMMainBookListTableViewCell" bundle:nil] forCellReuseIdentifier:@"BMMainBookListTableViewCell"];
    }
    return _bookTableView;
}

- (NSMutableArray *)bookList {
    if (!_bookList) {
        _bookList = [NSMutableArray array];
    }
    return _bookList;
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
