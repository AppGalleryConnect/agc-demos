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
using UIKit;
using Foundation;
using ObjCRuntime;

namespace AGCApmXamariniOSDemo
{
    public partial class FirstViewController : UICollectionViewController
    {
        static NSString animalCellId = new NSString("AnimalCell");
        static NSString headerId = new NSString("Header");
        List<Animal> animals;

        public FirstViewController(UICollectionViewLayout layout) : base(layout)
        {
            animals = new List<Animal>();
            for (int i = 0; i < 20; i++)
            {
                animals.Add(new Animal { Name = "Panda", Image = new UIImage("panda.png") });
            }
        }

        public override void ViewDidAppear(bool animated)
        {
            base.ViewDidAppear(animated);
        }

        public override void ViewDidLoad()
        {
            base.ViewDidLoad();

            CollectionView.RegisterClassForCell(typeof(AnimalCell), animalCellId);
            CollectionView.RegisterClassForSupplementaryView(typeof(Header), UICollectionElementKindSection.Header, headerId);
            
            UIMenuController.SharedMenuController.MenuItems = new UIMenuItem[] {
                new UIMenuItem ("Custom", new Selector ("custom"))
            };

           
        }

        public override nint NumberOfSections(UICollectionView collectionView)
        {
            return 1;
        }

        public override nint GetItemsCount(UICollectionView collectionView, nint section)
        {
            return animals.Count;
        }

        public override UICollectionViewCell GetCell(UICollectionView collectionView, NSIndexPath indexPath)
        {
            var animalCell = (AnimalCell)collectionView.DequeueReusableCell(animalCellId, indexPath);

            var animal = animals[indexPath.Row];

            animalCell.Image = animal.Image;

            return animalCell;
        }

        public override UICollectionReusableView GetViewForSupplementaryElement(UICollectionView collectionView, NSString elementKind, NSIndexPath indexPath)
        {
            var headerView = (Header)collectionView.DequeueReusableSupplementaryView(elementKind, headerId, indexPath);
            headerView.Text = "Panda Sanctuary";
            return headerView;
        }

        public override void ItemHighlighted(UICollectionView collectionView, NSIndexPath indexPath)
        {
            var cell = collectionView.CellForItem(indexPath);
            cell.ContentView.BackgroundColor = UIColor.Yellow;
        }

        public override void ItemUnhighlighted(UICollectionView collectionView, NSIndexPath indexPath)
        {
            var cell = collectionView.CellForItem(indexPath);
            cell.ContentView.BackgroundColor = UIColor.White;
        }

        public override bool ShouldHighlightItem(UICollectionView collectionView, NSIndexPath indexPath)
        {
            return true;
        }

 
        public override bool ShouldShowMenu(UICollectionView collectionView, NSIndexPath indexPath)
        {
            return true;
        }

        public override bool CanPerformAction(UICollectionView collectionView, Selector action, NSIndexPath indexPath, NSObject sender)
        {
            if (action == new Selector("custom"))
                return true;
            else
                return false;
        }

        public override void PerformAction(UICollectionView collectionView, Selector action, NSIndexPath indexPath, NSObject sender)
        {
            System.Diagnostics.Debug.WriteLine("code to perform action");
        }

        public override bool CanBecomeFirstResponder
        {
            get
            {
                return true;
            }
        }



        public override void WillRotate(UIInterfaceOrientation toInterfaceOrientation, double duration)
        {
            base.WillRotate(toInterfaceOrientation, duration);

            var lineLayout = CollectionView.CollectionViewLayout as LineLayout;
            if (lineLayout != null)
            {
                if ((toInterfaceOrientation == UIInterfaceOrientation.Portrait) || (toInterfaceOrientation == UIInterfaceOrientation.PortraitUpsideDown))
                    lineLayout.SectionInset = new UIEdgeInsets(400, 0, 400, 0);
                else
                    lineLayout.SectionInset = new UIEdgeInsets(220, 0.0f, 200, 0.0f);
            }
        }

    }
}


