package fr.xgouchet.kardmaker.core.paint

import fr.xgouchet.kardmaker.core.data.ImageMode
import fr.xgouchet.kardmaker.core.utils.bottom
import fr.xgouchet.kardmaker.core.utils.left
import fr.xgouchet.kardmaker.core.utils.right
import fr.xgouchet.kardmaker.core.utils.top
import java.awt.Graphics2D
import java.awt.Rectangle
import java.awt.image.BufferedImage
import java.awt.image.ImageObserver
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO
import kotlin.math.roundToInt

class PaintableImage(
    val inputFile: File,
    val rectangle: Rectangle,
    val mode: ImageMode = ImageMode.CROP
) : PaintableElement {

    override fun paint(graphics: Graphics2D, imageDimension: Rectangle, verbose: Boolean) {
        if (verbose) {
            println("    · Loading image from $inputFile")
        }

        val inputImage = try {
            ImageIO.read(inputFile)
        } catch (e: IOException) {
            null
        }

        if (inputImage == null) {
            println("    ✗ failed to load image from $inputFile")
            return
        }

        val src = getSourceRectangle(inputImage)
        val dest = getDestRectangle(inputImage)
        graphics.drawImage(
            inputImage,
            dest.left, dest.top, dest.right, dest.bottom,
            src.left, src.top, src.right, src.bottom,
            NoOpObserver
        )
    }


    private fun getSourceRectangle(inputImage: BufferedImage): Rectangle {
        val srcAspectRatio = inputImage.width.toFloat() / inputImage.height.toFloat()
        val destAspectRatio = rectangle.width.toFloat() / rectangle.height.toFloat()

        return when (mode) {
            ImageMode.RESIZE -> Rectangle(0, 0, inputImage.width, inputImage.height)

            ImageMode.CENTER -> Rectangle(
                if (inputImage.width > rectangle.width) (inputImage.width - rectangle.width) / 2 else 0,
                if (inputImage.height > rectangle.height) (inputImage.height - rectangle.height) / 2 else 0,
                if (inputImage.width > rectangle.width) rectangle.width else inputImage.width,
                if (inputImage.height > rectangle.height) rectangle.height else inputImage.height
            )

            ImageMode.CROP -> {
                if (srcAspectRatio > destAspectRatio) {
                    val cropWidth = (inputImage.height * destAspectRatio).roundToInt()
                    Rectangle((inputImage.width - cropWidth) / 2, 0, cropWidth, inputImage.height)
                } else if (srcAspectRatio < destAspectRatio) {
                    val cropHeight = (inputImage.width / destAspectRatio).roundToInt()
                    Rectangle(0, (inputImage.height - cropHeight) / 2, inputImage.width, cropHeight)
                } else {
                    Rectangle(0, 0, inputImage.width, inputImage.height)
                }
            }
        }
    }

    private fun getDestRectangle(
        inputImage: BufferedImage,
    ): Rectangle {
        return when (mode) {
            ImageMode.RESIZE,
            ImageMode.CROP -> rectangle

            ImageMode.CENTER -> Rectangle(
                if (inputImage.width > rectangle.width) rectangle.x else rectangle.x + (rectangle.width - inputImage.width) / 2,
                if (inputImage.height > rectangle.height) rectangle.y else rectangle.y + (rectangle.height - inputImage.height) / 2,
                if (inputImage.width > rectangle.width) rectangle.width else inputImage.width,
                if (inputImage.height > rectangle.height) rectangle.height else inputImage.height
            )
        }
    }

    companion object {
        val NoOpObserver = ImageObserver { img, infoflags, x, y, width, height -> true }
    }
}