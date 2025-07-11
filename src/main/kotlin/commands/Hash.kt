package commands
import commands.utilities.hashAndSave
import java.io.File
import java.io.IOException

fun hash(path: String, writeTheObject: Boolean, print: Boolean = false): Result<String> {
    val file = File(path)
    val content = file.readText()

    val header = "blob ${content.length}"
    val finalHashInput = header + '\u0000' + content

    return hashAndSave(finalHashInput, writeTheObject)

}