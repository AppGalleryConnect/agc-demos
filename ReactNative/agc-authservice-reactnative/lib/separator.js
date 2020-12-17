import * as React from 'react';
import { View, StyleSheet } from 'react-native';

export const Separator = () => {
    return <View style={Styles.separator} />;
}

export const Styles = StyleSheet.create({
    separator: {
        marginVertical: 8,
        borderBottomColor: '#737373',
        borderBottomWidth: StyleSheet.hairlineWidth,
    },
    sectionContainer: {
        marginTop: 32,
        paddingHorizontal: 24,
    },
});
