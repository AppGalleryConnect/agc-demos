//
//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved
//

#import "UserInfoViewController.h"
#import <SDWebImage/SDWebImage.h>
#import "UserInfoTableViewCell.h"
#import "UserLinkTableViewCell.h"
#import "UserSettingsViewController.h"
#import <AGConnectAuth/AGConnectAuth.h>
#import "WeixinProvider.h"
#import "QQProvider.h"
#import "WeiboProvider.h"
#import "FacebookProvider.h"
#import "GoogleProvider.h"
#import "TwitterProvider.h"
#import "PhoneProvider.h"
#import "EmailProvider.h"
#import "SelfBuildProvider.h"
#import "AnonymousProvider.h"
#import "AppleProvider.h"

@interface UserInfoViewController ()

@property (nonatomic) NSArray *linkAccounts;

@end

@implementation UserInfoViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    self.title = @"User Info";
    UIBarButtonItem *rightButton = [[UIBarButtonItem alloc] initWithTitle:@"Settings" style:UIBarButtonItemStyleDone target:self action:@selector(showSettingMenu)];
    self.navigationItem.rightBarButtonItem = rightButton;

    [self.tableView registerNib:[UINib nibWithNibName:@"UserInfoTableViewCell" bundle:nil] forCellReuseIdentifier:@"testid"];
    self.tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
}

- (void)viewDidAppear:(BOOL)animated {
    [self.tableView reloadData];
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 1;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UserInfoTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"testid"];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    AGCUser *user = [[AGCAuth getInstance] currentUser];
    [cell.headImageView sd_setImageWithURL:[NSURL URLWithString:user.photoUrl] placeholderImage:[UIImage imageNamed:@"head_icon.png"]];
    cell.nameLabel.text = user.displayName;
    cell.idLabel.text = user.uid;
    [cell.logoutButton addTarget:self action:@selector(logout) forControlEvents:UIControlEventTouchUpInside];
    return cell;
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 290;
}

- (void)showSettingMenu {
    UIStoryboard *story = [UIStoryboard storyboardWithName:@"Main" bundle:[NSBundle mainBundle]];
    UserInfoViewController *vc = [story instantiateViewControllerWithIdentifier:@"settingVC"];
    [self.navigationController pushViewController:vc animated:YES];
}

- (void)logout {
    [[AGCAuth getInstance] signOut];
    
    UIStoryboard *story = [UIStoryboard storyboardWithName:@"Main" bundle:[NSBundle mainBundle]];
    UserInfoViewController *vc = [story instantiateViewControllerWithIdentifier:@"LoginVC"];
    [self.navigationController setViewControllers:@[vc]];
}


@end
