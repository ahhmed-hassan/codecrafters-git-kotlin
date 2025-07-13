package commands.utilities

import commands.Constants
import java.io.ByteArrayOutputStream
import java.util.zip.DeflaterOutputStream
import java.io.File
import java.nio.file.Files
import java.security.MessageDigest

fun sha1Hash(input: String): String {
    val digest = MessageDigest.getInstance("SHA-1")
    val hashBytes = digest.digest(input.toByteArray())
    return hashBytes.joinToString("") { "%02x".format(it) }
}


fun zlibCompress(input: String): ByteArray {
    val bos = ByteArrayOutputStream()
    DeflaterOutputStream(bos).use { it.write(input.toByteArray(Charsets.ISO_8859_1)) }
    return bos.toByteArray()
}



fun hashAndSave(toHash: String, save: Boolean = true): Result<String> {
    val objectHash = sha1Hash(toHash)
    val objectDir = Constants.OBJECTS_PATH.resolve(objectHash.take(2)).toFile()
    val filePath = File(objectDir, objectHash.drop(2))

    return try {
        objectDir.mkdirs()
        val compressed = zlibCompress(toHash)

        if (save) {
            filePath.outputStream().use { it.write(compressed) }
        }

        Result.success(objectHash)
    } catch (e: Exception) {
        Result.failure(Exception("Failed to hash and save object: ${e.message}"))
    }
}
fun hexToByteArray(hex : String) : ByteArray{
    val cleanHex = if(hex.startsWith("0x") || hex.startsWith("0X")) hex.substring(2)
    else hex
    require(cleanHex.length %2 == 0) {"Hex string must have even length"}

    return ByteArray(cleanHex.length /2 ) { i ->
        val index = i*2
        val byteHex = cleanHex.substring(index, index+2)
        byteHex.toInt(16).toByte()
    }
}

data class Tree(

    val perm: String,
    val name: String,
    val shaHash: ByteArray // same as raw 20-byte SHA-1
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Tree

        if (perm != other.perm) return false
        if (name != other.name) return false
        if (!shaHash.contentEquals(other.shaHash)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = perm.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + shaHash.contentHashCode()
        return result
    }
    constructor(de : File, hash: ByteArray ) : this(
        getMode(de),
        de.name,
        hash
    )


    companion object {
        private fun getMode(file:File ) : String {
            return when{
                file.isDirectory -> Constants.GitTreeConstants.DIRECTORY
                file.isFile -> Constants.GitTreeConstants.REGULAR_FILE
                Files.isSymbolicLink(file.toPath()) -> Constants.GitTreeConstants.SYMBOL_LINK
                else -> Constants.GitTreeConstants.EXECUTABLE_FILE

            }
        }
    }
}
fun ByteArray.decodeToString(start: Int, end: Int): String =
    this.copyOfRange(start, end).toString(Charsets.UTF_8)

fun ByteArray.indexOf(value: Byte, startIndex: Int = 0): Int {
    for (i in startIndex until this.size) {
        if (this[i] == value) return i
    }
    return -1
}
fun ByteArray.concatToString(): String =
    String(this, Charsets.ISO_8859_1) // Preserves 1:1 byte mapping

fun ByteArray.toHex(): String = joinToString("") { "%02x".format(it) }

