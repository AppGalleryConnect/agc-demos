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
using System.Collections.Generic;
using CoreGraphics;
using Foundation;
using CoreAnimation;
using UIKit;

namespace AGCApmXamariniOSDemo
{
	public class CircleLayout : UICollectionViewLayout
	{
		const float ItemSize = 70.0f;
		int cellCount = 20;
		float radius;
		CGPoint center;

		static NSString myDecorationViewId = new NSString ("MyDecorationView");

		public CircleLayout ()
		{
			RegisterClassForDecorationView (typeof(MyDecorationView), myDecorationViewId);
		}

		public override void PrepareLayout ()
		{
			base.PrepareLayout ();

			CGSize size = CollectionView.Frame.Size;
			cellCount = (int)CollectionView.NumberOfItemsInSection (0);
			center = new CGPoint (size.Width / 2.0f, size.Height / 2.0f);
			radius = (float)Math.Min (size.Width, size.Height) / 2.5f;
		}

		public override CGSize CollectionViewContentSize {
			get {
				return CollectionView.Frame.Size;
			}
		}

        public override bool ShouldInvalidateLayoutForBoundsChange (CGRect newBounds)
        {
            return true;
        }

		public override UICollectionViewLayoutAttributes LayoutAttributesForItem (NSIndexPath path)
		{
			UICollectionViewLayoutAttributes attributes = UICollectionViewLayoutAttributes.CreateForCell (path);
			attributes.Size = new CGSize (ItemSize, ItemSize);
			attributes.Center = new CGPoint (center.X + radius * (float)Math.Cos (2 * path.Row * Math.PI / cellCount),
			                                center.Y + radius * (float)Math.Sin (2 * path.Row * Math.PI / cellCount));
			return attributes;
		}

		public override UICollectionViewLayoutAttributes[] LayoutAttributesForElementsInRect (CGRect rect)
		{
			var attributes = new UICollectionViewLayoutAttributes [cellCount + 1];

			for (int i = 0; i < cellCount; i++) {
				NSIndexPath indexPath = NSIndexPath.FromItemSection (i, 0);
				attributes [i] = LayoutAttributesForItem (indexPath);
			}

            var decorationAttribs = UICollectionViewLayoutAttributes.CreateForDecorationView (myDecorationViewId, NSIndexPath.FromItemSection (0, 0));
            decorationAttribs.Size = CollectionView.Frame.Size;
			decorationAttribs.Center = CollectionView.Center;
			decorationAttribs.ZIndex = -1;
			attributes [cellCount] = decorationAttribs;

			return attributes;
		}

	}

	public class MyDecorationView : UICollectionReusableView
	{
		[Export ("initWithFrame:")]
		public MyDecorationView (CGRect frame) : base (frame)
		{
			BackgroundColor = UIColor.FromRGB(232,151,1) ;
		}
	}
}
