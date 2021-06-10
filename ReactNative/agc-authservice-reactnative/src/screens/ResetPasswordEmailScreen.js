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

import React, { useState } from 'react'
import { View, SafeAreaView, StatusBar, TextInput, Alert } from 'react-native'
import Header from '../components/Header'
import AGCAuth, { VerifyCodeSettings, VerifyCodeAction, EmailAuthProvider } from '@react-native-agconnect/auth'
import CustomButton from '../components/CustomButton'
import VerifyCodeButton from '../components/VerifyCodeButton'
import styles from '../styles'

const ResetPasswordEmailScreen = (props) => {
  const [email, setEmail] = useState()
  const [verificationCode, setVerifyCode] = useState()
  const [newPassword, setNewPassword] = useState(null)

  const sendVerificationCode = () => {
    let settings = new VerifyCodeSettings(VerifyCodeAction.RESET_PASSWORD, "en_US", 30)

    EmailAuthProvider.requestVerifyCode(email, settings).then(response => {
      Alert.alert(JSON.stringify(response))
    }).catch(error => {
      Alert.alert("error: ", JSON.stringify(error.message))
    })
  };

  const resetEmailPassword = () => {
    AGCAuth.getInstance().resetPasswordWithEmail(email, newPassword, verificationCode).then(() => {
      Alert.alert("true")
    }).catch(error => {
      Alert.alert("error: ", JSON.stringify(error.message))
    })
  }

  return (
    <SafeAreaView style={styles.safeArea}>
      <StatusBar barStyle={'dark-content'} backgroundColor="#e1e1e1" />
      <Header
        enableBackButton={true}
        onBackPress={() => props.navigate("ResetPasswordScreen")}
      />
      <View style={styles.container}>
        <TextInput
          placeholder={"Email"}
          placeholderTextColor="#909090"
          keyboardType={'email-address'}
          onChangeText={(text) => setEmail(text)}
          style={{ color: "black", backgroundColor: "white", borderRadius: 5, height: 50, marginBottom: 10, paddingLeft: 10, }}
        />
        <VerifyCodeButton
          onPress={() => sendVerificationCode()}
        />
        <TextInput
          secureTextEntry={true}
          style={{ backgroundColor: "white", color: "black", borderRadius: 5, height: 50, marginTop: 10, paddingLeft: 10, }}
          placeholder={"New Password"}
          placeholderTextColor="#909090"
          onChangeText={(text) => setNewPassword(text)}
        />
        <TextInput
          style={{ backgroundColor: "white", color: "black", borderRadius: 5, height: 50, marginVertical: 10, paddingLeft: 10, }}
          placeholder={"Verification Code"}
          placeholderTextColor="#909090FF"
          keyboardType={'number-pad'}
          onChangeText={text => setVerifyCode(text)}
        />
        <CustomButton
          onPress={() => resetEmailPassword()}
          title={"Reset Password"}
        />
      </View>
    </SafeAreaView>
  )
}

export default ResetPasswordEmailScreen
