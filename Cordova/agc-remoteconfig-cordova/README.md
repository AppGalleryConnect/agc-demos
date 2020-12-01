# AGConnect Cordova Remoteconfig Demo

`In the root directory, perform the following operations:`

`Step 1: Run the following command to install the NPM dependency`
`$ npm install`

`Step 2: Run the following command to create the corresponding platform code`
`$ cordova platform add ios/android`

`Step 3: Run the following command to download and install the AGC remoteconfig plugin`
`$ cordova plugin add @cordova-plugin-agconnect/remoteconfig --save`

`Step 4: Put the corresponding agconnect-services.json/agconnect-services.plist into the appropriate directory`

`Step 5: Change the package name in the config.xml file to the package name in the agconnect-services.json/agconnect-services.plist file`

`Step 6: Add the agcp-plto the corresponding bulid.gradle(only Android)`

​     `apply plugin: 'com.huawei.agconnect'`

