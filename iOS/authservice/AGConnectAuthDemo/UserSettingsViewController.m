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

#import "UserSettingsViewController.h"
#import "BaseProvider.h"

@interface UserSettingsViewController ()

// account settings
@property (nonatomic) NSArray *settingsArray;

@end

@implementation UserSettingsViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.title = @"Settings";
    [self.tableView registerClass:[UITableViewCell class] forCellReuseIdentifier:@"settingCellId"];
    AGCAuthProviderType currentProvider = [[AGCAuth getInstance] currentUser].providerId;
    if (currentProvider == AGCAuthProviderTypeEmail) {
        _settingsArray = @[@"Update User Profile", @"Update Email", @"Update Email Password"];
    }else if (currentProvider == AGCAuthProviderTypePhone) {
        _settingsArray = @[@"Update User Profile", @"Update Phone", @"Update Phone Password"];
    }else{
        _settingsArray = @[@"Update User Profile"];
    }
}

#pragma mark - UITableViewDataSource
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return _settingsArray.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"settingCellId" forIndexPath:indexPath];
    cell.textLabel.text = [_settingsArray objectAtIndex:indexPath.row];
    return cell;
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
    }else if ([title isEqualToString:@"Update Email Password"]) {
        [self updateEmailPassword];
    }else if ([title isEqualToString:@"Update Phone Password"]) {
        [self updatePhonePassword];
    }
}

- (void)updateProfile {
    [self showAlertWithTitle:@"Update Profile" input:@[@"new displayName", @"new photoUrl"] completion:^(NSArray<NSString *> * _Nonnull inputArray) {
        NSString *name = inputArray[0];
        NSString *photo = inputArray[1];
        AGCProfileRequest *request = [AGCProfileRequest new];
        request.displayName = name;
        request.photoUrl = photo;
        // update the profile of the current user
        [[[[[AGCAuth getInstance] currentUser] updateProfile:request] addOnSuccessCallback:^(id  _Nullable result) {
            NSLog(@"profile update success");
        }] addOnFailureCallback:^(NSError * _Nonnull error) {
            NSLog(@"profile update failed : %@",error);
        }];
    }];
}

- (void)updateEmail {
    [self showAlertWithTitle:@"Send Verification Code" input:@[@"new email"] completion:^(NSArray<NSString *> * _Nonnull inputArray) {
        NSString *email = inputArray[0];
        [BaseProvider sendVerifyCodeWithEmail:email action:AGCVerifyCodeActionRegisterLogin];
        [self showAlertWithTitle:@"Update Email" input:@[@"new email", @"verification code"] completion:^(NSArray<NSString *> * _Nonnull inputArray) {
            NSString *email = inputArray[0];
            NSString *code = inputArray[1];
            // update the email of the current user
            [[[[[AGCAuth getInstance] currentUser] updateEmail:email verifyCode:code] addOnSuccessCallback:^(id  _Nullable result) {
                NSLog(@"email update success");
            }] addOnFailureCallback:^(NSError * _Nonnull error) {
                NSLog(@"email update failed : %@",error);
            }];
        }];
    }];
}

- (void)updatePhone {
    [self showAlertWithTitle:@"Send Verification Code" input:@[@"country code", @"new phone number"] completion:^(NSArray<NSString *> * _Nonnull inputArray) {
        NSString *countryCode = inputArray[0];
        NSString *phoneNumber = inputArray[1];
        [BaseProvider sendVerifyCodeWithCountryCode:countryCode phoneNumber:phoneNumber action:AGCVerifyCodeActionRegisterLogin];
        [self showAlertWithTitle:@"Update Phone" input:@[@"country code", @"new phone number", @"verification code"] completion:^(NSArray<NSString *> * _Nonnull inputArray) {
            NSString *countryCode = inputArray[0];
            NSString *phoneNumber = inputArray[1];
            NSString *code = inputArray[2];
            // update the phone number of the current user
            [[[[[AGCAuth getInstance] currentUser] updatePhoneWithCountryCode:countryCode phoneNumber:phoneNumber verifyCode:code] addOnSuccessCallback:^(id  _Nullable result) {
                NSLog(@"phone update success");
            }] addOnFailureCallback:^(NSError * _Nonnull error) {
                NSLog(@"phone update failed : %@",error);
            }];
        }];
    }];
}

- (void)updatePhonePassword {
    [self showAlertWithTitle:@"Send Verification Code" input:@[@"country code", @"phone number"] completion:^(NSArray<NSString *> * _Nonnull inputArray) {
        NSString *countryCode = inputArray[0];
        NSString *phoneNumber = inputArray[1];
        [BaseProvider sendVerifyCodeWithCountryCode:countryCode phoneNumber:phoneNumber action:AGCVerifyCodeActionResetPassword];
        [self showAlertWithTitle:@"Update Phone Password" input:@[@"new password" , @"verification code"] completion:^(NSArray<NSString *> * _Nonnull inputArray) {
            NSString *password = inputArray[0];
            NSString *code = inputArray[1];
            // update the password of the current user
            [[[[[AGCAuth getInstance] currentUser] updatePassword:password verifyCode:code provider:AGCAuthProviderTypePhone] addOnSuccessCallback:^(id  _Nullable result) {
                NSLog(@"password update success");
            }] addOnFailureCallback:^(NSError * _Nonnull error) {
                NSLog(@"password update failed : %@",error);
            }];
        }];
    }];
}

- (void)updateEmailPassword {
    [self showAlertWithTitle:@"Send Verification Code" input:@[@"email"] completion:^(NSArray<NSString *> * _Nonnull inputArray) {
        NSString *email = inputArray[0];
        [BaseProvider sendVerifyCodeWithEmail:email action:AGCVerifyCodeActionResetPassword];
        [self showAlertWithTitle:@"Update Email Password" input:@[@"new password" , @"verification code"] completion:^(NSArray<NSString *> * _Nonnull inputArray) {
            NSString *password = inputArray[0];
            NSString *code = inputArray[1];
            // update the password of the current user
            [[[[[AGCAuth getInstance] currentUser] updatePassword:password verifyCode:code provider:AGCAuthProviderTypeEmail] addOnSuccessCallback:^(id  _Nullable result) {
                NSLog(@"password update success");
            }] addOnFailureCallback:^(NSError * _Nonnull error) {
                NSLog(@"password update failed : %@",error);
            }];
        }];
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
