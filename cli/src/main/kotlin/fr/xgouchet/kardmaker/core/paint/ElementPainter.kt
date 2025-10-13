package fr.xgouchet.kardmaker.core.paint

import fr.xgouchet.kardmaker.core.data.CardData
import fr.xgouchet.kardmaker.core.data.Template
import fr.xgouchet.kardmaker.core.data.TemplateElement
import java.awt.Image

interface ElementPainter<E : TemplateElement> {
    fun paint(
        image: Image,
        template: Template,
        element: E,
        cardData: CardData
    )
}