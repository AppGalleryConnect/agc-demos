/*
    Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.

    Licensed under the Apache License, Version 2.0 (the "License")
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        https://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

const $ = (x) => document.getElementById(x);

let shortLinkResult = "";
let longLinkResult = "";
let resolvedLinkDataResult = "";
document.addEventListener("deviceready", onDeviceReady, false);

function onDeviceReady() {
  var parentElement = $("deviceready");
  var listeningElement = parentElement.querySelector(".listening");
  var receivedElement = parentElement.querySelector(".received");

  listeningElement.setAttribute("style", "display:none;");
  receivedElement.setAttribute("style", "display:block;");

  $('AGConnectAppLinkingResolvedLinkDataResult').innerHTML = "AGConnectAppLinkingResolvedLinkDataResult";
  AGCAppLinking.addListener(
    AGCAppLinking.Events.RECEIVE_LINK,
    (resolvedLinkDataResult) => {
      this.resolvedLinkDataResult = resolvedLinkDataResult;
      console.log(
        "resolvedLinkDataResult" +
          JSON.stringify(resolvedLinkDataResult, null, 1)
      );   
      $("AGConnectAppLinkingResolvedLinkDataResult").innerHTML = JSON.stringify(
        this.resolvedLinkDataResult,
        null,
        1
      );
    }
  );
}


$("buildShortLink").onclick = () => {
  const socialCardInfo1 = {
    description: "description of short link",
    imageUrl:
      "https://developer.huawei.com/Enexport/system/modules/org.opencms.portal.template.core/resources/images/codelabs/ic_computergraphics.png",
    title: "title of social card",
  };
  const campaignInfo1 = {
    medium: "JULY",
    name: "summer campaign",
    source: "Huawei",
  };
  const androidLinkInfo1 = {
    androidDeepLink:
      "https://developer.huawei.com/consumer/en/develop/programs",
    //androidFallbackUrl: 'https://consumer.huawei.com/en/',
    androidPackageName: "com.cdv.agc.applinking",
    androidOpenType:
      AGCAppLinking.AppLinkingAndroidLinkInfoAndroidOpenTypeConstants
        .LOCAL_MARKET,
  };
  const iosLinkInfo1 = {
    iosDeepLink: "cdvhuaweicom://consumer/en/develop/iosdeeplink",
    iosFallbackUrl: "https://swift.org/",
    iosBundleId: "com.cdv.agc.applinking",
    //ipadFallbackUrl: '',
    //ipadBundleId: '',
  };
  const iTunesConnectCampaingnInfo1 = {
    iTunesConnectProviderToken: "iTunesConnectProviderToken1",
    iTunesConnectCampaignToken: "iTunesConnectCampaignToken1",
    iTunesConnectAffiliateToken: "iTunesConnectAffiliateToken1",
    iTunesConnectMediaType: "iTunesConnectMediaType1",
  };
  const appLinkingWithInfo = {
    socialCardInfo: socialCardInfo1,
    campaignInfo: campaignInfo1,
    androidLinkInfo: androidLinkInfo1,
    iosLinkInfo: iosLinkInfo1,
    iTunesConnectCampaingnInfo: iTunesConnectCampaingnInfo1,
    previewType:
      AGCAppLinking.AppLinkingLinkingPreviewTypeConstants.SOCIAL_INFO,
    uriPrefix: "https://myagcapplinking.dre.agconnect.link",
    deepLink: "https://developer.huawei.com/consumer/en/develop/",
    //longLink: "your long link of app linking",
    shortAppLinkingLength: AGCAppLinking.ShortAppLinkingLengthConstants.LONG,
    expireMinute: 1000,
  };
  AGCAppLinking.buildShortLink(appLinkingWithInfo)
    .then((shortLinkResult) => {
      this.shortLinkResult = shortLinkResult;
      $("shortLinkResult").innerHTML = JSON.stringify(shortLinkResult, null, 1);
    })
    .catch(function (err) {
      alert("buildShortLink -> Error : " + JSON.stringify(err, null, 1));
    });
};

$("buildLongLink").onclick = () => {
  const socialCardInfo1 = {
    description: "description of long link",
    imageUrl:
      "https://developer.huawei.com/Enexport/system/modules/org.opencms.portal.template.core/resources/images/codelabs/ic_computergraphics.png",
    title: "title of social card",
  };
  const campaignInfo1 = {
    medium: "JULY",
    name: "summer campaign",
    source: "Huawei",
  };
  const androidLinkInfo1 = {
    androidDeepLink: "https://developer.huawei.com/consumer/en/develop/",
    androidPackageName: "com.cdv.agc.applinking",
    androidOpenType:
      AGCAppLinking.AppLinkingAndroidLinkInfoAndroidOpenTypeConstants
        .APP_GALLERY,
  };
  const iosLinkInfo1 = {
    iosDeepLink: "cdvhuaweicom://consumer/en/develop/iosdeeplink",
    iosFallbackUrl: "https://swift.org/",
    iosBundleId: "com.cdv.agc.applinking",
    //ipadFallbackUrl: '',
    //ipadBundleId: '',
  };
  const iTunesConnectCampaingnInfo1 = {
    iTunesConnectProviderToken: "iTunesConnectProviderToken1",
    iTunesConnectCampaignToken: "iTunesConnectCampaignToken1",
    iTunesConnectAffiliateToken: "iTunesConnectAffiliateToken1",
    iTunesConnectMediaType: "iTunesConnectMediaType1",
  };
  const appLinkingWithInfo = {
    socialCardInfo: socialCardInfo1,
    campaignInfo: campaignInfo1,
    androidLinkInfo: androidLinkInfo1,
    iosLinkInfo: iosLinkInfo1,
    iTunesConnectCampaingnInfo: iTunesConnectCampaingnInfo1,
    previewType:
      AGCAppLinking.AppLinkingLinkingPreviewTypeConstants.SOCIAL_INFO,
    uriPrefix: "https://myagcapplinking.dre.agconnect.link",
    //longLink: "your long link of app linking",
    deepLink: "https://developer.huawei.com/consumer/en/develop/",
    //expireMinute: 1000,
  };
  AGCAppLinking.buildLongLink(appLinkingWithInfo)
    .then((longLinkResult) => {
      this.longLinkResult = longLinkResult;
      $("longLinkResult").innerHTML = JSON.stringify(longLinkResult, null, 1);
    })
    .catch(function (err) {
      alert("buildLongLink -> Error : " + JSON.stringify(err, null, 1));
    });
};

$("copyShortLink").onclick = () => {
  if (this.shortLinkResult.shortLink) {
    textToClipboard(this.shortLinkResult.shortLink);
    alert("Copied shortLink to clipboard.");
  } else {
    alert("Please build shortLink.");
  }
};
$("copyTestUrl").onclick = () => {
  if (this.shortLinkResult.testUrl) {
    textToClipboard(this.shortLinkResult.testUrl);
    alert("Copied testURL to clipboard.");
  } else {
    alert("Please build shortLink.");
  }
};
$("copyLongLink").onclick = () => {
  if (this.longLinkResult.longLink) {
    textToClipboard(this.longLinkResult.longLink);
    alert("Copied longLink to clipboard.");
  } else {
    alert("Please build longLink.");
  }
};

function textToClipboard(text) {
  var dummy = document.createElement("textarea");
  document.body.appendChild(dummy);
  dummy.value = text;
  dummy.select();
  document.execCommand("copy");
  document.body.removeChild(dummy);
}
