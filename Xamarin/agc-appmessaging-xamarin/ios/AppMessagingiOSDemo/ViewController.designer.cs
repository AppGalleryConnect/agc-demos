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
    [Register ("ViewController")]
    partial class ViewController
    {
        [Outlet]
        [GeneratedCode ("iOS Designer", "1.0")]
        UIKit.UILabel lblAaid { get; set; }

        [Outlet]
        [GeneratedCode ("iOS Designer", "1.0")]
        UIKit.UILabel lblDismissType { get; set; }

        [Outlet]
        [GeneratedCode ("iOS Designer", "1.0")]
        UIKit.UILabel lblEndTime { get; set; }

        [Outlet]
        [GeneratedCode ("iOS Designer", "1.0")]
        UIKit.UILabel lblEventType { get; set; }

        [Outlet]
        [GeneratedCode ("iOS Designer", "1.0")]
        UIKit.UILabel lblFreqType { get; set; }

        [Outlet]
        [GeneratedCode ("iOS Designer", "1.0")]
        UIKit.UILabel lblFreqValue { get; set; }

        [Outlet]
        [GeneratedCode ("iOS Designer", "1.0")]
        UIKit.UILabel lblStartTime { get; set; }

        [Outlet]
        [GeneratedCode ("iOS Designer", "1.0")]
        UIKit.UILabel lbMessageId { get; set; }

        [Action ("SetBirthday:")]
        [GeneratedCode ("iOS Designer", "1.0")]
        partial void SetBirthday (UIKit.UIButton sender);

        void ReleaseDesignerOutlets ()
        {
            if (lblAaid != null) {
                lblAaid.Dispose ();
                lblAaid = null;
            }

            if (lblDismissType != null) {
                lblDismissType.Dispose ();
                lblDismissType = null;
            }

            if (lblEndTime != null) {
                lblEndTime.Dispose ();
                lblEndTime = null;
            }

            if (lblEventType != null) {
                lblEventType.Dispose ();
                lblEventType = null;
            }

            if (lblFreqType != null) {
                lblFreqType.Dispose ();
                lblFreqType = null;
            }

            if (lblFreqValue != null) {
                lblFreqValue.Dispose ();
                lblFreqValue = null;
            }

            if (lblStartTime != null) {
                lblStartTime.Dispose ();
                lblStartTime = null;
            }

            if (lbMessageId != null) {
                lbMessageId.Dispose ();
                lbMessageId = null;
            }
        }
    }
}