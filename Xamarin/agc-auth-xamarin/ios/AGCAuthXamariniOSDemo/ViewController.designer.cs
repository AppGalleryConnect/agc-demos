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
	[Register ("ViewController")]
	partial class ViewController
	{
		[Outlet]
		UIKit.UITextField accountText { get; set; }

		[Outlet]
		AuthenticationServices.ASAuthorizationAppleIdButton appleLoginButton { get; set; }

		[Outlet]
		UIKit.UITextField codeText { get; set; }

		[Outlet]
		UIKit.UILabel countryCodeLabel { get; set; }

		[Outlet]
		UIKit.UISegmentedControl loginTypeSwitch { get; set; }

		[Outlet]
		UIKit.UITextField passwordText { get; set; }

		[Action ("AnonymousLogin:")]
		partial void AnonymousLogin (Foundation.NSObject sender);

		[Action ("FacebookLogin:")]
		partial void FacebookLogin (Foundation.NSObject sender);

		[Action ("GooglePlusLogin:")]
		partial void GooglePlusLogin (Foundation.NSObject sender);

		[Action ("Login:")]
		partial void Login (Foundation.NSObject sender);

		[Action ("LoginSwitchChange:")]
		partial void LoginSwitchChange (Foundation.NSObject sender);

		[Action ("QQLogin:")]
		partial void QQLogin (Foundation.NSObject sender);

		[Action ("Register:")]
		partial void Register (Foundation.NSObject sender);

		[Action ("SendCode:")]
		partial void SendCode (Foundation.NSObject sender);

		[Action ("TwitterLogin:")]
		partial void TwitterLogin (Foundation.NSObject sender);

		[Action ("WeChatLogin:")]
		partial void WeChatLogin (Foundation.NSObject sender);

		[Action ("WeiboLogin:")]
		partial void WeiboLogin (Foundation.NSObject sender);
		
		void ReleaseDesignerOutlets ()
		{
			if (accountText != null) {
				accountText.Dispose ();
				accountText = null;
			}

			if (appleLoginButton != null) {
				appleLoginButton.Dispose ();
				appleLoginButton = null;
			}

			if (codeText != null) {
				codeText.Dispose ();
				codeText = null;
			}

			if (countryCodeLabel != null) {
				countryCodeLabel.Dispose ();
				countryCodeLabel = null;
			}

			if (loginTypeSwitch != null) {
				loginTypeSwitch.Dispose ();
				loginTypeSwitch = null;
			}

			if (passwordText != null) {
				passwordText.Dispose ();
				passwordText = null;
			}
		}
	}
}
