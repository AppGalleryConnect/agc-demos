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
import AgcAppLinking from '@react-native-agconnect/applinking';
import {Alert, Clipboard, Image, Linking, ScrollView, StyleSheet, Text, View} from 'react-native';
import {Colors} from 'react-native/Libraries/NewAppScreen';


/**
 * Provides methods to obtain AgcAppLinking Kit functions both In Android & IOS Platforms.
 */
export default class App extends React.Component {

    constructor(props) {
        super(props)
        this.state = {
            customViews: []
        }
    }

    componentDidMount() {
        this.getAGConnectAppLinkingResolvedLinkData()
    }

    /**
     * Asynchronously generates a short link with a string-type suffix. You can specify the suffix as a long or short one.
     */
    buildShortAppLinking() {
        /** socialCardInfo object is optional. **/
        const socialCardInfo = {
            "description": "SOME_DESCRIPTION",
            "title": "SOME_TITLE",
            "imageUrl": "https://www-file.huawei.com/-/media/corporate/images/home/logo/huawei_logo.png"
        }

        /** campaignInfo object is optional. **/
        const campaignInfo = {
            "medium": "MEDIUM",
            "name": "NAME",
            "source": "SOURCE"
        }

        /** androidLinkInfo object is optional. **/
        const androidLinkInfo = {
            "packageName": "com.huawei.agc.rn.applinking.example",
            "androidDeepLink": "https://huawei.com/",
            "openType": AgcAppLinking.AppLinkingAndroidLinkInfoAndroidOpenTypeConstants.APP_GALLERY
        }
        /** IOSLinkInfo object is optional. **/
        const IOSLinkInfo = {
            "iosBundleId": "com.huawei.agc.rn.applinking.example",
            "iosDeepLink": "huawei.com://exampleID=107"
        }

        /** ITunesLinkInfo object is optional. **/
        const ITunesLinkInfo = {
            "iTunesConnectMediaType": "iTunesConnectMediaType",
            "iTunesConnectAffiliateToken": "iTunesConnectAffiliateToken"
        }

        /** longLink object is optional **/
        const longLink = "LONG_LINK_EXAMPLE"

        /**
         * In building short link, domainUriPrefix & deepLink fields are mandatory, other params are optional.
         * In building long link, only longLink param is mandatory.
         **/
        const object = {
            "shortAppLinkingLength": AgcAppLinking.ShortAppLinkingLengthConstants.LONG,
            "domainUriPrefix": "DOMAIN_URL_PREFIX_EXAMPLE",//Add your url prefix here.
            "deepLink": "huawei.com://exampleID=107",
            "longLink": longLink,//Add to set a long link.
            "socialCardInfo": socialCardInfo,
            "campaignInfo": campaignInfo,
            "androidLinkInfo": androidLinkInfo,
            "IOSLinkInfo": IOSLinkInfo,
            "ITunesLinkInfo": ITunesLinkInfo,
            "previewType": AgcAppLinking.AppLinkingLinkingPreviewTypeConstants.APP_INFO
        }

        AgcAppLinking.buildShortAppLinking(object).then(result => {
            Alert.alert(
                "[buildShortAppLinking] ",
                JSON.stringify(result),
                [
                    {
                        text: "Copy Short Link", onPress: () => {
                            Clipboard.setString(result.shortLink)
                        }
                    }
                ],
                {cancelable: true}
            );
            this.createCustomView("buildShortAppLinking :  ", JSON.stringify(result.shortLink) + "")
        }).catch((err) => {
            Alert.alert("[buildShortAppLinking] Error/Exception: " + JSON.stringify(err));
            this.createCustomView("[buildShortAppLinking] Error/Exception: ", JSON.stringify(err) + "")
        });
    }

    /**
     * Generates a long link Uri.
     */
    buildLongAppLinking() {
        const object = {
            "shortAppLinkingLength": AgcAppLinking.ShortAppLinkingLengthConstants.SHORT,
            "domainUriPrefix": "DOMAIN_URL_PREFIX_EXAMPLE",//Add your url prefix here.
            "deepLink": "DEEP_LINK_EXAMPLE"
        }
        AgcAppLinking.buildLongAppLinking(object).then(result => {
            Alert.alert("[buildLongAppLinking] " + JSON.stringify(result));
            this.createCustomView("buildLongAppLinking :  ", JSON.stringify(result) + "")
        }).catch((err) => {
            Alert.alert("[buildLongAppLinking] Error/Exception: " + JSON.stringify(err));
            this.createCustomView("[buildLongAppLinking] Error/Exception: ", JSON.stringify(err) + "")
        });
    }

    /**
     * Fetches ResolvedLinkData instance with deepLink, clickTimeStamp, socialTitle, socialDescription, socialImageUrl, campaignName, campaignMedium, campaignSource and minimumAppVersion params.
     */
    getAGConnectAppLinkingResolvedLinkData() {
        AgcAppLinking.getAGConnectAppLinkingResolvedLinkData().then(result => {
            Alert.alert("[getAGConnectAppLinkingResolvedLinkData] " + JSON.stringify(result));
            this.createCustomView("getAGConnectAppLinkingResolvedLinkData :  ", JSON.stringify(result) + "")
        }).catch((err) => {
            Alert.alert("[getAGConnectAppLinkingResolvedLinkData] Error/Exception: " + JSON.stringify(err));
            this.createCustomView("[getAGConnectAppLinkingResolvedLinkData] Error/Exception: ", JSON.stringify(err) + "")
        });
    }

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
                    <Text style={styles.title}>AGC AppLinking RN</Text>
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
                                Creating a Link in AppGallery Connect
                            </Text>
                            <Text style={styles.sectionDescription}>
                                You can create links of App Linking in AppGallery Connect to send app promotion
                                information to users, for example, to send information about product promotion
                                activities or app promotion activities through SMS messages, emails, or social
                                platforms.
                            </Text>
                            <Text
                                style={styles.button}
                                onPress={() =>
                                    Linking.openURL('YOUR_LINK_CREATED_BY_AGCONNECT_CONSOLE')
                                }>
                                Open Link via Browser
                            </Text>
                            <Text
                                style={styles.button}
                                onPress={() =>
                                    Linking.openURL('YOUR_LINK_CREATED_BY_AGCONNECT_CONSOLE')
                                }>
                                Open Link via App
                            </Text>
                        </View>
                        <View style={styles.sectionContainer}>
                            <Text style={styles.sectionTitle}>
                                Creating a Link of App Linking in Your App
                            </Text>
                            <Text style={styles.sectionDescription}>
                                If you want your app to dynamically create links for users to share, you can integrate
                                the App Linking SDK into your app so that your app can use APIs provided by the SDK to
                                create long or short links.
                            </Text>
                            <Text
                                style={styles.button}
                                onPress={() => {
                                    this.buildShortAppLinking()
                                }
                                }>
                                buildShortAppLinking
                            </Text>
                            <Text
                                style={styles.button}
                                onPress={() => {
                                    this.buildLongAppLinking()
                                }
                                }>
                                buildLongAppLinking
                            </Text>

                            <Text
                                style={styles.button}
                                onPress={() => {
                                    this.getAGConnectAppLinkingResolvedLinkData()
                                }
                                }>
                                getAGConnectAppLinkingResolvedLinkData
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
