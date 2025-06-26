package commands

import java.nio.file.Path

import java.nio.file.Files


    fun init(beginPath: Path) {
        return try {
            // Create directories
            Files.createDirectories(beginPath.resolve(Constants.OBJECTS_PATH))
            Files.createDirectories(beginPath.resolve(Constants.REFS_PATH))

            // Create and write to HEAD file
            val headPath = beginPath.resolve(Constants.HEAD)
            val headFile = headPath.toFile()

            if (headFile.exists() || headFile.createNewFile()) {
                headFile.writeText("ref: refs/heads/main\n")
            } else {
                System.err.println("Failed to create ${headPath} file")
               // return EXIT_FAILURE
            }

            println("Initialized git directory in ${beginPath.resolve(Constants.OBJECTS_PATH).parent}")

           // EXIT_SUCCESS
        } catch (e: Exception) {
            System.err.println(e.message)
            //EXIT_FAILURE
        }
    }

