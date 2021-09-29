//  Copyright (c) Huawei Technologies Co., Ltd. 2021-2021. All rights reserved.

import 'dart:async';

import 'package:agconnect_auth/agconnect_auth.dart';
import 'package:agconnect_clouddb/agconnect_clouddb.dart';
import 'package:flutter/material.dart';

void main() {
  runApp(_App());
}

class _App extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: _HomeScreen(),
    );
  }
}

class _HomeScreen extends StatefulWidget {
  @override
  __HomeScreenState createState() => __HomeScreenState();
}

class __HomeScreenState extends State<_HomeScreen> {
  final String _zoneName = 'QuickStartDemo';
  final String _objectTypeName = 'BookInfo';
  AGConnectCloudDBZone _zone;
  String _currentUserUid;
  StreamSubscription<AGConnectCloudDBZoneSnapshot> _snapshotSubscription;
  StreamSubscription<String> _onEvent;
  StreamSubscription<bool> _onDataEncryptionKeyChanged;

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) async {
      try {
        await _initCurrentUser();
        await AGConnectCloudDB.getInstance().initialize();
        await AGConnectCloudDB.getInstance().createObjectType();
        _onEvent = AGConnectCloudDB.getInstance().onEvent().listen((String event) {
          _showDialog(context, 'On Event: $event');
        });
        _onDataEncryptionKeyChanged = AGConnectCloudDB.getInstance().onDataEncryptionKeyChanged().listen((bool data) {
          _showDialog(context, 'Data Encryption Key Changed: $data');
        });
      } catch (e) {
        _handleException(e);
      }
    });
  }

  @override
  void dispose() {
    if (_snapshotSubscription != null) {
      _snapshotSubscription.cancel();
    }
    if (_onEvent != null) {
      _onEvent.cancel();
    }
    if (_onDataEncryptionKeyChanged != null) {
      _onDataEncryptionKeyChanged.cancel();
    }
    super.dispose();
  }

  Future<void> _initCurrentUser() async {
    final AGCUser currentUser = await AGCAuth.instance.currentUser;
    if (currentUser != null) {
      setState(() => _currentUserUid = currentUser.uid);
    } else {
      final SignInResult signInResult = await AGCAuth.instance.signInAnonymously();
      if (signInResult.user != null) {
        setState(() => _currentUserUid = signInResult.user.uid);
      } else {
        setState(() => _currentUserUid = '???');
      }
    }
  }

  void _showDialog(BuildContext context, [String content = 'SUCCESS']) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: const Text('RESULT'),
          content: SingleChildScrollView(
            physics: const BouncingScrollPhysics(),
            child: Text(content),
          ),
        );
      },
    );
  }

  Widget _buildGroup({List<Widget> children}) {
    return Container(
      margin: const EdgeInsets.symmetric(vertical: 8),
      decoration: const BoxDecoration(
        color: Colors.black12,
        borderRadius: BorderRadius.all(Radius.circular(16)),
      ),
      child: Wrap(
        alignment: WrapAlignment.center,
        children: children,
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(_zoneName),
        actions: <Widget>[
          IconButton(
            icon: const Icon(Icons.refresh),
            onPressed: () async {
              await AGCAuth.instance.signOut();
              setState(() => _currentUserUid = null);
              await _initCurrentUser();
            },
          ),
        ],
      ),
      body: Column(
        crossAxisAlignment: CrossAxisAlignment.center,
        children: <Widget>[
          Padding(
            padding: const EdgeInsets.all(16),
            child: Text('UserID: $_currentUserUid'),
          ),
          Expanded(
            child: ListView(
              padding: const EdgeInsets.all(16),
              physics: const BouncingScrollPhysics(),
              children: <Widget>[
                _buildGroup(
                  children: <Widget>[
                    FlatButton(
                      child: const Text('openCloudDBZone'),
                      onPressed: _openCloudDBZone,
                    ),
                    FlatButton(
                      child: const Text('openCloudDBZone2'),
                      onPressed: _openCloudDBZone2,
                    ),
                    FlatButton(
                      child: const Text('closeCloudDBZone'),
                      onPressed: _closeCloudDBZone,
                    ),
                    FlatButton(
                      child: const Text('deleteCloudDBZone'),
                      onPressed: _deleteCloudDBZone,
                    ),
                  ],
                ),
                _buildGroup(
                  children: <Widget>[
                    FlatButton(
                      child: const Text('getCloudDBZoneConfig'),
                      onPressed: _getCloudDBZoneConfig,
                    ),
                    FlatButton(
                      child: const Text('getCloudDBZoneConfigs'),
                      onPressed: _getCloudDBZoneConfigs,
                    ),
                  ],
                ),
                _buildGroup(
                  children: <Widget>[
                    FlatButton(
                      child: const Text('enableNetwork'),
                      onPressed: _enableNetwork,
                    ),
                    FlatButton(
                      child: const Text('disableNetwork'),
                      onPressed: _disableNetwork,
                    ),
                  ],
                ),
                _buildGroup(
                  children: <Widget>[
                    FlatButton(
                      child: const Text('setUserKey'),
                      onPressed: _setUserKey,
                    ),
                    FlatButton(
                      child: const Text('updateDataEncryptionKey'),
                      onPressed: _updateDataEncryptionKey,
                    ),
                  ],
                ),
                const Divider(height: 32),
                _buildGroup(
                  children: <Widget>[
                    FlatButton(
                      child: const Text('subscribeSnapshot'),
                      onPressed: _subscribeSnapshot,
                    ),
                    FlatButton(
                      child: const Text('removeSnapshot'),
                      onPressed: () {
                        if (_snapshotSubscription != null) {
                          _snapshotSubscription.cancel().then((_) {
                            _snapshotSubscription = null;
                            _showDialog(context);
                          });
                        }
                      },
                    ),
                  ],
                ),
                const Divider(height: 32),
                _buildGroup(
                  children: <Widget>[
                    FlatButton(
                      child: const Text('executeUpsert'),
                      onPressed: _executeUpsert,
                    ),
                    FlatButton(
                      child: const Text('executeDelete'),
                      onPressed: _executeDelete,
                    ),
                    FlatButton(
                      child: const Text('runTransaction'),
                      onPressed: _runTransaction,
                    ),
                  ],
                ),
                _buildGroup(
                  children: <Widget>[
                    FlatButton(
                      child: const Text('executeQuery'),
                      onPressed: _executeQuery,
                    ),
                    FlatButton(
                      child: const Text('executeQueryUnsynced'),
                      onPressed: _executeQueryUnsynced,
                    ),
                  ],
                ),
                _buildGroup(
                  children: <Widget>[
                    FlatButton(
                      child: const Text('executeCountQuery'),
                      onPressed: _executeCountQuery,
                    ),
                    FlatButton(
                      child: const Text('executeSumQuery'),
                      onPressed: _executeSumQuery,
                    ),
                    FlatButton(
                      child: const Text('executeAverageQuery'),
                      onPressed: _executeAverageQuery,
                    ),
                    FlatButton(
                      child: const Text('executeMinimalQuery'),
                      onPressed: _executeMinimalQuery,
                    ),
                    FlatButton(
                      child: const Text('executeMaximumQuery'),
                      onPressed: _executeMaximumQuery,
                    ),
                  ],
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  void _handleException(dynamic e) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: const Text('ERROR'),
          content: Text(
            e is FormatException ? e.message : AGConnectCloudDBException.from(e).message,
          ),
        );
      },
    );
  }

  Future<void> _openCloudDBZone() async {
    try {
      if (_zone != null) {
        throw FormatException('Zone object is not null. First try close zone.', _zone);
      }

      _zone = await AGConnectCloudDB.getInstance().openCloudDBZone(
        zoneConfig: AGConnectCloudDBZoneConfig(
          zoneName: _zoneName,
        ),
      );
      _showDialog(context);
    } catch (e) {
      _handleException(e);
    }
  }

  Future<void> _openCloudDBZone2() async {
    try {
      if (_zone != null) {
        throw FormatException('Zone object is not null. First try close zone.', _zone);
      }

      _zone = await AGConnectCloudDB.getInstance().openCloudDBZone2(
        zoneConfig: AGConnectCloudDBZoneConfig(
          zoneName: _zoneName,
        ),
      );
      _showDialog(context);
    } catch (e) {
      _handleException(e);
    }
  }

  Future<void> _closeCloudDBZone() async {
    try {
      if (_zone == null) {
        throw FormatException('Zone object is null. First try open zone.', _zone);
      }

      await AGConnectCloudDB.getInstance().closeCloudDBZone(
        zone: _zone,
      );
      _zone = null;
      _showDialog(context);
    } catch (e) {
      _handleException(e);
    }
  }

  Future<void> _deleteCloudDBZone() async {
    try {
      await AGConnectCloudDB.getInstance().deleteCloudDBZone(
        zoneName: _zoneName,
      );
      _showDialog(context);
    } catch (e) {
      _handleException(e);
    }
  }

  Future<void> _enableNetwork() async {
    try {
      await AGConnectCloudDB.getInstance().enableNetwork(
        zoneName: _zoneName,
      );
      _showDialog(context);
    } catch (e) {
      _handleException(e);
    }
  }

  Future<void> _disableNetwork() async {
    try {
      await AGConnectCloudDB.getInstance().disableNetwork(
        zoneName: _zoneName,
      );
      _showDialog(context);
    } catch (e) {
      _handleException(e);
    }
  }

  Future<void> _setUserKey() async {
    try {
      await AGConnectCloudDB.getInstance().setUserKey(
        userKey: '123456789',
        userReKey: '',
      );
      _showDialog(context);
    } catch (e) {
      _handleException(e);
    }
  }

  Future<void> _updateDataEncryptionKey() async {
    try {
      await AGConnectCloudDB.getInstance().updateDataEncryptionKey();
      _showDialog(context);
    } catch (e) {
      _handleException(e);
    }
  }

  Future<void> _getCloudDBZoneConfig() async {
    try {
      if (_zone == null) {
        throw FormatException('Zone object is null. First try open zone.', _zone);
      }

      final AGConnectCloudDBZoneConfig config = await _zone.getCloudDBZoneConfig();
      _showDialog(context, config.toString());
    } catch (e) {
      _handleException(e);
    }
  }

  Future<void> _getCloudDBZoneConfigs() async {
    try {
      final List<AGConnectCloudDBZoneConfig> configs = await AGConnectCloudDB.getInstance().getCloudDBZoneConfigs();
      _showDialog(context, configs.toString());
    } catch (e) {
      _handleException(e);
    }
  }

  Future<void> _executeUpsert() async {
    try {
      if (_zone == null) {
        throw FormatException('Zone object is null. First try open zone.', _zone);
      }

      final int count = await _zone.executeUpsert(
        objectTypeName: _objectTypeName,
        entries: <Map<String, dynamic>>[
          <String, dynamic>{
            'id': 1,
            'bookName': 'Book Name - 1',
            'price': 14.80,
          },
          <String, dynamic>{
            'id': 2,
            'bookName': 'Book Name - 2',
            'price': 22.99,
          },
          <String, dynamic>{
            'id': 3,
            'bookName': 'Book Name - 3',
            'price': 5.60,
          },
        ],
      );
      _showDialog(context, '$count objects successfully written.');
    } catch (e) {
      _handleException(e);
    }
  }

  Future<void> _executeDelete() async {
    try {
      if (_zone == null) {
        throw FormatException('Zone object is null. First try open zone.', _zone);
      }

      final int count = await _zone.executeDelete(
        objectTypeName: _objectTypeName,
        entries: <Map<String, dynamic>>[
          <String, dynamic>{
            'id': 2,
          },
        ],
      );
      _showDialog(context, '$count objects successfully deleted.');
    } catch (e) {
      _handleException(e);
    }
  }

  Future<void> _executeQuery() async {
    try {
      if (_zone == null) {
        throw FormatException('Zone object is null. First try open zone.', _zone);
      }

      final AGConnectCloudDBZoneSnapshot snapshot = await _zone.executeQuery(
        query: AGConnectCloudDBQuery(_objectTypeName)..orderBy('price'),
        policy: AGConnectCloudDBZoneQueryPolicy.POLICY_QUERY_DEFAULT,
      );
      _showDialog(context, snapshot.toString());
    } catch (e) {
      _handleException(e);
    }
  }

  Future<void> _executeQueryUnsynced() async {
    try {
      if (_zone == null) {
        throw FormatException('Zone object is null. First try open zone.', _zone);
      }

      final AGConnectCloudDBZoneSnapshot snapshot = await _zone.executeQueryUnsynced(
        query: AGConnectCloudDBQuery(_objectTypeName)..orderBy('price'),
      );
      _showDialog(context, snapshot.toString());
    } catch (e) {
      _handleException(e);
    }
  }

  Future<void> _executeAverageQuery() async {
    try {
      if (_zone == null) {
        throw FormatException('Zone object is null. First try open zone.', _zone);
      }

      final dynamic average = await _zone.executeAverageQuery(
        field: 'price',
        query: AGConnectCloudDBQuery(_objectTypeName),
        policy: AGConnectCloudDBZoneQueryPolicy.POLICY_QUERY_DEFAULT,
      );
      _showDialog(context, average.toString());
    } catch (e) {
      _handleException(e);
    }
  }

  Future<void> _executeSumQuery() async {
    try {
      if (_zone == null) {
        throw FormatException('Zone object is null. First try open zone.', _zone);
      }

      final dynamic sum = await _zone.executeSumQuery(
        field: 'price',
        query: AGConnectCloudDBQuery(_objectTypeName),
        policy: AGConnectCloudDBZoneQueryPolicy.POLICY_QUERY_DEFAULT,
      );
      _showDialog(context, sum.toString());
    } catch (e) {
      _handleException(e);
    }
  }

  Future<void> _executeMaximumQuery() async {
    try {
      if (_zone == null) {
        throw FormatException('Zone object is null. First try open zone.', _zone);
      }

      final dynamic maximum = await _zone.executeMaximumQuery(
        field: 'price',
        query: AGConnectCloudDBQuery(_objectTypeName),
        policy: AGConnectCloudDBZoneQueryPolicy.POLICY_QUERY_DEFAULT,
      );
      _showDialog(context, maximum.toString());
    } catch (e) {
      _handleException(e);
    }
  }

  Future<void> _executeMinimalQuery() async {
    try {
      if (_zone == null) {
        throw FormatException('Zone object is null. First try open zone.', _zone);
      }

      final dynamic minimal = await _zone.executeMinimalQuery(
        field: 'price',
        query: AGConnectCloudDBQuery(_objectTypeName),
        policy: AGConnectCloudDBZoneQueryPolicy.POLICY_QUERY_DEFAULT,
      );
      _showDialog(context, minimal.toString());
    } catch (e) {
      _handleException(e);
    }
  }

  Future<void> _executeCountQuery() async {
    try {
      if (_zone == null) {
        throw FormatException('Zone object is null. First try open zone.', _zone);
      }

      final int count = await _zone.executeCountQuery(
        field: 'price',
        query: AGConnectCloudDBQuery(_objectTypeName),
        policy: AGConnectCloudDBZoneQueryPolicy.POLICY_QUERY_DEFAULT,
      );
      _showDialog(context, count.toString());
    } catch (e) {
      _handleException(e);
    }
  }

  Future<void> _runTransaction() async {
    try {
      if (_zone == null) {
        throw FormatException('Zone object is null. First try open zone.', _zone);
      }

      await _zone.runTransaction(
        transaction: AGConnectCloudDBTransaction()
          ..executeUpsert(
            objectTypeName: _objectTypeName,
            entries: <Map<String, dynamic>>[
              <String, dynamic>{
                'id': 10,
                'bookName': 'Book_10',
                'price': 5.10,
              },
              <String, dynamic>{
                'id': 20,
                'bookName': 'Book_20',
                'price': 25.20,
              },
            ],
          )
          ..executeDelete(
            objectTypeName: _objectTypeName,
            entries: <Map<String, dynamic>>[
              <String, dynamic>{
                'id': 10,
              },
            ],
          ),
      );
      _showDialog(context);
    } catch (e) {
      _handleException(e);
    }
  }

  Future<void> _subscribeSnapshot() async {
    try {
      if (_snapshotSubscription == null) {
        if (_zone == null) {
          throw FormatException('Zone object is null. First try open zone.', _zone);
        }

        final Stream<AGConnectCloudDBZoneSnapshot> stream = await _zone.subscribeSnapshot(
          query: AGConnectCloudDBQuery(_objectTypeName)..equalTo('id', 2),
          policy: AGConnectCloudDBZoneQueryPolicy.POLICY_QUERY_DEFAULT,
        );
        _snapshotSubscription = stream.listen((AGConnectCloudDBZoneSnapshot snapshot) {
          _showDialog(context, snapshot.toString());
        }, onError: (dynamic e) {
          _handleException(e);
        });
      }
    } catch (e) {
      _handleException(e);
    }
  }
}
