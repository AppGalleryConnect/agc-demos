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

class PageEmailAuth extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return _PageEmailAuthState();
  }
}

class _PageEmailAuthState extends State<PageEmailAuth> {
  String _log = '';
  TextEditingController _emailController = TextEditingController();
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

  _createEmailUser() async {
    bool result = await _showEmailDialog(VerifyCodeAction.registerLogin);
    if (result == null) {
      print("cancel");
      return;
    }
    String email = _emailController.text;
    String verifyCode = _verifyCodeController.text;
    String password = _passwordController.text;
    AGCAuth.instance
        .createEmailUser(EmailUser(email, verifyCode, password: password))
        .then((value) {
      setState(() {
        _log = 'createEmailUser = ${value.user.uid} , ${value.user.providerId}';
      });
    }).catchError((error) => print(error));
  }

  _signInWithPassword() async {
    bool result = await _showEmailDialog(VerifyCodeAction.registerLogin);
    if (result == null) {
      print("cancel");
      return;
    }
    String email = _emailController.text;
    String password = _passwordController.text;
    AGCAuthCredential credential =
        EmailAuthProvider.credentialWithPassword(email, password);
    AGCAuth.instance.signIn(credential).then((value) {
      setState(() {
        _log =
            'signInWithPassword = ${value.user.uid} , ${value.user.providerId}';
      });
    });
  }

  _signInWithVerifyCode() async {
    bool result = await _showEmailDialog(VerifyCodeAction.registerLogin);
    if (result == null) {
      print("cancel");
      return;
    }
    String email = _emailController.text;
    String verifyCode = _verifyCodeController.text;
    String password = _passwordController.text;
    AGCAuthCredential credential = EmailAuthProvider.credentialWithVerifyCode(
        email, verifyCode,
        password: password);
    AGCAuth.instance.signIn(credential).then((value) {
      setState(() {
        _log =
            'signInWithVerifyCode = ${value.user.uid} , ${value.user.providerId}';
      });
    });
  }

  _resetEmailPassword() async {
    bool result = await _showEmailDialog(VerifyCodeAction.resetPassword);
    if (result == null) {
      print("cancel");
      return;
    }
    String email = _emailController.text;
    String verifyCode = _verifyCodeController.text;
    String password = _passwordController.text;
    AGCAuth.instance
        .resetPasswordWithEmail(email, password, verifyCode)
        .then((value) => print('resetEmailPassword'));
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
    bool result = await _showEmailDialog(VerifyCodeAction.registerLogin);
    if (result == null) {
      print("cancel");
      return;
    }
    String email = _emailController.text;
    String verifyCode = _verifyCodeController.text;
    String password = _passwordController.text;
    AGCAuthCredential credential = EmailAuthProvider.credentialWithVerifyCode(
        email, verifyCode,
        password: password);
    SignInResult signInResult = await user.link(credential).catchError((error) {
      print(error);
    });
    setState(() {
      _log = 'link email = ${signInResult?.user?.uid}';
    });
  }

  _unlink() async {
    AGCUser user = await AGCAuth.instance.currentUser;
    if (user == null) {
      print("no user signed in");
      return;
    }
    SignInResult result =
        await user.unlink(AuthProviderType.email).catchError((error) {
      print(error);
    });
    setState(() {
      _log = 'unlink email = ${result?.user?.uid}';
    });
  }

  _updateEmail() async {
    AGCUser user = await AGCAuth.instance.currentUser;
    if (user == null) {
      print("no user signed in");
      return;
    }
    bool result = await _showEmailDialog(VerifyCodeAction.registerLogin);
    if (result == null) {
      print("cancel");
      return;
    }
    String email = _emailController.text;
    String verifyCode = _verifyCodeController.text;
    await user.updateEmail(email, verifyCode).catchError((error) {
      print(error);
    });
    print('updateEmail');
  }

  _updatePassword() async {
    AGCUser user = await AGCAuth.instance.currentUser;
    if (user == null) {
      print("no user signed in");
      return;
    }
    bool result = await _showEmailDialog(VerifyCodeAction.resetPassword);
    if (result == null) {
      print("cancel");
      return;
    }
    String verifyCode = _verifyCodeController.text;
    String password = _passwordController.text;
    await user.updatePassword(password, verifyCode, AuthProviderType.email).catchError((error){
      print(error);
    });
    print('updatePassword');
  }

  Future<bool> _showEmailDialog(VerifyCodeAction action) {
    return showDialog<bool>(
      context: context,
      builder: (ctx) {
        return AlertDialog(
          title: Text("Input"),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            children: <Widget>[
              CupertinoTextField(
                controller: _emailController,
                placeholder: 'email',
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
                        onPressed: () => _requestEmailVerifyCode(action)),
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

  _requestEmailVerifyCode(VerifyCodeAction action) {
    String email = _emailController.text;
    VerifyCodeSettings settings = VerifyCodeSettings(action, sendInterval: 30);
    EmailAuthProvider.requestVerifyCode(email, settings)
        .then((value) => print(value.validityPeriod));
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Email Auth'),
      ),
      body: Center(
          child: ListView(
        children: <Widget>[
          Text(_log),
          CupertinoButton(
              child: Text('getCurrentUser'), onPressed: _getCurrentUser),
          CupertinoButton(
              child: Text('createEmailUser'), onPressed: _createEmailUser),
          CupertinoButton(
              child: Text('signInWithPassword'),
              onPressed: _signInWithPassword),
          CupertinoButton(
              child: Text('signInWithVerifyCode'),
              onPressed: _signInWithVerifyCode),
          CupertinoButton(
              child: Text('resetEmailPassword'),
              onPressed: _resetEmailPassword),
          CupertinoButton(child: Text('signOut'), onPressed: _signOut),
          CupertinoButton(child: Text('deleteUser'), onPressed: _deleteUser),
          CupertinoButton(child: Text('link email'), onPressed: _link),
          CupertinoButton(child: Text('unlink email'), onPressed: _unlink),
          CupertinoButton(child: Text('updateEmail'), onPressed: _updateEmail),
          CupertinoButton(child: Text('updatePassword'), onPressed: _updatePassword),
        ],
      )),
    );
  }
}
