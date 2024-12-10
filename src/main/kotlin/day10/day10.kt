package day10

import java.io.File

fun main() {
  partOneAndTwo()
}

data class Cell(
    val row: Int,
    val column: Int,
    val value: Int,
)

typealias InputMap = List<List<Cell>>

typealias Path = List<Cell>

fun printPath(path: Path, map: InputMap) {
  map.forEach { row ->
    row.forEach { cell ->
      if (path.contains(cell)) {
        print(cell.value)
      } else {
        print('.')
      }
    }
    println()
  }
}

fun getOrNull(row: Int, column: Int, map: InputMap): Cell? {
  if (row >= 0 && row < map.size && column >= 0 && column < map[row].size) {
    return map[row][column]
  }

  return null
}

fun getNeighbours(row: Int, column: Int, map: InputMap): List<Cell> {
  val right = getOrNull(row, column + 1, map)
  val left = getOrNull(row, column - 1, map)
  val up = getOrNull(row - 1, column, map)
  val down = getOrNull(row + 1, column, map)

  return listOfNotNull(
      right,
      left,
      up,
      down,
  )
}

fun getNeighbours(cell: Cell, input: InputMap) = getNeighbours(cell.row, cell.column, input)

fun findPaths(from: Cell, map: InputMap): List<Path>? {
  val neighbours = getNeighbours(from, map)
  val next = neighbours.filter { it.value == from.value + 1 }

  if (next.isEmpty()) {
    return null
  }

  val nextIsGoal = next.filter { it.value == 9 }
  if (nextIsGoal.isNotEmpty()) {
    return nextIsGoal.map { listOf(from, it) }
  }

  val continuedSearchPath = next.mapNotNull { findPaths(it, map) }.flatten()
  if (continuedSearchPath.isEmpty()) {
    return null
  }

  return continuedSearchPath.map { listOf(from) + it }
}

fun partOneAndTwo() {
  val input =
      File("input/day10_input.txt").readLines().map { line ->
        line.split("").mapNotNull { if (it.isNotEmpty()) it.toInt() else null }
      }

  val map =
      input.mapIndexed { rowIndex, row ->
        row.mapIndexed { columnIndex, value -> Cell(rowIndex, columnIndex, value) }
      }

  val cells = map.flatten()
  val startingPoints = cells.filter { it.value == 0 }

  val paths = startingPoints.mapNotNull { findPaths(it, map) }

  val partOne = paths.map { it.distinctBy { path -> path.last() } }.flatten()
  val partTwo = paths.flatten()

  println(
      """
    paths 1: ${partOne.size}
    paths 2: ${partTwo.size}
    """
          .trimIndent())
}
