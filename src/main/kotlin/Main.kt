import java.io.File
import kotlin.system.exitProcess
import commands.init
import java.nio.file.Paths
import kotlin.io.path.absolute

const val DEBUG = true

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
    } else {
        println("Unknown command: ${args[0]}")
        exitProcess(1)
    }
}
