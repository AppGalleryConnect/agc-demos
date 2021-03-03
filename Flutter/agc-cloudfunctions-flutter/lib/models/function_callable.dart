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
import 'dart:convert';

import 'package:flutter/services.dart';

import 'agc_timeunit.dart';
import 'function_result.dart';

class FunctionCallable {
  static const int DEFAULT_TIMEOUT = 70;
  static const AGCTimeUnit DEFAULT_TIMEUNIT = AGCTimeUnit.SECONDS;

  String httpTriggerURI;
  int timeout;
  AGCTimeUnit units;

  FunctionCallable(this.httpTriggerURI,
      {this.timeout = 70, this.units = AGCTimeUnit.SECONDS});

  Future<FunctionResult> call([dynamic functionParameters]) async {
    final Map args = toMap()
      ..putIfAbsent('functionParameters', () => functionParameters);
    return _AGCPlatform._callFunction(args);
  }

  FunctionCallable clone({
    String httpTriggerURI,
    int timeout,
    AGCTimeUnit units,
  }) =>
      FunctionCallable(
        httpTriggerURI ?? this.httpTriggerURI,
        timeout: timeout ?? this.timeout,
        units: units ?? this.units,
      );

  factory FunctionCallable.fromJson(String source) =>
      FunctionCallable.fromMap(json.decode(source));

  String toJson() => json.encode(toMap());

  factory FunctionCallable.fromMap(Map<String, dynamic> map) {
    if (map == null) return null;

    return FunctionCallable(
      map['httpTriggerURI'] == null ? null : map['httpTriggerURI'],
      timeout: map['timeout'] == null ? null : map['timeout'],
      units: map['units'] == null ? null : AGCTimeUnit.fromInt(map['units']),
    );
  }

  Map<String, dynamic> toMap() => {
        'httpTriggerURI': httpTriggerURI == null ? null : httpTriggerURI,
        'timeout': timeout == null ? null : timeout,
        'units': units == null ? null : units.toInt(),
      };

  @override
  String toString() =>
      'FunctionCallable(httpTriggerURI: $httpTriggerURI, timeout: $timeout, units: $units)';

  @override
  bool operator ==(Object o) {
    if (identical(this, o)) return true;

    return o is FunctionCallable &&
        o.httpTriggerURI == httpTriggerURI &&
        o.timeout == timeout &&
        o.units == units;
  }

  @override
  int get hashCode =>
      httpTriggerURI.hashCode ^ timeout.hashCode ^ units.hashCode;
}

class _AGCPlatform {
  static const MethodChannel _channel = const MethodChannel(
      'com.huawei.agc.flutter.cloudfunctions/MethodChannel');

  static Future<FunctionResult> _callFunction(Map<String, dynamic> args) async {
    final String _result =
        await _channel.invokeMethod<String>('callFunction', args);
    return FunctionResult(_result);
  }
}
