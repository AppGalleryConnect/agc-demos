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
  StyleSheet,
  Image,
  TouchableOpacity,
  ScrollView,
  SafeAreaView,
  StatusBar,
} from "react-native";
import AGCCloudFunctions, {
  AGCTimeUnit,
} from "@react-native-agconnect/cloudfunctions";

const App = () => {

  const callCloudFunction = () => {
    const triggerIdentifier = "<your_trigger_identifier>";

    const options = {
      timeout: 1000,
      timeUnit: AGCTimeUnit.SECONDS,
      params: {
        key1: "testString",
        key2: 123
      }
    }

    AGCCloudFunctions.call(triggerIdentifier, options)
      .then(response => {
        Alert.alert("response", JSON.stringify(response));
      })
      .catch((error) => {
        Alert.alert("Error", error.toString());
      });
  };

  return (
    <SafeAreaView style={styles.safeArea}>
      <StatusBar barStyle={'light-content'} backgroundColor="#1c1c1c" />
      <View style={styles.container}>
        <View style={styles.header}>
          <View style={styles.titleContainer}>
            <Text style={styles.headerTitle} >
              AGC React Native Cloud Functions Plugin
          </Text>
          </View>
          <Image
            style={styles.headerImage}
            source={require("./src/assets/agc-rn-logo.png")}
          />
        </View>
        <ScrollView>
          <View style={styles.buttonContainer}>
            <TouchableOpacity
              style={styles.button}
              onPress={() => callCloudFunction()}
            >
              <Text style={styles.buttonText}>Call Cloud Function</Text>
            </TouchableOpacity>
          </View>
        </ScrollView>
      </View>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  safeArea: {
    flex: 1,
    backgroundColor: "#1c1c1c",
  },
  container: {
    flex: 1,
    backgroundColor: "#1c1c1c",
  },
  header: {
    height: 100,
    justifyContent: "space-between",
    alignItems: "center",
    flexDirection: "row",
    marginVertical: 40,
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
  buttonContainer: {
    padding: 10,
  },
  button: {
    height: 50,
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "#2e2e2e",
    marginBottom: 15,
    elevation: 5,
    borderRadius: 5,
  },
  buttonText: {
    fontSize: 18,
    fontWeight: "bold",
    color: "#909090",
  },
  textInputStyle: {
    height: 40,
    color: '#909090',
    borderRadius: 5,
    backgroundColor: '#1c1c1c',
    elevation: 0
  }
});

export default App;
