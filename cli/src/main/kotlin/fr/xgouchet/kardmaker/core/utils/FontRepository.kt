package fr.xgouchet.kardmaker.core.utils

import java.awt.Font
import java.io.File

object FontRepository {

    private val fontMap: MutableMap<String, Font> = mutableMapOf()

    fun getFont(inputDir: File, fontPath: String): Font {
        val fontFile = File(inputDir, fontPath)
        val memoFont = fontMap[fontFile.absolutePath]

        if (memoFont != null) return memoFont

        println("  · Loading font from $fontFile")
        // TODO discriminate font if more than one
        val font = Font.createFonts(fontFile).firstOrNull()
        checkNotNull(font)
        fontMap[fontFile.absolutePath] = font
        return font
    }
}