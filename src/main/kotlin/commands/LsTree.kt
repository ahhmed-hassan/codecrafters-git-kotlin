package commands

import java.io.File
import java.io.InputStream
import java.util.zip.InflaterInputStream
import commands.utilities.*

fun parseTrees(treeHash: String): List<Tree> {
    val file = (Constants.OBJECTS_PATH / treeHash.take(2) / treeHash.drop(2)).toFile()



    if (!file.exists()) {
        throw IllegalArgumentException("Cannot open the file: ${file.path}")
    }

    val rawBlob = InflaterInputStream(file.inputStream()).use { inflater ->
        inflater.readBytes()
    }

    val blob = rawBlob
    val entries = mutableListOf<Tree>()

    var pos = blob.indexOf(0).takeIf { it >= 0 }?.plus(1) ?: return emptyList()

    while (pos < blob.size) {
        // Find mode (ends at space)
        val space = blob.indexOf(' '.code.toByte(), startIndex = pos)
        if (space == -1) break
        val mode = blob.decodeToString(pos, space)

        // Find null terminator after name
        val nul = blob.indexOf(0, startIndex = space + 1)
        if (nul == -1) break
        val name = blob.decodeToString(space + 1, nul)

        pos = nul + 1

        if (pos + Constants.SHA1Size > blob.size) break
        val sha = blob.copyOfRange(pos, pos + Constants.SHA1Size)
        pos += Constants.SHA1Size

        entries += Tree(mode, name, sha)
    }

    return entries
}

fun  LsTree(arg : String,  namesOnly : Boolean ) : Int{
    val entries = parseTrees(arg)
    val treePrinter: (Tree) -> String = { t ->
        "${t.perm} ${t.name} ${t.shaHash.joinToString("") { b -> "%02x".format(b) }}"
    }

    val projection: (Tree) -> String = if (namesOnly) {
        { it.name }
    } else {
        treePrinter
    }

    entries
        .map(projection)
        .forEach { println(it) }
    return 0
}
