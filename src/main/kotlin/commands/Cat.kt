package commands

import java.io.File
import java.io.FileNotFoundException
import java.util.zip.InflaterInputStream

fun cat(option: String, hash: String): Result<String> {
    if (option != "-p") {
        return Result.failure(IllegalArgumentException("Unknown command!"))
    }

    val objectsDir = File(Constants.OBJECTS_PATH.toString())
    val dir = File(objectsDir, hash.take(2))
    val file = File(dir, hash.drop(2))

    if (!file.exists()) {
        return Result.failure(FileNotFoundException("File not found: ${file.path}"))
    }

    return runCatching {
        val inflater = InflaterInputStream(file.inputStream())
        val decompressed = inflater.reader().readText()
        inflater.close()

        val content = decompressed.substringAfter('\u0000', missingDelimiterValue = "")
        if (content.isEmpty()) {
            throw IllegalStateException("Null character not found in the object content")
        }
        content
    }
}