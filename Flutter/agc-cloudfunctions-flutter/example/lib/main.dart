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

import 'dart:async';

import 'package:agconnect_cloudfunctions/agconnect_cloudfunctions.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  static const _PADDING = EdgeInsets.symmetric(vertical: 1.0, horizontal: 16);
  static const _DEFAULT_FUNCTION_TEXT = 'testfunc-\$latest';
  static const _DEFAULT_RESULT_TEXT =
      'Change the function name as you wish and click \'Call the Function\' button to get response';

  TextEditingController _resultTextController;
  TextEditingController _functionTextController;

  @override
  void initState() {
    super.initState();
    _resultTextController = TextEditingController(text: _DEFAULT_RESULT_TEXT);
    _functionTextController =
        TextEditingController(text: _DEFAULT_FUNCTION_TEXT);
  }

  @override
  void dispose() {
    _resultTextController?.dispose();
    _functionTextController?.dispose();
    super.dispose();
  }

  Future<void> _callFunction() async {
    if (!mounted) return;

    try {
      FunctionCallable functionCallable =
          FunctionCallable(_functionTextController.text);

      Map<String, dynamic> parameters = <String, dynamic>{
        'number1': 15,
        'number2': 14
      };

      FunctionResult functionResult = await functionCallable.call(parameters);

      setState(() {
        _resultTextController.text = functionResult.getValue();
      });
    } on PlatformException catch (e) {
      setState(() {
        _resultTextController.text = FunctionError.fromException(e).toJson();
      });
    }
  }

  void _clear() {
    setState(() {
      _resultTextController.text = _DEFAULT_RESULT_TEXT;
      _functionTextController.text = _DEFAULT_FUNCTION_TEXT;
    });
  }

  Widget expandedButton(
    Function func,
    String txt, {
    double fontSize = 16.0,
    Color color,
  }) {
    return Flex(
      direction: Axis.horizontal,
      children: [
        Padding(
          padding: _PADDING,
          child: RaisedButton(
            onPressed: func,
            color: color,
            child: Text(
              txt,
              style: TextStyle(fontSize: fontSize),
            ),
          ),
        )
      ],
    );
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('AGC Cloud Functions Demo App'),
          centerTitle: true,
        ),
        body: Center(
          child: ListView(
            shrinkWrap: true,
            children: <Widget>[
              Padding(
                padding: _PADDING,
                child: TextField(
                  controller: _functionTextController,
                  textAlign: TextAlign.center,
                  decoration: new InputDecoration(
                    focusedBorder: OutlineInputBorder(
                      borderSide:
                          BorderSide(color: Colors.blueAccent, width: 3.0),
                    ),
                    enabledBorder: OutlineInputBorder(
                      borderSide:
                          BorderSide(color: Colors.blueGrey, width: 3.0),
                    ),
                  ),
                ),
              ),
              Row(
                children: [
                  expandedButton(_callFunction, 'Call the Function',
                      fontSize: 20),
                  expandedButton(_clear, 'Reset', fontSize: 20),
                ],
              ),
              Padding(
                padding: _PADDING,
                child: TextField(
                  controller: _resultTextController,
                  keyboardType: TextInputType.multiline,
                  maxLines: 15,
                  readOnly: true,
                  decoration: new InputDecoration(
                    focusedBorder: OutlineInputBorder(
                      borderSide:
                          BorderSide(color: Colors.blueAccent, width: 3.0),
                    ),
                    enabledBorder: OutlineInputBorder(
                      borderSide:
                          BorderSide(color: Colors.blueGrey, width: 3.0),
                    ),
                  ),
                ),
              )
            ],
          ),
        ),
      ),
    );
  }
}
