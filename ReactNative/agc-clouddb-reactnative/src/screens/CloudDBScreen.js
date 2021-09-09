/*
    Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.

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

import React, { useState } from "react";
import {
    View,
    Text,
    Alert,
    StyleSheet,
    TouchableOpacity,
    ScrollView,
    SafeAreaView,
    StatusBar,
} from "react-native";
import CustomButton from '../components/CustomButton'
import AGCCloudDB, { AGCCloudDBQuery, AGCCloudDBZoneConfig, AGCCloudDBTransaction } from "@react-native-agconnect/clouddb";
import AGCAuth from '@react-native-agconnect/auth'
import Header from "../components/Header";

const CloudDBScreen = (props) => {

    const [dbZone, setDbZone] = useState(null)

    const showResult = (res) => Alert.alert("Result", JSON.stringify(res, null, 4))
    const listener = (value) => Alert.alert("Listener", JSON.stringify(value, null, 4))

    const className = "BookInfo";
    const zoneName = "QuickStartDemo";
    const book1 = {
        id: 1,
        bookName: "myBook",
        author: "RDJ",
        price: 123.99,
        publisher: "Huawei",
    }
    const book2 = {
        id: 2,
        bookName: "myBook",
        price: 12
    }
    const book3 = {
        id: 3,
        bookName: "myBook",
        price: 125.32
    }

    const book4 = {
        id: 1,
        bookName: "MyTransactionBook",
        author: "King Arthur",
        price: 54,
        publisher: "Huawei",
    }
    const initCloudDB = () => {
        AGCCloudDB.getInstance().createObjectType()
        .then(res => showResult(res))
        .catch(err => showResult(err.message))
    };

    const openCloudDBZone = () => {
        let config = new AGCCloudDBZoneConfig(
            zoneName,
            AGCCloudDBZoneConfig.CloudDBZoneSyncProperty.CLOUDDBZONE_CLOUD_CACHE,
            AGCCloudDBZoneConfig.CloudDBZoneAccessProperty.CLOUDDBZONE_PUBLIC
        );
        AGCCloudDB.getInstance().openCloudDBZone2(config, true).then(response => {
            setDbZone(response);
        }).catch(error => {
            Alert.alert("error: ", JSON.stringify(error.message))
        })
        AGCCloudDB.getInstance().addEventListener(listener)
        AGCCloudDB.getInstance().addDataEncryptionKeyListener(listener)
    }

    const queryData = () => {
        let query = AGCCloudDBQuery.where(className)
            .greaterThanOrEqualTo("price", 12)
            .isNotNull("id")
            .build()
        dbZone.executeQuery(query, AGCCloudDBQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY)
            .then(res => showResult(res))
            .catch(err => showResult(err.message))
    }

    const querySum = () => {
        let query = AGCCloudDBQuery.where(className).build()
        dbZone.executeSumQuery(query, "price", AGCCloudDBQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_DEFAULT)
            .then(res => showResult(res))
            .catch(err => showResult(err.message))
    }

    const queryCount = () => {
        let query = AGCCloudDBQuery.where(className).build()
        dbZone.executeCountQuery(query, "id", AGCCloudDBQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_DEFAULT)
            .then(res => showResult(res))
            .catch(err => showResult(err.message))
    }

    const queryAverage = () => {
        let query = AGCCloudDBQuery.where(className).build()
        dbZone.executeAverageQuery(query, "price", AGCCloudDBQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY)
            .then(res => showResult(res))
            .catch(err => showResult(err.message))
    }

    const getDataAndDelete = () => {
        let query = AGCCloudDBQuery.where(className)
            .equalTo("id", 1)
            .build()
        dbZone.executeQuery(query, AGCCloudDBQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY)
            .then(res => {
                dbZone.executeDelete(className, res.snapshotObjects)
                    .then(res => showResult(res))
                    .catch(err => showResult(err.message))
            })
            .catch(err => showResult(err.message))
    }

    const writeData = async () => {
        let user = await AGCAuth.getInstance().currentUser();
        if (user) {
            dbZone.executeUpsert(className, [book1, book2, book3])
                .then(res => showResult(res))
                .catch(err => showResult(err.message))
        } else {
            Alert.alert("Error!", "Please sign in to use delete functionality")
        }
    }

    const deleteData = async () => {
        let user = await AGCAuth.getInstance().currentUser();
        if (user) {
            dbZone.executeDelete(className, [book1, { id: 2 }, { id: 3 }])
                .then(res => showResult(res))
                .catch(err => showResult(err.message))
        } else {
            Alert.alert("Error!", "Please sign in to use delete functionality")
        }
    }

    const closeCloudDB = () => {
        AGCCloudDB.getInstance().closeCloudDBZone(dbZone).then(response => {
            setDbZone(null)
            showResult(response)
        }).catch(error => {
            showResult(error.message)
        })
    }

    const subscribeSnapshot = () => {
        let query = AGCCloudDBQuery.where(className).equalTo("publisher", "Huawei").build();
        dbZone.subscribeSnapshot(query, AGCCloudDBQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_DEFAULT, listener)
    }

    const runTransaction = () => {
        let transaction = AGCCloudDBTransaction.Function()
            .executeDelete(className, [{ id: 1 }])
            .executeUpsert(className, [book4])
            .build();
        dbZone.runTransaction(transaction)
            .then(res => showResult(res))
    }

    const getCloudConfig = () => {
        AGCCloudDB.getInstance().getCloudDBZoneConfigs().then(response => {
            showResult(response);
        }).catch(error => {
            showResult(error.message)
        })
    }

    const enableNetwork = () => {
        AGCCloudDB.getInstance().enableNetwork(zoneName)
            .then(res => showResult(res))
            .catch(err => showResult(err.message))
    }

    const disableNetwork = () => {
        AGCCloudDB.getInstance().disableNetwork(zoneName)
            .then(res => showResult(res))
            .catch(err => showResult(err.message))
    }

    const setUserKey = () => {
        AGCCloudDB.getInstance().setUserKey("gbkpassword10", "")
            .then(res => showResult(res))
            .catch(err => showResult(err.message))
    }

    return (
        <SafeAreaView style={styles.safeArea}>
            <StatusBar barStyle={'light-content'} backgroundColor="#1c1c1c" />
            <View style={styles.container}>
                <Header />
                <ScrollView>
                    <CustomButton
                        title={"init Cloud DB"}
                        onPress={() => initCloudDB()}
                    />
                    <CustomButton
                        title={"Open Cloud DB Zone"}
                        onPress={() => openCloudDBZone()}
                    />
                    {dbZone ?
                        <View>
                            <CustomButton
                                title={"Query Data"}
                                onPress={() => queryData()}
                            />
                            <CustomButton
                                title={"Write Data"}
                                onPress={() => writeData()}
                            />
                            <CustomButton
                                title={"Delete Data"}
                                onPress={() => deleteData()}
                            />
                            <CustomButton
                                title={"Get Total Price"}
                                onPress={() => querySum()}
                            />
                            <CustomButton
                                title={"Get Total Book Count"}
                                onPress={() => queryCount()}
                            />
                            <CustomButton
                                title={"Get Average Book  Price"}
                                onPress={() => queryAverage()}
                            />
                            <CustomButton
                                title={"Get data and delete"}
                                onPress={() => getDataAndDelete()}
                            />
                            <CustomButton
                                title={"Subscribe Snapshot"}
                                onPress={() => subscribeSnapshot()}
                            />
                            <CustomButton
                                title={"Run Transaction"}
                                onPress={() => runTransaction()}
                            />
                            <CustomButton
                                title={"Close Cloud DB Zone"}
                                onPress={() => closeCloudDB()}
                            />
                        </View>
                        : null}
                    <CustomButton
                        title={"Enable Network"}
                        onPress={() => enableNetwork()}
                    />
                    <CustomButton
                        title={"Disable Network"}
                        onPress={() => disableNetwork()}
                    />
                    <CustomButton
                        title={"Set User Key"}
                        onPress={() => setUserKey()}
                    />
                    <CustomButton
                        title={"Get Config"}
                        onPress={() => getCloudConfig()}
                    />
                </ScrollView>
                <View style={styles.footerContainer}>
                    <Text style={styles.footerText}>To enable all features please </Text>
                    <TouchableOpacity onPress={() => props.navigate("AuthScreen")}>
                        <Text style={styles.loginButtonText}>Log In</Text>
                    </TouchableOpacity>
                </View>
            </View>
        </SafeAreaView>
    );
};

const styles = StyleSheet.create({
    safeArea: {
        flex: 1,
        backgroundColor: "#1c1c1c",
    },
    container: {
        flex: 1,
        backgroundColor: "#1c1c1c",
    },
    textInputStyle: {
        height: 40,
        color: '#909090',
        borderRadius: 5,
        backgroundColor: '#1c1c1c',
        elevation: 0
    },
    footerContainer: {
        flexDirection: "row",
        width: '100%',
        justifyContent: "center",
        padding: 10,
    },

    footerText: {
        color: "#909090",
    },

    loginButtonText: {
        color: "#34abeb"
    }
});

export default CloudDBScreen;
