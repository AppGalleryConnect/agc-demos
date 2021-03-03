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

import UIKit
import AuthenticationServices
import AGConnectAuth

class ViewController: UIViewController {
    
    @IBOutlet weak var loginTypeSwitch: UISegmentedControl!
    @IBOutlet weak var countryCodeLabel: UILabel!
    @IBOutlet weak var accountText: UITextField!
    @IBOutlet weak var codeText: UITextField!
    @IBOutlet weak var passwordText: UITextField!
    @IBOutlet weak var accountLeftConstraint: NSLayoutConstraint!
    @IBOutlet weak var appleLoginButton: ASAuthorizationAppleIDButton!
    
    static func instantiate() -> UIViewController {
        let storyboard = UIStoryboard(name: "Main", bundle: Bundle.main)
        let vc = storyboard.instantiateViewController(withIdentifier: "LoginVC")
        return vc
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        title = "Sign In"
        self.appleLoginButton.addTarget(self, action: #selector(appleLogin(_:)), for: UIControl.Event.touchUpInside)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        // check if a user is logged in
        if AGCAuth.instance().currentUser != nil {
            self.navigationController?.viewControllers = [UserInfoViewController.instantiate()]
        }
    }
    
    @IBAction func switchLoginType(_ sender: Any) {
        let index = loginTypeSwitch.selectedSegmentIndex
        countryCodeLabel.isHidden = index==1;
        accountLeftConstraint.constant = index==1 ? 0 : 40;
        accountText.text = nil;
        accountText.placeholder = index==1 ? "please enter email":"please enter phone number";
    }
    
    @IBAction func login(_ sender: Any) {
        if loginTypeSwitch.selectedSegmentIndex == 0 {
            phoneLogin()
        }else{
            emailLogin()
        }
    }
    
    func phoneLogin() {
        let countryCode = "86"
        let phoneNumber = accountText.text ?? ""
        let password = passwordText.text ?? ""
        let verificationCode = codeText.text ?? ""
        if verificationCode.count == 0 {
            // Generate a credential to sign in phone account with password
            let credential = AGCPhoneAuthProvider.credential(withCountryCode: countryCode, phoneNumber: phoneNumber, password: password)
            signIn(credential: credential)
        }else{
            // Generate a credential to sign in phone account with verification code
            let credential = AGCPhoneAuthProvider.credential(withCountryCode: countryCode, phoneNumber: phoneNumber, password: password, verifyCode: verificationCode)
            signIn(credential: credential)
        }
    }
    
    func emailLogin() {
        let email = accountText.text ?? ""
        let password = passwordText.text ?? ""
        let verificationCode = codeText.text ?? ""
        if verificationCode.count == 0 {
            // Generate a credential to sign in to email account with password
            let credential = AGCEmailAuthProvider.credential(withEmail: email, password: password)
            signIn(credential: credential)
        }else{
            // Generate a credential to sign in to email account with verification code
            let credential = AGCEmailAuthProvider.credential(withEmail: email, password: password, verifyCode: verificationCode)
            signIn(credential: credential)
        }
    }
    
    // send sign in request with credential
    func signIn(credential:AGCAuthCredential?) {
        if let credential = credential {
            AGCAuth.instance().signIn(credential: credential).onSuccess { (result) in
                print("sign in success")
                self.navigationController?.viewControllers = [UserInfoViewController.instantiate()]
            }.onFailure { (error) in
                print("sign in failed")
            }
        }else{
            print("no credential")
        }
    }
    
    @IBAction func registerAccount(_ sender: Any) {
        if loginTypeSwitch.selectedSegmentIndex == 0 {
            registerPhoneAccount()
        }else{
            registerEmailAccount()
        }
    }
    
    func registerPhoneAccount() {
        let countryCode = "86"
        let phoneNumber = accountText.text ?? ""
        let password = passwordText.text ?? ""
        let verificationCode = codeText.text ?? ""
        // register phone account
        AGCAuth.instance().createUser(withCountryCode: countryCode, phoneNumber: phoneNumber, password: password, verifyCode: verificationCode).onSuccess { (result) in
            print("account register success")
        }.onFailure { (error) in
            print("account register failed")
        }
    }
    
    func registerEmailAccount() {
        let email = accountText.text ?? ""
        let password = passwordText.text ?? ""
        let verificationCode = codeText.text ?? ""
        // register email account
        AGCAuth.instance().createUser(withEmail: email, password: password, verifyCode: verificationCode).onSuccess { (result) in
            print("account register success")
        }.onFailure { (error) in
            print("account register failed")
        }
    }
    
    @IBAction func requestVerifyCode(_ sender: Any) {
        if loginTypeSwitch.selectedSegmentIndex == 0 {
            if let phone = accountText.text {
                // send verification code to phone
                BaseProvider.sendVerifyCode(countryCode: "86", phoneNumber: phone, action: AGCVerifyCodeAction.registerLogin)
            }else{
                print("please enter phone number")
            }
        }else{
            if let email = accountText.text {
                // send verification code to email
                BaseProvider.sendVerifyCode(email: email, action: AGCVerifyCodeAction.registerLogin)
            }else{
                print("please enter email")
            }
        }
    }
    

    @IBAction func anonymousLogin(_ sender: Any) {
        // sign in anonymously
        AGCAuth.instance().signInAnonymously().onSuccess { (result) in
            print("sign in success")
            self.navigationController?.viewControllers = [UserInfoViewController.instantiate()]
        }.onFailure { (error) in
            print("sign in failed")
        }
    }
    
    @objc func appleLogin(_ sender: Any) {
        // Generate a credential to sign in to Apple account
        AppleProvider.sharedInstance.fetchCredential(controller: self) { (credential) in
            self.signIn(credential: credential)
        }
    }
    
    @IBAction func wechatLogin(_ sender: Any) {
        // Generate a credential to sign in to Wechat account
        WechatProvider.sharedInstance.fetchCredential(controller: self) { (credential) in
            self.signIn(credential: credential)
        }
    }
    
    @IBAction func weiboLogin(_ sender: Any) {
        // Generate a credential to sign in to Weibo account
        WeiboProvider.sharedInstance.fetchCredential(controller: self) { (credential) in
            self.signIn(credential: credential)
        }
    }
    
    @IBAction func qqLogin(_ sender: Any) {
        // Generate a credential to sign in to QQ account
        QQProvider.sharedInstance.fetchCredential(controller: self) { (credential) in
            self.signIn(credential: credential)
        }
    }
    
    @IBAction func facebookLogin(_ sender: Any) {
        // Generate a credential to sign in to Facebook account
        FacebookProvider.sharedInstance.fetchCredential(controller: self) { (credential) in
            self.signIn(credential: credential)
        }
    }
    
    @IBAction func googleLogin(_ sender: Any) {
        // Generate a credential to sign in to Google account
        GoogleProvider.sharedInstance.fetchCredential(controller: self) { (credential) in
            self.signIn(credential: credential)
        }
    }
    
    @IBAction func twitterLogin(_ sender: Any) {
        // Generate a credential to sign in to Twitter account
        TwitterProvider.sharedInstance.fetchCredential(controller: self) { (credential) in
            self.signIn(credential: credential)
        }
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.view.endEditing(true)
    }
}

