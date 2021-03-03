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

import 'package:agconnect_appmessaging/constants/app_messaging.dart';
import 'package:agconnect_appmessaging/models/app_message.dart';
import 'package:flutter/services.dart';

class AgconnectAppmessaging {
  static const MethodChannel _channel =
      const MethodChannel('com.huawei.agc.flutter.appmessaging_methodchannel');
  static const EventChannel _eventChannelOnMessageDismiss = const EventChannel(
      "com.huawei.agc.flutter.appmessaging_eventchannel_onMessageDismiss");
  static const EventChannel _eventChannelOnMessageClick = const EventChannel(
      "com.huawei.agc.flutter.appmessaging_eventchannel_onMessageClick");
  static const EventChannel _eventChannelOnMessageDisplay = const EventChannel(
      "com.huawei.agc.flutter.appmessaging_eventchannel_onMessageDisplay");
  static const EventChannel _eventChannelOnMessageError = const EventChannel(
      "com.huawei.agc.flutter.appmessaging_eventchannel_onMessageError");
  static const EventChannel _eventChannelCustomEvent = const EventChannel(
      "com.huawei.agc.flutter.appmessaging_eventchannel_customEvent");

  Stream<AppMessage> _onMessageDisplay;
  Stream<AppMessage> _onMessageDismiss;
  Stream<AppMessage> _onMessageClick;
  Stream<AppMessage> _onMessageError;
  Stream<AppMessage> _customEvent;

  Future<bool> isDisplayEnable() async {
    return await _channel.invokeMethod<bool>('isDisplayEnable', {});
  }

  Future<void> setDisplayEnable(bool enable) async {
    return _channel.invokeMethod<void>('setDisplayEnable', enable);
  }

  Future<bool> isFetchMessageEnable() async {
    return await _channel.invokeMethod<bool>('isFetchMessageEnable', {});
  }

  Future<void> setFetchMessageEnable(bool enable) async {
    return _channel.invokeMethod<void>('setFetchMessageEnable', enable);
  }

  Future<void> setForceFetch() async {
    return await _channel.invokeMethod<void>('setForceFetch', {});
  }

  Future<void> setDisplayLocation(int locationConstant) async {
    return _channel.invokeMethod<void>('setDisplayLocation', locationConstant);
  }

  Future<void> removeCustomView() async {
    return _channel.invokeMethod<void>('removeCustomView', {});
  }

  Future<void> trigger(String eventId) async {
    return _channel.invokeMethod<void>('trigger', eventId);
  }

  Future<void> handleCustomViewMessageEvent(Map<String, dynamic> map) async {
    return _channel.invokeMethod<void>('handleCustomViewMessageEvent', map);
  }

  Stream<AppMessage> get onMessageDismiss {
    if (_onMessageDismiss == null) {
      _onMessageDismiss =
          _eventChannelOnMessageDismiss.receiveBroadcastStream().map((event) {
        return AppMessage.fromMap(event);
      });
    }
    return _onMessageDismiss;
  }

  Stream<AppMessage> get onMessageClick {
    if (_onMessageClick == null) {
      _onMessageClick =
          _eventChannelOnMessageClick.receiveBroadcastStream().map((event) {
        return AppMessage.fromMap(event);
      });
    }
    return _onMessageClick;
  }

  Stream<AppMessage> get onMessageDisplay {
    if (_onMessageDisplay == null) {
      _onMessageDisplay =
          _eventChannelOnMessageDisplay.receiveBroadcastStream().map((event) {
        return AppMessage.fromMap(event);
      });
    }
    return _onMessageDisplay;
  }

  Stream<AppMessage> get onMessageError {
    if (_onMessageError == null) {
      _onMessageError =
          _eventChannelOnMessageError.receiveBroadcastStream().map((event) {
        return AppMessage.fromMap(event);
      });
    }
    return _onMessageError;
  }

  Stream<AppMessage> get customEvent {
    if (_customEvent == null) {
      _customEvent =
          _eventChannelCustomEvent.receiveBroadcastStream().map((event) {
        return AppMessage.fromMap(event);
      });
    }
    return _customEvent;
  }
}
