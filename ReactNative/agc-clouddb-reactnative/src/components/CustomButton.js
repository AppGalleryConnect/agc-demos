import React from 'react'
import { StyleSheet, Text, View, TouchableOpacity, } from 'react-native'

const CustomButton = (props) => {
    return (
        <View style={styles.buttonContainer}>
        <TouchableOpacity
          style={styles.button}
          onPress={props.onPress}
        >
          <Text style={styles.buttonText}>{props.title}</Text>
        </TouchableOpacity>
      </View>
    )
}

export default CustomButton

const styles = StyleSheet.create({
    buttonContainer: {
        padding: 10,
      },
      button: {
        height: 50,
        justifyContent: "center",
        alignItems: "center",
        backgroundColor: "#2e2e2e",
        marginBottom: 15,
        elevation: 5,
        borderRadius: 5,
      },
      buttonText: {
        fontSize: 18,
        fontWeight: "bold",
        color: "#909090",
      },
})
