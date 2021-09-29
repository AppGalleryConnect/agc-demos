/*
 * Copyright 2021. Huawei Technologies Co., Ltd. All rights reserved.

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
using CoreGraphics;
using Foundation;
using Huawei.Agconnect.Apms;
using System;
using System.Threading.Tasks;
using UIKit;

namespace AGCApmXamariniOSDemo
{
    public partial class ViewController : UIViewController
    {
        private AGCAPM apmInstance;

        public ViewController(IntPtr handle) : base(handle)
        {
        }

        public override void ViewDidLoad()
        {
            base.ViewDidLoad();
            apmInstance = AGCAPM.GetSharedInstance();
        }

        public override void DidReceiveMemoryWarning()
        {
            base.DidReceiveMemoryWarning();
           
        }

        private void completionHandler(NSData data, NSUrlResponse response, NSError error)
        {
            NSString str = NSString.FromData(data, NSStringEncoding.UTF8);
            Console.WriteLine(str);
        }
       
        partial void SendNetworkReqClickedAsync(NSObject sender)
        {
            NSUrl url = new NSUrl("https://developer.huawei.com");
            NSUrlSession session = NSUrlSession.SharedSession;
            NSUrlSessionDataTask task = session.CreateDataTask(url, completionHandler);
            task.Resume();
        }

        partial void DisableApmClicked(NSObject sender)
        {
            apmInstance.EnableCollection(false);
            Console.WriteLine("APMS has been disabled");
        }

        partial void EnableApmClicked(NSObject sender)
        {
            apmInstance.EnableCollection(true);
            Console.WriteLine("APMS has been enabled");
        }

        UICollectionViewController firstViewController;
        UICollectionViewController secondViewController;
        UICollectionViewController thirdViewController;
        CircleLayout circleLayout;
        LineLayout lineLayout;
        UICollectionViewFlowLayout flowLayout;
        partial void OpenFirstVC(NSObject sender)
        {
            
            // Flow Layout
            flowLayout = new UICollectionViewFlowLayout()
            {
                HeaderReferenceSize = new CGSize(100, 100),
                SectionInset = new UIEdgeInsets(20, 20, 20, 20),
                ScrollDirection = UICollectionViewScrollDirection.Vertical,
                MinimumInteritemSpacing = 50, // minimum spacing between cells
                MinimumLineSpacing = 50 // minimum spacing between rows if ScrollDirection is Vertical or between columns if Horizontal
            };


            firstViewController = new FirstViewController(flowLayout);

            firstViewController.CollectionView.ContentInset = new UIEdgeInsets(50, 0, 0, 0);

            this.PresentModalViewController(firstViewController, true);
        }

       
        partial void OpenThirdVC(NSObject sender)
        {

            // Circle Layout
            circleLayout = new CircleLayout();

            
            thirdViewController = new SecondViewController(circleLayout);

            thirdViewController.CollectionView.ContentInset = new UIEdgeInsets(50, 0, 0, 0);

            this.PresentModalViewController(thirdViewController, true);
        }
    }
}