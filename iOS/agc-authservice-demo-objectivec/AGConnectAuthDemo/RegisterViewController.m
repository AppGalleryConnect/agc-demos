//
//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved
//

#import "RegisterViewController.h"
#import "PhoneProvider.h"
#import "EmailProvider.h"

typedef NS_ENUM(NSInteger, AccountType) {
    AccountTypePhone,
    AccountTypeEmail
};

@interface RegisterViewController ()

@property (nonatomic) AccountType accountType;

@end

@implementation RegisterViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.title = @"Register Account";
    if (_isLinkAccount) {
        self.title = @"Link Account";
        [_registerButton setTitle:@"Link" forState:UIControlStateNormal];
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
    }
}

- (IBAction)requestVerifyCode:(UIButton *)sender {
    if (_accountType == AccountTypePhone) {
        [[PhoneProvider sharedInstance] sendVerifyCodeForRegisterLogin:@"86" nationNumber:_accountText.text];
    }else if (_accountType == AccountTypeEmail){
        [[EmailProvider sharedInstance] sendVerifyCodeForRegisterLogin:_accountText.text];
    }
}

- (IBAction)register:(id)sender {
    if (!_isLinkAccount) {
        if (_accountType == AccountTypePhone) {
            [[PhoneProvider sharedInstance] createPhoneUserWithCountryCode:@"86" nationNumber:_accountText.text password:_passwordText.text verifyCode:_codeText.text];
        }else if (_accountType == AccountTypeEmail){
            [[EmailProvider sharedInstance] createUserWithEmail:_accountText.text password:_passwordText.text verifyCode:_codeText.text];
        }
    }else {
        if (_accountType == AccountTypePhone) {
            [[PhoneProvider sharedInstance] linkWithCountryCode:@"86" nationNumber:_accountText.text password:_passwordText.text verifyCode:_codeText.text];
        }else if (_accountType == AccountTypeEmail){
            [[EmailProvider sharedInstance] linkWithEmail:_accountText.text password:_passwordText.text verifyCode:_codeText.text];
        }
    }
    
}


@end
