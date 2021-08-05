import React from 'react'
import { StyleSheet, Text, View, Image } from 'react-native'

const Header = () => {
    return (
        <View style={styles.header}>
          <View style={styles.titleContainer}>
            <Text style={styles.headerTitle} >
              AGC React Native Cloud DB Plugin
          </Text>
          </View>
          <Image
            style={styles.headerImage}
            source={require("../assets/agc-rn-logo.png")}
          />
        </View>
    )
}

export default Header

const styles = StyleSheet.create({
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
})
