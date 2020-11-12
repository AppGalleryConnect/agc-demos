//
//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved
//

#import "ViewController.h"
#import "ToastUtil.h"
#import "RegisterViewController.h"
#import "UserInfoViewController.h"

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

typedef NS_ENUM(NSInteger, AccountType) {
    AccountTypePhone,
    AccountTypeEmail,
    AccountTypeSelfBuild
};

@interface ViewController () <SignInDelegate>

@property (nonatomic) AccountType accountType;
@property (nonatomic) BOOL isVerifyCodeLogin;

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.title = @"Sign In";
    
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    if ([[AGCAuth getInstance] currentUser]) {
        [self.navigationController setViewControllers:@[[UserInfoViewController new]]];
    }
}

- (IBAction)switchValueChanged:(UISegmentedControl *)sender {
    _accountType = sender.selectedSegmentIndex;
    _countryCodeLabel.hidden = _accountType;
    _accountLeftConstraint.constant = _accountType == AccountTypePhone ? 40 : 0;
    _accountText.text = nil;
    if (_accountType == AccountTypePhone) {
        _accountText.placeholder = @"please enter phone number";
    }else if (_accountType == AccountTypeEmail) {
        _accountText.placeholder = @"please enter email";
    }else {
        _accountText.placeholder = @"please enter self build account";
    }
}

- (IBAction)changeLoginType:(UIButton *)sender {
    sender.selected = !sender.selected;
    
    _isVerifyCodeLogin = sender.selected;
    _codeText.hidden = !_isVerifyCodeLogin;
    _verifyCodeButton.hidden = !_isVerifyCodeLogin;
}

- (IBAction)login:(id)sender {
    if (_accountType == AccountTypePhone) {
        [PhoneProvider sharedInstance].signInDelegate = self;
        [[PhoneProvider sharedInstance] loginWithCountryCode:@"86" nationNumber:_accountText.text password:_passwordText.text verifyCode:_codeText.text];
    }else if (_accountType == AccountTypeEmail){
        [EmailProvider sharedInstance].signInDelegate = self;
        [[EmailProvider sharedInstance] loginWithEmail:_accountText.text password:_passwordText.text verifyCode:_codeText.text];
    }else if (_accountType == AccountTypeSelfBuild){
        [SelfBuildProvider sharedInstance].signInDelegate = self;
        [[SelfBuildProvider sharedInstance] loginWithAccount:_accountText.text password:_passwordText.text verifyCode:_codeText.text];
    }
}

- (IBAction)registerAccount:(id)sender {
    UIStoryboard *story = [UIStoryboard storyboardWithName:@"Main" bundle:[NSBundle mainBundle]];
    RegisterViewController *vc = [story instantiateViewControllerWithIdentifier:@"RegisterVC"];
    [self.navigationController pushViewController:vc animated:YES];
}

- (IBAction)requestVerifyCode:(UIButton *)sender {
    if (_accountType == AccountTypePhone) {
        if (_accountText.text.length == 0) {
            [ToastUtil showToast:@"please enter phone number"];
        }
        [[PhoneProvider sharedInstance] sendVerifyCodeForRegisterLogin:@"86" nationNumber:_accountText.text];
    }else if (_accountType == AccountTypeEmail){
        if (_accountText.text.length == 0) {
            [ToastUtil showToast:@"please enter email"];
        }
        [[EmailProvider sharedInstance] sendVerifyCodeForRegisterLogin:_accountText.text];
    }else if (_accountType == AccountTypeSelfBuild){
        
    }
}

- (IBAction)anonymousLogin:(id)sender {
    [[AnonymousProvider sharedInstance] loginWithViewController:self];
}

- (IBAction)appleLogin:(id)sender {
    [[AppleProvider sharedInstance] loginWithViewController:self];
}

- (IBAction)wechatLogin:(id)sender {
    [[WeixinProvider sharedInstance] loginWithViewController:self];
}

- (IBAction)weiboLogin:(id)sender {
    [[WeiboProvider sharedInstance] loginWithViewController:self];
}

- (IBAction)qqLogin:(id)sender {
    [[QQProvider sharedInstance] loginWithViewController:self];
}

- (IBAction)facebookLogin:(id)sender {
    [[FacebookProvider sharedInstance] loginWithViewController:self];
}

- (IBAction)googleLogin:(id)sender {
    [[GoogleProvider sharedInstance] loginWithViewController:self];
}

- (IBAction)twitterLogin:(id)sender {
    [[TwitterProvider sharedInstance] loginWithViewController:self];
}

#pragma mark - SignInDelegate
- (void)signInFailed {
    [ToastUtil showToast:@"sign in failed"];
}

- (void)signInSucceed {
    [self.navigationController setViewControllers:@[[UserInfoViewController new]]];
}


- (void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event {
    [self.view endEditing:YES];
}

@end
