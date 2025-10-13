package fr.xgouchet.kardmaker.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import fr.xgouchet.kardmaker.core.Maker
import fr.xgouchet.kardmaker.core.data.Configuration
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.File

class KardMakerCommand : CliktCommand("km") {

    val verbose by option("-v", "--verbose").flag()
    val folder by option("-f", "--folder").flag()
    val debug by option("-d", "--debug").flag()

    val inputFile: File by argument(help = "path to the configuration json file").file(
        mustExist = true,
        canBeFile = true,
        canBeDir = true
    )


    @OptIn(ExperimentalSerializationApi::class)
    override fun run() {
        if (folder) {
            check(inputFile.isDirectory && inputFile.canRead())
            inputFile.listFiles { file ->
                file.isFile && file.canRead() && file.extension == "json"
            }?.forEach {
                runOnJsonFile(it)
            }
        } else {
            check(inputFile.isFile && inputFile.canRead())
            runOnJsonFile(inputFile)
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun runOnJsonFile(jsonFile: File) {
        if (verbose) {
            println("· Reading config from ${inputFile.absolutePath}")
        }
        val json = Json {
            allowTrailingComma = true
        }
        val configuration = json.decodeFromStream<Configuration>(jsonFile.inputStream())

        Maker(jsonFile.parentFile, verbose, debug).generateCards(configuration)
    }

}