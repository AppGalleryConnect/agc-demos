import 'dart:developer';

import 'package:flutter/material.dart';

import 'package:agconnect_remote_config/agconnect_remote_config.dart';

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
  }

  _buttonOneClicked() {
    Navigator.push(
        context,
        MaterialPageRoute(
            builder: (context) => PageModeOne(), fullscreenDialog: true));
  }

  _buttonTwoClicked() {
    Navigator.push(
        context,
        MaterialPageRoute(
            builder: (context) => PageModeTwo(), fullscreenDialog: true));
  }

  _clearData() async {
    await AGCRemoteConfig.instance.clearAll();
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
              MaterialButton(
                  onPressed: _buttonOneClicked,
                  color: Colors.blue,
                  child: Text(
                    'Mode 1:Fetch And Activate Immediately',
                    style: TextStyle(color: Colors.white),
                  )),
              MaterialButton(
                  onPressed: _buttonTwoClicked,
                  color: Colors.blue,
                  child: Text(
                    'Mode 2:Fetch And Activate Next Time',
                    style: TextStyle(color: Colors.white),
                  )),
              Text('Please clear data before switching modes'),
              MaterialButton(
                  onPressed: _clearData,
                  color: Colors.blue,
                  child: Text(
                    'Clear Data',
                    style: TextStyle(color: Colors.white),
                  )),
            ],
          ),
        ),
      ),
    );
  }
}

class PageModeOne extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return _PageModeOneState();
  }
}

class _PageModeOneState extends State<PageModeOne> {
  Map _allValue;

  @override
  void initState() {
    super.initState();
    _fetchAndActivateImmediately();
  }

  _fetchAndActivateImmediately() async {
    await AGCRemoteConfig.instance.fetch().catchError((error)=>log(error.toString()));
    await AGCRemoteConfig.instance.applyLastFetched();
    Map value = await AGCRemoteConfig.instance.getMergedAll();
    setState(() {
      _allValue = value;
    });
  }


  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text('Mode 1'),
        ),
        body: Center(
          child: Text('$_allValue'),
        ));
  }
}

class PageModeTwo extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return _PageModeTwoState();
  }
}

class _PageModeTwoState extends State<PageModeTwo> {
  Map _allValue;

  @override
  void initState() {
    super.initState();
    _fetchAndActivateNextTime();
  }

  _fetchAndActivateNextTime() async {
    await AGCRemoteConfig.instance.applyLastFetched();
    Map value = await AGCRemoteConfig.instance.getMergedAll();
    setState(() {
      _allValue = value;
    });
    await AGCRemoteConfig.instance.fetch().catchError((error)=>log(error.toString()));
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text('Mode 2'),
        ),
        body: Center(
          child: Text('$_allValue'),
        ));
  }
}