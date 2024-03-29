package com.molchanovai.mcintegral.stats

import space.kscience.kmath.distributions.UniformDistribution
import space.kscience.kmath.random.DefaultGenerator
import space.kscience.kmath.samplers.GaussianSampler

//import space.kscience.kmath

sealed class Distribution {
  /**
   * @param range - range where to search points
   * @param num - number of random variable output
   * @return list of points (arguments for function)
   */
  // TODO: must be multidimensional
  abstract fun branchPoints(range: ClosedFloatingPointRange<Double>, num: Int): List<Double>
  abstract fun branchPrediction(): Boolean

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

    override fun branchPrediction(): Boolean {
      TODO("Not yet implemented")
    }

    suspend fun sampleRange(range: ClosedFloatingPointRange<Double>, num: Int): List<Double> {
      val ret = mutableListOf<Double>()

      val uniformDist = UniformDistribution(range)
      val chain = uniformDist.sample(DefaultGenerator())
      for (i in 0..<num) {
        ret.add(chain.next())
      }

      return ret
    }
  }

  class Bernoulli(private val p: Float) : Distribution() {
    override fun branchPoints(range: ClosedFloatingPointRange<Double>, num: Int): List<Double> {
      TODO("Not yet implemented")
    }

    override fun branchPrediction(): Boolean {
      return (System.currentTimeMillis() % 10) * 0.1 <= p
    }
  }

  class KScienceTest {
    val sampler = GaussianSampler(0.0, 1.0)
    val uniformDist = UniformDistribution(0.0..1.0)

    suspend fun test() {
      val chain = uniformDist.sample(DefaultGenerator())
      val next = chain.next()
    }
  }
}