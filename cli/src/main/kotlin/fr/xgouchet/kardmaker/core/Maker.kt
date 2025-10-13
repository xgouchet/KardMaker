package fr.xgouchet.kardmaker.core

import fr.xgouchet.kardmaker.core.data.Configuration
import fr.xgouchet.kardmaker.core.data.Template
import fr.xgouchet.kardmaker.core.paint.PaintableElement
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Rectangle
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.awt.image.ColorModel
import java.io.File
import javax.imageio.ImageIO

class Maker(
    workingDir: File,
    val verbose: Boolean,
    val debug: Boolean
) {

    private val inputDir = File(workingDir, "input")
    private val outputDir = File(workingDir, "output").also { it.mkdirs() }

    fun generateCards(configuration: Configuration) {
        val template = configuration.template
        val solver = TemplateSolver(template, inputDir)

        val imageDimension = solver.getImageDimension()
        configuration.cards.forEach {
            val elements = solver.resolveElements(it)
            generateCard(it.name, imageDimension, elements)
//            generateCard(configuration.template, it)
        }
    }

    private fun generateCard(
        name: String,
        imageDimension: Rectangle,
        elements: List<PaintableElement>
    ) {
        if (verbose) {
            println("· Generating card $name")
        }
        val image = BufferedImage(imageDimension.width, imageDimension.height, ColorModel.OPAQUE)
        val graphics = image.graphics as? Graphics2D
        checkNotNull(graphics)
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        elements.forEach { it.paint(graphics, imageDimension) }

        // TODO DEBUG lines
        val outFile = File(outputDir, "${name}.$EXT")
        if (verbose) {
            println("· Writing $outFile")
        }
        ImageIO.write(image, EXT, outFile)
    }

    private fun paintDebugMarks(
        image: BufferedImage,
        template: Template
    ) {

        val graphics = image.graphics as? Graphics2D
        checkNotNull(graphics)
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        val cutRectangle = template.unitToPixel(template.cutRectangle())
        graphics.color = Color.RED
        graphics.drawRect(cutRectangle.x, cutRectangle.y, cutRectangle.width, cutRectangle.height)

        val safeRectangle = template.unitToPixel(template.safeRectangle())
        graphics.color = Color.GREEN
        graphics.drawRect(safeRectangle.x, safeRectangle.y, safeRectangle.width, safeRectangle.height)
    }

    private fun createBaseImage(template: Template): BufferedImage {
        val widthPixel = template.unitToPixel(template.width)
        val heightPixel = template.unitToPixel(template.height)
        return BufferedImage(
            widthPixel,
            heightPixel,
            ColorModel.OPAQUE
        )
    }

    companion object {
        const val EXT = "png"
    }
}