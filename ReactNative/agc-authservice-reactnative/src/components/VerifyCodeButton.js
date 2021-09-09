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
import { TouchableOpacity, Text, StyleSheet } from 'react-native'

const VerifyCodeButton = (props) => {
  return (
    <TouchableOpacity
      style={styles.verifyCodeButton}
      onPress={() => props.onPress()}
    >
      <Text style={styles.verifyCodeButtonText}>Send Verification Code</Text>
    </TouchableOpacity>
  )
}

const styles = StyleSheet.create({
  verifyCodeButton: {
    height: 40,
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "#00ff00",
    borderRadius: 5,
  },
  verifyCodeButtonText: {
    fontSize: 18,
    fontWeight: "bold",
    color: "#ffffff",
  }
})

export default VerifyCodeButton
