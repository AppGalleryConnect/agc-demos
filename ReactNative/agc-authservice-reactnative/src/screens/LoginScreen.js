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

import React from "react";
import {
  View,
  Text,
  Alert,
  TouchableOpacity,
  ScrollView,
  SafeAreaView,
  StatusBar,
  Platform
} from "react-native";
import AGCAuth, {
  FacebookAuthProvider,
  HwIdAuthProvider,
} from '@react-native-agconnect/auth'
import Header from "../components/Header";
import FBSDK, { LoginManager } from 'react-native-fbsdk';
import HMSAccount, {
  HMSAuthParamConstants,
  HMSAuthRequestOptionConstants,
} from "@hmscore/react-native-hms-account";
import CustomButton from "../components/CustomButton";
import styles from '../styles'

const LoginScreen = (props) => {

  AGCAuth.getInstance().currentUser()
    .then(user => {
      if (user) {
        props.navigate("HomeScreen")
      }
    })
    .catch(error => { })

  const goSignUpScreen = () => {
    props.navigate("SignUpScreen")
  }

  goToResetPasswordScreen = () => {
    props.navigate("ResetPasswordScreen")
  }

  const signInAnonymously = () => {
    AGCAuth.getInstance().signInAnonymously().then(response => {
      props.navigate("HomeScreen")
    }).catch(error => {
      Alert.alert("error", JSON.stringify(error.message))
    })
  }

  const signIn = (credential) => {
    AGCAuth.getInstance().signIn(credential).then((signInResult) => {
      props.navigate("HomeScreen")
    }).catch(error => {
      Alert.alert("error", JSON.stringify(error.message))
    })
  }

  const loginWithHwID = () => {
    let signInData = {
      huaweiIdAuthParams: HMSAuthParamConstants.DEFAULT_AUTH_REQUEST_PARAM,
      authRequestOption: [
        HMSAuthRequestOptionConstants.ID_TOKEN,
        HMSAuthRequestOptionConstants.EMAIL,
        HMSAuthRequestOptionConstants.ACCESS_TOKEN
      ],
    };
    HMSAccount.signIn(signInData)
      .then((response) => {
        let credential = HwIdAuthProvider.credential(response.accessToken);
        signIn(credential)
      })
      .catch((err) => {
        Alert.alert("Error", err.toString())
      });
  }

  const loginWithFacebook = async () => {
    LoginManager.logInWithPermissions(["public_profile"]).then(
      function (result) {
        if (result.isCancelled) {
          Alert.alert("Login Cancelled.")
        } else {
          FBSDK.AccessToken.getCurrentAccessToken().then(data => {
            let credential = FacebookAuthProvider.credential(data.accessToken);
            signIn(credential);
          })
        }
      },
      function (error) {
        Alert.alert("Login fail with error: ", error)
      }
    );
  }

  const loginWithWeChat = async () => {
    /**
     * Implement authentication with Wechat
     */
    showUnimplementedAlert();
  }

  const loginWithTwitter = async () => {
    /**
     * Implement authentication with Twitter
     */
    showUnimplementedAlert();
  }

  const loginWithQQ = () => {
    /**
     * Implement authentication with QQ
     */
    showUnimplementedAlert();
  }

  const loginWithWeibo = () => {
    /**
     * Implement authentication with Weibo
     */
    showUnimplementedAlert();
  }

  const loginWithApple = () => {
    /**
     * Implement authentication with Apple
     */
    showUnimplementedAlert();;
  }

  const signInWithGoogle = async () => {
    /**
     * Implement authentication with Google
     */
    showUnimplementedAlert();
  }

  const showUnimplementedAlert = () => {
    Alert.alert("Error", "Please integrate related third plugins and retrive the parameters to auth plugin to enable this function, you can read readme for detail.")
  }

  return (
    <SafeAreaView style={styles.safeArea}>
      <StatusBar barStyle={'dark-content'} backgroundColor="#e1e1e1" />
      <View style={styles.container}>
        <Header />
        <ScrollView>
          <CustomButton
            title={"Login With E-mail"}
            onPress={() => props.navigate("EmailLoginScreen")}
          />
          <CustomButton
            title={"Login With Phone"}
            onPress={() => props.navigate("PhoneLoginScreen")}
          />
          <CustomButton
            title={"Login With Facebook"}
            onPress={() => loginWithFacebook()}
          />
          {Platform.OS === "ios" ?
            <CustomButton
              title={"Log In With Apple"}
              onPress={() => loginWithApple()}
            />
            : null}
          {Platform.OS === "android" ?
            <CustomButton
              title={"Log In With Huawei ID"}
              onPress={() => loginWithHwID()}
            />
            : null}
          <CustomButton
            title={"Log In With Google"}
            onPress={() => signInWithGoogle()}
          />
          <CustomButton
            title={"Log In With QQ"}
            onPress={() => loginWithQQ()}
          />
          <CustomButton
            title={"Log In With WeChat"}
            onPress={() => loginWithWeChat()}
          />
          <CustomButton
            title={"Log In With Twitter"}
            onPress={() => loginWithTwitter()}
          />
          <CustomButton
            title={"Sign In Anonymously"}
            onPress={() => signInAnonymously()}
          />
          <CustomButton
            title={"Log In With Weibo"}
            onPress={() => loginWithWeibo()}
          />
        </ScrollView>
        <View style={styles.linkContainer}>
          <Text>If you do not have account. Please </Text>
          <TouchableOpacity onPress={() => goSignUpScreen()}>
            <Text style={styles.link}>Sign Up</Text>
          </TouchableOpacity>
        </View>
        <View style={styles.linkContainer}>
          <Text>To reset your password. Click </Text>
          <TouchableOpacity onPress={() => goToResetPasswordScreen()}>
            <Text style={styles.link}>Reset Password</Text>
          </TouchableOpacity>
        </View>
      </View>
    </SafeAreaView >
  );
};

export default LoginScreen;
