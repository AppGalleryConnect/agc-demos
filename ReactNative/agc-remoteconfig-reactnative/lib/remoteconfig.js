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

import * as React from 'react';
import { View, Button, StyleSheet } from 'react-native';
import AGCRemoteConfig, { SOURCE} from '@react-native-agconnect/remoteconfig';

const Separator = () => {
    return <View style={styles.separator} />;
}

export default function ConfigScreen() {
    return (
        <View style={{ marginTop: 30, paddingHorizontal: 20 }}>
            <Button
                title="apply Default"
                onPress={() => {
                    var map = new Map();
                    map.set("default_key_string", '11');
                    map.set("default_key_true", true);
                    map.set("default_key_int", 100);
                    map.set("default_key_double", 3.14);
                    AGCRemoteConfig.applyDefault(map);
                }}
            />
            <Separator />

            <Button
                title="fetch 0"
                onPress={() => {
                    AGCRemoteConfig.fetch(0)
                    .then(() => {
                        console.log("fetch result success");
                    })
                    .catch((configError)=>{
                        console.log("code==" + configError.code);
                        console.log("message==" + configError.message);
                        console.log("throttleEndTime==" + configError.throttleEndTime);
                    });
                }}
            />
            <Separator />

            <Button
                title="apply last fetch"
                onPress={() => {
                    AGCRemoteConfig.applyLastFetched();
                    console.log("applyLastFetched");
                }}
            />
            <Separator />

            <Button
                title="get Merged All"
                onPress={() => {
                    AGCRemoteConfig.getMergedAll().then((map) => {
                        map.forEach(function (value, key) {
                            console.log("key-->" + key + ",value-->" + value);
                        });
                    });
                }}
            />
            <Separator />

            <Button
                title="get Source"
                onPress={() => {
                    AGCRemoteConfig.getSource("default_key_string").then((source) => {
                        switch (source) {
                            case SOURCE.STATIC:
                                console.log('source is STATIC');
                                break;
                            case SOURCE.DEFAULT:
                                console.log('source is DEFAULT');
                                break;
                            case SOURCE.REMOTE:
                                console.log('source is REMOTE');
                                break;
                        }
                    });
                }}
            />
            <Separator />

            <Button
                title="get Value"
                onPress={() => {
                    AGCRemoteConfig.getValue("default_key_string").then((data) => {
                        console.log("default_key_string ==" + (data));
                    });

                    AGCRemoteConfig.getValue("default_key_true").then((data) => {
                        console.log("default_key_true ==" + (data));
                    });

                    AGCRemoteConfig.getValue("key_static").then((data) => {
                        console.log("key_static ==" + (data));
                    });
                    AGCRemoteConfig.getValue("testkey").then((data) => {
                        console.log("remote_testkey ==" + (data));
                    });
                }}
            />
            <Separator />

            <Button
                title="clearAll"
                onPress={() => {
                    AGCRemoteConfig.clearAll();
                }}
            />
            <Separator />
        </View>
    );
}

const styles = StyleSheet.create({
    separator: {
        marginVertical: 8,
        borderBottomColor: '#737373',
        borderBottomWidth: StyleSheet.hairlineWidth,
    },
});