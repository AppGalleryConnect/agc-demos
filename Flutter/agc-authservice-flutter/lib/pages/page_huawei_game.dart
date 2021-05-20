/*
    Copyright 2020-2021. HuaweiGame Technologies Co., Ltd. All rights reserved.
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
import 'package:huawei_account/hmsauthservice/hms_auth_service.dart';
import 'package:huawei_account/huawei_account.dart';
import 'package:huawei_account/model/hms_auth_huawei_id.dart';
import 'package:huawei_gameservice/huawei_gameservice.dart';

class PageHuaweiGameAuth extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return _PageHuaweiGameAuthState();
  }
}

class _PageHuaweiGameAuthState extends State<PageHuaweiGameAuth> {
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
    final helper = new HmsAuthParamHelper();
    helper
      ..setIdToken()
      ..setAccessToken()
      ..setAuthorizationCode()
      ..setEmail()
      ..setProfile()
      ..setDefaultParam(HmsAuthParams().defaultAuthRequestParamGame)
      ..setScopeList([
        HmsScope.game,
      ]);
    try {
      HmsAuthHuaweiId huaweiGame =
          await HmsAuthService.signIn(authParamHelper: helper);

      Player result = await PlayersClient.getCurrentPlayer();
      AGCAuthCredential credential = HuaweiGameAuthProvider.credentialWithToken(
          result.playerSign,
          result.iconImageUri,
          result.level,
          result.playerId,
          result.displayName,
          result.signTs);
      AGCAuth.instance.signIn(credential).then((value) {
        setState(() {
          _log =
              'signInHuaweiGameId = ${value.user.uid} , ${value.user.providerId}';
        });
      });
    } on Exception catch (e) {
      print(e.toString());
    }
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
    final helper = new HmsAuthParamHelper();
    helper
      ..setIdToken()
      ..setAccessToken()
      ..setAuthorizationCode()
      ..setEmail()
      ..setProfile()
      ..setDefaultParam(HmsAuthParams().defaultAuthRequestParamGame)
      ..setScopeList([
        HmsScope.game,
      ]);
    try {
      HmsAuthHuaweiId huaweiGame =
          await HmsAuthService.signIn(authParamHelper: helper);

      Player result = await PlayersClient.getCurrentPlayer();
      AGCAuthCredential credential = HuaweiGameAuthProvider.credentialWithToken(
          result.playerSign,
          result.iconImageUri,
          result.level,
          result.playerId,
          result.displayName,
          result.signTs);
      SignInResult signInResult =
          await user.link(credential).catchError((error) {
        print(error);
      });
      setState(() {
        _log = 'link HuaweiGame = ${signInResult?.user?.uid}';
      });
    } on Exception catch (e) {
      print(e.toString());
    }
  }

  _unlink() async {
    AGCUser user = await AGCAuth.instance.currentUser;
    if (user == null) {
      print("no user signed in");
      return;
    }
    SignInResult result =
        await user.unlink(AuthProviderType.hms).catchError((error) {
      print(error);
    });
    setState(() {
      _log = 'unlink HuaweiGame = ${result?.user?.uid}';
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('HuaweiGame Auth'),
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
                            CustomButton('signInHuaweiGame', _signIn),
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
