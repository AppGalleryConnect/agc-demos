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
import CloudDBScreen from './CloudDBScreen'
import AuthScreen  from './AuthScreen'

const AppNavigator = () => {
  const [route, setRoute] = useState("LoginScreen");

  const changeRoute = (route) => {
    setRoute(route)
  }

  const setScreen = () => {
    switch (route) {
      case "AuthScreen":
        return <AuthScreen navigate={(route) => changeRoute(route)} />
      default:
        return <CloudDBScreen navigate={(route) => changeRoute(route)} />
        
    }
  }
  return setScreen()
}

export default AppNavigator
