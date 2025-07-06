
import commands.LsTree
import commands.cat
import commands.hash
import commands.init
import kotlin.system.exitProcess
import java.nio.file.Paths


const val DEBUG = false
fun <T> convertResult(result: Result<T>, print : Boolean ) : Int{
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

        return init(Paths.get("").toAbsolutePath())
    }
    else if(realArgs[0] == "cat-file"){
        exitProcess(convertResult( cat(realArgs[1], realArgs[2]), print = true))
    }
    else if(realArgs[0] == "hash-object"){
        val file = realArgs.last()
        val writeOption: Boolean = realArgs.contains("-w")
        val res = hash(file,  writeTheObject = writeOption, print = false)
        exitProcess(convertResult(res, print = true))
    }
    else if(realArgs[0] == "ls-tree"){
        val hash = realArgs.last()
        val namesOnly = realArgs.contains("--name-only")
        exitProcess(LsTree(hash, namesOnly))

    }
    else {
        println("Unknown command: ${realArgs[0]}")
        exitProcess(1)
    }
}
