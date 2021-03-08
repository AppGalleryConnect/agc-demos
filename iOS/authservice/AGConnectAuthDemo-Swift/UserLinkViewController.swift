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
import AGConnectAuth

struct LinkItem {
    var name:String
    var providerType:Int
    var isLinked:Bool
    init(name:String, providerType:Int, isLinked:Bool) {
        self.name = name
        self.providerType = providerType
        self.isLinked = isLinked
    }
}

class UserLinkViewController: UITableViewController {
    
    // link accounts
    var linkAccounts = [LinkItem]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.title = "Link Accounts"
        self.tableView.register(UITableViewCell.self, forCellReuseIdentifier: "linkCellId")
        
        linkAccounts = [
            LinkItem(name: "Wechat", providerType: AGCAuthProviderType.weiXin.rawValue, isLinked: false),
            LinkItem(name: "QQ", providerType: AGCAuthProviderType.QQ.rawValue, isLinked: false),
            LinkItem(name: "Weibo", providerType: AGCAuthProviderType.weiBo.rawValue, isLinked: false),
            LinkItem(name: "Facebook", providerType: AGCAuthProviderType.facebook.rawValue, isLinked: false),
            LinkItem(name: "Google", providerType: AGCAuthProviderType.google.rawValue, isLinked: false),
            LinkItem(name: "Twitter", providerType: AGCAuthProviderType.twitter.rawValue, isLinked: false),
            LinkItem(name: "Phone", providerType: AGCAuthProviderType.phone.rawValue, isLinked: false),
            LinkItem(name: "Email", providerType: AGCAuthProviderType.email.rawValue, isLinked: false),
            LinkItem(name: "Apple", providerType: AGCAuthProviderType.apple.rawValue, isLinked: false)
        ]
        refreshLinkState()
    }
    
    func refreshLinkState() {
        var linkedAccounts = [String]()
        let providers = AGCAuth.instance().currentUser?.providerInfo ?? []
        for provider in providers {
            if let providerId = provider["provider"] as? Int {
                linkedAccounts.append(String(providerId))
            }
        }
        for var item in linkAccounts {
            if linkedAccounts.contains(String(item.providerType)) {
                item.isLinked = true
            }
        }
    }

    // MARK: UITableViewDataSource
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return linkAccounts.count
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "linkCellId", for: indexPath)
        let item = linkAccounts[indexPath.row]
        cell.textLabel?.text = String(format: "%@ %@", item.isLinked ? "Unlink":"Link", item.name)
        return cell
    }
    
    // MARK: UITableViewDelegate
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        let item = linkAccounts[indexPath.row]
        if item.isLinked {
            unlinkAccount(type: AGCAuthProviderType.init(rawValue: item.providerType)!)
        }else{
            linkAccount(row: indexPath.row)
        }
    }
    
    func linkAccount(row:Int) {
        switch row {
        case 0:
            WechatProvider.sharedInstance.fetchCredential(controller: self) { (credential) in
                self.linkAccount(credential: credential)
            }
        case 1:
            QQProvider.sharedInstance.fetchCredential(controller: self) { (credential) in
                self.linkAccount(credential: credential)
            }
        case 2:
            WeiboProvider.sharedInstance.fetchCredential(controller: self) { (credential) in
                self.linkAccount(credential: credential)
            }
        case 3:
            FacebookProvider.sharedInstance.fetchCredential(controller: self) { (credential) in
                self.linkAccount(credential: credential)
            }
        case 4:
            GoogleProvider.sharedInstance.fetchCredential(controller: self) { (credential) in
                self.linkAccount(credential: credential)
            }
        case 5:
            TwitterProvider.sharedInstance.fetchCredential(controller: self) { (credential) in
                self.linkAccount(credential: credential)
            }
        case 6:
            linkPhoneAccount()
        case 7:
            linkEmailAccount()
        case 8:
            AppleProvider.sharedInstance.fetchCredential(controller: self) { (credential) in
                self.linkAccount(credential: credential)
            }
        default:
            print("none")
        }
    }
    
    func linkAccount(credential:AGCAuthCredential?) {
        if let credential = credential {
            // link accounts
            AGCAuth.instance().currentUser?.link(credential).onSuccess(callback: { (result) in
                print("link success")
            }).onFailure(callback: { (error) in
                print("link failed")
            })
        }else{
            print("link failed")
        }
        
    }
    
    func linkPhoneAccount() {
        showAlert(title: "Send Verification Code", input: ["country code", "phone number"]) { (inputArray) in
            let countryCode = inputArray[0]
            let phoneNumber = inputArray[1]
            BaseProvider.sendVerifyCode(countryCode: countryCode, phoneNumber: phoneNumber, action: AGCVerifyCodeAction.registerLogin)
            self.showAlert(title: "Link Phone Account", input: ["country code", "phone number", "password" ,"verification code"]) { (inputArray) in
                let countryCode = inputArray[0]
                let phoneNumber = inputArray[1]
                let password = inputArray[2]
                let verificationCode = inputArray[3]
                var credential : AGCAuthCredential
                if verificationCode.count == 0 {
                    // Generate a credential to link phone account with password
                    credential = AGCPhoneAuthProvider.credential(withCountryCode: countryCode, phoneNumber: phoneNumber, password: password)
                }else{
                    // Generate a credential to link phone account with verification code
                    credential = AGCPhoneAuthProvider.credential(withCountryCode: countryCode, phoneNumber: phoneNumber, password: password, verifyCode: verificationCode)
                }
                // link phone account with credential
                AGCAuth.instance().currentUser?.link(credential).onSuccess(callback: { (result) in
                    print("link success")
                }).onFailure(callback: { (error) in
                    print("link failed")
                })
            }
        }
    }
    
    func linkEmailAccount() {
        showAlert(title: "Send Verification Code", input: ["email"]) { (inputArray) in
            let email = inputArray[0]
            BaseProvider.sendVerifyCode(email: email, action: AGCVerifyCodeAction.registerLogin)
            self.showAlert(title: "Link Email Account", input: ["email", "password", "verification code"]) { (inputArray) in
                let email = inputArray[0]
                let password = inputArray[1]
                let verificationCode = inputArray[2]
                var credential : AGCAuthCredential
                if verificationCode.count == 0 {
                    // Generate a credential to link to email account with password
                    credential = AGCEmailAuthProvider.credential(withEmail: email, password: password)
                }else{
                    // Generate a credential to link to email account with verification code
                    credential = AGCEmailAuthProvider.credential(withEmail: email, password: password, verifyCode: verificationCode)
                }
                // link email account with credential
                AGCAuth.instance().currentUser?.link(credential).onSuccess(callback: { (result) in
                    print("link success")
                }).onFailure(callback: { (error) in
                    print("link failed")
                })
            }
        }
    }
    
    func unlinkAccount(type:AGCAuthProviderType) {
        // unlink accounts
        AGCAuth.instance().currentUser?.unlink(type).onSuccess(callback: { (result) in
            print("unlink success")
        }).onFailure(callback: { (error) in
            print("unlink failed")
        })
    }
    
    // show alert controller with text fields
    func showAlert(title:String, input placeholders:[String], completion: @escaping ([String])->Void) -> Void {
        let alertController = UIAlertController(title: title, message: nil, preferredStyle: UIAlertController.Style.alert)
        // add cancel button
        alertController.addAction(UIAlertAction(title: "Cancel", style: UIAlertAction.Style.default, handler: nil))
        // add ok button
        alertController.addAction(UIAlertAction(title: "OK", style: UIAlertAction.Style.default, handler: { (action) in
            var res = [String]()
            for textField in alertController.textFields! {
                res.append(textField.text ?? "")
            }
            completion(res)
        }))
        // add text fields
        for text in placeholders {
            alertController.addTextField { (textField) in
                textField.placeholder = text
            }
        }
        // show alert controller
        self.present(alertController, animated: true, completion: nil)
    }
}
