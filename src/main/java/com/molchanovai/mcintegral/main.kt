package com.molchanovai.mcintegral

import Integral
import com.molchanovai.mcintegral.math.MCFunction

fun main() {
  val integral = Integral(MCFunction { x -> x * x - 3 })
  integral.start()
}