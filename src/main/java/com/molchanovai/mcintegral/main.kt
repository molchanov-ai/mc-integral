package com.molchanovai.mcintegral

import com.molchanovai.mcintegral.math.MCFunction

fun main() {
  val integral = Integral(MCFunction { x -> x * x - 3 })
  integral.start()
}