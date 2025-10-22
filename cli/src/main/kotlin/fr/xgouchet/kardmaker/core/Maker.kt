package fr.xgouchet.kardmaker.core

import fr.xgouchet.kardmaker.core.data.Configuration
import fr.xgouchet.kardmaker.core.paint.PaintableElement
import fr.xgouchet.kardmaker.core.paint.PaintableImage.Companion.NoOpObserver
import fr.xgouchet.kardmaker.core.utils.bottom
import fr.xgouchet.kardmaker.core.utils.left
import fr.xgouchet.kardmaker.core.utils.right
import fr.xgouchet.kardmaker.core.utils.top
import java.awt.Graphics2D
import java.awt.Rectangle
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.awt.image.ColorModel
import java.io.File
import javax.imageio.ImageIO

class Maker(
    workingDir: File,
    val noBleed: Boolean,
    val verbose: Boolean,
    val debug: Boolean
) {

    private val inputDir = File(workingDir, "input")
    private val outputDir = File(workingDir, "output").also { it.mkdirs() }

    fun generateCards(configuration: Configuration) {
        val template = configuration.template
        val solver = TemplateSolver(template, inputDir, verbose)

        val imageDimension = solver.getImageDimension()

        val debugElements = if (debug) solver.getDebugElements() else emptyList()
        val cutRectangle = if (noBleed) solver.getCutRectangle() else imageDimension

        configuration.cards.forEach {
            val elements = solver.resolveElements(it) + debugElements
            generateCard(it.name, imageDimension, cutRectangle, elements)
        }
    }

    private fun generateCard(
        name: String,
        imageDimension: Rectangle,
        cutRectangle: Rectangle,
        elements: List<PaintableElement>,
    ) {
        if (verbose) {
            println("  · Generating card $name")
        }
        val image = BufferedImage(imageDimension.width, imageDimension.height, ColorModel.OPAQUE)
        val graphics = image.graphics as? Graphics2D
        if (graphics == null) {
            println("  ✗ Can't convert graphics to Graphics2D")
            return
        }
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)

        elements.forEach { it.paint(graphics, imageDimension, verbose) }

        val finalImage = BufferedImage(cutRectangle.width, cutRectangle.height, ColorModel.OPAQUE)
        finalImage.graphics.drawImage(
            image,
            0, 0, cutRectangle.width, cutRectangle.height,
            cutRectangle.left, cutRectangle.top, cutRectangle.right, cutRectangle.bottom,
            NoOpObserver
        )

        // TODO DEBUG lines
        val outFile = File(outputDir, "${name}.$EXT")
        if (verbose) {
            println("  · Writing to $outFile")
        }
        val result = ImageIO.write(finalImage, EXT, outFile)
        if (result) {
            println("  ✓ Successfully wrote to $outFile")
        } else {
            println("  ✗ Enable to write to $outFile")
        }
    }


    companion object {
        const val EXT = "png"
    }
}