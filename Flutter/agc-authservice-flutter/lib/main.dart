/*
    Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
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

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'dart:async';

import 'package:agconnect_auth/agconnect_auth.dart';

import 'page_anonymous.dart';
import 'page_email.dart';
import 'page_phone.dart';
import 'page_self_build.dart';
import 'page_user.dart';

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
                CupertinoButton(
                    child: Text('Anonymous'),
                    onPressed: () async {
                      Navigator.push(
                          context,
                          MaterialPageRoute(
                              builder: (context) => PageAnonymousAuth()));
                    }),
                CupertinoButton(
                    child: Text('Phone'),
                    onPressed: () async {
                      Navigator.push(context,
                          MaterialPageRoute(builder: (context) => PagePhoneAuth()));
                    }),
                CupertinoButton(
                    child: Text('Email'),
                    onPressed: () async {
                      Navigator.push(context,
                          MaterialPageRoute(builder: (context) => PageEmailAuth()));
                    }),
                CupertinoButton(
                    child: Text('Self Build'),
                    onPressed: () async {
                      Navigator.push(
                          context,
                          MaterialPageRoute(
                              builder: (context) => PageSelfBuildAuth()));
                    }),
                CupertinoButton(
                    child: Text('User Info'),
                    onPressed: () async {
                      Navigator.push(context,
                          MaterialPageRoute(builder: (context) => PageUser()));
                    }),
              ],
            )),
      ),
    );
  }
}
