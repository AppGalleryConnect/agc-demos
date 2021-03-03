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

class AppMessagingLocationConstants {
  static const int BOTTOM = 0;
  static const int CENTER = 1;
}

class AppMessagingDismissTypeConstants {
  static const String CLICK = "dismissTypeClick";
  static const String CLICK_OUTSIDE = "dismissTypeClickOutside";
  static const String AUTO = "dismissTypeAuto";
  static const String SWIPE = "dismissTypeSwipe";
  static const String BACK_BUTTON = "dismissTypeBack";
}

class AppMessagingEventType {
  static const Map<String, dynamic> onMessageDisplay = {
    "eventType": "onMessageDisplay"
  };
  static const Map<String, dynamic> onMessageClick = {
    "eventType": "onMessageClick"
  };
  static const Map<String, dynamic> onMessageError = {
    "eventType": "onMessageError"
  };

  static Map<String, dynamic> onMessageDismiss(String dismissType) {
    return {"eventType": "onMessageDismiss", "dismissType": dismissType};
  }
}
