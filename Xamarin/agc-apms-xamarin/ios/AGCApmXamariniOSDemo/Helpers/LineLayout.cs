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
using Foundation;
using UIKit;
using CoreGraphics;
using CoreAnimation;

namespace AGCApmXamariniOSDemo
{
	public class LineLayout : UICollectionViewFlowLayout
	{
		public const float ITEM_SIZE = 200.0f;
		public const int ACTIVE_DISTANCE = 200;
		public const float ZOOM_FACTOR = 0.3f;
		static NSString lineDecorationViewId = new NSString("LineDecorationViewId");
		public LineLayout ()
		{
			ItemSize = new CGSize (ITEM_SIZE, ITEM_SIZE);
			ScrollDirection = UICollectionViewScrollDirection.Horizontal;
            SectionInset = new UIEdgeInsets (400,0,400,0);
			MinimumLineSpacing = 50.0f;

			RegisterClassForDecorationView(typeof(LineDecorationView), lineDecorationViewId);
		}

		public override bool ShouldInvalidateLayoutForBoundsChange (CGRect newBounds)
		{
			return true;
		}

		public override UICollectionViewLayoutAttributes[] LayoutAttributesForElementsInRect (CGRect rect)
		{
			var array = base.LayoutAttributesForElementsInRect (rect);
            var visibleRect = new CGRect (CollectionView.ContentOffset, CollectionView.Bounds.Size);

			foreach (var attributes in array) {
				if (attributes.Frame.IntersectsWith (rect)) {
					float distance = (float)(visibleRect.GetMidX () - attributes.Center.X);
					float normalizedDistance = distance / ACTIVE_DISTANCE;
					if (Math.Abs (distance) < ACTIVE_DISTANCE) {
						float zoom = 1 + ZOOM_FACTOR * (1 - Math.Abs (normalizedDistance));
						attributes.Transform3D = CATransform3D.MakeScale (zoom, zoom, 1.0f);
						attributes.ZIndex = 1;
					}
				}
			}
			return array;
		}

		public override CGPoint TargetContentOffset (CGPoint proposedContentOffset, CGPoint scrollingVelocity)
		{
			float offSetAdjustment = float.MaxValue;
			float horizontalCenter = (float)(proposedContentOffset.X + (this.CollectionView.Bounds.Size.Width / 2.0));
			CGRect targetRect = new CGRect (proposedContentOffset.X, 0.0f, this.CollectionView.Bounds.Size.Width, this.CollectionView.Bounds.Size.Height);
			var array = base.LayoutAttributesForElementsInRect (targetRect);
			foreach (var layoutAttributes in array) {
				float itemHorizontalCenter = (float)layoutAttributes.Center.X;
				if (Math.Abs (itemHorizontalCenter - horizontalCenter) < Math.Abs (offSetAdjustment)) {
					offSetAdjustment = itemHorizontalCenter - horizontalCenter;
				}
			}
            return new CGPoint (proposedContentOffset.X + offSetAdjustment, proposedContentOffset.Y);
		}

	}
	public class LineDecorationView : UICollectionReusableView
	{
		[Export("initWithFrame:")]
		public LineDecorationView(CGRect frame) : base(frame)
		{
			BackgroundColor = UIColor.FromRGB(125, 12, 13);
		}
	}
}

