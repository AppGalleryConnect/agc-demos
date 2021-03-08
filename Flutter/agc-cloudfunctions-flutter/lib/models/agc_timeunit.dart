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

import 'package:flutter/foundation.dart';

@immutable
class AGCTimeUnit {
  static const NANOSECONDS = const AGCTimeUnit._create(0);
  static const MICROSECONDS = const AGCTimeUnit._create(1);
  static const MILLISECONDS = const AGCTimeUnit._create(2);
  static const SECONDS = const AGCTimeUnit._create(3);
  static const MINUTES = const AGCTimeUnit._create(4);
  static const HOURS = const AGCTimeUnit._create(5);
  static const DAYS = const AGCTimeUnit._create(6);

  final int _value;

  const AGCTimeUnit._create(this._value);

  factory AGCTimeUnit.fromInt(int value) {
    if (value > -1 && value < 7) {
      return AGCTimeUnit._create(value);
    } else {
      throw Exception('Value must be in the correct range: -1 < value < 7');
    }
  }

  int toInt() => _value;

  @override
  String toString() => 'AGCTimeUnit(value: $_value)';

  @override
  bool operator ==(Object o) {
    if (identical(this, o)) return true;

    return o is AGCTimeUnit && o._value == _value;
  }

  @override
  int get hashCode => _value.hashCode;
}
