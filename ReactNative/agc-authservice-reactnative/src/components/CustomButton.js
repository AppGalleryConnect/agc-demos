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

const CustomButton = (props) => {
  return (
    <TouchableOpacity
      style={styles.button}
      onPress={() => props.onPress()}
    >
      <Text style={styles.buttonText}>{props.title}</Text>
    </TouchableOpacity>
  )
}

const styles = StyleSheet.create({
  button: {
    height: 50,
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "#ffffff",
    marginVertical: 10,
    borderRadius: 5,
  },
  buttonText: {
    fontSize: 18,
    fontWeight: "bold",
    color: "#909090",
  }
});

export default CustomButton
