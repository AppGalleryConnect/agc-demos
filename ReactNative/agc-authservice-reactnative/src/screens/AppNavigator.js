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
import EmailLoginScreen from './EmailLoginScreen';
import HomeScreen from './HomeScreen';
import LoginScreen from './LoginScreen';
import PhoneLoginScreen from './PhoneLoginScreen';
import ResetPasswordEmailScreen from './ResetPasswordEmailScreen';
import ResetPasswordPhoneScreen from './ResetPasswordPhoneScreen';
import ResetPasswordScreen from './ResetPasswordScreen';
import SignUpEmailScreen from './SignUpEmailScreen';
import SignUpPhoneScreen from './SignUpPhoneScreen'
import SignUpScreen from './SignUpScreen';

const AppNavigator = () => {
  const [route, setRoute] = useState("LoginScreen");

  const changeRoute = (route) => {
    setRoute(route)
  }

  const setScreen = () => {
    switch (route) {
      case "SignUpScreen":
        return <SignUpScreen navigate={(route) => changeRoute(route)} />
      case "SignUpPhoneScreen":
        return <SignUpPhoneScreen navigate={(route) => changeRoute(route)} />
      case "EmailLoginScreen":
        return <EmailLoginScreen navigate={(route) => changeRoute(route)} />
      case "HomeScreen":
        return <HomeScreen navigate={(route) => changeRoute(route)} />
      case "PhoneLoginScreen":
        return <PhoneLoginScreen navigate={(route) => changeRoute(route)} />
      case "SignUpEmailScreen":
        return <SignUpEmailScreen navigate={(route) => changeRoute(route)} />
      case "ResetPasswordScreen":
        return <ResetPasswordScreen navigate={(route) => changeRoute(route)} />
      case "ResetPasswordPhoneScreen":
        return <ResetPasswordPhoneScreen navigate={(route) => changeRoute(route)} />
      case "ResetPasswordEmailScreen":
        return <ResetPasswordEmailScreen navigate={(route) => changeRoute(route)} />
      default:
        return <LoginScreen navigate={(route) => changeRoute(route)} />
    }
  }
  return setScreen()
}

export default AppNavigator
