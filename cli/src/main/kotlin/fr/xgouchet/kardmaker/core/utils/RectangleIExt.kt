package fr.xgouchet.kardmaker.core.utils

import java.awt.Rectangle

val Rectangle.left: Int
    get() = x

val Rectangle.top: Int
    get() = y

val Rectangle.right: Int
    get() = x + width

val Rectangle.bottom: Int
    get() = y + height
