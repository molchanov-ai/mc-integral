import com.molchanovai.mcintegral.math.MCFunction
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.concurrent.atomic.AtomicInteger

// TODO: can we set distribution?
// TODO: check consistency
// TODO: stochastic step
class Integral(
  private val func: MCFunction,
  private val prob: Float = 0.3f, // Probability that the cell will create children
  private val shareDistribution: Float = 0.5f, // Here must be distribution like {0-0.1 part: x, 0.1-0.5 part: y, ...}
  private val startEnergy: Float = 1.0f,
  private val loss: Float = 0.01f, // Energy loss that
  private val maxRunning: Int = 3
  ) {
  // Must be event
  // Add drawer with event
  // Draw realtime approximation
  // Remove stupid "9" and delay 30 secs
  // Add stochastic
  private val messages = MutableSharedFlow<Cell>()

  fun start(): Unit {
    runBlocking {
      println("Hello from hell")
      delay(1000)
      branch(Cell(0f, x = 0.0, running = true))

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
    assert(cell.running)

    val newStep = cell.step / 2
    val newCells = mutableListOf<Cell>()
    val x1 = cell.x-cell.step
    val x2 = cell.x+cell.step
    val f1 = func(x1)
    val f2 = func(x2)
    newCells.add(Cell(cell.energy + 1, x=x1, step = newStep, running = f1 < f2))
    newCells.add(Cell(cell.energy + 1, x=x2, step = newStep, running = f1 >= f2))

    // val branchPredicate = System.currentTimeMillis() % 10 >= 6
    // println("branch predicate for current=$current is $branchPredicate")

    if (cell.stopPredicate()) {
      // TODO: emit terminate event
      messages.emit(cell)
    } else {
      // TODO: emit event state
      messages.emit(cell)
      for (newCell in newCells) {
        runCell(newCell)
      }
    }
  }

  // Here we can set different dispatcher
  private suspend fun runCell(cell: Cell) = coroutineScope {
    async(Job()) {
      println("branching")
      println(func(cell.x))
      println(cell)
      // delay(1000)
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
  var running: Boolean = false,
  var finalized: Boolean = false, // TODO: do we need this?
  val children: MutableList<Cell> = mutableListOf()
) {
  fun stopPredicate(): Boolean {
    return energy >= 9f
  }
}