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

import React, { useEffect, useState } from 'react'
import { View, Text, SafeAreaView, StatusBar, Alert, Image, TextInput, ScrollView } from 'react-native'
import Header from '../components/Header'
import AGCAuth from '@react-native-agconnect/auth'
import CustomButton from '../components/CustomButton'
import styles from '../styles'

const HomeScreen = (props) => {
  const [user, setUser] = useState({})
  const [displayName, setDisplayName] = useState()
  const [photoUrl, setPhotoUrl] = useState()

  useEffect(() => {
    getUser()
  }, []);

  const getUser = async () => {
    const user = await AGCAuth.getInstance().currentUser();
    setUser(user)
  }

  const updateProfile = async () => {
    const params = {
      displayName: displayName,
      photoUrl: photoUrl
    }
    user.updateProfile(params).then(() => {
      getUser()
    }).catch(error => {
      Alert.alert("error", JSON.stringify(error.message))
    })
  }

  const getUserExtra = async () => {
    const user = await AGCAuth.getInstance().currentUser();
    user.getUserExtra().then(response => {
      Alert.alert("User Extra", JSON.stringify(response))
    }).catch(error => {
      Alert.alert("Error", error.toString())
    })
  }

  const getToken = async () => {
    const user = await AGCAuth.getInstance().currentUser();
    user.getToken(true).then(response => {
      Alert.alert("token", JSON.stringify(response))
    }).catch(error => {
      Alert.alert("error", JSON.stringify(error.message))
    })
  }

  const signOut = () => {
    AGCAuth.getInstance().signOut().then(response => {
      Alert.alert("signOut", JSON.stringify(response))
    })
    props.navigate("LoginScreen")
  }

  const deleteUser = () => {
    AGCAuth.getInstance().deleteUser();
    props.navigate("LoginScreen")
  }

  const renderUpdateProfileView = () => {
    return (
      <View style={{ padding: 5, borderWidth: 0.5, borderRadius: 5, marginVertical: 5 }}>
        <TextInput
          style={{ backgroundColor: "white", color: "black", borderRadius: 5, height: 50, marginTop: 10, paddingLeft: 10, }}
          placeholder={"DisplayName"}
          placeholderTextColor="#909090"
          onChangeText={(text) => setDisplayName(text)}
        />
        <TextInput
          style={{ backgroundColor: "white", color: "black", borderRadius: 5, height: 50, marginVertical: 10, paddingLeft: 10, }}
          placeholder={"Photo Url"}
          placeholderTextColor="#909090"
          onChangeText={text => setPhotoUrl(text)}
        />
        <CustomButton
          onPress={() => updateProfile()}
          title={"Update Profile"}
        />
      </View>
    )
  }

  return (
    <SafeAreaView style={styles.safeArea}>
      <StatusBar barStyle={'dark-content'} backgroundColor="#e1e1e1" />
      <Header />
      <View style={styles.container}>
        <ScrollView>
          <View style={styles.infoContainer}>
            <Image
              style={{ width: 80, height: 80, resizeMode: 'center' }}
              source={{ uri: user.photoUrl }}
            />
            <View style={{ paddingHorizontal: 10 }}>
              <Text>{"Email: " + user.email}</Text>
              <Text>{"Phone: " + user.phoneNumber}</Text>
              <Text>{"Display Name: " + user.displayName}</Text>
            </View>
          </View>
          <Text>{"Email verified: " + user.emailVerified}</Text>
          <Text>{"Is Anonymous: " + user.isAnonymous}</Text>
          <Text>{"Uid: " + user.uid}</Text>
          <Text>{"providerId: " + user.providerId}</Text>
          <Text>{"passwordSetted: " + user.passwordSetted}</Text>
          {renderUpdateProfileView()}
          <CustomButton
            onPress={() => getToken()}
            title={"Get Token"}
          />
          <CustomButton
            onPress={() => getUserExtra()}
            title={"Get User Extra"}
          />
          <CustomButton
            onPress={() => signOut()}
            title={"Sign Out"}
          />
          <CustomButton
            onPress={() => deleteUser()}
            title={"Delete User"}
          />
        </ScrollView>
      </View>
    </SafeAreaView>
  )
}

export default HomeScreen;
