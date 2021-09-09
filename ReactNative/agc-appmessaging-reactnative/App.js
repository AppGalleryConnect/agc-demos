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

import React from 'react';
import AGCAppMessaging from '@react-native-agconnect/appmessaging';
import {Alert, Image, ScrollView, StyleSheet, Text, View} from 'react-native';
import {Colors} from 'react-native/Libraries/NewAppScreen';

export const isIOS = Platform.OS === 'ios';
export const isAndroid = Platform.OS === 'android';


/**
 * Provides methods to obtain AGCAppMessaging Kit functions both In Android & IOS Platforms.
 */
export default class App extends React.Component {

    constructor(props) {
        super(props)
        this.state = {
            customViews: []
        }
    }

    /**
     * Creating app and listening all the events.
     */
    componentDidMount() {
        //Creating & Calling Event Listeners
        this.addMessageDisplayListener();
        this.addMessageClickListener();
        this.addMessageErrorListener();
        this.addMessageDismissListener();
        this.addMessageCustomViewListener();
    }

    /**
     * While using default app message layout, listen for app message display events.
     */
    addMessageDisplayListener = () => {
        AGCAppMessaging.getInstance().addMessageDisplayListener((result) => Alert.alert(AGCAppMessaging.EventTypes.ON_MESSAGE_DISPLAY + JSON.stringify(result)));
    }

    /**
     * While using default app message layout, listen for app message click events.
     */
    addMessageClickListener = () => {
        AGCAppMessaging.getInstance().addMessageClickListener((result) => Alert.alert(AGCAppMessaging.EventTypes.ON_MESSAGE_CLICK + JSON.stringify(result)));
    }

    /**
     *  While using default app message layout, listen for app message error events.
     */
    addMessageErrorListener = () => {
        AGCAppMessaging.getInstance().addMessageErrorListener((result) => Alert.alert(AGCAppMessaging.EventTypes.ON_MESSAGE_ERROR + JSON.stringify(result)));
    }

    /**
     * While using default app message layout, listen for app message dismiss events.
     */
    addMessageDismissListener = () => {
        AGCAppMessaging.getInstance().addMessageDismissListener((result) => Alert.alert(AGCAppMessaging.EventTypes.ON_MESSAGE_DISMISS + JSON.stringify(result)));
    }

    /**
     * While using custom app message layout, listen for custom app message display events.
     */
    addMessageCustomViewListener = () => {
        AGCAppMessaging.getInstance().addMessageCustomViewListener((result) => {
            Alert.alert(AGCAppMessaging.EventTypes.CUSTOM_VIEW + JSON.stringify(result))
            /*
             * Add Custom Message Event Handler method.
             * When using custom app message layout, handle custom app message click events like below.
             * gets eventType ( AGCAppMessaging.ON_MESSAGE_DISPLAY, AGCAppMessaging.ON_MESSAGE_CLICK, AGCAppMessaging.ON_MESSAGE_DISMISS, AGCAppMessaging.ON_MESSAGE_ERROR)
             * and dismissType param( AGCAppMessaging.DISMISS_TYPE_CLICK, AGCAppMessaging.DISMISS_TYPE_CLICK_OUTSIDE, AGCAppMessaging.DISMISS_TYPE_AUTO, AGCAppMessaging.DISMISS_TYPE_SWIPE) when using AGCAppMessaging.ON_MESSAGE_DISMISS.
             */
            this.handleCustomViewMessageEvent();
        });
    }

    /**
     * Data synchronization from the AppGallery Connect server.
     */
    setFetchMessageEnable() {
        AGCAppMessaging.getInstance().setFetchMessageEnable(true).then(result => {
            Alert.alert("[setFetchMessageEnable] " + JSON.stringify(result));
            this.createCustomView("setFetchMessageEnable :  ", JSON.stringify(result) + "")
        }).catch((err) => {
            Alert.alert("[setFetchMessageEnable] Error/Exception: " + JSON.stringify(err));
            this.createCustomView("[setFetchMessageEnable] Error/Exception: ", JSON.stringify(err) + "")
        });
    }

    /**
     * Sets displayEnable in AGCAppMessaging.
     */
    setDisplayEnable() {
        AGCAppMessaging.getInstance().setDisplayEnable(false).then(result => {
            Alert.alert("[setDisplayEnable] " + JSON.stringify(result));
            this.createCustomView("setDisplayEnable :  ", JSON.stringify(result) + "")
        }).catch((err) => {
            Alert.alert("[setDisplayEnable] Error/Exception: " + JSON.stringify(err));
            this.createCustomView("[setDisplayEnable] Error/Exception: ", JSON.stringify(err) + "")
        });
    }

    /**
     * Returns the result of isDisplayEnable.
     */
    isDisplayEnable() {
        AGCAppMessaging.getInstance().isDisplayEnable().then(result => {
            Alert.alert("[isDisplayEnable] " + JSON.stringify(result));
            this.createCustomView("isDisplayEnable :  ", JSON.stringify(result) + "")
        }).catch((err) => {
            Alert.alert("[isDisplayEnable] Error/Exception: " + JSON.stringify(err));
            this.createCustomView("[isDisplayEnable] Error/Exception: ", JSON.stringify(err) + "")
        });
    }

    /**
     * Sets whether to allow data synchronization from the AppGallery Connect server.
     */
    isFetchMessageEnable() {
        AGCAppMessaging.getInstance().isFetchMessageEnable().then(result => {
            Alert.alert("[isFetchMessageEnable] " + JSON.stringify(result));
            this.createCustomView("isFetchMessageEnable :  ", JSON.stringify(result) + "")
        }).catch((err) => {
            Alert.alert("[isFetchMessageEnable] Error/Exception: " + JSON.stringify(err));
            this.createCustomView("[isFetchMessageEnable] Error/Exception: ", JSON.stringify(err) + "")
        });
    }

    /**
     * Sets force fetch to data synchronization from the AppGallery Connect server.
     */
    setForceFetch() {
        AGCAppMessaging.getInstance().setForceFetch().then(result => {
            Alert.alert("[setForceFetch] " + JSON.stringify(result));
            this.createCustomView("setForceFetch :  ", JSON.stringify(result) + "")
        }).catch((err) => {
            Alert.alert("[setForceFetch] Error/Exception: " + JSON.stringify(err));
            this.createCustomView("[setForceFetch] Error/Exception: ", JSON.stringify(err) + "")
        });
    }

    /**
     * Sets display location of appMessage whether at the bottom or the center.
     */
    setDisplayLocation() {
        const bottomType = AGCAppMessaging.LocationTypes.LOCATION_TYPE_CENTER;
        AGCAppMessaging.getInstance().setDisplayLocation(bottomType).then(result => {
            Alert.alert("[setDisplayLocation] " + JSON.stringify(result));
            this.createCustomView("setDisplayLocation :  ", JSON.stringify(result) + "")
        }).catch((err) => {
            Alert.alert("[setDisplayLocation] Error/Exception: " + JSON.stringify(err));
            this.createCustomView("[setDisplayLocation] Error/Exception: ", JSON.stringify(err) + "")
        });
    }

    /**
     * Removes Custom View.
     */
    removeCustomView() {
        AGCAppMessaging.getInstance().removeCustomView().then(result => {
            Alert.alert("[removeCustomView] " + JSON.stringify(result));
            this.createCustomView("removeCustomView :  ", JSON.stringify(result) + "")
        }).catch((err) => {
            Alert.alert("[removeCustomView] Error/Exception: " + JSON.stringify(err));
            this.createCustomView("[removeCustomView] Error/Exception: ", JSON.stringify(err) + "")
        });
    }

    /**
     * Triggers message display.
     */
    trigger() {
        AGCAppMessaging.getInstance().trigger("SOME_EVENT_ID").then(result => {
            Alert.alert("[trigger] " + JSON.stringify(result));
            this.createCustomView("trigger :  ", JSON.stringify(result) + "")
        }).catch((err) => {
            Alert.alert("[trigger] Error/Exception: " + JSON.stringify(err));
            this.createCustomView("[trigger] Error/Exception: ", JSON.stringify(err) + "")
        });
    }

    /**
     * When using custom app message layout, handle custom app message click events like below.
     * gets eventType ( AGCAppMessaging.ON_MESSAGE_DISPLAY, AGCAppMessaging.ON_MESSAGE_CLICK, AGCAppMessaging.ON_MESSAGE_DISMISS, AGCAppMessaging.ON_MESSAGE_ERROR)
     * and dismissType param( AGCAppMessaging.DISMISS_TYPE_CLICK, AGCAppMessaging.DISMISS_TYPE_CLICK_OUTSIDE, AGCAppMessaging.DISMISS_TYPE_AUTO, AGCAppMessaging.DISMISS_TYPE_SWIPE) when using AGCAppMessaging.ON_MESSAGE_DISMISS.
     */
    handleCustomViewMessageEvent() {
        const customMessageDisplayReq = {
            "eventType" : AGCAppMessaging.EventTypes.ON_MESSAGE_DISPLAY
        }

        const customMessageClickReq = {
            "eventType" : AGCAppMessaging.EventTypes.ON_MESSAGE_CLICK
        }

        const customMessageDismissReq = {
            "eventType" : AGCAppMessaging.EventTypes.ON_MESSAGE_DISMISS,
            "dismissType": AGCAppMessaging.DismissTypes.DISMISS_TYPE_CLICK
        }

        const customMessageErrorReq = {
            "eventType" : AGCAppMessaging.EventTypes.ON_MESSAGE_ERROR
        }

        AGCAppMessaging.getInstance().handleCustomViewMessageEvent(customMessageDisplayReq).then(result => {
            Alert.alert("[handleCustomViewMessageEvent] " + JSON.stringify(result));
            this.createCustomView("handleCustomViewMessageEvent :  ", JSON.stringify(result) + "")
        }).catch((err) => {
            Alert.alert("[handleCustomViewMessageEvent] Error/Exception: " + JSON.stringify(err));
            this.createCustomView("[handleCustomViewMessageEvent] Error/Exception: ", JSON.stringify(err) + "")
        });
    }

    /**
     * Shows results from the AppMessaging kit functions in the bottom of the screen.
     * @param title: Refers to title of the result.
     * @param description: Refers to description of the result.
     */
    createCustomView(title, description) {
        var view = (
            <View key={title + description} style={styles.resultView}>
                <Text style={[styles.txt, {width: 110, textAlign: "left"}]}>{title}</Text>
                <Text style={[styles.txt, {
                    color: Colors.dark,
                    textAlign: 'left',
                    width: 186,
                    marginLeft: 5
                }]}>{description}</Text>
            </View>
        )
        var views = []
        views.push(view)
        this.setState({customViews: views})
    }

    render() {
        return (
            <>
                <View style={styles.header}>
                    <Text style={styles.title}>AGC AppMessaging RN</Text>
                    <Image
                        resizeMode="contain"
                        style={styles.logo}
                        source={require('./assets/images/logo.jpg')}/>
                </View>
                <ScrollView
                    style={styles.scrollView}>
                    <View style={styles.body}>
                        <View style={styles.sectionContainer}>
                            <Text style={styles.sectionTitle}>
                                RN AGC
                                App Messaging
                            </Text>
                            <Text style={styles.sectionDescription}>
                                You can use App Messaging of AppGallery Connect to send relevant messages to target
                                users actively using your app to encourage them to use key app functions.
                                For example, you can send in-app messages to encourage users to subscribe to certain
                                products, provide tips on passing a game level, or recommend activities of a restaurant.
                            </Text>
                            <Text
                                style={styles.button}
                                onPress={() =>
                                    this.setFetchMessageEnable()
                                }>
                                setFetchMessageEnable
                            </Text>
                            <Text
                                style={styles.button}
                                onPress={() =>
                                    this.setDisplayEnable()
                                }>
                                setDisplayEnable
                            </Text>

                            <Text
                                style={styles.button}
                                onPress={() =>
                                    this.isDisplayEnable()
                                }>
                                isDisplayEnable
                            </Text>

                            <Text
                                style={styles.button}
                                onPress={() =>
                                    this.isFetchMessageEnable()
                                }>
                                isFetchMessageEnable
                            </Text>

                            <Text
                                style={styles.button}
                                onPress={() =>
                                    this.setForceFetch()
                                }>
                                setForceFetch
                            </Text>

                            <Text
                                style={styles.button}
                                onPress={() =>
                                    this.setDisplayLocation()
                                }>
                                setDisplayLocation
                            </Text>

                            <Text
                                style={styles.button}
                                onPress={() =>
                                    this.removeCustomView()
                                }>
                                removeCustomView
                            </Text>

                            <Text
                                style={styles.button}
                                onPress={() =>
                                    this.trigger()
                                }>
                                trigger
                            </Text>

                        </View>
                        <Text style={[styles.title]}> Results </Text>
                        {this.state.customViews}

                        <View style={{height: 70, width: '100%'}}/>

                    </View>
                </ScrollView>
            </>
        )
    }

}
const styles = StyleSheet.create({
    scrollView: {
        backgroundColor: Colors.lighter,
    },
    header: {
        height: 140,
        width: '100%',
        backgroundColor: '#222222',
        flexDirection: 'row',
        paddingLeft: 20,
        alignItems: 'center'
    },
    title: {
        fontSize: 20,
        fontWeight: 'bold',
        color: Colors.black,
        margin: 20,
        marginTop: 50,
        textAlign: 'center',
        width: '40%',
    },
    button: {
        fontSize: 20,
        color: Colors.white,
        margin: 20,
        textAlign: 'center',
        marginTop: 20,
        backgroundColor: '#0b1528',
        borderRadius: 10,
        marginLeft: 5,
        marginRight: 5
    },
    logo: {
        height: '100%',
        width: '60%'
    },
    txt: {
        fontSize: 14,
        color: '#00ffad',
        textAlign: 'center'
    },
    resultView: {
        flexDirection: 'row',
        width: 250,
        alignSelf: 'center',
        marginLeft: 5,
        marginRight: 5,
        marginTop: 20
    },
    engine: {
        position: 'absolute',
        right: 0
    },
    body: {
        backgroundColor: Colors.white,
    },
    sectionContainer: {
        marginTop: 32,
        paddingHorizontal: 24,
    },
    sectionTitle: {
        fontSize: 24,
        fontWeight: '600',
        color: Colors.black,
    },
    sectionDescription: {
        marginTop: 8,
        fontSize: 18,
        fontWeight: '400',
        color: Colors.dark,
    },
    highlight: {
        fontWeight: '700',
    },
    footer: {
        color: Colors.dark,
        fontSize: 12,
        fontWeight: '600',
        padding: 4,
        paddingRight: 12,
        textAlign: 'right',
    },
});



