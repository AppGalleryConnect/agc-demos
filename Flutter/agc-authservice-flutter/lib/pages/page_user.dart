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

import 'dart:async';

import 'package:agconnect_auth/agconnect_auth.dart';
import 'package:agconnect_auth_example/custom_button.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class PageUser extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return _PageUserState();
  }
}

class _PageUserState extends State<PageUser> {
  String _log = '';
  TextEditingController _displayNameController = TextEditingController();
  TextEditingController _photoUrlController = TextEditingController();

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

  _updateProfile() async {
    AGCUser user = await AGCAuth.instance.currentUser;
    if (user == null) {
      print("no user signed in");
      return;
    }
    bool result = await _showProfileDialog();
    if (result == null) {
      print("cancel");
      return;
    }
    String name = _displayNameController.text;
    String photo = _photoUrlController.text;
    ProfileRequest request = ProfileRequest(name, photo);
    await user.updateProfile(request).catchError((error) {
      print(error);
    });
    print('updateProfile');
  }

  _getUserExtra() async {
    AGCUser user = await AGCAuth.instance.currentUser;
    AGCUserExtra userExtra = await user.userExtra.catchError((error) {
      print(error);
    });
    setState(() {
      _log =
          'getUserExtra ${userExtra.createTime}, ${userExtra.lastSignInTime}';
    });
  }

  _getToken() async {
    AGCUser user = await AGCAuth.instance.currentUser.catchError((e) {
      AGCAuthException exception = e;
    });
    TokenResult res = await user?.getToken();
    setState(() {
      _log = 'getToken ${res?.token} ';
    });
  }

  Future<bool> _showProfileDialog() {
    return showDialog<bool>(
      context: context,
      builder: (ctx) {
        return AlertDialog(
          title: Text("Input"),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            children: <Widget>[
              CupertinoTextField(
                controller: _displayNameController,
                placeholder: 'display name',
              ),
              CupertinoTextField(
                controller: _photoUrlController,
                placeholder: 'photo url',
              ),
            ],
          ),
          actions: <Widget>[
            FlatButton(
              child: Text("CANCEL"),
              onPressed: () => Navigator.of(context).pop(),
            ),
            FlatButton(
              child: Text("OK"),
              onPressed: () {
                //关闭对话框并返回true
                Navigator.of(context).pop(true);
              },
            ),
          ],
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('User Info'),
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
              Expanded(
                child: CustomScrollView(
                  shrinkWrap: true,
                  slivers: <Widget>[
                    SliverPadding(
                      padding: const EdgeInsets.all(12),
                      sliver: SliverList(
                        delegate: SliverChildListDelegate(
                          <Widget>[
                            CustomButton('getCurrentUser', _getCurrentUser),
                            CustomButton('updateProfile', _updateProfile),
                            CustomButton('getUserExtra', _getUserExtra),
                            CustomButton('getToken', _getToken),
                          ],
                        ),
                      ),
                    ),
                  ],
                ),
              )
            ],
          )),
    );
  }
}
