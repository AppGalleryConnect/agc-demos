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

import 'package:agconnect_auth/agconnect_auth.dart';
import 'package:agconnect_auth_example/custom_button.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class PageAnonymousAuth extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return _PageAnonymousAuthState();
  }
}

class _PageAnonymousAuthState extends State<PageAnonymousAuth> {
  String _log = '';

  @override
  void initState() {
    super.initState();
    _getCurrentUser();
  }

  _getCurrentUser() async {
    AGCAuth.instance.currentUser.then((value) {
      setState(() {
        _log = 'current user = ${value?.uid} , ${value?.providerId}';
      });
    });
  }

  _signIn() async {
    AGCAuth.instance.signInAnonymously().then((value) {
      setState(() {
        _log =
            'signInAnonymously = ${value.user.uid} , ${value.user.providerId}';
      });
    });
  }

  _signOut() {
    AGCAuth.instance.signOut().then((value) {
      setState(() {
        _log = 'signOut';
      });
    }).catchError((error) => print(error));
  }

  _deleteUser() {
    AGCAuth.instance.deleteUser().then((value) {
      setState(() {
        _log = 'deleteUser';
      });
    }).catchError((error) => print(error));
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Anonymous Auth'),
      ),
      body: Container(
          padding: EdgeInsets.symmetric(horizontal: 12),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: <Widget>[
              Container(
                padding: EdgeInsets.only(
                  top: 10,
                ),
                height: 90,
                child: Text(_log),
              ),
              Divider(
                thickness: 0.1,
                color: Colors.black,
              ),
              CustomButton('getCurrentUser', _getCurrentUser),
              CustomButton('signInAnonymously', _signIn),
              CustomButton('signOut', _signOut),
              CustomButton('deleteUser', _deleteUser),
            ],
          )),
    );
  }
}
