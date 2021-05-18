// WARNING
//
// This file has been generated automatically by Visual Studio to store outlets and
// actions made in the UI designer. If it is removed, they will be lost.
// Manual changes to this file may not be handled correctly.
//
using Foundation;
using System.CodeDom.Compiler;

namespace AGCAuthXamariniOSDemo.Views
{
	[Register ("LinkViewController")]
	partial class LinkViewController
	{
		[Outlet]
		UIKit.UITableView linkTableView { get; set; }
		
		void ReleaseDesignerOutlets ()
		{
			if (linkTableView != null) {
				linkTableView.Dispose ();
				linkTableView = null;
			}
		}
	}
}
