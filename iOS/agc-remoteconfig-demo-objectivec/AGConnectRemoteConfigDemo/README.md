## auth quickstart

## Introduction
HUAWEI AppGallery Connect provides the Remote Configuration service for you to manage parameters online. With the service, you can change the behavior and appearance of your app online without requiring users to update the app.

Remote Configuration provides cloud-based services, the console, and the client SDK. By integrating the client SDK, your app can periodically obtain parameter values delivered on the console to modify the app's behavior and appearance.

## Preparing the Environment
Before using the quickstart app, prepare your iOS development environment.


## Environment Requirements
XCode 8.0 or later.
	
## Configuration
Sign in to AppGalery Connect, enable Remote Configuration for your app, and set parameters and conditions.

Integrate the AppGallery Connect Remote Configuration SDK into your app.

Set default parameter values in your app so that it can run properly before being connected to Remote Configuration.

Call the fetch method provided by the SDK to periodically obtain parameter value updates from Remote Configuration. When receiving a request from the app, Remote Configuration checks whether a reported parameter has an on-cloud value and returns the value if so.

After obtaining parameter value updates, the app calls the apply() method immediately or at a specified time based on service requirements to override the values in the app by obtained values.

To dynamically obtain parameter values from Remote Configuration, call various get methods provided by the SDK.


## License
Auth Service quickstart is licensed under the [Apache License, version 2.0] (http://www.apache.org/licenses/LICENSE-2.0).
