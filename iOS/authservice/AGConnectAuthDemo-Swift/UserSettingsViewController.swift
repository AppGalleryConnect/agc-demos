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

class UserSettingsViewController: UITableViewController {
    
    // account settings
    var settingsArray:[String] = []
    

    override func viewDidLoad() {
        super.viewDidLoad()
        self.title = "Settings"
        self.tableView.register(UITableViewCell.self, forCellReuseIdentifier: "settingCellId")
        let currentProvider = AGCAuth.instance().currentUser?.providerId
        if currentProvider == AGCAuthProviderType.email {
            settingsArray = ["Update User Profile", "Update Email", "Update Email Password"]
        }else if currentProvider == AGCAuthProviderType.phone {
            settingsArray = ["Update User Profile", "Update Phone", "Update Phone Password"]
        }else{
            settingsArray = ["Update User Profile"]
        }
    }
    

    // MARK: UITableViewDataSource
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return settingsArray.count
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "settingCellId", for: indexPath)
        cell.textLabel?.text = settingsArray[indexPath.row]
        return cell
    }
    
    // MARK: UITableViewDelegate
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        let title = settingsArray[indexPath.row]
        switch title {
        case "Update User Profile":
            updateProfile()
        case "Update Email":
            updateEmail()
        case "Update Phone":
            updatePhone()
        case "Update Email Password":
            updateEmailPassword()
        case "Update Phone Password":
            updatePhonePassword()
        default:
            print("none")
        }
    }
    
    func updateProfile() {
        showAlert(title: "Update Profile", input: ["new displayName", "new photoUrl"]) { (inputArray) in
            let name = inputArray[0]
            let photo = inputArray[1]
            let request = AGCProfileRequest()
            request.displayName = name
            request.photoUrl = photo
            // update the profile of the current user
            AGCAuth.instance().currentUser?.updateProfile(request).onSuccess { (result) in
                print("profile update success")
            }.onFailure { (error) in
                print("profile update failed")
            }
        }
    }
    
    func updateEmail() {
        showAlert(title: "Send Verification Code", input: ["new email"]) { (inputArray) in
            let email = inputArray[0]
            BaseProvider.sendVerifyCode(email: email, action: AGCVerifyCodeAction.registerLogin)
            self.showAlert(title: "Update Email", input: ["new email", "verification code"]) { (inputArray) in
                let email = inputArray[0]
                let code = inputArray[1]
                // update the email of the current user
                AGCAuth.instance().currentUser?.updateEmail(email, verifyCode: code).onSuccess { (result) in
                    print("email update success")
                }.onFailure { (error) in
                    print("email update failed")
                }
            }
        }
    }
    
    func updatePhone() {
        showAlert(title: "Send Verification Code", input: ["country code", "new phone number"]) { (inputArray) in
            let countryCode = inputArray[0]
            let phoneNumber = inputArray[1]
            BaseProvider.sendVerifyCode(countryCode: countryCode, phoneNumber: phoneNumber, action: AGCVerifyCodeAction.registerLogin)
            self.showAlert(title: "Update Phone", input: ["country code", "new phone number", "verification code"]) { (inputArray) in
                let countryCode = inputArray[0]
                let phoneNumber = inputArray[1]
                let code = inputArray[2]
                // update the phone number of the current user
                AGCAuth.instance().currentUser?.updatePhone(withCountryCode: countryCode, phoneNumber: phoneNumber, verifyCode: code).onSuccess { (result) in
                    print("phone update success")
                }.onFailure { (error) in
                    print("phone update failed")
                }
            }
        }
    }
    
    func updateEmailPassword() {
        showAlert(title: "Send Verification Code", input: ["email"]) { (inputArray) in
            let email = inputArray[0]
            BaseProvider.sendVerifyCode(email: email, action: AGCVerifyCodeAction.resetPassword)
            self.showAlert(title: "Update Email Password", input: ["new password" , "verification code"]) { (inputArray) in
                let password = inputArray[0]
                let code = inputArray[1]
                // update the password of the current user
                AGCAuth.instance().currentUser?.updatePassword(password, verifyCode: code, provider: AGCAuthProviderType.email.rawValue).onSuccess { (result) in
                    print("password update success")
                }.onFailure { (error) in
                    print("password update failed")
                }
            }
        }
    }
    
    func updatePhonePassword() {
        showAlert(title: "Send Verification Code", input: ["country code", "phone number"]) { (inputArray) in
            let countryCode = inputArray[0]
            let phoneNumber = inputArray[1]
            BaseProvider.sendVerifyCode(countryCode: countryCode, phoneNumber: phoneNumber, action: AGCVerifyCodeAction.resetPassword)
            self.showAlert(title: "Update Phone Password", input: ["new password" , "verification code"]) { (inputArray) in
                let password = inputArray[0]
                let code = inputArray[1]
                // update the password of the current user
                AGCAuth.instance().currentUser?.updatePassword(password, verifyCode: code, provider: AGCAuthProviderType.phone.rawValue).onSuccess { (result) in
                    print("password update success")
                }.onFailure { (error) in
                    print("password update failed")
                }
            }
        }
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
