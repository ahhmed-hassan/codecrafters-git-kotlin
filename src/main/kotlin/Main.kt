
import kotlin.system.exitProcess
import java.nio.file.Paths


const val DEBUG = false
fun <T> convert_result(result: Result<T> , print : Boolean ) : Int{
    return result.fold(
        onFailure = {
            println(it.message ?: "Unknown error")
            1
        },
        onSuccess = {
            if(print) print(it)
            0
        }
    )
}

fun main(args: Array<String>) {
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    val realArgs: List<String> = if (DEBUG) {
        println("Enter arguments separated by spaces:")
        val input = readlnOrNull() ?: ""
        // Split by whitespace, filter empty strings
        input.split("\\s+".toRegex()).filter { it.isNotEmpty() }
    } else {
        args.toList()
    }

    System.err.println("Logs from your program will appear here!")

    if (realArgs.isEmpty()) {
        println("Usage: your_program.sh <command> [<args>]")
        exitProcess(1)
    }

    if (realArgs[0] == "init") {

        return commands.init(Paths.get("").toAbsolutePath())
    }
    else if(realArgs[0] == "cat-file"){
        exitProcess(convert_result( commands.cat(realArgs[1], realArgs[2]), print = true))
    }
    else {
        println("Unknown command: ${realArgs[0]}")
        exitProcess(1)
    }
}
