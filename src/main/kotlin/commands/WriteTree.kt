package commands

import commands.utilities.Tree
import commands.utilities.concatToString
import commands.utilities.hashAndSave
import commands.utilities.hexToByteArray
import java.io.File
import java.nio.file.FileSystemException

fun writeTreeAndGetHash(pathToTree: File) : Result<String>{

    //Base case
    if((pathToTree.listFiles()?.isEmpty() == true) && pathToTree.isDirectory()) {
        return Result.success("")
    }
    val entries = pathToTree
        .listFiles()

        ?.filter {
            //it.name!= ".git" &&(!it.isDirectory || it.listFiles()?.isNotEmpty() == true)
            !it.isDirectory ||
                    (it.isDirectory &&it.listFiles()?.isNotEmpty() == true  && it.name!=".git")
        }
        ?.sortedBy { it.name }
        ?: return Result.failure(FileSystemException("Cnnot list files in ${pathToTree.name}"))
    data class HashAndEntry (val hash: ByteArray, val file:File)

    val entryResults : List<Result<HashAndEntry>> = entries.map { file ->
        val shaResult = if(file.isDirectory) {writeTreeAndGetHash(file)}
        else {hash(file.path, writeTheObject = true, print = false)}
        shaResult.map {  HashAndEntry(hexToByteArray(it), file) }
    }
    entryResults.firstOrNull { it.isFailure }?.let {
        return Result.failure(it.exceptionOrNull()?: Exception("Unknown Error")) }

    entryResults.mapNotNull { result: Result<HashAndEntry> -> result.getOrNull() }
        .filter { it.file.isDirectory }.forEach { System.err.println("Directory ${it.file.name}") }

    val trees : List<Tree> = entryResults.mapNotNull { result ->
        result.getOrNull()?.let { (hash, file) ->

            Tree(file, hash)
        }
    }

    val charset = Charsets.ISO_8859_1
    val contentBytes = trees.fold(ByteArray(0)){acc, tree ->
        val header = "${tree.perm} ${tree.name}".toByteArray(charset)
        acc + header + 0 + tree.shaHash
    }
    val contentByteArray = trees.flatMap {
        it.perm.toByteArray().asIterable() +
                listOf(' '.code.toByte()) +
                it.name.toByteArray().asIterable() +
                listOf(0.toByte()) +
                it.shaHash.toList()
    }.toByteArray()
    val treeContent = trees.joinToString(separator = "") {
      "${it.perm} ${it.name}\u0000${it.shaHash.concatToString()}"
    }

    //val fullTreeContent = "tree ${treeContent.length}\u0000${treeContent}"
    val fullTreeContent = "tree ${contentBytes.size}\u0000".toByteArray() + contentBytes

    return hashAndSave(fullTreeContent.concatToString(), save = true)
}

