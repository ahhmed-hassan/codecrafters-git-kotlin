package commands.utilities


class ExampleTest {
fun allTests(){
    testHexConversion()
    testEncodingAndDecoding()
}
    fun testHexConversion() {
        // Test case: Valid hexadecimal string
        val hexString = "c48f2d21b6ca2f0f4d30fc6efd505ead6be7f107"
        val expectedBytes = byteArrayOf(
            0xc4.toByte(), 0x8f.toByte(), 0x2d.toByte(), 0x21.toByte(),
            0xb6.toByte(), 0xca.toByte(), 0x2f.toByte(), 0x0f.toByte(),
            0x4d.toByte(), 0x30.toByte(), 0xfc.toByte(), 0x6e.toByte(),
            0xfd.toByte(), 0x50.toByte(), 0x5e.toByte(), 0xad.toByte(),
            0x6b.toByte(), 0xe7.toByte(), 0xf1.toByte(), 0x07.toByte()
        )

        // Convert hex string to ByteArray
        val byteArray = hexToByteArray(hexString)

        // Verify conversion from hex to ByteArray
        assert(expectedBytes.contentToString()==byteArray.contentToString())

        // Convert ByteArray back to hex string
        val convertedBackHex = byteArray.toHex()

        // Verify round-trip conversion (hex → ByteArray → hex)
        assert(hexString == convertedBackHex)

        println("testHexConversion passed!")
    }

    fun testEncodingAndDecoding() {
        // Test case: Raw binary data as a ByteArray
        val originalBytes = byteArrayOf(
            -60, -113, -45, -33, -40, -12, -78, -55,
            -99, -86, -120, -122, -77, -110, -101,
            -76, -104, 120
        )

        // Serialize bytes into a string using ISO_8859_1 encoding
        val serializedString = originalBytes.concatToString()

        // Deserialize the string back into a ByteArray using ISO_8859_1 encoding
        val deserializedBytes = serializedString.toByteArray(Charsets.ISO_8859_1)

        // Verify that the deserialized bytes match the original bytes
        assert(originalBytes.contentToString() ==  deserializedBytes.contentToString())

        println("testEncodingAndDecoding passed!")
    }
}
