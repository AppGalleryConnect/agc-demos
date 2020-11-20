//
//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved
//

import UIKit
import AGConnectAppLinking

class ViewController: UIViewController {

    @IBOutlet weak var shortLink: UILabel!
    @IBOutlet weak var longLink: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }

    @IBAction func createLink(_ sender: Any) {
        let components = AGCAppLinkingComponents()
        components.uriPrefix = "https://example.drcn.agconnect.link"
        components.deepLink = "https://www.example.com"
        components.iosBundleId = Bundle.main.bundleIdentifier
        components.iosDeepLink = "agckit://ios/detail"
        components.androidDeepLink = "agckit://android/detail"
        components.androidPackageName = "com.android.demo"
        components.campaignName = "name"
        components.campaignMedium = "App"
        components.campaignSource = "AGC"
        components.socialTitle = "Title"
        components.socialImageUrl = "https://example.com/1.png"
        components.socialDescription = "Description"
        
        longLink.text = components.buildLongLink().absoluteString
        components.buildShortLink { (shortLink, error) in
            if let e = error {
                let alert = UIAlertController.init(title: "Error", message: e.localizedDescription, preferredStyle: .alert)
                alert.addAction(UIAlertAction.init(title: "OK", style: .cancel, handler: nil))
                self.present(alert, animated: true, completion: nil)
                return
            }
            self.shortLink.text = shortLink?.url.absoluteString
        }
    }
    
    
    @IBAction func copyShortLink(_ sender: Any) {
        copyToPasteboard(value: self.shortLink.text)
    }
    
    @IBAction func copyLongLink(_ sender: Any) {
        copyToPasteboard(value: self.longLink.text)
    }
    
    func copyToPasteboard(value : String?) {
        UIPasteboard.general.string = value
    }
}

