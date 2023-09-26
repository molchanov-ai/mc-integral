package com.molchanovai.mcintegral.math

class MCFunction(private val f: (Double) -> Double) {
  fun argMin(xs: List<Double>) {

  }

  fun argMax(xs: List<Double>) {

  }

  operator fun invoke(x: Double): Double {
    return f(x)
  }
}