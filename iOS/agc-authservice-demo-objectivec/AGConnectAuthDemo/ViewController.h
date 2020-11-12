//
//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved
//

#import <UIKit/UIKit.h>
#import <AuthenticationServices/AuthenticationServices.h>

@interface ViewController : UIViewController

@property (weak, nonatomic) IBOutlet UISegmentedControl *topSwitch;
@property (weak, nonatomic) IBOutlet UILabel *countryCodeLabel;
@property (weak, nonatomic) IBOutlet UITextField *accountText;
@property (weak, nonatomic) IBOutlet UITextField *codeText;
@property (weak, nonatomic) IBOutlet UITextField *passwordText;
@property (weak, nonatomic) IBOutlet UIButton *verifyCodeButton;
@property (weak, nonatomic) IBOutlet UIButton *signInButton;
@property (weak, nonatomic) IBOutlet UIButton *typeChangeButton;
@property (weak, nonatomic) IBOutlet UIButton *registerButton;
@property (weak, nonatomic) IBOutlet ASAuthorizationAppleIDButton *appleButton;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *accountLeftConstraint;

@end

