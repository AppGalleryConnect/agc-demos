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

import 'dart:async';

import 'package:agconnect_applinking/agconnect_applinking.dart';
import 'package:agconnect_applinking/constants/app_linking.dart';
import 'package:agconnect_applinking/models/app_linking_parameters.dart';
import 'package:agconnect_applinking/models/long_app_linking.dart';
import 'package:agconnect_applinking/models/resolved_link_data.dart';
import 'package:agconnect_applinking/models/short_app_linking.dart';
import 'package:agconnect_applinking_example/widgets/custom_button.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() => runApp(MaterialApp(home: MyHomePage()));

class MyHomePage extends StatefulWidget {
  _MyHomePageState createState() => new _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  final String _appTitle = 'HUAWEI AGC AppLinking';
  final AGCAppLinking agcAppLinking = new AGCAppLinking();
  StreamSubscription<ResolvedLinkData> _streamSubscription;

  void _showDialog(BuildContext context, String content) {
    showDialog(
        context: context,
        builder: (BuildContext context) {
          return AlertDialog(
            key: Key("dialog"),
            title: Text("Result"),
            content: Text(content),
            actions: <Widget>[
              FlatButton(
                child: new Text("Close"),
                onPressed: () {
                  Navigator.of(context).pop();
                },
              )
            ],
          );
        });
  }

  void _showDialogLong(BuildContext context, LongAppLinking content) {
    showDialog(
        context: context,
        builder: (BuildContext context) {
          return AlertDialog(
            key: Key("dialog"),
            title: Text("Result"),
            content: Text(content.toString()),
            actions: <Widget>[
              FlatButton(
                  child: new Text("Copy LongLink"),
                  onPressed: () {
                    Clipboard.setData(
                        new ClipboardData(text: content.longLink.toString()));
                  }),
              FlatButton(
                child: new Text("Close"),
                onPressed: () {
                  Navigator.of(context).pop();
                },
              )
            ],
          );
        });
  }

  void _showDialogShort(BuildContext context, ShortAppLinking content) {
    showDialog(
        context: context,
        builder: (BuildContext context) {
          return AlertDialog(
            key: Key("dialog"),
            title: Text("Result"),
            content: Text(content.toString()),
            actions: <Widget>[
              FlatButton(
                  child: new Text("Copy ShortLink"),
                  onPressed: () {
                    Clipboard.setData(
                        new ClipboardData(text: content.shortLink.toString()));
                  }),
              FlatButton(
                child: new Text("Close"),
                onPressed: () {
                  Navigator.of(context).pop();
                },
              )
            ],
          );
        });
  }

  initState() {
    super.initState();

    _streamSubscription = agcAppLinking.onResolvedData.listen((event) {
      _showDialog(context, event.toString());
    });
  }

  Future<void> _buildShortAppLinking(BuildContext context) async {
    SocialCardInfo socialCardInfo = SocialCardInfo(
        description: 'description short link',
        imageUrl: 'https://avatars2.githubusercontent.com/u/64997192?s=200&v=4',
        title: 'AGC Guides');

    CampaignInfo campaignInfo =
        CampaignInfo(medium: 'JULY', name: 'summer campaign', source: 'Huawei');

    AndroidLinkInfo androidLinkInfo = new AndroidLinkInfo(
        androidFallbackUrl:
            'https://developer.huawei.com/consumer/en/doc/overview/AppGallery-connect',
        androidOpenType: AppLinkingAndroidLinkInfoAndroidOpenTypeConstants.CUSTOM_URL,
        androidPackageName: "<package name>",
        androidDeepLink:
            'https://developer.huawei.com/consumer/en/doc/overview/AppGallery-connect');

    iOSLinkInfo iosLinkInfo = iOSLinkInfo(
        iosDeepLink: 'pages://flutter.huawei.applinking.com',
        iosFallbackUrl: 'https://developer.huawei.com/consumer/en/doc/overview/AppGallery-connect',
        iosBundleId: '<bundle id>');

    ApplinkingInfo appLinkingInfo = ApplinkingInfo(
        socialCardInfo: socialCardInfo,
        androidLinkInfo: androidLinkInfo,
        iosLinkInfo: iosLinkInfo,
        domainUriPrefix: '<uri prefix>',
        deepLink: 'https://developer.huawei.com',
        campaignInfo: campaignInfo,
        expireMinute: 5,
        shortAppLinkingLength: ShortAppLinkingLengthConstants.LONG);

    try {
      ShortAppLinking shortAppLinking =
          await agcAppLinking.buildShortAppLinking(appLinkingInfo);
      print(shortAppLinking.toString());
      _showDialogShort(context, shortAppLinking);
    } on PlatformException catch (e) {
      _showDialog(context, e.toString());
    }
  }

  Future<void> _buildLongAppLinking(BuildContext context) async {
    SocialCardInfo socialCardInfo = SocialCardInfo(
        description: 'description short link',
        imageUrl: 'https://avatars2.githubusercontent.com/u/64997192?s=200&v=4',
        title: 'AGC Guides');

    CampaignInfo campaignInfo =
        CampaignInfo(medium: 'JULY', name: 'summer campaign', source: 'Huawei');

    AndroidLinkInfo androidLinkInfo = new AndroidLinkInfo(
        androidFallbackUrl:
            'https://developer.huawei.com/consumer/en/doc/overview/AppGallery-connect',
        androidOpenType: AppLinkingAndroidLinkInfoAndroidOpenTypeConstants.CUSTOM_URL,
        androidPackageName: "<package name>",
        androidDeepLink:
            'https://developer.huawei.com/consumer/en/doc/overview/AppGallery-connect');

    iOSLinkInfo iosLinkInfo = iOSLinkInfo(
        iosDeepLink: 'pages://flutteraplinking.com',
        iosFallbackUrl: 'https://developer.huawei.com/consumer/en/doc/overview/AppGallery-connect',
        iosBundleId: '<bundle id>');

    ApplinkingInfo appLinkingInfo = ApplinkingInfo(
        socialCardInfo: socialCardInfo,
        androidLinkInfo: androidLinkInfo,
        iosLinkInfo: iosLinkInfo,
        campaignInfo: campaignInfo,
        domainUriPrefix: '<uri prefix>',
        deepLink: 'https://developer.huawei.com');

    try {
      LongAppLinking longAppLinking =
          await agcAppLinking.buildLongAppLinking(appLinkingInfo);
      print(longAppLinking.longLink.toString());
      _showDialogLong(context, longAppLinking);
    } on PlatformException catch (e) {
      _showDialog(context, e.toString());
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(_appTitle),
        backgroundColor: Colors.blue,
      ),
      body: SingleChildScrollView(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: <Widget>[
            CustomButton("buildAppLinking", _buildLongAppLinking),
            CustomButton("buildShortLinking", _buildShortAppLinking)
          ],
        ),
      ),
    );
  }
}
