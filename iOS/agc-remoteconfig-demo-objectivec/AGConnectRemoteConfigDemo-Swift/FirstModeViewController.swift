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

class FirstModeViewController: ViewController {

    @IBOutlet weak var label: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // the following code demonstrates how to fetch config and apply immediately.
        
        // apply default config if you want
        let defaultConfig:[String : Any] = ["test1":"value1", "test2":2]
        AGCRemoteConfig.sharedInstance().applyDefaults(defaultConfig)
        
        // fetch config
        AGCRemoteConfig.sharedInstance().fetch().onSuccess { (result) in
            print("fetch successfully")
            // apply the config when fetch is successful
            if let result = result {
                AGCRemoteConfig.sharedInstance().apply(result)
            }
            
            // get all applied config and show it in label
            let appliedConfig = AGCRemoteConfig.sharedInstance().getMergedAll()
            self.label.text = appliedConfig.description;
        }.onFailure { (error) in
            print("fetch failed")
        }
    }
}
