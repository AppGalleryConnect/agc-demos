/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import 'package:flutter/material.dart';
import 'package:agconnect_crash/agconnect_crash.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {

  _enableCollection() async {
    await AGCCrash.instance.enableCrashCollection(true);
  }

  _disableCollection() async {
    await AGCCrash.instance.enableCrashCollection(false);
  }

  _testCrash() async {
    await AGCCrash.instance.testIt();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                MaterialButton(onPressed: _enableCollection, color: Colors.blue, child: Text('Enable Crash Collection', style: TextStyle(color: Colors.white),)),
                MaterialButton(onPressed: _disableCollection, color: Colors.blue, child: Text('Disable Crash Collection', style: TextStyle(color: Colors.white),)),
                MaterialButton(onPressed: _testCrash, color: Colors.blue, child: Text('Test Crash', style: TextStyle(color: Colors.white),)),
              ],)
        ),
      ),
    );
  }
}
