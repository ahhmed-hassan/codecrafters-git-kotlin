package commands

import java.io.File
import java.io.FileNotFoundException
import java.util.zip.InflaterInputStream

fun cat(option: String, hash: String): Result<String> {
    if (option != "-p") {
        return Result.failure(IllegalArgumentException("Unknown command!"))
    }
    val file = Constants.OBJECTS_PATH
        .resolve(hash.take(2))
        .resolve(hash.drop(2)).toFile()

    if (!file.exists()) {
        return Result.failure(FileNotFoundException("File not found: ${file.path}"))
    }

    return runCatching {
            InflaterInputStream(file.inputStream()).use { inflater ->
            val decompressed = inflater.reader().readText()
            val content = decompressed.substringAfter('\u0000', missingDelimiterValue = "")
            content.takeUnless {it.isEmpty()}?:
            throw IllegalStateException("Null charachter not found in the object content")
        }

    }
}