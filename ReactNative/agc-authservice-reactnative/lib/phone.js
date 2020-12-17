import * as React from 'react';
import { View, Text, Button } from 'react-native';
import { Separator, Styles } from './separator';
import AGCAuth, { AuthProvider, AGCUser, PhoneAuthProvider, VerifyCodeSettings, VerifyCodeAction, EmailAuthProvider } from '@react-native-agconnect/auth';
import Dialog from "react-native-dialog";

export default function PhoneScreen() {
  

    const [mPhone, setmPhone] = React.useState("");
    const [mVerifyCode, setmVerifyCode] = React.useState("");
    const [current, setCurrent] = React.useState("no user login");
    const [visiblePhone, setVisiblePhone] = React.useState(false);
    const [visibleVerifyCode, setVisibleVerifyCode] = React.useState(false);
    const [phone, setPhone] = React.useState("");
    const [verifyCode, setVerifyCode] = React.useState("");

    function getUser() {
        AGCAuth.getInstance().currentUser().then((user) => {
            var info = user ? ("userid=" + user.uid + ", providerId=" + user.providerId):"no user login";
            setCurrent(info);
        });
    }
    React.useEffect(() => { return getUser();}, []);

    return (
        <View style={Styles.sectionContainer}>
            <Text>{current}</Text>

            <Separator />
            <Button
                title="Input Phone"
                onPress={() => {
                    setVisiblePhone(true);
                }}
            />

            <Dialog.Container visible={visiblePhone}>
                <Dialog.Title>Input Phone</Dialog.Title>
                <Dialog.Input
                    keyboardType='phone-pad'
                    placeholder='input phone number'
                    onChangeText={(text) => {
                        setPhone(text);
                    }}
                />
                <Dialog.Button label="OK" onPress={() => {
                    if (phone) {
                        setmPhone(phone);
                        console.log('you input is:' + phone);
                    }
                    setVisiblePhone(false);
                }} />
            </Dialog.Container>

            <Separator />
            <Button
                title="Input VerifyCode"
                onPress={() => {
                    setVisibleVerifyCode(true);
                }}
            />

            <Dialog.Container visible={visibleVerifyCode}>
                <Dialog.Title>Input VerifyCode</Dialog.Title>
                <Dialog.Input
                    keyboardType='number-pad'
                    placeholder='input a VerifyCode'
                    onChangeText={(text) => {
                        setVerifyCode(text);
                    }}
                />
                <Dialog.Button label="OK" onPress={() => {
                    if (verifyCode) {
                        setmVerifyCode(verifyCode);
                        console.log('you input is:' + verifyCode);
                    }
                    setVisibleVerifyCode(false);
                }} />
            </Dialog.Container>

            <Separator />
            <Button
                title="request verify code"
                onPress={() => {
                    var settings = new VerifyCodeSettings(VerifyCodeAction.REGISTER_OR_LOGIN, 'zh_cn', 30);
                    PhoneAuthProvider.requestVerifyCode('86', mPhone, settings)
                        .then(verifyCodeResult => {
                            console.log("request verify code success");
                        })
                        .catch(error => {
                            console.log(error.code, error.message);
                        });
                }}
            />

            <Separator />
            <Button
                title="create phone user"
                onPress={() => {
                    AGCAuth.getInstance().createPhoneUser('86', mPhone, null, mVerifyCode)
                        .then(signInResult => {
                            console.log("create phone user success");
                            getUser();
                        })
                        .catch(error => {
                            console.log(error.code, error.message);
                        });

                }}
            />

            <Separator />
            <Button
                title="signIn"
                onPress={() => {
                    var credential = PhoneAuthProvider.credentialWithVerifyCode('86', mPhone, null, mVerifyCode);
                    AGCAuth.getInstance().signIn(credential)
                        .then(() => {
                            console.log("signIn success");
                            getUser();
                        })
                        .catch(error => {
                            console.log(error.code, error.message);
                        });
                }}
            />
           
            <Separator />
            <Button
                title="signOut"
                onPress={() => {
                    AGCAuth.getInstance().signOut().then(() => {
                        console.log("signOut success");
                        getUser();
                    });
                }}
            />

        </View>
    );
}






