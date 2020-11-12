//
//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved
//

#import "UserSettingsViewController.h"
#import "BaseProvider.h"
#import "UserLinkTableViewCell.h"
#import "RegisterViewController.h"
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


@interface LinkItem : NSObject

@property(nonatomic)NSString *name;

@property(nonatomic)NSInteger providerType;

@property(nonatomic)BOOL isLinked;

@end

@implementation LinkItem

@end

LinkItem *item(NSString *name, NSInteger providerType, BOOL isLinked) {
    LinkItem *linkItem = [[LinkItem alloc] init];
    linkItem.name = name;
    linkItem.providerType = providerType;
    linkItem.isLinked = isLinked;
    return linkItem;
}


@interface UserSettingsViewController ()

// account settings
@property (nonatomic) NSArray *settingsArray;
// link accounts
@property (nonatomic) NSArray *linkAccounts;
// current account type
@property (nonatomic) NSInteger providerId;

@end

@implementation UserSettingsViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.title = @"Settings";
    [self.tableView registerClass:[UITableViewCell class] forCellReuseIdentifier:@"settingcellid"];
    _settingsArray = @[@"Update User Profile"];
    _providerId = [[AGCAuth getInstance] currentUser].providerId;
    if (_providerId == AGCAuthProviderTypeEmail) {
        _settingsArray = @[@"Update User Profile", @"Update Email", @"Update Password"];
    }else if (_providerId == AGCAuthProviderTypePhone) {
        _settingsArray = @[@"Update User Profile", @"Update Phone", @"Update Password"];
    }
    
    _linkAccounts = @[item(@"Wechat", AGCAuthProviderTypeWeiXin, NO), item(@"QQ", AGCAuthProviderTypeQQ, NO), item(@"Weibo", AGCAuthProviderTypeWeiBo, NO), item(@"Facebook", AGCAuthProviderTypeFacebook, NO), item(@"Google", AGCAuthProviderTypeGoogle, NO), item(@"Twitter", AGCAuthProviderTypeTwitter, NO), item(@"Phone", AGCAuthProviderTypePhone, NO), item(@"Email", AGCAuthProviderTypeEmail, NO), item(@"Apple", AGCAuthProviderTypeApple, NO)];
    [self refreshLinkState];
}

- (void)refreshLinkState {
    NSMutableArray *linkedAccounts = [NSMutableArray array];
    NSArray *providers = [[[AGCAuth getInstance] currentUser] providerInfo];
    for (NSDictionary *provider in providers) {
        NSNumber *providerId = (NSNumber *)[provider objectForKey:@"provider"];
        if (providerId) {
            [linkedAccounts addObject:providerId.stringValue];
        }
    }
    for (LinkItem *item in _linkAccounts) {
        if ([linkedAccounts containsObject:[NSString stringWithFormat:@"%ld",item.providerType]]) {
            item.isLinked = YES;
        }
    }
}

#pragma mark - UITableViewDataSource

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 2;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if (section == 0) {
        return _settingsArray.count;
    }else {
        return _linkAccounts.count;
    }
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    if (indexPath.section == 0) {
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"settingcellid" forIndexPath:indexPath];
        cell.textLabel.text = [_settingsArray objectAtIndex:indexPath.row];
        return cell;
    }else {
        UserLinkTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"userlinkcellid"];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        LinkItem *item = [_linkAccounts objectAtIndex:indexPath.row];
        cell.descLabel.text = item.name;
        [cell.linkButton addTarget:self action:@selector(linkAccount:) forControlEvents:UIControlEventTouchUpInside];
        cell.linkButton.tag = indexPath.row;
        if (item.isLinked) {
            [cell.linkButton setTitle:@"Unlink" forState:UIControlStateNormal];
        }else {
            [cell.linkButton setTitle:@"Link" forState:UIControlStateNormal];
        }
        
        return cell;
    }
    
}

#pragma mark - UITableViewDelegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:YES];

    NSString *title = (NSString *)[_settingsArray objectAtIndex:indexPath.row];
    if ([title isEqualToString:@"Update User Profile"]) {
        [self updateProfile];
    }else if ([title isEqualToString:@"Update Email"]) {
        [self updateEmail];
    }else if ([title isEqualToString:@"Update Phone"]) {
        [self updatePhone];
    }else if ([title isEqualToString:@"Update Password"] && _providerId == AGCAuthProviderTypeEmail) {
        [self updateEmailPassword];
    }else if ([title isEqualToString:@"Update Password"] && _providerId == AGCAuthProviderTypePhone) {
        [self updatePhonePassword];
    }
}



- (void)updateProfile {
    [self showTextFieldAlert:@[@"new displayName", @"new photoUrl"] successHandler:^(NSArray<NSString *> * _Nonnull inputArray) {
        NSString *name = inputArray[0];
        NSString *photo = inputArray[1];
        [BaseProvider updateProfileWithDisplayName:name photoUrl:photo];
    } verifyCodeHandler:nil];
}

- (void)updateEmail {
    
    [self showTextFieldAlert:@[@"new email", @"verification code"]
              successHandler:^(NSArray<NSString *> * _Nonnull inputArray) {
        NSString *email = inputArray[0];
        NSString *code = inputArray[1];
        [[EmailProvider sharedInstance] updateEmail:email verifyCode:code];
    } verifyCodeHandler:^(NSArray<NSString *> * _Nonnull inputArray) {
        NSString *email = inputArray[0];
        [[EmailProvider sharedInstance] sendVerifyCodeForResetPassword:email];
    }];
}

- (void)updatePhone {
    [self showTextFieldAlert:@[@"new phone number", @"verification code"]
              successHandler:^(NSArray<NSString *> * _Nonnull inputArray) {
        NSString *countryCode = @"86";
        NSString *nationNumber = inputArray[0];
        NSString *code = inputArray[1];
        [[PhoneProvider sharedInstance] updatePhoneWithCountryCode:countryCode nationNumber:nationNumber verifyCode:code];
    } verifyCodeHandler:^(NSArray<NSString *> * _Nonnull inputArray) {
        NSString *countryCode = @"86";
        NSString *phoneNum = inputArray[0];
        [[PhoneProvider sharedInstance] sendVerifyCodeForRegisterLogin:countryCode nationNumber:phoneNum];
    }];
}

- (void)updatePhonePassword {
    [self showTextFieldAlert:@[@"phone number", @"new password" , @"verification code"]
              successHandler:^(NSArray<NSString *> * _Nonnull inputArray) {
        NSString *password = inputArray[1];
        NSString *code = inputArray[2];
        [[PhoneProvider sharedInstance] updatePassword:password verifyCode:code];
    } verifyCodeHandler:^(NSArray<NSString *> * _Nonnull inputArray) {
        NSString *phone = inputArray[0];
        [[PhoneProvider sharedInstance] sendVerifyCodeForRegisterLogin:@"86" nationNumber:phone];
    }];
}

- (void)updateEmailPassword {
    [self showTextFieldAlert:@[@"email", @"new password" , @"verification code"]
              successHandler:^(NSArray<NSString *> * _Nonnull inputArray) {
        NSString *password = inputArray[1];
        NSString *code = inputArray[2];
        [[EmailProvider sharedInstance] updatePassword:password verifyCode:code];
    } verifyCodeHandler:^(NSArray<NSString *> * _Nonnull inputArray) {
        NSString *email = inputArray[0];
        [[EmailProvider sharedInstance] sendVerifyCodeForResetPassword:email];
    }];
}

- (void)linkAccount:(UIButton *)btn {
    if ([btn.titleLabel.text isEqualToString:@"Unlink"]) {
        [self unlinkAccount:btn.tag];
        return;
    }
    
    switch (btn.tag) {
        case 0:
            [[WeixinProvider sharedInstance] linkWithViewController:self];
            break;
        case 1:
            [[QQProvider sharedInstance] linkWithViewController:self];
            break;
        
        case 2:
            [[WeiboProvider sharedInstance] linkWithViewController:self];
            break;
        
        case 3:
            [[FacebookProvider sharedInstance] linkWithViewController:self];
            break;
        
        case 4:
            [[GoogleProvider sharedInstance] linkWithViewController:self];
            break;
            
        case 5:
            [[TwitterProvider sharedInstance] linkWithViewController:self];
            break;
            
        case 6:
        case 7:
        {
            UIStoryboard *story = [UIStoryboard storyboardWithName:@"Main" bundle:[NSBundle mainBundle]];
            RegisterViewController *vc = [story instantiateViewControllerWithIdentifier:@"RegisterVC"];
            vc.isLinkAccount = YES;
            [self.navigationController pushViewController:vc animated:YES];
            break;
        }
            
            
        case 8:
            [[AppleProvider sharedInstance] linkWithViewController:self];
            break;
            
        default:
            
            break;
    }
}

- (void)unlinkAccount:(NSInteger)row {
    switch (row) {
        case 0:
            [[WeixinProvider sharedInstance] unlink];
            break;
        case 1:
            [[QQProvider sharedInstance] unlink];
            break;
        
        case 2:
            [[WeiboProvider sharedInstance] unlink];
            break;
        
        case 3:
            [[FacebookProvider sharedInstance] unlink];
            break;
        
        case 4:
            [[GoogleProvider sharedInstance] unlink];
            break;
            
        case 5:
            [[TwitterProvider sharedInstance] unlink];
            break;
            
        case 6:
            [[PhoneProvider sharedInstance] unlink];
            break;
        
        case 7:
            [[EmailProvider sharedInstance] unlink];
            break;
            
        case 8:
            [[AppleProvider sharedInstance] unlink];
            break;
            
        default:
            
            break;
    }
}

// show alert view with text fields
- (void)showTextFieldAlert:(NSArray<NSString *> *)textPlaceholders successHandler:(void (^)(NSArray<NSString *> * _Nonnull))handler verifyCodeHandler:(void(^)(NSArray<NSString *> * _Nonnull))verifyHandler  {
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"Input" message:nil preferredStyle:UIAlertControllerStyleAlert];
    // add ok button
    [alertController addAction:[UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        NSMutableArray *res = [NSMutableArray arrayWithCapacity:alertController.textFields.count];
        for (UITextField *textField in alertController.textFields) {
            NSString *input = textField.text ?: @"";
            [res addObject:input];
        }
        if (handler) {
            handler(res);
        }
    }]];
    // add button
    if (verifyHandler) {
        [alertController addAction:[UIAlertAction actionWithTitle:@"Send Verify Code" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            NSMutableArray *res = [NSMutableArray arrayWithCapacity:alertController.textFields.count];
            for (UITextField *textField in alertController.textFields) {
                NSString *input = textField.text ?: @"";
                [res addObject:input];
            }
            if (verifyHandler) {
                verifyHandler(res);
            }
        }]];
    }
    // add cancel button
    [alertController addAction:[UIAlertAction actionWithTitle:@"Cancel" style:UIAlertActionStyleDefault handler:nil]];
    // add textfield
    for (NSString *text in textPlaceholders) {
        [alertController addTextFieldWithConfigurationHandler:^(UITextField * _Nonnull textField) {
          textField.placeholder = text;
        }];
    }
    [self presentViewController:alertController animated:true completion:nil];
}

@end
