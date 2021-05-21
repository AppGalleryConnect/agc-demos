// WARNING
//
// This file has been generated automatically by Visual Studio to store outlets and
// actions made in the UI designer. If it is removed, they will be lost.
// Manual changes to this file may not be handled correctly.
//
using Foundation;
using System.CodeDom.Compiler;

namespace AGCCrashXamarinDemo
{
	[Register ("ViewController")]
	partial class ViewController
	{
		[Outlet]
		[GeneratedCode ("iOS Designer", "1.0")]
		UIKit.UISwitch switchCrashEnabled { get; set; }

		[Outlet]
		[GeneratedCode ("iOS Designer", "1.0")]
		UIKit.UITextField txtUserId { get; set; }

		[Outlet]
		[GeneratedCode ("iOS Designer", "1.0")]
		UIKit.UITextField txtValue { get; set; }

		[Action ("AddLog_TouchUpInside:")]
		partial void AddLog_TouchUpInside (UIKit.UIButton sender);

		[Action ("EnableCrash_Changed:")]
		partial void EnableCrash_Changed (UIKit.UISwitch sender);

		[Action ("NSException_TouchUpInside:")]
		partial void NSException_TouchUpInside (UIKit.UIButton sender);

		[Action ("NSInvalidArgumentException_TouchUpInside:")]
		partial void NSInvalidArgumentException_TouchUpInside (UIKit.UIButton sender);

		[Action ("RecordException_TouchUpInside:forEvent:")]
		partial void RecordException_TouchUpInside(Foundation.NSObject sender, UIKit.UIEvent @event);

		[Action ("RecordError_TouchUpInside:")]
		partial void RecordError_TouchUpInside (UIKit.UIButton sender);
		
		void ReleaseDesignerOutlets ()
		{
			if (switchCrashEnabled != null) {
				switchCrashEnabled.Dispose ();
				switchCrashEnabled = null;
			}

			if (txtUserId != null) {
				txtUserId.Dispose ();
				txtUserId = null;
			}

			if (txtValue != null) {
				txtValue.Dispose ();
				txtValue = null;
			}
		}
	}
}
