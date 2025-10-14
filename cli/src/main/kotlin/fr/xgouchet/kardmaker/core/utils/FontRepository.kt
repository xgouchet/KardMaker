package fr.xgouchet.kardmaker.core.utils

import java.awt.Font
import java.io.File
import java.io.IOException

object FontRepository {

    private val fontMap: MutableMap<String, Font> = mutableMapOf()

    fun getFont(inputDir: File, fontPath: String): Font {
        // 1. Check if we have this font in our cache
        val memoFont = fontMap[fontPath]
        if (memoFont != null) return memoFont

        val fontFile = File(inputDir, fontPath)

        // 2. Try to load as a file first if it exists
        var font: Font? = null
        if (fontFile.exists() && fontFile.isFile) {
            println("  · Loading custom font from $fontFile")
            try {
                font = Font.createFont(Font.TRUETYPE_FONT, fontFile)
            } catch (e: Exception) {
                System.err.println("Warning: Failed to load font file '$fontFile'. It might be corrupt. Falling back to system fonts.")
            }
        }

        // 3. If not loaded from file, try to load as a system font
        if (font == null) {
            println("  · Resolving system font '$fontPath'")
            try {
                font = Font(fontPath, Font.PLAIN, 12)
            } catch (e: Exception) {
                System.err.println("Warning: Failed to resolve system font '$fontPath'.")
            }
        }

        // 4. If all else fails, use a known default
        if (font == null) {
            System.err.println("-> Falling back to default SANS_SERIF font.")
            font = Font(Font.SANS_SERIF, Font.PLAIN, 12)
        }
        
        // 5. Cache and return the result
        fontMap[fontPath] = font
        return font
    }
}
