//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved

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
