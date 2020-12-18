# CloudDB 快速入门


## 简介
本工程是用CloudDB APIs开发的一个入门示例。

## 快速入门
- 用Xcode打开工程。

- 在[应用市场我的应用]( https://developer.huawei.com/consumer/cn/service/josp/agc/index.html#/myApp )页面新建一个应用。

- 在**认证服务**一栏，开启**华为账号**认证服务。

- 在**云数据库**一栏，选择开启服务，然后创建Schema信息。可以选择从文件导入，模板文件预放在工程根目录的CloudDBQuickStart_1.json中；也可以自己从新创建，Schema的名称必须为**BookInfo**，所有字段需要和工程中BookInfo.h的字段相同。

- 从概览页下载agconnect-services.plist文件，添加到app目录下。

- 启动运行前，需要执行pod install操作，关联相关库。


- 在你的ios设备或Xcode 模拟器上运行示例。

## 结果

<img src="./screenshot.jpg" height="550" width="320" />

## 许可证

CloudDB QuickStart is licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).
