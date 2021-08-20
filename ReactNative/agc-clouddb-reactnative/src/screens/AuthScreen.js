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

import React, { useState } from "react";
import {
    StyleSheet,
    TouchableOpacity,
    ScrollView,
    View,
    Text,
    Alert,
    SafeAreaView,
    StatusBar,
    TextInput
} from "react-native";
import CustomButton from '../components/CustomButton'
import AGCAuth, { VerifyCodeSettings, VerifyCodeAction, PhoneAuthProvider } from '@react-native-agconnect/auth'
import Header from "../components/Header";

const AuthScreen = (props) => {
    const [phone, setPhone] = useState()
    const [countryCode, setCountryCode] = useState()
    const [verifyCode, setVerifyCode] = useState()
    const [password, setPassword] = useState()

    const getVerificationCode = () => {
        let settings = new VerifyCodeSettings(VerifyCodeAction.REGISTER_OR_LOGIN, "en_US", 30);
        PhoneAuthProvider.requestVerifyCode(countryCode, phone, settings).then(response => {
            Alert.alert(JSON.stringify(response))
        }).catch(error => {
            Alert.alert("error: ", JSON.stringify(error.message))
        })
    }

    const createPhoneUser = () => {
        AGCAuth.getInstance().createPhoneUser(countryCode, phone, password, verifyCode).then(response => {
            Alert.alert("Success", "User created successfully.",
                [
                    { text: 'Okey.', onPress: () => props.navigate("CloudDBScreen") }
                ])
        }).catch(error => {
            Alert.alert("error: ", JSON.stringify(error.message))
        })
    }

    const loginPhone = () => {
        let credential = PhoneAuthProvider.credentialWithPassword(countryCode, phone, password);
        AGCAuth.getInstance().signIn(credential).then(response => {
            Alert.alert("Success", "Logged in  successfully.",
                [
                    { text: 'Okey.', onPress: () => props.navigate("CloudDBScreen") }
                ])
        }).catch(error => {
            Alert.alert("error: ", JSON.stringify(error.message))
        })
    }

    const signOut = () => {
        AGCAuth.getInstance().signOut().then(response => {
            Alert.alert("Success", "Signed out  successfully.",
                [
                    { text: 'Okey.', onPress: () => props.navigate("CloudDBScreen") }
                ])
        }).catch(err => {
            Alert.alert("Error", err.message)
        })
    }

    return (
        <SafeAreaView style={styles.safeArea}>
            <StatusBar barStyle={'light-content'} backgroundColor="#1c1c1c" />
            <View style={styles.container}>
                <Header />
                <TouchableOpacity
                    style={styles.backButton}
                    onPress={() => props.navigate("CloudDBScreen")}>
                    <Text style={styles.backButtonText}> Back </Text>
                </TouchableOpacity>
                <ScrollView>
                    <View style={styles.phoneInputContainer}>
                        <TextInput
                            placeholder={"Country Code"}
                            placeholderTextColor="#909090"
                            style={[styles.textInputStyle, { flex: 1 }]}
                            keyboardType={'phone-pad'}
                            onChangeText={(text) => setCountryCode(text)}
                        />
                        <TextInput
                            placeholder={"Phone Number"}
                            style={[styles.textInputStyle, { flex: 2.5 }]}
                            placeholderTextColor="#909090ff"
                            keyboardType={'number-pad'}
                            onChangeText={(text) => setPhone(text)}
                        />
                    </View>
                    <TextInput
                        style={styles.textInputStyle}
                        placeholder={"Password"}
                        placeholderTextColor="#909090"
                        secureTextEntry={true}
                        onChangeText={(text) => setPassword(text)}
                    />
                    <TextInput
                        style={styles.textInputStyle}
                        placeholder={"Verification Code"}
                        onChangeText={text => setVerifyCode(text)}
                        placeholderTextColor="#909090"
                        keyboardType={'number-pad'}
                    />
                    <CustomButton
                        title={"Get Phone Verify Code"}
                        onPress={() => getVerificationCode()}
                    />
                    <CustomButton
                        title={"Sign Up"}
                        onPress={() => createPhoneUser()}
                    />
                    <CustomButton
                        title={"Log In"}
                        onPress={() => loginPhone()}
                    />
                    <CustomButton
                        title={"Sign Out"}
                        onPress={() => signOut()}
                    />
                </ScrollView>
            </View>
        </SafeAreaView>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: "#1c1c1c",
    },
    safeArea: {
        flex: 1,
        backgroundColor: "#1c1c1c",
    },
    textInputStyle: {
        backgroundColor: "#2e2e2e",
        color: "white",
        borderRadius: 5,
        height: 50,
        margin: 10,
        padding: 10,
    },
    phoneInputContainer: {
        height: 60,
        flexDirection: 'row',
        width: '100%',
        marginBottom: 10,
    },
    backButton: {
        position: "absolute",
        top: 10,
        left: 10,
    },
    backButtonText: {
        color: "#34abeb",
        fontSize: 20,
    }
});

export default AuthScreen;
