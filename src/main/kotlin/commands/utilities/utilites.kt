package commands.utilities

import commands.Constants
import java.io.ByteArrayOutputStream
import java.util.zip.DeflaterOutputStream
import java.io.File
import java.security.MessageDigest

fun sha1Hash(input: String): String {
    val digest = MessageDigest.getInstance("SHA-1")
    val hashBytes = digest.digest(input.toByteArray())
    return hashBytes.joinToString("") { "%02x".format(it) }
}


fun zlibCompress(input: String): ByteArray {
    val bos = ByteArrayOutputStream()
    DeflaterOutputStream(bos).use { it.write(input.toByteArray()) }
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


