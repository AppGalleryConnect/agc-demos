//
//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved
//

import UIKit
import AGConnectFunction

class NumberInfo {
    var number1 = ""
    var number2 = ""
}

class ResultInfo {
    var result = 0
}

class ViewController: UIViewController {

    @IBOutlet var input1 : UITextField!
    @IBOutlet var input2 : UITextField!
    @IBOutlet var sumLabel : UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }

    @IBAction func clickAddButton(_: UIButton) {
        let callable = AGCFunction.getInstance().wrap("addtest-$latest")
        let num = NumberInfo()
        num.number1 = self.input1.text ?? ""
        num.number2 = self.input2.text ?? ""
        callable.call(with: num).onSuccess { (result) in
            let sum = result?.value(with: ResultInfo.self) as! ResultInfo
            self.sumLabel.text = "Sum : \(sum.result)"
        }.onFailure { (error) in
            print(error)
        }
    }
}

