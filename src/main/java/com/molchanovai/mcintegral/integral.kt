package com.molchanovai.mcintegral

import com.molchanovai.mcintegral.events.EventBase
import com.molchanovai.mcintegral.math.MCFunction
import com.molchanovai.mcintegral.stats.Distribution
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.jetbrains.letsPlot.export.ggsave
import org.jetbrains.letsPlot.geom.geomPoint
import org.jetbrains.letsPlot.letsPlot

// TODO: add mode how to run: one thread or otherwise
class Integral(
  private val func: MCFunction,
  private val branchProb: Float = 0.3f, // Probability that the cell will create children
  private val shareDistribution: Float = 0.5f, // Here must be distribution law like {0-0.1 part: x, 0.1-0.5 part: y, ...}
  private val childrenNumber: Int = 3, // Must be stochastic
  private val startEnergy: Float = 100.0f,
  private val loss: Float = 0.01f, // Energy loss that
  ) {
  // Must be event
  // Add drawer with event
  // Draw realtime approximation
  // Remove stupid "9" and delay 30 secs
  // Add stochastic
  private val messages = MutableSharedFlow<EventBase>()

  fun start(): Unit {
    runBlocking {
      println("Starting...")
      delay(1000)

      branch(Cell(startEnergy, x = 5.0))

      var timePassed = 0L
      val xs = mutableListOf<Double>()
      messages.takeWhile {
        timePassed < 100000L
      }.collect {// TODO: boost
        timePassed++
        if (it is EventBase.BranchEvent) {
          println(func(it.child.x))
          xs.add(it.child.x)
          val ys = xs.map { x -> func(x) }
          val data = mapOf<String, Any>("x" to xs, "y" to ys)

          val fig = letsPlot(data) + geomPoint(
            color = "dark-green",
          ) { x = "x"; y = "y" }

          ggsave(fig, "plot.png")
        }
      }
      println("END")
    }
  }

  // Invariant: at least one must be running in out
  private fun CoroutineScope.branch(cell: Cell): Job = launch {
    if (cell.stopPredicate()) {
      cell.finalized = true
      messages.emit(EventBase.EventTerminate(cell))
    } else {

      val newStep = cell.step

      // For future: Must be based on history. Or on function min or max approx
      val willBranch = Distribution.Bernoulli(branchProb).branchPrediction()

      // Run the same state
      if (!willBranch) {
        messages.emit(EventBase.BranchEvent(cell, cell))
        cell.reduceEnergyUnbranched()
        branch(cell)
      } else {
        val prevValue = func(cell.x)
        val points = Distribution.Uniform.sampleRange(cell.x - newStep..cell.x + newStep, childrenNumber)
        val pointsSorted = points.sortedBy { func(it) }
        val values = pointsSorted.map { func(it) }

        var fullEnergy = cell.energy
        val cells = List(values.size) { i ->
//          val prevEnergy = fullEnergy
          val takingEnergy = 0.9f * fullEnergy
          fullEnergy -= takingEnergy
          Cell(
            if (i == values.size-1) fullEnergy else takingEnergy,
            x = pointsSorted[i],
          )
        }

        cells.forEach {
          messages.emit(EventBase.BranchEvent(cell, it))
          branch(it)
        }
      }
    }
  }

  // Here we can set different dispatcher
  private fun CoroutineScope.runCell(cell: Cell) {
      branch(cell)
  }
}

data class State(
  val cells: List<Cell>
)

data class Cell(
  var energy: Float,
  val x: Double = 0.0,
  val step: Double = 2.0,
  var finalized: Boolean = false, // TODO: do we need this?
  val children: MutableList<Cell> = mutableListOf()
) {
  fun stopPredicate(): Boolean {
    return energy <= 0f
  }

  fun reduceEnergyUnbranched() {
    energy -= 1f
  }
}