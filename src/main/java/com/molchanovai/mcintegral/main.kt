package com.molchanovai.mcintegral

import com.molchanovai.mcintegral.math.MCFunction
import org.jetbrains.letsPlot.export.ggsave
import org.jetbrains.letsPlot.geom.geomPoint
import org.jetbrains.letsPlot.letsPlot

fun main() {
  val integral = Integral(MCFunction { x -> x * x - 3 })
  integral.start()
}