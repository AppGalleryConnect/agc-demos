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
using System; 
using UIKit;
using Foundation;
using CoreGraphics;
using ObjCRuntime;

namespace AGCApmXamariniOSDemo
{
    public class AnimalCell : UICollectionViewCell
    {
        UIImageView imageView;

        [Export("initWithFrame:")]
        public AnimalCell(CGRect frame) : base(frame)
        {
            BackgroundView = new UIView { BackgroundColor = UIColor.Orange };

            SelectedBackgroundView = new UIView { BackgroundColor = UIColor.Green };

            ContentView.Layer.BorderColor = UIColor.LightGray.CGColor;
            ContentView.Layer.BorderWidth = 2.0f;
            ContentView.BackgroundColor = UIColor.White;
            ContentView.Transform = CGAffineTransform.MakeScale(0.8f, 0.8f);

            imageView = new UIImageView(UIImage.FromBundle("placeholder.png"));
            imageView.Center = ContentView.Center;
            imageView.Transform = CGAffineTransform.MakeScale(0.7f, 0.7f);

            ContentView.AddSubview(imageView);
        }

        public UIImage Image
        {
            set
            {
                imageView.Image = value;
            }
        }

        [Export("custom")]
        void Custom()
        {
            // Put all your custom menu behavior code here
            Console.WriteLine("custom in the cell");
        }


        public override bool CanPerform(Selector action, NSObject withSender)
        {
            if (action == new Selector("custom"))
                return true;
            else
                return false;
        }
    }

    public class Header : UICollectionReusableView
    {
        UILabel label;

        public string Text
        {
            get
            {
                return label.Text;
            }
            set
            {
                label.Text = value;
                SetNeedsDisplay();
            }
        }

        [Export("initWithFrame:")]
        public Header(CGRect frame) : base(frame)
        {
            label = new UILabel() { Frame = new CGRect(0, 0, 300, 50), BackgroundColor = UIColor.Yellow };
            AddSubview(label);
        }
    }
}
