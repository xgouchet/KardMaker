package fr.xgouchet.kardmaker.core.paint

import fr.xgouchet.kardmaker.core.data.TemplateArray
import fr.xgouchet.kardmaker.core.data.CardData
import fr.xgouchet.kardmaker.core.data.Template
import fr.xgouchet.kardmaker.core.data.TemplateElement
import java.awt.Image

class ArrayPainter(val painter: ElementPainter<TemplateElement>, val verbose: Boolean) : ElementPainter<TemplateArray> {
    override fun paint(
        image: Image,
        template: Template,
        element: TemplateArray,
        cardData: CardData
    ) {
//        for (i in 0 until element.count) {
//            if (verbose) {
//                println("  · Instantiating elements for iteration $i")
//            }
//            val offset = element.offset * i
//            element.elements.forEach { subElement ->
//                val offsetElement = subElement.offset(offset)
//                painter.paint(image, template, offsetElement, cardData)
//            }
//        }
    }

}
