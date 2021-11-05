#  Cloud DB JS SDK Demo


## Introduction
This project is a quick start sample developed using Cloud DB JS SDK.

##  Quick Start
- On the [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html#/myApp) page, create a project and add an application with named QuickStartDemo.

- Click **Auth Service** on the navigation bar and enable authentication using an anonymous account.

- Click **Cloud DB** on the navigation bar and enable database service. Then, perform the following operations:

    （1）Create a schema by importing a template file stored in **CloudDBQuickStart_1.json** in the root directory of the project. Alternatively, create a schema named **BookInfo** and ensure that all fields must be the same as those in **BookInfo.js** in the project.

    （2） Create a Cloud DB zone. On the **Cloud DB Zone** tab page, click **Add** to create a Cloud DB zone named **QuickStartDemo**.


- On the Project Setting page, obtain the app configuration information. Save it to the context object in the agconnect-services.js file.

- Integrate the Cloud DB SDK.

    （1）Run the following command to install the Cloud DB JavaScript SDK service module in the project:
        
     ```
    npm install --save @agconnect/database
    ```
        
    （2）Import the database component to the project.
        
    ```
    import "@agconnect/database";
    ```

### Compiles and hot-reloads for development
```
click debug or compilation button
```

