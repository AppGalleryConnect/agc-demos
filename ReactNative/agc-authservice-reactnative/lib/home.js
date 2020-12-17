/*
Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

import * as React from 'react';
import { View, Text, Button, StyleSheet } from 'react-native';
import {Separator, Styles } from './separator';

export default function HomeScreen({ navigation }) {
    return (
        <View style={Styles.sectionContainer}>
            <Text>React Native SDK DEMO</Text>
            <Button
                title="Auth"
                onPress={() => navigation.navigate('Auth')}
            />
            <Separator />
        </View>
    );
}



