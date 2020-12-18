# AGConnect iOS SDK

AppGallery Connect致力于为应用的创意、开发、分发、运营、经营各环节提供一站式服务，构建全场景智慧化的应用生态体验。AppGallery Connect深度整合华为内部各项优质服务，将华为在全球化、质量、安全、工程管理等领域长期积累的能力开放给开发者，大幅降低应用开发与运维难度，提高版本质量，开放分发和运营服务，帮助开发者获得用户并实现收入的规模增长。

## SDK接入

### 添加配置文件

AGC为了简化开发者的配置步骤，向开发者提供了保存应用配置信息的配置文件，您只需要将配置文件添加到您的工程目录即可自动将您在AGC上的应用信息加载到您的开发环境。

1. 登录 AppGallery Connect 网站，选择“我的应用”。
2. 选择需要集成AGC SDK的iOS应用，进入应用开发页面。
3. 选择“开发”页签，在“项目设置”页面下载配置文件“agconnect-services.plist”。
4. 将“agconnect-services.plist”文件添加到Xcode工程目录下。

### 添加SDK

1. 打开命令行窗口，导航至Xcode项目所在的位置。
2. 创建Podfile文件。如果已经存在，可跳过本步骤。
```
cd project-directory 
pod init
```
3. 在podfile中添加SDK的pod。当前支持的服务如下表所示。
```
pod 'AGConnectDatabase'
```
|服务|配置方式|
|----|-----|
|云数据库|pod ‘AGConnectDatabase’|

4. 安装 pod，然后打开.xcworkspace文件查看该项目。
```
pod install
```

## LICENSE

详见[华为License文件](./LICENSE)
