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
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:google_sign_in/google_sign_in.dart';

import '../custom_button.dart';

class PageGoogleAuth extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return _PageGoogleAuthState();
  }
}

class _PageGoogleAuthState extends State<PageGoogleAuth> {
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
    GoogleSignIn _googleSignIn = GoogleSignIn(
      scopes: <String>[
        'email',
        'https://www.googleapis.com/auth/contacts.readonly',
      ],
    );
    final GoogleSignInAccount googleUser = await _googleSignIn.signIn();
    // Obtain the auth details from the request
    final GoogleSignInAuthentication googleAuth =
        await googleUser.authentication;
    AGCAuthCredential credential =
        GoogleAuthProvider.credentialWithToken(googleAuth.idToken);
    AGCAuth.instance.signIn(credential).then((value) {
      setState(() {
        _log = 'signInGoogle = ${value.user.uid} , ${value.user.providerId}';
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

  _link() async {
    AGCUser user = await AGCAuth.instance.currentUser;
    if (user == null) {
      print("no user signed in");
      return;
    }
    GoogleSignIn _googleSignIn = GoogleSignIn(
      scopes: <String>[
        'email',
        'https://www.googleapis.com/auth/contacts.readonly',
      ],
    );
    final GoogleSignInAccount googleUser = await _googleSignIn.signIn();
    // Obtain the auth details from the request
    final GoogleSignInAuthentication googleAuth =
        await googleUser.authentication;
    AGCAuthCredential credential = GoogleAuthProvider.credentialWithToken(
        googleAuth.idToken);
    SignInResult signInResult = await user.link(credential).catchError((error) {
      print(error);
    });
    setState(() {
      _log = 'link Google = ${signInResult?.user?.uid}';
    });
  }

  _unlink() async {
    AGCUser user = await AGCAuth.instance.currentUser;
    if (user == null) {
      print("no user signed in");
      return;
    }
    SignInResult result =
        await user.unlink(AuthProviderType.google).catchError((error) {
      print(error);
    });
    setState(() {
      _log = 'unlink google = ${result?.user?.uid}';
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Google Auth'),
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
                            CustomButton('signInGoogle', _signIn),
                            CustomButton('signOut', _signOut),
                            CustomButton('deleteUser', _deleteUser),
                            CustomButton('link', _link),
                            CustomButton('unlink', _unlink)
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
