## Cloud Storage-server quickstart

## Introduction
Cloud Storage provides cloud services and the SDK to help you quickly build your system.

## Preparing the Environment
Before using the Cloud Storage sdk, your server need support Java 8 and higher.

## Getting Started
Before running the Cloud Storage sdk, you need to:
1. Enabling Cloud Storage.

2. Enter the storage instance name and select the default data processing location.

3. Config the BUCKET_NAME parameter in the ServerApi.java file.

4. Config Security Rules as Allow all, the code configuration is as follows:
    ```
	agc.cloud.storage[
	   match: /{bucket}/{path=**} {
			allow read, write:  if true;
		}
	]
    ```
5. Download authentication credential file and Config the file name to File_NAME.

6. In IDEA, run the demo.java file to run the project.


Please refer to：https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-cloudstorage-introduction-0000001054847259

## Sample Code

  src\main\java\ServerApi.java

## License
Auth Service Demo is licensed under the [Apache License, version 2.0] (http://www.apache.org/licenses/LICENSE-2.0).
