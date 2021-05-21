// WARNING
//
// This file has been generated automatically by Visual Studio to store outlets and
// actions made in the UI designer. If it is removed, they will be lost.
// Manual changes to this file may not be handled correctly.
//
using Foundation;
using System.CodeDom.Compiler;

namespace AGCCloudFunctionsiOSDemo
{
	[Register ("ViewController")]
	partial class ViewController
	{
		[Outlet]
		UIKit.UILabel lblName { get; set; }

		[Outlet]
		UIKit.UITextField txtName { get; set; }

		[Action ("Submit_Clicked:")]
		partial void Submit_Clicked (Foundation.NSObject sender);
		
		void ReleaseDesignerOutlets ()
		{
			if (lblName != null) {
				lblName.Dispose ();
				lblName = null;
			}

			if (txtName != null) {
				txtName.Dispose ();
				txtName = null;
			}
		}
	}
}
