package fr.xgouchet.kardmaker.core.paint

import java.awt.Graphics2D
import java.awt.Rectangle

interface PaintableElement {
    fun paint(graphics: Graphics2D, imageDimension: Rectangle)
}

