package com.molchanovai.mcintegral

import com.molchanovai.mcintegral.math.MCFunction
import org.jetbrains.letsPlot.export.ggsave
import org.jetbrains.letsPlot.geom.geomPoint
import org.jetbrains.letsPlot.letsPlot

fun main() {
  val xs = listOf(0,  0.5, 1, 2)
  val ys = listOf(0, 0.25, 1, 4)
  val data = mapOf<String, Any>("x" to xs, "y" to ys)

  val fig = letsPlot(data) + geomPoint(
    color = "dark-green",
    size = 4.0
  ) { x = "x"; y = "y" }

  ggsave(fig, "plot.png")

  val integral = Integral(MCFunction { x -> x * x - 3 })
  integral.start()
}