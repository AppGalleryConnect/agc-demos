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
import AGConnectRemoteConfig
import HiAnalytics

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        
        // if you add user attributes condition in remote configuration, you can set user attributes like this.
        HiAnalytics .setUserProfile("favorite_color", setValue: "red")
    }


    @IBAction func navigateToMode1(_ sender: Any) {
        self.navigationController?.pushViewController(FirstModeViewController.init(), animated: true)
    }
    
    
    @IBAction func navigateToMode2(_ sender: Any) {
        self.navigationController?.pushViewController(FirstModeViewController.init(), animated: true)
    }
    
    
    @IBAction func clearData(_ sender: Any) {
        print("clear all config")
        // clear all config
        AGCRemoteConfig.sharedInstance().clearAll()
    }
}

