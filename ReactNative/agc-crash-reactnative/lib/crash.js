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
import AGCCrash, { LogLevel } from '@react-native-agconnect/crash';

const Separator = () => {
  return <View style={styles.separator} />;
}

export default function CrashScreen() {
  return (
    <View style={{ marginTop: 30, paddingHorizontal: 20 }}>
      <Button
        title="enableCrashCollection"
        onPress={() => AGCCrash.enableCrashCollection(true)}
      />
      <Separator />
      <Button
        style={{ marginTop: 30 }}
        title="testIt"
        onPress={() => AGCCrash.testIt()}
      />
      <Separator />
      <Button
        title="setUserId"
        onPress={() => AGCCrash.setUserId('userid001')}
      />
      <Separator />
      <Button
        title="setCustomKey"
        onPress={() => {
          AGCCrash.setCustomKey('key_string', 'value_string');
          AGCCrash.setCustomKey('key_boolean', true);
          AGCCrash.setCustomKey('key_number', 3.14);
        }}
      />
      <Separator />
      <Button
        title="log"
        onPress={() => AGCCrash.log('log:default message001')}
      />
      <Separator />
      <Button
        title="logWithLevel"
        onPress={() => {
          AGCCrash.logWithLevel(LogLevel.DEBUG, 'logWithLevel:DEBUG message DEBUG')
          AGCCrash.logWithLevel(LogLevel.INFO, 'logWithLevel:INFO message INFO')
          AGCCrash.logWithLevel(LogLevel.WARN, 'logWithLevel:WARN message WARN')
          AGCCrash.logWithLevel(100, 'logWithLevel:ERROR message ERROR')
        }}
      />
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