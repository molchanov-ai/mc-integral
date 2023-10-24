package com.molchanovai.mcintegral.stats

sealed class Distribution {
  /**
   * @param range - range where to search points
   * @param num - number of random variable output
   * @return list of points (arguments for function)
   */
  // TODO: must be multidimensional
  abstract fun branchPoints(range: ClosedFloatingPointRange<Double>, num: Int): List<Double>

  data object Uniform : Distribution() {
    override fun branchPoints(range: ClosedFloatingPointRange<Double>, num: Int): List<Double> {
      if (num == 0) {
        return emptyList()
      }

      val ret = mutableListOf<Double>()
      val slice = (range.endInclusive - range.start) / num
      for (i in IntRange(1, num)) {
        ret.add(slice * i)
      }

      return ret
    }
  }

//  class Bernoulli(p: Float) : Distribution() {
//    override fun branchPoints(range: ClosedFloatingPointRange<Double>, num: Int): List<Double> {
//      val
//    }
//  }
}