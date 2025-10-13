package fr.xgouchet.kardmaker

import com.github.ajalt.clikt.core.main
import fr.xgouchet.kardmaker.cli.KardMakerCommand

fun main(args: Array<String>) {
    KardMakerCommand().main(args)
}