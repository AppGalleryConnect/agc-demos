/*
    Copyright 2020-2021. Huawei Technologies Co., Ltd. All rights reserved.
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

import 'dart:io';

import 'package:agconnect_auth_example/custom_button.dart';
import 'package:agconnect_auth_example/pages/page_huawei.dart';
import 'package:agconnect_auth_example/pages/page_huawei_game.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'dart:async';

import 'package:agconnect_auth/agconnect_auth.dart';

import 'pages/page_anonymous.dart';
import 'pages/page_email.dart';
import 'pages/page_google.dart';
import 'pages/page_google_game.dart';
import 'pages/page_phone.dart';
import 'pages/page_self_build.dart';
import 'pages/page_user.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(home: HomePage());
  }
}

class HomePage extends StatefulWidget {
  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  @override
  void initState() {
    super.initState();
    addTokenListener();
  }

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

  Future<void> addTokenListener() async {
    if (!mounted) return;

    AGCAuth.instance.tokenChanges().listen((TokenSnapshot event) {
      print("onEvent: $event , ${event.state}, ${event.token}");
    }, onError: (Object error) {
      print('onError: $error');
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
            child: ListView(
          children: <Widget>[
            CustomButton('Anonymous', () async {
              Navigator.push(context,
                  MaterialPageRoute(builder: (context) => PageAnonymousAuth()));
            }),
            CustomButton('Phone', () async {
              Navigator.push(context,
                  MaterialPageRoute(builder: (context) => PagePhoneAuth()));
            }),
            CustomButton('Email', () async {
              Navigator.push(context,
                  MaterialPageRoute(builder: (context) => PageEmailAuth()));
            }),
            CustomButton('Self Build', () async {
              Navigator.push(context,
                  MaterialPageRoute(builder: (context) => PageSelfBuildAuth()));
            }),
            CustomButton('User Info', () async {
              Navigator.push(
                  context, MaterialPageRoute(builder: (context) => PageUser()));
            }),
            CustomButton('Huawei Login', () async {
              if (Platform.isAndroid) {
                Navigator.push(context,
                    MaterialPageRoute(builder: (context) => PageHuaweiAuth()));
              } else if (Platform.isIOS) {
                _showDialog(context,
                    "This authentication mode is available only for Android.");
              }
            }),
            CustomButton('Huawei Game Login', () async {
              if (Platform.isAndroid) {
                Navigator.push(
                    context,
                    MaterialPageRoute(
                        builder: (context) => PageHuaweiGameAuth()));
              } else if (Platform.isIOS) {
                _showDialog(context,
                    "This authentication mode is available only for Android.");
              }
            }),
            CustomButton('Google Login', () async {
              Navigator.push(context,
                  MaterialPageRoute(builder: (context) => PageGoogleAuth()));
            }),
            CustomButton('GoogleGame Login', () async {
              if (Platform.isAndroid) {
                Navigator.push(
                    context,
                    MaterialPageRoute(
                        builder: (context) => PageGoogleGameAuth()));
              } else if (Platform.isIOS) {
                _showDialog(context,
                    "This authentication mode is available only for Android.");
              }
            }),
            CustomButton('Twitter Login', () async {
              _showDialog(
                  context,
                  "Please integrate related third plugins and retrive "
                  "the parameters to auth plugin to enable this function, you can read readme for detail.");
            }),
            CustomButton('Facebook Login', () async {
              _showDialog(
                  context,
                  "Please integrate related third plugins and retrive "
                  "the parameters to auth plugin to enable this function, you can read readme for detail.");
            }),
            CustomButton('Apple Login', () async {
              if (Platform.isAndroid) {
                _showDialog(context,
                    "This authentication mode is available only for iOS.");
              } else if (Platform.isIOS) {
                _showDialog(
                    context,
                    "Please integrate related third plugins and retrive "
                    "the parameters to auth plugin to enable this function, you can read readme for detail.");
              }
            }),
            CustomButton('WeChat Login', () async {
              _showDialog(
                  context,
                  "Please integrate related third plugins and retrive "
                  "the parameters to auth plugin to enable this function, you can read readme for detail.");
            }),
            CustomButton('WeiBo Login', () async {
              _showDialog(
                  context,
                  "Please integrate related third plugins and retrive "
                  "the parameters to auth plugin to enable this function, you can read readme for detail.");
            }),
            CustomButton('QQ Login', () async {
              _showDialog(
                  context,
                  "Please integrate related third plugins and retrive "
                  "the parameters to auth plugin to enable this function, you can read readme for detail.");
            }),
          ],
        )),
      ),
    );
  }
}
