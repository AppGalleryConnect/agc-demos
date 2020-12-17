import * as React from 'react';
import { View, Button } from 'react-native';
import { Separator, Styles } from './separator';

export default function HomeScreen({ navigation }) {
    return (
        <View style={Styles.sectionContainer}>
            <Button
                title="Anonymously"
                onPress={() => navigation.navigate('Anonymously')}
            />
            <Separator />
            <Button
                title="Email"
                onPress={() => navigation.navigate('Email')}
            />

            <Separator />
            <Button
                title="Phone"
                onPress={() => navigation.navigate('Phone')}
            />

            <Separator />
            <Button
                title="SelfBuild"
                onPress={() => navigation.navigate('SelfBuild')}
            />
        </View>
    );
}



