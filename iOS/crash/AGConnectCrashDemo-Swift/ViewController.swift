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
import AGConnectCrash

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
    }

    @IBAction func enableCrash(_ sender: Any) {
        // Enable crash collection and reporting
        AGCCrash.sharedInstance().enableCrashCollection(enable: true)
    }
    
    @IBAction func disableCrash(_ sender: Any) {
        // Disable crash collection and reporting
        AGCCrash.sharedInstance().enableCrashCollection(enable: false)
    }
    
    @IBAction func testException(_ sender: Any) {
        // Create a crash, you can view the crash information on the appgallery connect website after restarting
        AGCCrash.sharedInstance().testIt()
    }
    
    @IBAction func recordNonfatalException(_ sender: Any) {
        // catch the error thrown by your function
        // or generate the error as needed.
        let yourErrorDomain = "your_error_domain"
        let yourErrorCode = 0
        let yourErrorInfo = [String : Any]()
        let error = NSError.init(domain: yourErrorDomain, code: yourErrorCode, userInfo: yourErrorInfo)
        // record error.
        AGCCrash.sharedInstance().recordError(error)
    }
    
}

