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
import { View, Text, StyleSheet, TouchableOpacity, Image } from 'react-native'

const Header = (props) => {
  return (
    <View style={styles.header}>
      {props.enableBackButton ?
        <TouchableOpacity
          onPress={() => props.onBackPress()}
          style={{ position: "absolute", left: 0, top: 10 }}
        >
          <Text style={styles.backButtonTitle}>{"<  BACK"}</Text>
        </TouchableOpacity>
        : null}
      <View style={styles.titleContainer}>
        <Text style={styles.headerTitle} >
          AGC React Native Auth Services Plugin
    </Text>
      </View>
      <Image
        style={styles.headerImage}
        source={require("../assets/agc-rn-logo.png")}
      />
    </View>
  )
}

const styles = StyleSheet.create({
  header: {
    height: 120,
    justifyContent: "space-between",
    alignItems: 'flex-end',
    flexDirection: "row",
    marginBottom: 10,
    marginHorizontal: 20,
  },
  titleContainer: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    padding: 10,
  },
  headerTitle: {
    fontWeight: "bold",
    color: "#909090",
    fontSize: 20,
  },
  headerImage: {
    resizeMode: "center",
    width: 100,
    height: 100,
  },
  backButtonTitle: {
    color: "#0055ff",
    fontSize: 18,
  }
})

export default Header
