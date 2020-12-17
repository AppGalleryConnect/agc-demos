import * as React from 'react';
import { View, Text, Button } from 'react-native';
import { Separator, Styles } from './separator';
import AGCAuth, {AGCUser,SelfBuildProvider } from '@react-native-agconnect/auth';

const token = "jwt_token";
export default function SelfBuildScreen() {

    const [current, setCurrent] = React.useState("no user login");
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
                title="get user"
                onPress={() => {
                    AGCAuth.getInstance().currentUser().then((user) => {
                        console.log(user);
                    });
                }}
            />

            <Separator />
            <Button
                title="signIn"
                onPress={() => {
                    var credential = SelfBuildProvider.credential(token);
                    console.log(credential.token);
                    AGCAuth.getInstance().signIn(credential).then((signInResult) => {
                        getUser();
                    }).catch(err => {
                        console.log(err.code + "  message=" + err.message);
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



