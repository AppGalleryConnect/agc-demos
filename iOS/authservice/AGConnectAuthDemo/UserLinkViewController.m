/*
    Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

#import "UserLinkViewController.h"
#import "BaseProvider.h"
#import "WechatProvider.h"
#import "QQProvider.h"
#import "WeiboProvider.h"
#import "FacebookProvider.h"
#import "GoogleProvider.h"
#import "TwitterProvider.h"
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


@interface UserLinkViewController ()

// link accounts
@property (nonatomic) NSArray *linkAccounts;

@end

@implementation UserLinkViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.title = @"Link Accounts";
    [self.tableView registerClass:[UITableViewCell class] forCellReuseIdentifier:@"linkCellId"];
    
    _linkAccounts = @[item(@"Wechat", AGCAuthProviderTypeWeiXin, NO),
                      item(@"QQ", AGCAuthProviderTypeQQ, NO),
                      item(@"Weibo", AGCAuthProviderTypeWeiBo, NO),
                      item(@"Facebook", AGCAuthProviderTypeFacebook, NO),
                      item(@"Google", AGCAuthProviderTypeGoogle, NO),
                      item(@"Twitter", AGCAuthProviderTypeTwitter, NO),
                      item(@"Phone", AGCAuthProviderTypePhone, NO),
                      item(@"Email", AGCAuthProviderTypeEmail, NO),
                      item(@"Apple", AGCAuthProviderTypeApple, NO)];
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
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return _linkAccounts.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"linkCellId" forIndexPath:indexPath];
    LinkItem *item = [_linkAccounts objectAtIndex:indexPath.row];
    cell.textLabel.text = [NSString stringWithFormat:@"%@ %@", item.isLinked ? @"Unlink":@"Link", item.name];
    return cell;
}

#pragma mark - UITableViewDelegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    LinkItem *item = [_linkAccounts objectAtIndex:indexPath.row];
    if (item.isLinked) {
        [self unlinkAccount:item.providerType];
    }else{
        [self linkAccount:indexPath.row];
    }
}

- (void)linkAccount:(NSInteger)row {
    switch (row) {
        case 0:
        {
            [[WechatProvider sharedInstance] fetchCredentialWithController:self completion:^(AGCAuthCredential * _Nullable credential) {
                [self linkAccountWithCredential:credential];
            }];
            break;
        }
        case 1:
        {
            [[QQProvider sharedInstance] fetchCredentialWithController:self completion:^(AGCAuthCredential * _Nullable credential) {
                [self linkAccountWithCredential:credential];
            }];
            break;
        }
        case 2:
        {
            [[WeiboProvider sharedInstance] fetchCredentialWithController:self completion:^(AGCAuthCredential * _Nullable credential) {
                [self linkAccountWithCredential:credential];
            }];
        }
            break;
        
        case 3:
        {
            [[FacebookProvider sharedInstance] fetchCredentialWithController:self completion:^(AGCAuthCredential * _Nullable credential) {
                [self linkAccountWithCredential:credential];
            }];
            break;
        }
        case 4:
        {
            [[GoogleProvider sharedInstance] fetchCredentialWithController:self completion:^(AGCAuthCredential * _Nullable credential) {
                [self linkAccountWithCredential:credential];
            }];
            break;
        }
        case 5:
        {
            [[TwitterProvider sharedInstance] fetchCredentialWithController:self completion:^(AGCAuthCredential * _Nullable credential) {
                [self linkAccountWithCredential:credential];
            }];
            break;
        }
        case 6:
        {
            [self linkPhoneAccount];
            break;
        }
        case 7:
        {
            [self linkEmailAccount];
            break;
        }
        case 8:
        {
            [[AppleProvider sharedInstance] fetchCredentialWithController:self completion:^(AGCAuthCredential * _Nullable credential) {
                [self linkAccountWithCredential:credential];
            }];
            break;
        }
        default:
            break;
    }
}

- (void)linkAccountWithCredential:(AGCAuthCredential *)credential {
    // link accounts
    [[[[[AGCAuth getInstance] currentUser] link:credential] addOnSuccessCallback:^(AGCSignInResult * _Nullable result) {
        NSLog(@"link success");
    }] addOnFailureCallback:^(NSError * _Nonnull error) {
        NSLog(@"link failed");
    }];
}

- (void)linkPhoneAccount {
    [self showAlertWithTitle:@"Send Verification Code" input:@[@"country code", @"phone number"] completion:^(NSArray<NSString *> * _Nonnull inputArray) {
        NSString *countryCode = inputArray[0];
        NSString *phoneNumber = inputArray[1];
        [BaseProvider sendVerifyCodeWithCountryCode:countryCode phoneNumber:phoneNumber action:AGCVerifyCodeActionRegisterLogin];
        [self showAlertWithTitle:@"Link Phone Account" input:@[@"country code", @"phone number", @"password" , @"verification code"] completion:^(NSArray<NSString *> * _Nonnull inputArray) {
            NSString *countryCode = inputArray[0];
            NSString *phoneNumber = inputArray[1];
            NSString *password = inputArray[2];
            NSString *verificationCode = inputArray[3];
            AGCAuthCredential *credential;
            if (verificationCode.length == 0) {
                // Generate a credential to link phone account with password
                credential = [AGCPhoneAuthProvider credentialWithCountryCode:countryCode phoneNumber:phoneNumber password:password];
            }else {
                // Generate a credential to link phone account with verification code
                credential = [AGCPhoneAuthProvider credentialWithCountryCode:countryCode phoneNumber:phoneNumber password:password verifyCode:verificationCode];
            }
            // link phone account with credential
            [[[[[AGCAuth getInstance] currentUser] link:credential] addOnSuccessCallback:^(AGCSignInResult * _Nullable result) {
                NSLog(@"link success");
            }] addOnFailureCallback:^(NSError * _Nonnull error) {
                NSLog(@"link failed");
            }];
        }];
    }];
}

- (void)linkEmailAccount {
    [self showAlertWithTitle:@"Send Verification Code" input:@[@"email"] completion:^(NSArray<NSString *> * _Nonnull inputArray) {
        NSString *email = inputArray[0];
        [BaseProvider sendVerifyCodeWithEmail:email action:AGCVerifyCodeActionRegisterLogin];
        [self showAlertWithTitle:@"Link Email Account" input:@[@"email", @"password" , @"verification code"] completion:^(NSArray<NSString *> * _Nonnull inputArray) {
            NSString *email = inputArray[0];
            NSString *password = inputArray[1];
            NSString *verificationCode = inputArray[2];
            AGCAuthCredential *credential;
            if (verificationCode.length == 0) {
                // Generate a credential to link to email account with password
                credential = [AGCEmailAuthProvider credentialWithEmail:email password:password];
            }else {
                // Generate a credential to link to email account with verification code
                credential = [AGCEmailAuthProvider credentialWithEmail:email password:password verifyCode:verificationCode];
            }
            // link email account with credential
            [[[[[AGCAuth getInstance] currentUser] link:credential] addOnSuccessCallback:^(AGCSignInResult * _Nullable result) {
                NSLog(@"link success");
            }] addOnFailureCallback:^(NSError * _Nonnull error) {
                NSLog(@"link failed");
            }];
        }];
    }];
}

- (void)unlinkAccount:(AGCAuthProviderType)type {
    // unlink accounts
    [[[[[AGCAuth getInstance] currentUser] unlink:type] addOnSuccessCallback:^(AGCSignInResult * _Nullable result) {
        NSLog(@"unlink success");
    }] addOnFailureCallback:^(NSError * _Nonnull error) {
        NSLog(@"unlink failed");
    }];
}

// show alert controller with text fields
- (void)showAlertWithTitle:(NSString *)title input:(NSArray<NSString *> *)placeholders completion:(void (^)(NSArray<NSString *> * _Nonnull))completeBlock {
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:title message:nil preferredStyle:UIAlertControllerStyleAlert];
    // add cancel button
    [alertController addAction:[UIAlertAction actionWithTitle:@"Cancel" style:UIAlertActionStyleDefault handler:nil]];
    // add ok button
    [alertController addAction:[UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        NSMutableArray *res = [NSMutableArray arrayWithCapacity:alertController.textFields.count];
        for (UITextField *textField in alertController.textFields) {
            NSString *input = textField.text ?: @"";
            [res addObject:input];
        }
        if (completeBlock) {
            completeBlock(res);
        }
    }]];
    // add text fields
    for (NSString *text in placeholders) {
        [alertController addTextFieldWithConfigurationHandler:^(UITextField * _Nonnull textField) {
          textField.placeholder = text;
        }];
    }
    // show alert controller
    [self presentViewController:alertController animated:true completion:nil];
}

@end
