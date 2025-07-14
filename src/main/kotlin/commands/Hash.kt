package commands
import commands.utilities.hashAndSave
import commands.utilities.hashAndSaveBytes
import java.io.File
import java.io.IOException

fun hash(path: String, writeTheObject: Boolean, print: Boolean = false): Result<String> {
    val file = File(path)
    val content = file.readText()

    val header = "blob ${content.length}"
    val finalHashInput = header + '\u0000' + content

    return hashAndSave(finalHashInput, writeTheObject)

}

fun hashWithBytes(path: String, writeTheObject: Boolean, print: Boolean): Result<String> {
    val file = File(path)
    val content = file.readBytes()
    val header = "blob ${content.size}\u0000".toByteArray(Charsets.ISO_8859_1)
    val fullBytes = header + content
    return hashAndSaveBytes(fullBytes, save = writeTheObject)
}