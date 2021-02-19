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

    @IBAction func create(_ sender: Any) {
        let components = AGCAppLinkingComponents()
        components.uriPrefix = "https://example.drcn.agconnect.link"
        components.deepLink = "https://www.example.com"
        components.iosBundleId = Bundle.main.bundleIdentifier
        components.iosDeepLink = "example://ios/detail"
        components.androidDeepLink = "example://android/detail"
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
                print(e)
                return
            }
            self.shortLink.text = shortLink?.url.absoluteString
        }
    }
    
    @IBAction func openShortLink(_ sender: Any) {
        if let url = URL(string: self.shortLink.text ?? "") {
            UIApplication.shared.open(url)
        }
    }
    
    @IBAction func openLongLink(_ sender: Any) {
        if let url = URL(string: self.longLink.text ?? "") {
            UIApplication.shared.open(url)
        }
    }
    
}

