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

#import "ViewController.h"
#import "UserInfoViewController.h"

#import "WechatProvider.h"
#import "QQProvider.h"
#import "WeiboProvider.h"
#import "FacebookProvider.h"
#import "GoogleProvider.h"
#import "TwitterProvider.h"
#import "AppleProvider.h"

@interface ViewController ()

@end

@implementation ViewController

+ (instancetype)instantiate {
    UIStoryboard *story = [UIStoryboard storyboardWithName:@"Main" bundle:[NSBundle mainBundle]];
    ViewController *vc = [story instantiateViewControllerWithIdentifier:@"LoginVC"];
    return vc;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.title = @"Sign In";
    [self.appleLoginButton addTarget:self action:@selector(appleLogin:) forControlEvents:UIControlEventTouchUpInside];
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    // check if a user is logged in
    if ([[AGCAuth getInstance] currentUser]) {
        [self.navigationController setViewControllers:@[[UserInfoViewController instantiate]]];
    }
}

- (IBAction)switchLoginType:(UISegmentedControl *)sender {
    NSInteger index = sender.selectedSegmentIndex;
    _countryCodeLabel.hidden = index;
    _accountLeftConstraint.constant = index ? 0 : 40;
    _accountText.text = nil;
    _accountText.placeholder = index ? @"please enter email":@"please enter phone number";
}

- (IBAction)login:(id)sender {
    NSInteger index = _loginTypeSwitch.selectedSegmentIndex;
    if (index == 0) {
        [self phoneLogin];
    }else{
        [self emailLogin];
    }
}

- (void)phoneLogin {
    AGCAuthCredential *credential;
    NSString *countryCode = @"86";
    NSString *phoneNumber = _accountText.text;
    NSString *password = _passwordText.text;
    NSString *verificationCode = _codeText.text;
    if (_codeText.text.length == 0) {
        // Generate a credential to sign in phone account with password
        credential = [AGCPhoneAuthProvider credentialWithCountryCode:countryCode phoneNumber:phoneNumber password:password];
    }else {
        // Generate a credential to sign in phone account with verification code
        credential = [AGCPhoneAuthProvider credentialWithCountryCode:countryCode phoneNumber:phoneNumber password:password verifyCode:verificationCode];
    }
    [self signInWithCredential:credential];
}

- (void)emailLogin {
    AGCAuthCredential *credential;
    NSString *email = _accountText.text;
    NSString *password = _passwordText.text;
    NSString *verificationCode = _codeText.text;
    if (_codeText.text.length == 0) {
        // Generate a credential to sign in to email account with password
        credential = [AGCEmailAuthProvider credentialWithEmail:email password:password];
    }else {
        // Generate a credential to sign in to email account with verification code
        credential = [AGCEmailAuthProvider credentialWithEmail:email password:password verifyCode:verificationCode];
    }
    [self signInWithCredential:credential];
}

- (void)signInWithCredential:(AGCAuthCredential *)credential {
    if (!credential) {
        NSLog(@"no credential");
    }
    // send sign in request with credential
    [[[[AGCAuth getInstance] signIn:credential] addOnSuccessCallback:^(AGCSignInResult * _Nullable result) {
        NSLog(@"sign in success");
        [self.navigationController setViewControllers:@[[UserInfoViewController instantiate]]];
    }] addOnFailureCallback:^(NSError * _Nonnull error) {
        NSLog(@"sign in failed");
    }];
}

- (IBAction)registerAccount:(id)sender {
    NSInteger index = _loginTypeSwitch.selectedSegmentIndex;
    if (index == 0) {
        [self registerPhoneAccount];
    }else{
        [self registerEmailAccount];
    }
}

- (void)registerPhoneAccount {
    NSString *countryCode = @"86";
    NSString *phoneNumber = _accountText.text;
    NSString *password = _passwordText.text;
    NSString *verificationCode = _codeText.text;
    // register phone account
    [[[[AGCAuth getInstance] createUserWithCountryCode:countryCode phoneNumber:phoneNumber password:password verifyCode:verificationCode] addOnSuccessCallback:^(AGCSignInResult * _Nullable result) {
        NSLog(@"account register success");
    }] addOnFailureCallback:^(NSError * _Nonnull error) {
        NSLog(@"account register failed : %@",error);
    }];
}

- (void)registerEmailAccount {
    NSString *email = _accountText.text;
    NSString *password = _passwordText.text;
    NSString *verificationCode = _codeText.text;
    // register email account
    [[[[AGCAuth getInstance] createUserWithEmail:email password:password verifyCode:verificationCode] addOnSuccessCallback:^(AGCSignInResult * _Nullable result) {
        NSLog(@"account register success");
    }] addOnFailureCallback:^(NSError * _Nonnull error) {
        NSLog(@"Email create failed : %@",error);
    }];
}

- (IBAction)requestVerifyCode:(UIButton *)sender {
    NSInteger index = _loginTypeSwitch.selectedSegmentIndex;
    if (index == 0) {
        if (_accountText.text.length == 0) {
            NSLog(@"please enter phone number");
        }
        // send verification code to phone
        [BaseProvider sendVerifyCodeWithCountryCode:@"86" phoneNumber:_accountText.text action:AGCVerifyCodeActionRegisterLogin];
    }else{
        if (_accountText.text.length == 0) {
            NSLog(@"please enter email");
        }
        // send verification code to email
        [BaseProvider sendVerifyCodeWithEmail:_accountText.text action:AGCVerifyCodeActionRegisterLogin];
    }
}

- (IBAction)anonymousLogin:(id)sender {
    // sign in anonymously
    [[[[AGCAuth getInstance] signInAnonymously] addOnSuccessCallback:^(AGCSignInResult * _Nullable result) {
        NSLog(@"sign in success");
        [self.navigationController setViewControllers:@[[UserInfoViewController instantiate]]];
    }] addOnFailureCallback:^(NSError * _Nonnull error) {
        NSLog(@"sign in failed");
    }];
}

- (void)appleLogin:(id)sender {
    // Generate a credential to sign in to Apple account
    [[AppleProvider sharedInstance] fetchCredentialWithController:self completion:^(AGCAuthCredential * _Nullable credential) {
        [self signInWithCredential:credential];
    }];
}

- (IBAction)wechatLogin:(id)sender {
    // Generate a credential to sign in to Wechat account
    [[WechatProvider sharedInstance] fetchCredentialWithController:self completion:^(AGCAuthCredential * _Nullable credential) {
        [self signInWithCredential:credential];
    }];
}

- (IBAction)weiboLogin:(id)sender {
    // Generate a credential to sign in to Weibo account
    [[WeiboProvider sharedInstance] fetchCredentialWithController:self completion:^(AGCAuthCredential * _Nullable credential) {
        [self signInWithCredential:credential];
    }];
}

- (IBAction)qqLogin:(id)sender {
    // Generate a credential to sign in to QQ account
    [[QQProvider sharedInstance] fetchCredentialWithController:self completion:^(AGCAuthCredential * _Nullable credential) {
        [self signInWithCredential:credential];
    }];
}

- (IBAction)facebookLogin:(id)sender {
    // Generate a credential to sign in to Facebook account
    [[FacebookProvider sharedInstance] fetchCredentialWithController:self completion:^(AGCAuthCredential * _Nullable credential) {
        [self signInWithCredential:credential];
    }];
}

- (IBAction)googleLogin:(id)sender {
    // Generate a credential to sign in to Google account
    [[GoogleProvider sharedInstance] fetchCredentialWithController:self completion:^(AGCAuthCredential * _Nullable credential) {
        [self signInWithCredential:credential];
    }];
}

- (IBAction)twitterLogin:(id)sender {
    // Generate a credential to sign in to Twitter account
    [[TwitterProvider sharedInstance] fetchCredentialWithController:self completion:^(AGCAuthCredential * _Nullable credential) {
        [self signInWithCredential:credential];
    }];
}

- (void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event {
    [self.view endEditing:YES];
}

@end
