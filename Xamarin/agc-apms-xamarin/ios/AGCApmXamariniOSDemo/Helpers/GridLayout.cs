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
using CoreGraphics;
using Foundation;
using UIKit;

namespace AGCApmXamariniOSDemo
{
    public class GridLayout : UICollectionViewFlowLayout
    {
        static NSString gridDecorationViewId = new NSString("GridDecorationViewId");
        public GridLayout ()
        {
            RegisterClassForDecorationView(typeof(GridDecorationView),gridDecorationViewId);
        }

        public override bool ShouldInvalidateLayoutForBoundsChange (CGRect newBounds)
        {
            return true;
        }

        public override UICollectionViewLayoutAttributes LayoutAttributesForItem (NSIndexPath path)
        {
            return base.LayoutAttributesForItem (path);
        }

        public override UICollectionViewLayoutAttributes[] LayoutAttributesForElementsInRect (CGRect rect)
        {
            return base.LayoutAttributesForElementsInRect (rect);
        }
    }

    public class GridDecorationView : UICollectionReusableView
    {
        [Export("initWithFrame:")]
        public GridDecorationView(CGRect frame) : base(frame)
        {
            BackgroundColor = UIColor.FromRGB(182, 7, 9);
        }
    }
}

