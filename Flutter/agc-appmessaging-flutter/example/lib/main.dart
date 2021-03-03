/*
    Copyright 2021. Huawei Technologies Co., Ltd. All rights reserved.

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
import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:agconnect_appmessaging/agconnect_appmessaging.dart';
import 'package:agconnect_appmessaging/constants/app_messaging.dart';
import 'custom_button.dart';

void main() => runApp(MaterialApp(home: MyApp()));

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  final String _appTitle = 'HUAWEI AGC AppMessaging';

  final AgconnectAppmessaging agconnectAppmessaging =
      new AgconnectAppmessaging();
  StreamSubscription<dynamic> _streamSubscriptionDisplay;
  StreamSubscription<dynamic> _streamSubscriptionDismiss;
  StreamSubscription<dynamic> _streamSubscriptionClick;
  StreamSubscription<dynamic> _streamSubscriptionError;
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

  @override
  void initState() {
    super.initState();
/*
    #For Use CustomView
    _streamSubscriptionDisplay = agconnectAppmessaging.customEvent.listen((event) {
      _showDialog(context ,event.toString());
      agconnectAppmessaging.handleCustomViewMessageEvent
        (AppMessagingEventType.onMessageDismiss(AppMessagingDismissTypeConstants.CLICK));
    });
*/
    _streamSubscriptionDisplay =
        agconnectAppmessaging.onMessageDisplay.listen((event) {
      print('onMessageDisplay       $event');
    });
    _streamSubscriptionClick =
        agconnectAppmessaging.onMessageClick.listen((event) {
      print('onMessageClick       $event');
    });
    _streamSubscriptionDismiss =
        agconnectAppmessaging.onMessageDismiss.listen((event) {
      print('onMessageDismiss       $event');
    });

    _streamSubscriptionError =
        agconnectAppmessaging.onMessageError.listen((event) {
      print('onMessageError       $event');
    });
  }

  Future<void> _isDisplayEnable(BuildContext context) async {
    try {
      bool isDisplayEnable = await agconnectAppmessaging.isDisplayEnable();
      print(isDisplayEnable.toString());
      _showDialog(context, isDisplayEnable.toString());
    } on PlatformException catch (e) {
      _showDialog(context, e.toString());
    }
  }

  Future<void> _setDisplayEnableFalse(BuildContext context) async {
    try {
      agconnectAppmessaging.setDisplayEnable(false);
      _showDialog(context, 'setDisplayEnable false');
    } on PlatformException catch (e) {
      _showDialog(context, e.toString());
    }
  }

  Future<void> _setDisplayEnableTrue(BuildContext context) async {
    try {
      agconnectAppmessaging.setDisplayEnable(true);
      _showDialog(context, 'setDisplayEnable true');
    } on PlatformException catch (e) {
      _showDialog(context, e.toString());
    }
  }

  Future<void> _isFetchMessageEnable(BuildContext context) async {
    try {
      bool isFetchMessageEnable =
          await agconnectAppmessaging.isFetchMessageEnable();
      print(isFetchMessageEnable.toString());
      _showDialog(context, isFetchMessageEnable.toString());
    } on PlatformException catch (e) {
      _showDialog(context, e.toString());
    }
  }

  Future<void> _setFetchMessageEnableFalse(BuildContext context) async {
    try {
      agconnectAppmessaging.setFetchMessageEnable(false);
      _showDialog(context, 'setFetchMessageEnable false');
    } on PlatformException catch (e) {
      _showDialog(context, e.toString());
    }
  }

  Future<void> _setFetchMessageEnableTrue(BuildContext context) async {
    try {
      agconnectAppmessaging.setFetchMessageEnable(true);
      _showDialog(context, 'setFetchMessageEnable true');
    } on PlatformException catch (e) {
      _showDialog(context, e.toString());
    }
  }

  Future<void> _setDisplayLocation(BuildContext context) async {
    try {
      agconnectAppmessaging
          .setDisplayLocation(AppMessagingLocationConstants.CENTER);
      _showDialog(context, 'setDisplayLocation');
    } on PlatformException catch (e) {
      _showDialog(context, e.toString());
    }
  }

  Future<void> _trigger(BuildContext context) async {
    try {
      agconnectAppmessaging.trigger("OnAppForeGround");
      _showDialog(context, 'trigger ');
    } on PlatformException catch (e) {
      _showDialog(context, e.toString());
    }
  }

  Future<void> _setForceFetch(BuildContext context) async {
    try {
      agconnectAppmessaging.setForceFetch();
      _showDialog(context, 'setForceFetch');
    } on PlatformException catch (e) {
      _showDialog(context, e.toString());
    }
  }

  Future<void> _handleCustomViewMessageEvent(BuildContext context) async {
    try {
      agconnectAppmessaging
          .handleCustomViewMessageEvent(AppMessagingEventType.onMessageDisplay);
      _showDialog(context, 'success');
    } on PlatformException catch (e) {
      _showDialog(context, e.toString());
    }
  }

  Future<void> _removeCustomView(BuildContext context) async {
    try {
      agconnectAppmessaging.removeCustomView();
      _showDialog(context, "removeCustomView");
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
            CustomButton("isDisplayEnable", _isDisplayEnable),
            CustomButton("setDisplayEnableTrue", _setDisplayEnableTrue),
            CustomButton("setDisplayEnableFalse", _setDisplayEnableFalse),
            CustomButton(
                "setFetchMessageEnableTrue", _setFetchMessageEnableTrue),
            CustomButton(
                "setFetchMessageEnableFalse", _setFetchMessageEnableFalse),
            CustomButton("isFetchMessageEnable", _isFetchMessageEnable),
            CustomButton("setDisplayLocation", _setDisplayLocation),
            CustomButton("setForceFetchTrue", _setForceFetch),
            CustomButton("trigger", _trigger),
            CustomButton("removeCustomView", _removeCustomView)
          ],
        ),
      ),
    );
  }
}
