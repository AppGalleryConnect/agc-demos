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
import AGCAuth, { VerifyCodeSettings, VerifyCodeAction, PhoneAuthProvider } from '@react-native-agconnect/auth'
import CustomButton from '../components/CustomButton'
import VerifyCodeButton from '../components/VerifyCodeButton'
import styles from '../styles'

const SignUpPhoneScreen = (props) => {
  const [verificationCode, setVerificationCode] = useState()
  const [password, setPassword] = useState(null)
  const [countryCode, setcountryCode] = useState()
  const [phoneNumber, setPhoneNumber] = useState()

  const sendVerificationCode = () => {
    let settings = new VerifyCodeSettings(VerifyCodeAction.REGISTER_OR_LOGIN, "en_US", 30);
    PhoneAuthProvider.requestVerifyCode(countryCode, phoneNumber, settings).then(response => {
      Alert.alert(JSON.stringify(response))
    }).catch(error => {
      Alert.alert("error: ", JSON.stringify(error.message))
    })
  };

  const createUser = () => {
    AGCAuth.getInstance().createPhoneUser(countryCode, phoneNumber, password, verificationCode).then(response => {
      props.navigate("LoginScreen")
    }).catch(error => {
      Alert.alert("error: ", JSON.stringify(error.message))
    })
  }

  return (
    <SafeAreaView style={styles.safeArea}>
      <StatusBar barStyle={'dark-content'} backgroundColor="#e1e1e1" />
      <Header
        enableBackButton={true}
        onBackPress={() => props.navigate("SignUpScreen")}
      />
      <View style={styles.container}>
        <View style={styles.phoneInputContainer}>
          <TextInput
            placeholder={"Country Code"}
            placeholderTextColor="#909090"
            style={{ flex: 1, color: "black", backgroundColor: "white", borderRadius: 5, width: 50, marginRight: 10, paddingLeft: 10, }}
            keyboardType={'phone-pad'}
            onChangeText={(text) => setcountryCode(text)}
          />
          <TextInput
            placeholder={"Phone Number"}
            style={{ flex: 2.5, color: "black", backgroundColor: "white", borderRadius: 5, width: 50, paddingLeft: 10, }}
            placeholderTextColor="#909090ff"
            keyboardType={'number-pad'}
            onChangeText={(text) => setPhoneNumber(text)}
          />
        </View>
        <VerifyCodeButton
          onPress={() => sendVerificationCode()}
        />
        <TextInput
          style={{ backgroundColor: "white", color: "black", height: 50, borderRadius: 5, marginTop: 10, paddingLeft: 10, }}
          placeholder={"Password (Optional)"}
          placeholderTextColor="#909090"
          secureTextEntry={true}
          onChangeText={(text) => setPassword(text)}
        />
        <TextInput
          style={{ backgroundColor: "white", color: "black", borderRadius: 5, height: 50, marginVertical: 10, paddingLeft: 10, }}
          placeholder={"Verification Code"}
          onChangeText={text => setVerificationCode(text)}
          placeholderTextColor="#909090"
          keyboardType={'number-pad'}
        />
        <CustomButton
          onPress={() => createUser()}
          title={"Sign Up With Phone Number"}
        />
      </View>
    </SafeAreaView>
  )
}

export default SignUpPhoneScreen
