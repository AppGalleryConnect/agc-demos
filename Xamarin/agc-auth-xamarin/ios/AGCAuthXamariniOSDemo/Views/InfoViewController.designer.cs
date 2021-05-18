// WARNING
//
// This file has been generated automatically by Visual Studio to store outlets and
// actions made in the UI designer. If it is removed, they will be lost.
// Manual changes to this file may not be handled correctly.
//
using Foundation;
using System.CodeDom.Compiler;

namespace AGCAuthXamariniOSDemo
{
	[Register ("InfoViewController")]
	partial class InfoViewController
	{
		[Outlet]
		UIKit.UILabel txtId { get; set; }

		[Outlet]
		UIKit.UILabel txtName { get; set; }

		[Outlet]
		UIKit.UIImageView userImage { get; set; }

		[Action ("DeleteUser_Clicked:")]
		partial void DeleteUser_Clicked (Foundation.NSObject sender);

		[Action ("LnkAccount_Clicked:")]
		partial void LnkAccount_Clicked (Foundation.NSObject sender);

		[Action ("SignOut_Clicked:")]
		partial void SignOut_Clicked (Foundation.NSObject sender);

		[Action ("UserSettings_Clicked:")]
		partial void UserSettings_Clicked (Foundation.NSObject sender);
		
		void ReleaseDesignerOutlets ()
		{
			if (txtId != null) {
				txtId.Dispose ();
				txtId = null;
			}

			if (txtName != null) {
				txtName.Dispose ();
				txtName = null;
			}

			if (userImage != null) {
				userImage.Dispose ();
				userImage = null;
			}
		}
	}
}
