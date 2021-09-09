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

import React from 'react'
import { View, SafeAreaView, StatusBar } from 'react-native'
import CustomButton from '../components/CustomButton'
import Header from '../components/Header'

const RestPasswordScreen = (props) => {
  return (
    <SafeAreaView style={styles.safeArea}>
      <StatusBar barStyle={'dark-content'} backgroundColor="#e1e1e1" />
      <Header
        enableBackButton={true}
        onBackPress={() => props.navigate("LoginScreen")}
      />
      <View style={styles.container}>
        <CustomButton
          onPress={() => props.navigate("ResetPasswordPhoneScreen")}
          title={"Reset Password With Phone Number"}
        />
        <CustomButton
          onPress={() => props.navigate("ResetPasswordEmailScreen")}
          title={"Reset Password With E-mail"}
        />
      </View>
    </SafeAreaView>
  )
}

export default RestPasswordScreen
