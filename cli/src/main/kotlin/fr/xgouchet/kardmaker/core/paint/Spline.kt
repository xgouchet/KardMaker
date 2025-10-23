package fr.xgouchet.kardmaker.core.paint

import fr.xgouchet.kardmaker.core.data.Point

fun spline(
    a: Point, b: Point, c: Point, d: Point, steps: Int = 10
): List<Point> {
    val result = mutableListOf<Point>()

    for (i in 0..steps) {
        val t = i.toFloat() / steps
        val s = 1f - t

        val k = (a * s) + (b * t)
        val l = (b * s) + (c * t)
        val m = (c * s) + (d * t)

        val o = (k * s) + (l * t)
        val p = (l * s) + (m * t)

        result.add((o * s) + (p * t))
    }
    return result
}