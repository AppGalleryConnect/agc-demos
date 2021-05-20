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

class PagePhoneAuth extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return _PagePhoneAuthState();
  }
}

class _PagePhoneAuthState extends State<PagePhoneAuth> {
  String _log = '';
  TextEditingController _countryCodeController = TextEditingController();
  TextEditingController _phoneNumberController = TextEditingController();
  TextEditingController _passwordController = TextEditingController();
  TextEditingController _verifyCodeController = TextEditingController();

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

  _createPhoneUser() async {
    bool result = await _showPhoneDialog(VerifyCodeAction.registerLogin);
    if (result == null) {
      print("cancel");
      return;
    }
    String countryCode = _countryCodeController.text;
    String phoneNumber = _phoneNumberController.text;
    String verifyCode = _verifyCodeController.text;
    String password = _passwordController.text;
    AGCAuth.instance
        .createPhoneUser(
            PhoneUser(countryCode, phoneNumber, verifyCode, password: password))
        .then((value) {
      setState(() {
        _log = 'createPhoneUser = ${value.user.uid} , ${value.user.providerId}';
      });
    }).catchError((error) => print(error));
  }

  _signInWithPassword() async {
    bool result = await _showPhoneDialog(VerifyCodeAction.registerLogin);
    if (result == null) {
      print("cancel");
      return;
    }
    String countryCode = _countryCodeController.text;
    String phoneNumber = _phoneNumberController.text;
    String password = _passwordController.text;
    AGCAuthCredential credential = PhoneAuthProvider.credentialWithPassword(
        countryCode, phoneNumber, password);
    AGCAuth.instance.signIn(credential).then((value) {
      setState(() {
        _log =
            'signInWithPassword = ${value.user.uid} , ${value.user.providerId}';
      });
    });
  }

  _signInWithVerifyCode() async {
    bool result = await _showPhoneDialog(VerifyCodeAction.registerLogin);
    if (result == null) {
      print("cancel");
      return;
    }
    String countryCode = _countryCodeController.text;
    String phoneNumber = _phoneNumberController.text;
    String verifyCode = _verifyCodeController.text;
    String password = _passwordController.text;
    AGCAuthCredential credential = PhoneAuthProvider.credentialWithVerifyCode(
        countryCode, phoneNumber, verifyCode,
        password: password);
    AGCAuth.instance.signIn(credential).then((value) {
      setState(() {
        _log =
            'signInWithVerifyCode = ${value.user.uid} , ${value.user.providerId}';
      });
    });
  }

  _resetPhonePassword() async {
    bool result = await _showPhoneDialog(VerifyCodeAction.resetPassword);
    if (result == null) {
      print("cancel");
      return;
    }
    String countryCode = _countryCodeController.text;
    String phoneNumber = _phoneNumberController.text;
    String verifyCode = _verifyCodeController.text;
    String password = _passwordController.text;
    AGCAuth.instance
        .resetPasswordWithPhone(countryCode, phoneNumber, password, verifyCode)
        .then((value) => print('resetPhonePassword'));
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

  _link() async {
    AGCUser user = await AGCAuth.instance.currentUser;
    if (user == null) {
      print("no user signed in");
      return;
    }
    bool result = await _showPhoneDialog(VerifyCodeAction.registerLogin);
    if (result == null) {
      print("cancel");
      return;
    }
    String countryCode = _countryCodeController.text;
    String phoneNumber = _phoneNumberController.text;
    String verifyCode = _verifyCodeController.text;
    String password = _passwordController.text;
    AGCAuthCredential credential = PhoneAuthProvider.credentialWithVerifyCode(
        countryCode, phoneNumber, verifyCode,
        password: password);
    SignInResult signInResult = await user.link(credential).catchError((error) {
      print(error);
    });
    setState(() {
      _log = 'link phone = ${signInResult?.user?.uid}';
    });
  }

  _unlink() async {
    AGCUser user = await AGCAuth.instance.currentUser;
    if (user == null) {
      print("no user signed in");
      return;
    }
    SignInResult result =
        await user.unlink(AuthProviderType.phone).catchError((error) {
      print(error);
    });
    setState(() {
      _log = 'unlink phone = ${result?.user?.uid}';
    });
  }

  _updatePhone() async {
    AGCUser user = await AGCAuth.instance.currentUser;
    if (user == null) {
      print("no user signed in");
      return;
    }
    bool result = await _showPhoneDialog(VerifyCodeAction.registerLogin);
    if (result == null) {
      print("cancel");
      return;
    }
    String countryCode = _countryCodeController.text;
    String phoneNumber = _phoneNumberController.text;
    String verifyCode = _verifyCodeController.text;
    await user
        .updatePhone(countryCode, phoneNumber, verifyCode)
        .catchError((error) {
      print(error);
    });
    print('updatePhone');
  }

  _updatePassword() async {
    AGCUser user = await AGCAuth.instance.currentUser;
    if (user == null) {
      print("no user signed in");
      return;
    }
    bool result = await _showPhoneDialog(VerifyCodeAction.resetPassword);
    if (result == null) {
      print("cancel");
      return;
    }
    String verifyCode = _verifyCodeController.text;
    String password = _passwordController.text;
    await user
        .updatePassword(password, verifyCode, AuthProviderType.phone)
        .catchError((error) {
      print(error);
    });
    print('updatePassword');
  }

  Future<bool> _showPhoneDialog(VerifyCodeAction action) {
    return showDialog<bool>(
      context: context,
      builder: (ctx) {
        return AlertDialog(
          title: Text("Input"),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            children: <Widget>[
              CupertinoTextField(
                controller: _countryCodeController,
                placeholder: 'country code',
              ),
              CupertinoTextField(
                controller: _phoneNumberController,
                placeholder: 'phone number',
              ),
              CupertinoTextField(
                controller: _passwordController,
                placeholder: 'password',
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.start,
                children: <Widget>[
                  Expanded(
                    flex: 1,
                    child: CupertinoTextField(
                      controller: _verifyCodeController,
                      placeholder: 'verification code',
                    ),
                  ),
                  Container(
                    width: 100,
                    height: 32,
                    child: FlatButton(
                        color: Colors.white,
                        child: Text('send'),
                        onPressed: () => _requestPhoneVerifyCode(action)),
                  ),
                ],
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
                Navigator.of(context).pop(true);
              },
            ),
          ],
        );
      },
    );
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

  _requestPhoneVerifyCode(VerifyCodeAction action) {
    String countryCode = _countryCodeController.text;
    String phoneNumber = _phoneNumberController.text;
    VerifyCodeSettings settings = VerifyCodeSettings(action, sendInterval: 30);
    PhoneAuthProvider.requestVerifyCode(countryCode, phoneNumber, settings)
        .then((value) => _showDialog(context, "Verification code sent"));
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Phone Auth'),
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
                            CustomButton('createPhoneUser', _createPhoneUser),
                            CustomButton(
                                'signInWithPassword', _signInWithPassword),
                            CustomButton(
                                'signInWithVerifyCode', _signInWithVerifyCode),
                            CustomButton(
                                'resetPhonePassword', _resetPhonePassword),
                            CustomButton('signOut', _signOut),
                            CustomButton('deleteUser', _deleteUser),
                            CustomButton('link phone', _link),
                            CustomButton('unlink phone', _unlink),
                            CustomButton('updatePhone', _updatePhone),
                            CustomButton('updatePassword', _updatePassword),
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
