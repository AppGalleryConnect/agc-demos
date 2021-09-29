/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2021-2021. All rights reserved.
 */

// Wait for the deviceready event before using any of Cordova's device APIs.
// See https://cordova.apache.org/docs/en/latest/cordova/events/events.html#deviceready
document.addEventListener("deviceready", onDeviceReady, false);
const $ = (id) => document.getElementById(id);
let agcCloudDBZone;
let listener;
let subscribeSnapshotCounter;

async function onDeviceReady() {
    // Cordova is now initialized. Have fun!

    console.log("Running cordova-" + cordova.platformId + "@" + cordova.version);

    AGCCloudDBPlugin.initialize().then(() => {
        console.log("initialize :: Success! ");
    }).catch((error) => alert("initialize :: Error! " + JSON.stringify(error, null, 1)));

    AGCAuthPlugin.getCurrentUser().then((currentUser) => {
        console.log("getCurrentUser :: Success. User: " + currentUser.isAnonymous());
    }).catch((error) => {
        AGCAuthPlugin.signInAnonymously().then((result) => {
            console.log("signInAnonymously :: Success");
        }).catch((error) => alert("signInAnonymously :: Error! " + JSON.stringify(error, null, 1)));
    });

}

$("createObjectType").onclick = () => {
    AGCCloudDBPlugin.createObjectType()
        .then(() => {
            alert("createObjectType :: success ");
        }).catch((error) => alert("createObjectType :: Error! " + JSON.stringify(error, null, 1)));
}

$("openCloudDBZone2").onclick = () => {
    let config = {
        cloudDBZoneName: "QuickStartDemo",
        syncProperty: AGCCloudDBPlugin.CloudDBZoneSyncProperty.CLOUDDBZONE_CLOUD_CACHE,
        accessProperty: AGCCloudDBPlugin.CloudDBZoneAccessProperty.CLOUDDBZONE_PUBLIC,
        isEncrypted: false
    }
    let isAllowToCreate = true;
    AGCCloudDBPlugin.openCloudDBZone2(config, isAllowToCreate)
        .then((_agcCloudDBZone) => {
            agcCloudDBZone = _agcCloudDBZone;
            console.log("openCloudDBZone2 : success");
            alert("openCloudDBZone2 : success");
        }).catch((error) => alert("openCloudDBZone2 :: Error! " + JSON.stringify(error, null, 1)));
}
$("addEventListener").onclick = () => {
    AGCCloudDBPlugin.addEventListener((eventType) => {
        alert("eventType: " + JSON.stringify(eventType, null, 1));
    }).then(() => {
        alert("addEventListener :: Success! ");
    }).catch((error) => alert("addEventListener :: Error! " + JSON.stringify(error, null, 1)));
}
$("addDataEncryptionKeyListener").onclick = () => {
    let needFetchDataEncryptionKey = true;
    AGCCloudDBPlugin.addDataEncryptionKeyListener(needFetchDataEncryptionKey, (isDataKeyChange) => {
        alert("onDataKeyChange: " + JSON.stringify(isDataKeyChange, null, 1));
    }).then(() => {
        alert("addDataEncryptionKeyListener :: Success! ");
    }).catch((error) => alert("addDataEncryptionKeyListener :: Error! " + JSON.stringify(error, null, 1)));
}

$("getCloudDBZoneConfigs").onclick = () => {
    AGCCloudDBPlugin.getCloudDBZoneConfigs()
        .then((cloudDBZoneConfig) => {
            alert("cloudDBZoneConfigs: " + JSON.stringify(cloudDBZoneConfig, null, 1));
        }).catch((error) => alert("cloudDBZoneConfigs :: Error! " + JSON.stringify(error, null, 1)));
}
$("getCloudDBZoneConfig").onclick = () => {
    agcCloudDBZone.getCloudDBZoneConfig().then((cloudDBZoneConfig) => {
        console.log("cloudDBZoneConfig: " + JSON.stringify(cloudDBZoneConfig, null, 1));
        alert("cloudDBZoneConfig: " + JSON.stringify(cloudDBZoneConfig, null, 1));
    }).catch((error) => alert("getCloudDBZoneConfig :: Error! " + JSON.stringify(error, null, 1)));
}
$("closeCloudDBZone").onclick = () => {
    AGCCloudDBPlugin.closeCloudDBZone(agcCloudDBZone.id).then(() => {
        alert("closeCloudDBZone : success");
    }).catch((error) => alert("closeCloudDBZone :: Error! " + JSON.stringify(error, null, 1)));
}
$("deleteCloudDBZone").onclick = () => {
    AGCCloudDBPlugin.deleteCloudDBZone(agcCloudDBZone.id, "QuickStartDemo").then(() => {
        alert("deleteCloudDBZone : success");
    }).catch((error) => alert("deleteCloudDBZone :: Error! " + JSON.stringify(error, null, 1)));
}
$("enableNetwork").onclick = () => {
    AGCCloudDBPlugin.enableNetwork("QuickStartDemo").then(() => {
        alert("enableNetwork : success");
    }).catch((error) => alert("enableNetwork :: Error! " + JSON.stringify(error, null, 1)));
}
$("disableNetwork").onclick = () => {
    AGCCloudDBPlugin.disableNetwork("QuickStartDemo").then(() => {
        alert("disableNetwork : success");
    }).catch((error) => alert("disableNetwork :: Error! " + JSON.stringify(error, null, 1)));
}
$("updateDataEncryptionKey").onclick = () => {
    AGCCloudDBPlugin.updateDataEncryptionKey().then((result) => {
        alert("updateDataEncryptionKey : success : " + result);
    }).catch((error) => alert("updateDataEncryptionKey :: Error! " + JSON.stringify(error, null, 1)));
}
$("setUserKey").onclick = () => {
    let userKey = $("txt_user_key").value;
    let userRekey = $("txt_user_rekey").value;
    if (userKey === "" || userKey === undefined || userKey === null) {
        alert("User key should not be empty.");
        return;
    }
    if (userRekey === "" || userRekey === undefined || userRekey === null) {
        userRekey= "";
    }
    AGCCloudDBPlugin.setUserKey(userKey, userRekey).then((result) => {
        alert("setUserKey : success : " + result);
    }).catch((error) => alert("setUserKey :: Error! " + JSON.stringify(error, null, 1)));
}
$("executeUpsert").onclick = () => {
    let className = "BookInfo";

    const myDate = Date.parse("2012-12-24");
    let cloudDBZoneObjectArray = [{
        id: 1,
        bookName: "myBook",
        author: "author1",
        price: 123.99,
        publisher: "Huawei",
        publishTime: new Date('2021-05-13T21:21:50.321').getTime(),
        shadowFlag: false,

    }, {
        id: 2,
        bookName: "m2book",
        author: "author1",
        price: 130,
        publisher: "Huawei",
        publishTime: myDate,
        shadowFlag: false,

    }, {
        id: 3,
        bookName: "m3book",
        author: "author1",
        price: 140,
        publisher: "Huawei",
        publishTime: myDate,
        shadowFlag: false,

    }, {
        id: 4,
        bookName: "m4book",
        author: "author1",
        price: 150,
        publisher: "Huawei",
        publishTime: myDate,
        shadowFlag: false,

    }];
    agcCloudDBZone.executeUpsert(className, cloudDBZoneObjectArray).then((result) => {
        alert("executeUpsert : success :" + result);
    }).catch((error) => alert("executeUpsert :: Error! " + JSON.stringify(error, null, 1)));
}
$("executeDelete").onclick = () => {
    let className = "BookInfo";
    let cloudDBZoneObjectArray = [{id: 1}, {id: 2}, {id: 3}];
    agcCloudDBZone.executeDelete(className, cloudDBZoneObjectArray).then((result) => {
        alert("executeDelete : success :" + result);
    }).catch((error) => alert("executeDelete :: Error! " + JSON.stringify(error, null, 1)));
}

$("executeQuery").onclick = () => {
    const queryPolicy = AGCCloudDBPlugin.CloudDBZoneQueryPolicy.POLICY_QUERY_DEFAULT;
    const queryObject = AGCCloudDBPlugin.AGCCloudDBQuery.where("BookInfo").lessThan("price", 145).orderByDesc("price").build();
    agcCloudDBZone.executeQuery(queryObject, queryPolicy).then(result => alert(JSON.stringify(result, null, 1))).catch(error => alert(JSON.stringify(error, null, 1)));
}

$("executeAverageQuery").onclick = () => {
    const queryPolicy = AGCCloudDBPlugin.CloudDBZoneQueryPolicy.POLICY_QUERY_DEFAULT;
    const queryObject = AGCCloudDBPlugin.AGCCloudDBQuery.where('BookInfo').lessThan('price', 155).orderByAsc('price').build();
    agcCloudDBZone.executeAverageQuery(queryObject, "price", queryPolicy).then(result => alert(JSON.stringify(result, null, 1))).catch(error => alert(JSON.stringify(error, null, 1)));
}
$("executeSumQuery").onclick = () => {
    const queryPolicy = AGCCloudDBPlugin.CloudDBZoneQueryPolicy.POLICY_QUERY_DEFAULT;
    const queryObject = AGCCloudDBPlugin.AGCCloudDBQuery.where('BookInfo').lessThan('price', 155).orderByAsc('price').build();
    agcCloudDBZone.executeSumQuery(queryObject, "price", queryPolicy).then(result => alert(JSON.stringify(result, null, 1))).catch(error => alert(JSON.stringify(error, null, 1)));
}
$("executeMaximumQuery").onclick = () => {
    const queryPolicy = AGCCloudDBPlugin.CloudDBZoneQueryPolicy.POLICY_QUERY_DEFAULT;
    const queryObject = AGCCloudDBPlugin.AGCCloudDBQuery.where('BookInfo').lessThan('price', 155).orderByAsc('price').build();
    agcCloudDBZone.executeMaximumQuery(queryObject, "price", queryPolicy).then(result => alert(JSON.stringify(result, null, 1))).catch(error => alert(JSON.stringify(error, null, 1)));
}
$("executeMinimalQuery").onclick = () => {
    const queryPolicy = AGCCloudDBPlugin.CloudDBZoneQueryPolicy.POLICY_QUERY_DEFAULT;
    const queryObject = AGCCloudDBPlugin.AGCCloudDBQuery.where('BookInfo').lessThan('price', 155).orderByAsc('price').build();
    agcCloudDBZone.executeMinimalQuery(queryObject, "price", queryPolicy).then(result => alert(JSON.stringify(result, null, 1))).catch(error => alert(JSON.stringify(error, null, 1)));
}
$("executeCountQuery").onclick = () => {
    const queryPolicy = AGCCloudDBPlugin.CloudDBZoneQueryPolicy.POLICY_QUERY_DEFAULT;
    const queryObject = AGCCloudDBPlugin.AGCCloudDBQuery.where('BookInfo').lessThan('price', 155).orderByAsc('price').build();
    agcCloudDBZone.executeCountQuery(queryObject, "price", queryPolicy).then(result => alert(JSON.stringify(result, null, 1))).catch(error => alert(JSON.stringify(error, null, 1)));
}
$("executeQueryUnsynced").onclick = () => {
    const queryObject = AGCCloudDBPlugin.AGCCloudDBQuery.where('BookInfo').lessThan('price', 155).orderByAsc('price').build();
    agcCloudDBZone.executeQueryUnsynced(queryObject).then(result => alert(JSON.stringify(result, null, 1))).catch(error => alert(JSON.stringify(error, null, 1)));
}
$("runTransaction").onclick = () => {
    const myDate = Date.parse("2012-12-24");
    const className = 'BookInfo';
    const executeUpsertData = [
        {
            id: 5,
            bookName: "m5book",
            author: "author1",
            price: 160,
            publisher: "Huawei",
            publishTime: myDate,
            shadowFlag: false,

        }
    ];
    const executeDeleteData = [
        {
            id: 2,
            bookName: "m2book",
            author: "author1",
            price: 130,
            publisher: "Huawei",
            publishTime: myDate,
            shadowFlag: false,
        }

    ];
    const queryObject = AGCCloudDBPlugin.AGCCloudDBQuery.where(className).lessThan('price', 135).orderByAsc('price').build();
    const transactions = AGCCloudDBPlugin.AGCCloudDBTransaction.function().executeQuery(className, queryObject).executeUpsert(className, executeUpsertData).executeDelete(className, executeDeleteData).build();

    agcCloudDBZone.runTransaction(transactions)
        .then(result => alert(JSON.stringify(result, null, 1)))
        .catch(error => alert(JSON.stringify(error, null, 1)));
}

$("unsubscribeSnapshot").onclick = () => {
    listener.remove().then(() => alert("unsubscribeSnapshot :: Success! ")).catch(error => alert(JSON.stringify(error, null, 1)));
}
$("subscribeSnapshot").onclick = () => {
    const queryPolicy = AGCCloudDBPlugin.CloudDBZoneQueryPolicy.POLICY_QUERY_DEFAULT;
    const queryObject = AGCCloudDBPlugin.AGCCloudDBQuery.where('BookInfo').equalTo('publisher', 'Huawei').build();
    agcCloudDBZone.subscribeSnapshot(queryObject, queryPolicy, (cloudDBZoneSnapshot) => {
        console.log(JSON.stringify(cloudDBZoneSnapshot));
        let h = $("subscribeSnapshotData");
        h.insertAdjacentHTML("afterend", `<p>=${++subscribeSnapshotCounter}-subscribeSnapshot finish=</p>`);
        h.insertAdjacentHTML("afterend", `<p>=${JSON.stringify(cloudDBZoneSnapshot, null, 1)}</p>`);
        h.insertAdjacentHTML("afterend", `<p>=${subscribeSnapshotCounter}-subscribeSnapshot start=</p>`);
    }).then(result => {
        if(listener != null){
            listener.remove().then(() => console.log("unsubscribeSnapshot :: Success! ")).catch(error => console.log(JSON.stringify(error, null, 1)));
        }
        listener = result;
        alert("subscribeSnapshot: " +JSON.stringify(result, null, 1));
        subscribeSnapshotCounter = 0;
    }).catch(error => console.log(JSON.stringify(error, null, 1)));

}
