package com.molchanovai.mcintegral

import com.molchanovai.mcintegral.events.EventBase
import com.molchanovai.mcintegral.math.MCFunction
import com.molchanovai.mcintegral.stats.Distribution
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class Integral(
  private val func: MCFunction,
  private val branchProb: Float = 0.3f, // Probability that the cell will create children
  private val shareDistribution: Float = 0.5f, // Here must be distribution law like {0-0.1 part: x, 0.1-0.5 part: y, ...}
  private val childrenNumber: Int = 3, // Must be stochastic
  private val startEnergy: Float = 1.0f,
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

      branch(Cell(0f, x = 0.0))

      var timePassed = 0L
      messages.takeWhile {
        timePassed < 100000L
      }.collect {
        timePassed++
      }
      println("END")
    }
  }

  // Invariant: at least one must be running in out
  private suspend fun branch(cell: Cell): Unit {
    if (cell.stopPredicate()) {
      cell.finalized = true
      messages.emit(EventBase.EventTerminate(cell))
    } else {

      val newStep = cell.step / 2

      // For future: Must be based on history
      val willBranch = Distribution.Bernoulli(branchProb).branchPrediction()

      // Run the same state
      if (!willBranch) {
        messages.emit(EventBase.BranchEvent(cell, cell))
        runCell(cell)
      } else {
        val points =
          Distribution.Uniform.branchPoints(cell.x - newStep..cell.x + newStep, childrenNumber)
        val values = points.map { func(it) }
        // TODO: set more energy where function is more close to extreme point
        val cells = List(values.size) { i ->
          Cell(
            cell.energy + 1,
            x = points[i],
            step = newStep
          )
        }

        cells.forEach {
          messages.emit(EventBase.BranchEvent(cell, it))
          runCell(it)
        }
      }
    }
  }

  // Here we can set different dispatcher
  private suspend fun runCell(cell: Cell) = coroutineScope {
    async(Job()) {
      branch(cell)
    }
  }
}

data class State(
  val cells: List<Cell>
)

data class Cell(
  val energy: Float,
  val x: Double = 0.0,
  val step: Double = 100.0,
  var finalized: Boolean = false, // TODO: do we need this?
  val children: MutableList<Cell> = mutableListOf()
) {
  fun stopPredicate(): Boolean {
    return energy >= 9f
  }
}