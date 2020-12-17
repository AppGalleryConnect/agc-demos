import * as React from 'react';
import { View, Text, Button } from 'react-native';
import { Separator, Styles } from './separator';
import AGCAuth, { AGCUser, AuthProvider, EmailAuthProvider, VerifyCodeSettings, VerifyCodeAction } from '@react-native-agconnect/auth';
import Dialog from "react-native-dialog";

export default function EmailScreen() {

    const [mEmail, setmEmail] = React.useState("");
    const [mVerifyCode, setmVerifyCode] = React.useState("");
    const [current, setCurrent] = React.useState("no user login");
    const [visibleEmail, setVisibleEmail] = React.useState(false);
    const [visibleVerifyCode, setVisibleVerifyCode] = React.useState(false);
    const [email, setEmail] = React.useState("");
    const [verifyCode, setVerifyCode] = React.useState("");

    function getUser() {
        AGCAuth.getInstance().currentUser().then((user) => {
            var info = user ? ("userid=" + user.uid + ", providerId=" + user.providerId) : "no user login";
            setCurrent(info);
        });
    }
    React.useEffect(() => { return getUser(); }, []);

    return (
        <View style={Styles.sectionContainer}>
            <Text>{current}</Text>

            <Separator />
            <Button
                title="Input Email"
                onPress={() => {
                    setVisibleEmail(true);
                 
                }}
            />

            <Dialog.Container visible={visibleEmail}>
                <Dialog.Title>Input Email</Dialog.Title>
                <Dialog.Input
                    keyboardType='email-address'
                    placeholder='input email address'
                    onChangeText={(text) => {
                        setEmail(text);
                    }}
                />
                <Dialog.Button label="OK" onPress={() => {
                    if (email) {
                        console.log('you input is:' + email);
                        setmEmail(email);
                    }
                    setVisibleEmail(false);
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
                title="get user"
                onPress={() => {
                    AGCAuth.getInstance().currentUser().then((user) => {
                        console.log(user);
                    });
                }}
            />

            <Separator />
            <Button
                title="request verify code"
                onPress={() => {
                    var settings = new VerifyCodeSettings(VerifyCodeAction.REGISTER_OR_LOGIN);
                    EmailAuthProvider.requestVerifyCode(mEmail, settings)
                        .then(verifyCodeResult => {
                            console.log("request verify code success:");
                        })
                        .catch(error => {
                            console.log(error.code, error.message);
                        });
                }}
            />
            <Separator />

            <Button
                title="create email user"
                onPress={() => {
                    AGCAuth.getInstance().createEmailUser(mEmail, null, mVerifyCode)
                        .then(signInResult => {
                            console.log("signIn success, uid == " + signInResult.user.uid);
                            getUser();
                        })
                        .catch(error => {
                            console.log(error.code, error.message);
                        });
                }}
            />

            <Separator />
            <Button
                title="signIn with verifycode"
                onPress={() => {
                    var credential = EmailAuthProvider.credentialWithVerifyCode(mEmail, null, mVerifyCode);
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

            <Separator />
            <Button
                title="get token"
                onPress={() => {
                    AGCAuth.getInstance().currentUser().then((user) => {
                        if (user) {
                            var agcUser = new AGCUser(user);
                            agcUser.getToken(false).then(tokenResult => {
                                console.log(tokenResult.token);
                            }).catch(err => {
                                console.log(err);
                            });
                        }
                    });
                }}
            />
        </View>
    );
}



