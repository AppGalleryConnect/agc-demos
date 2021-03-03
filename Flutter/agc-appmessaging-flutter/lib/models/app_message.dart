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
import 'dart:convert';

class AppMessage {
  int startTime;
  String id;
  int endTime;
  int frequencyType;
  int frequencyValue;
  int testFlag;
  String dismissType;
  List<dynamic> triggerEvents;
  String messageType;
  CardMessage cardMessage;
  BannerMessage bannerMessage;
  PictureMessage pictureMessage;

  AppMessage(
      {this.startTime = 0,
      this.endTime = 0,
      this.frequencyType = 0,
      this.frequencyValue = 0,
      this.id = "",
      this.testFlag = 0,
      this.dismissType = "",
      this.triggerEvents = const [],
      this.messageType = "",
      this.cardMessage,
      this.pictureMessage,
      this.bannerMessage});

  Map<String, dynamic> toMap() {
    return {
      'startTime': startTime,
      'id': id,
      'endTime': endTime,
      'frequencyType': frequencyType,
      'frequencyValue': frequencyValue,
      'testFlag': testFlag,
      'dismissType': dismissType,
      'triggerEvents': triggerEvents,
      'messageType': messageType,
      'bannerMessage': bannerMessage.toMap(),
      'cardMessage': cardMessage.toMap(),
      'pictureMessage': pictureMessage.toMap()
    };
  }

  factory AppMessage.fromMap(Map<dynamic, dynamic> map) {
    if (map == null) return null;

    Map<dynamic, dynamic> baseMap = map['base'];
    return AppMessage(
        startTime: baseMap["startTime"] == null ? null : baseMap["startTime"],
        id: baseMap["id"] == null ? null : baseMap["id"],
        endTime: baseMap["endTime"] == null ? null : baseMap["endTime"],
        frequencyType:
            baseMap["frequencyType"] == null ? null : baseMap["frequencyType"],
        frequencyValue: baseMap["frequencyValue"] == null
            ? null
            : baseMap["frequencyValue"],
        testFlag: baseMap["testFlag"] == null ? null : baseMap["testFlag"],
        dismissType: map["dismissType"] == null ? null : map["dismissType"],
        triggerEvents:
            baseMap["triggerEvents"] == null ? null : baseMap["triggerEvents"],
        messageType:
            baseMap["messageType"] == null ? null : baseMap["messageType"],
        cardMessage:
            map["card"] == null ? null : CardMessage.fromMap(map["card"]),
        bannerMessage:
            map["banner"] == null ? null : BannerMessage.fromMap(map["banner"]),
        pictureMessage: map["picture"] == null
            ? null
            : PictureMessage.fromMap(map["picture"]));
  }

  String toJson() => json.encode(toMap());

  factory AppMessage.fromJson(String source) =>
      AppMessage.fromMap(json.decode(source));

  @override
  String toString() {
    return 'AppMessage(startTime: $startTime, '
            'id: $id,  '
            'endTime: $endTime, frequencyType: $frequencyType, '
            'frequencyValue: $frequencyValue, testFlag: $testFlag, ' +
        (dismissType == null ? "" : 'dismissType: $dismissType, ') +
        'triggerEvents: $triggerEvents, messageType: $messageType  ' +
        (bannerMessage == null ? "" : bannerMessage.toString()) +
        (pictureMessage == null ? "" : pictureMessage.toString()) +
        (cardMessage == null ? "" : cardMessage.toString());
  }
}

class BannerMessage {
  String title;
  String titleColor;
  num titleColorOpenness;
  String body;
  String bodyColor;
  num bodyColorOpenness;
  String backgroundColor;
  num backgroundColorOpenness;
  String pictureUrl;
  String actionUrl;

  BannerMessage(
      {this.title = "",
      this.titleColorOpenness = 0,
      this.body = "",
      this.bodyColor = "",
      this.titleColor = "",
      this.bodyColorOpenness = 0,
      this.backgroundColor = "",
      this.backgroundColorOpenness = 0,
      this.pictureUrl = "",
      this.actionUrl = ""});

  Map<String, dynamic> toMap() {
    return {
      'title': title,
      'titleColor': titleColor,
      'titleColorOpenness': titleColorOpenness,
      'body': body,
      'bodyColor': bodyColor,
      'bodyColorOpenness': bodyColorOpenness,
      'backgroundColor': backgroundColor,
      'backgroundColorOpenness': backgroundColorOpenness,
      'pictureURL': pictureUrl,
      'actionURL': actionUrl
    };
  }

  factory BannerMessage.fromMap(Map<dynamic, dynamic> map) {
    if (map == null) return null;

    return BannerMessage(
      title: map["title"] == null ? null : map["title"],
      titleColor: map["titleColor"] == null ? null : map["titleColor"],
      titleColorOpenness:
          map["titleColorOpenness"] == null ? null : map["titleColorOpenness"],
      body: map["body"] == null ? null : map["body"],
      bodyColor: map["bodyColor"] == null ? null : map["bodyColor"],
      bodyColorOpenness:
          map["bodyColorOpenness"] == null ? null : map["bodyColorOpenness"],
      backgroundColor:
          map["backgroundColor"] == null ? null : map["backgroundColor"],
      backgroundColorOpenness: map["backgroundColorOpenness"] == null
          ? null
          : map["backgroundColorOpenness"],
      pictureUrl: map["pictureURL"] == null ? null : map["pictureURL"],
      actionUrl: map["actionURL"] == null ? null : map["actionURL"],
    );
  }

  String toJson() => json.encode(toMap());

  factory BannerMessage.fromJson(String source) =>
      BannerMessage.fromMap(json.decode(source));

  @override
  String toString() {
    return 'BannerMessage(title: $title, '
        'titleColor: $titleColor,  '
        'titleColorOpenness: $titleColorOpenness, body: $body, '
        'bodyColor: $bodyColor, bodyColorOpenness: $bodyColorOpenness, '
        'backgroundColor: $backgroundColor, backgroundColorOpenness: $backgroundColorOpenness, '
        'pictureURL: $pictureUrl,  actionURL: $actionUrl)';
  }
}

class CardMessage {
  String title;
  String titleColor;
  num titleColorOpenness;
  String body;
  String bodyColor;
  num bodyColorOpenness;
  String backgroundColor;
  num backgroundColorOpenness;
  String portraitPictureURL;
  String landscapePictureURL;
  Button majorButton;
  Button minorButton;

  CardMessage(
      {this.title = "",
      this.titleColorOpenness = 0,
      this.body = "",
      this.bodyColor = "",
      this.titleColor = "",
      this.bodyColorOpenness = 0,
      this.backgroundColor = "",
      this.backgroundColorOpenness = 0,
      this.portraitPictureURL = "",
      this.landscapePictureURL = "",
      this.minorButton,
      this.majorButton});

  Map<String, dynamic> toMap() {
    return {
      'title': title,
      'titleColor': titleColor,
      'titleColorOpenness': titleColorOpenness,
      'body': body,
      'bodyColor': bodyColor,
      'bodyColorOpenness': bodyColorOpenness,
      'backgroundColor': backgroundColor,
      'backgroundColorOpenness': backgroundColorOpenness,
      'portraitPictureURL': portraitPictureURL,
      'landscapePictureURL': landscapePictureURL,
      'minorButton': minorButton,
      'majorButton': majorButton
    };
  }

  factory CardMessage.fromMap(Map<dynamic, dynamic> map) {
    if (map == null) return null;

    return CardMessage(
      title: map["title"] == null ? null : map["title"],
      titleColor: map["titleColor"] == null ? null : map["titleColor"],
      titleColorOpenness:
          map["titleColorOpenness"] == null ? null : map["titleColorOpenness"],
      body: map["body"] == null ? null : map["body"],
      bodyColor: map["bodyColor"] == null ? null : map["bodyColor"],
      bodyColorOpenness:
          map["bodyColorOpenness"] == null ? null : map["bodyColorOpenness"],
      backgroundColor:
          map["backgroundColor"] == null ? null : map["backgroundColor"],
      backgroundColorOpenness: map["backgroundColorOpenness"] == null
          ? null
          : map["backgroundColorOpenness"],
      portraitPictureURL:
          map["portraitPictureURL"] == null ? null : map["portraitPictureURL"],
      landscapePictureURL: map["landscapePictureURL"] == null
          ? null
          : map["landscapePictureURL"],
      majorButton: map["majorButton"] == null
          ? null
          : Button.fromMap(map["majorButton"]),
      minorButton: map["minorButton"] == null
          ? null
          : Button.fromMap(map["minorButton"]),
    );
  }

  String toJson() => json.encode(toMap());

  factory CardMessage.fromJson(String source) =>
      CardMessage.fromMap(json.decode(source));

  @override
  String toString() {
    return 'CardMessage(title: $title, '
            'titleColor: $titleColor,  '
            'titleColorOpenness: $titleColorOpenness, body: $body, '
            'bodyColor: $bodyColor, bodyColorOpenness: $bodyColorOpenness, '
            'backgroundColor: $backgroundColor, backgroundColorOpenness: $backgroundColorOpenness, '
            'portraitPictureURL: $portraitPictureURL,  landscapePictureURL: $landscapePictureURL ' +
        (minorButton == null ? "" : minorButton.toString()) +
        " " +
        (majorButton == null ? "" : majorButton.toString()) +
        ' )';
  }
}

class PictureMessage {
  String pictureURL;
  String actionURL;

  PictureMessage({this.pictureURL = "", this.actionURL = ""});

  Map<String, dynamic> toMap() {
    return {'pictureURL': pictureURL, 'actionURL': actionURL};
  }

  factory PictureMessage.fromMap(Map<dynamic, dynamic> map) {
    if (map == null) return null;

    return PictureMessage(
        pictureURL: map["pictureURL"] == null ? null : map["pictureURL"],
        actionURL: map["actionURL"] == null ? null : map["actionURL"]);
  }

  String toJson() => json.encode(toMap());

  factory PictureMessage.fromJson(String source) =>
      PictureMessage.fromMap(json.decode(source));

  @override
  String toString() {
    return 'PictureMessage(pictureURL: $pictureURL, '
        'actionURL: $actionURL';
  }
}

class Button {
  String text;
  String textColor;
  num textColorOpenness;
  String actionURL;

  Button({
    this.text = "",
    this.textColorOpenness = 0,
    this.actionURL = "",
    this.textColor = "",
  });

  Map<String, dynamic> toMap() {
    return {
      'text': text,
      'textColor': textColor,
      'textColorOpenness': textColorOpenness,
      'actionURL': actionURL
    };
  }

  factory Button.fromMap(Map<dynamic, dynamic> map) {
    if (map == null) return null;

    return Button(
        text: map["text"] == null ? null : map["text"],
        textColor: map["textColor"] == null ? null : map["textColor"],
        textColorOpenness:
            map["textColorOpenness"] == null ? null : map["textColorOpenness"],
        actionURL: map["actionURL"] == null ? null : map["actionURL"]);
  }

  String toJson() => json.encode(toMap());

  factory Button.fromJson(String source) => Button.fromMap(json.decode(source));

  @override
  String toString() {
    return 'Button(text: $text, '
        'textColor: $textColor,  '
        'textColorOpenness: $textColorOpenness, actionURL: $actionURL ';
  }
}
