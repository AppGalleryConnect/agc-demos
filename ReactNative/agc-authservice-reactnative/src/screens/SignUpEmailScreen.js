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

const SignUpEmailScreen = (props) => {
  const [email, setEmail] = useState()
  const [verificationCode, setVerificationCode] = useState()
  const [password, setPassword] = useState(null)

  const createEmailUser = () => {
    AGCAuth.getInstance().createEmailUser(email, password, verificationCode)
      .then(() => {
        props.navigate("HomeScreen")
      }).catch(error => {
        Alert.alert("error: ", JSON.stringify(error.message))
      })
  }

  const sendVerificationCode = () => {
    let settings = new VerifyCodeSettings(VerifyCodeAction.REGISTER_OR_LOGIN, "en_US", 30)

    EmailAuthProvider.requestVerifyCode(email, settings).then(response => {
      Alert.alert(JSON.stringify(response))
    }).catch(error => {
      Alert.alert("error: ", JSON.stringify(error.message))
    })
  };

  return (
    <SafeAreaView style={styles.safeArea}>
      <StatusBar barStyle={'dark-content'} backgroundColor="#e1e1e1" />
      <Header
        onBackPress={() => props.navigate("LoginScreen")}
        enableBackButton={true}
      />
      <View style={styles.container}>
        <TextInput
          placeholder={"Email"}
          placeholderTextColor="#909090"
          keyboardType={'email-address'}
          style={{ color: "black", backgroundColor: "white", borderRadius: 5, height: 50, marginBottom: 10, paddingLeft: 10, }}
          onChangeText={(text) => setEmail(text)}
        />
        <VerifyCodeButton
          onPress={() => sendVerificationCode()}
        />
        <TextInput
          style={{ backgroundColor: "white", color: "black", borderRadius: 5, height: 50, marginTop: 10, paddingLeft: 10, }}
          secureTextEntry={true}
          onChangeText={(text) => setPassword(text)}
          placeholder={"Password"}
          placeholderTextColor="#909090"
        />
        <TextInput
          style={{ backgroundColor: "white", color: "black", borderRadius: 5, height: 50, marginVertical: 10, paddingLeft: 10, }}
          keyboardType={'number-pad'}
          onChangeText={text => setVerificationCode(text)}
          placeholder={"Verification Code"}
          placeholderTextColor="#909090"
        />
        <CustomButton
          onPress={() => createEmailUser()}
          title={"Sign Up With E-mail"}
        />
      </View>
    </SafeAreaView>
  )
}

export default SignUpEmailScreen
