import com.molchanovai.mcintegral.math.MCFunction
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

// TODO: can we set distribution?
class Integral(
  private val func: MCFunction,
  private val prob: Float = 0.3f, // Probability that the cell will create children
  private val shareDistribution: Float = 0.5f, // Here must be distribution like {0-0.1 part: x, 0.1-0.5 part: y, ...}
  private val startEnergy: Float = 1.0f,
  private val loss: Float = 0.01f, // Energy loss that
  ) {

  private val cells = mutableMapOf<Cell, Int>(Cell(startEnergy) to -1)
  private val messages = MutableSharedFlow<Cell>()

  private var sum = 0

  fun start(): Unit {
    runBlocking {
      launch {
        println("Hello from hell")
        delay(1000)
        // start(Cell(3f))
        branch(Cell(0f, x=0.0, running = true))

        delay(1000 * 30)
        println("RESULT")
        println(sum)
      }
    }
  }

  private suspend fun branch(cell: Cell): Unit = withContext(Dispatchers.IO) {
    if (cell.energy > 9) {
      return@withContext
    }
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

    if (cell.energy == 9f) {
      messages.emit(cell)
    } else {
      for (newCell in newCells) {
        runCell(newCell)
      }
    }
  }

  private suspend fun runCell(cell: Cell) = withContext(Dispatchers.IO) {
    async(Job()) {
      if (cell.running) {
        println("branching")
        println(cell)
        // delay(1000)
        branch(cell)
      } else {
        println("collecting")
        println(cell)
        messages.take(1).collect {
          // Do stuff with cell
          val funValue = func(it.x)
          println("FUNCTION RESULT")
          println(funValue)
          cell.running = true
        }
        branch(cell)
      }
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
)
