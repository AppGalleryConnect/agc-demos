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

class UserInfoViewController: UIViewController {
    
    @IBOutlet weak var nameLabel: UILabel!
    
    @IBOutlet weak var idLabel: UILabel!
    
    static func instantiate() -> UIViewController {
        let storyboard = UIStoryboard(name: "Main", bundle: Bundle.main)
        let vc = storyboard.instantiateViewController(withIdentifier: "UserInfoVC")
        return vc
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        self.title = "User Info"
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
        // Get the currently logged-in user
        let user = AGCAuth.instance().currentUser
        refreshView(with: user)
    }
    
    func refreshView(with user:AGCUser?) {
        nameLabel.text = user?.displayName
        idLabel.text = user?.uid
    }
    
    @IBAction func signOut(_ sender: Any) {
        // sign out
        AGCAuth.instance().signOut()
        
        self.navigationController?.viewControllers = [ViewController.instantiate()]
    }
    
    @IBAction func showSettings(_ sender: Any) {
        self.navigationController?.pushViewController(UserSettingsViewController(), animated: true)
    }
    
    @IBAction func linkAccounts(_ sender: Any) {
        self.navigationController?.pushViewController(UserLinkViewController(), animated: true)
    }
}
