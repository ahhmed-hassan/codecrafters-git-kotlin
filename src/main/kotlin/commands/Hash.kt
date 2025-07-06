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
//     val x =  runCatching {
//        val content = file.readText()
//
//        val header = "blob ${content.length}"
//        val finalHashInput = header + '\u0000' + content
//
//        hashAndSave(finalHashInput, writeTheObject)
////            .also { result ->
////            if (print) {
////                result
////                    .onSuccess { println(it) }
////                    .onFailure { System.err.println(it.message) }
////            }
////        }
//    }
//     catch (e: IOException) {
//        //if (print) System.err.println("Cannot open file: ${e.message}")
//        Result.failure(Exception("File read error: ${e.message}"))
//    } catch (e: OutOfMemoryError) {
//        //if (print) System.err.println(e.message)
//        Result.failure(Exception("Memory error: ${e.message}"))
//    }
}