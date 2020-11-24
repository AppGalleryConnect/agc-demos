## function quickstart

## Introduction
Cloud Functions enables serverless computing. It provides the Function as a Service (FaaS) capabilities to simplify app development and O&M so your ideas can be realized more easily and your service capabilities can be built more quickly.
1. Cloud Functions provides an efficient and reliable framework for developing and running functions. It frees you from complex traditional development and O&M of apps. Server configuration and management, code deployment, load balancing, autoscaling, and high reliability assurance are now streamlined.
You only need to focus on service logic and function code to build reliable and scalable serverless apps. 
2. As the core of serverless computing, Cloud Functions works with other cloud services like building blocks to implement your service logic. 

## Preparing the Environment
Before using the quickstart app, prepare your Android development environment.


## Environment Requirements
Android Studio 3.0 or later.
	
## Configuration
Before running the quickstart app, you need to:
1. If you do not have a HUAWEI Developer account, you need to register an account and pass identity verification.
2. Use your account to sign in to AppGallery Connect, create an app, and set Package type to APK (Android app).
3. Create a cloud function. (For details, please refer to the development guide.)
3.1 Sign in to AppGallery Connect, select My apps, and click the product of the app for which you want to enable Auth Service.
3.2 Go to Develop > Build > Cloud functions. The Cloud Functions page is displayed.
3.3 Click New Function on the Functions page. 
3.4 Define the function on the page that is displayed.
3.5 Click Save.
4. Download the agconnect-services.json file from AppGallery Connect and replace the JSON file of the quickstart app with it.

## Sample Code
The SDK can call cloud functions and send back the result.
Sample code: src\main\java\com\huawei\agc\quickstart\MainActivity.java

## License
Cloud Functions quickstart is licensed under the [Apache License, version 2.0] (http://www.apache.org/licenses/LICENSE-2.0).
