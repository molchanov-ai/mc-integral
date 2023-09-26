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
        branch(Cell(0f))

        delay(1000 * 30)
        println(sum)
      }
    }
  }

  private val running = mutableListOf<List<Int>>()

  private suspend fun start(root: Cell) = withContext(Dispatchers.IO) {
  }

  private suspend fun branch(cell: Cell): Unit = withContext(Dispatchers.IO) {
    val newCells = mutableListOf<Cell>()
    newCells.add(Cell(cell.energy+1))
    newCells.add(Cell(cell.energy+100))

    // val branchPredicate = System.currentTimeMillis() % 10 >= 6
    // println("branch predicate for current=$current is $branchPredicate")

    if (cell.energy == 999999f) {
      messages.emit(cell)
    } else {
      run_cell(newCells[0], true)
      run_cell(newCells[1], false)
    }
  }

  private suspend fun run_cell(cell: Cell, running: Boolean) = withContext(Dispatchers.IO) { launch(Job()) {
    if (running) {
      println("branching")
      println(cell.energy)
      // delay(1000)
      branch(cell)
    } else {
      println("collecting")
      println(cell.energy)
      messages.collect {
        println("collected")
        sum++;
        println(cell.energy)
        println(it.energy)
      }
    }
  }
  }

  private fun chooseCell(cells: List<Cell>): Cell {
    assert(cells.isNotEmpty())

    // Return by max fun value with probability
    return cells[0]
  }
}

data class State(
  val cells: List<Cell>
)

data class Cell(
  val energy: Float,
  val x: Int = 0,
  var finalized: Boolean = false, // TODO: do we need this?
  val children: MutableList<Cell> = mutableListOf()
)
