package commands

import java.nio.file.Path
import java.nio.file.Paths

operator fun Path.div(other: String) : Path = this.resolve(other)
object Constants
{
    val GIT_DIR: Path = Paths.get(".git")
    val OBJECTS_PATH : Path = GIT_DIR /"objects"
    val REFS_PATH: Path = GIT_DIR.resolve("refs")
    val HEAD: Path = GIT_DIR.resolve("HEAD")
    val SHA1Size : Int = 20
}