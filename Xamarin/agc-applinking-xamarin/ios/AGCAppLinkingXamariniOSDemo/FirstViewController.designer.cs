// WARNING
//
// This file has been generated automatically by Visual Studio from the outlets and
// actions declared in your storyboard file.
// Manual changes to this file will not be maintained.
//
using Foundation;
using System;
using System.CodeDom.Compiler;
using UIKit;

namespace AGCAppLinkingXamariniOSDemo
{
    [Register ("FirstViewController")]
    partial class FirstViewController
    {
        [Outlet]
        [GeneratedCode ("iOS Designer", "1.0")]
        UIKit.UIButton btnClear { get; set; }

        [Outlet]
        [GeneratedCode ("iOS Designer", "1.0")]
        UIKit.UIButton btnConvert { get; set; }

        [Outlet]
        [GeneratedCode ("iOS Designer", "1.0")]
        UIKit.UIButton btnCreateAppLink { get; set; }

        [Outlet]
        [GeneratedCode ("iOS Designer", "1.0")]
        UIKit.UISwitch switchAndroidParams { get; set; }

        [Outlet]
        [GeneratedCode ("iOS Designer", "1.0")]
        UIKit.UISwitch switchCampaign { get; set; }

        [Outlet]
        [GeneratedCode ("iOS Designer", "1.0")]
        UIKit.UISwitch switchiOSParams { get; set; }

        [Outlet]
        [GeneratedCode ("iOS Designer", "1.0")]
        UIKit.UISwitch switchITunesParams { get; set; }

        [Outlet]
        [GeneratedCode ("iOS Designer", "1.0")]
        UIKit.UISwitch switchSocial { get; set; }

        [Action ("AndroidParamsChanged:")]
        [GeneratedCode ("iOS Designer", "1.0")]
        partial void AndroidParamsChanged (UIKit.UISwitch sender);

        [Action ("BtnConvert_TouchUpInside:")]
        [GeneratedCode ("iOS Designer", "1.0")]
        partial void BtnConvert_TouchUpInside (UIKit.UIButton sender);

        [Action ("CampaignChanged:")]
        [GeneratedCode ("iOS Designer", "1.0")]
        partial void CampaignChanged (UIKit.UISwitch sender);

        [Action ("ClearAll:")]
        [GeneratedCode ("iOS Designer", "1.0")]
        partial void ClearAll (UIKit.UIButton sender);

        [Action ("CreateAppLink:")]
        [GeneratedCode ("iOS Designer", "1.0")]
        partial void CreateAppLink (UIKit.UIButton sender);

        [Action ("iOSParamsChanged:")]
        [GeneratedCode ("iOS Designer", "1.0")]
        partial void iOSParamsChanged (UIKit.UISwitch sender);

        [Action ("iTunesParamsChanged:")]
        [GeneratedCode ("iOS Designer", "1.0")]
        partial void iTunesParamsChanged (UIKit.UISwitch sender);

        [Action ("SocialChanged:")]
        [GeneratedCode ("iOS Designer", "1.0")]
        partial void SocialChanged (UIKit.UISwitch sender);

        void ReleaseDesignerOutlets ()
        {
            if (btnClear != null) {
                btnClear.Dispose ();
                btnClear = null;
            }

            if (btnConvert != null) {
                btnConvert.Dispose ();
                btnConvert = null;
            }

            if (btnCreateAppLink != null) {
                btnCreateAppLink.Dispose ();
                btnCreateAppLink = null;
            }

            if (switchAndroidParams != null) {
                switchAndroidParams.Dispose ();
                switchAndroidParams = null;
            }

            if (switchCampaign != null) {
                switchCampaign.Dispose ();
                switchCampaign = null;
            }

            if (switchiOSParams != null) {
                switchiOSParams.Dispose ();
                switchiOSParams = null;
            }

            if (switchITunesParams != null) {
                switchITunesParams.Dispose ();
                switchITunesParams = null;
            }

            if (switchSocial != null) {
                switchSocial.Dispose ();
                switchSocial = null;
            }
        }
    }
}