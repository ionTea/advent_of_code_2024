package day8

import java.io.File

fun main() {
  partOne()
  partTwo()
}

data class Distance(val row: Int, val column: Int)

data class Cell(
    val row: Int,
    val column: Int,
) {
  fun distanceTo(cell: Cell): Distance {
    return Distance(row = cell.row - row, column = cell.column - column)
  }
}

typealias InputMap = List<List<String>>

fun cellIsInMap(cell: Cell, map: InputMap): Boolean {
  return cell.row >= 0 &&
      cell.row < map.size &&
      cell.column >= 0 &&
      cell.column < map[cell.row].size
}

data class Tower(val type: String, val location: Cell)

fun partOne() {

  val map =
      File("input/day8_input.txt").readLines().map { line ->
        line.split("").filter { it.isNotEmpty() }
      }

  val towerLocations =
      map.flatMapIndexed { row, it ->
            it.mapIndexedNotNull { column, cell ->
              if (cell != ".") {
                Tower(cell, Cell(row, column))
              } else {
                null
              }
            }
          }
          .groupBy { it.type }

  val foundAntinodes = mutableSetOf<Cell>()

  for ((type, towers) in towerLocations) {
    for (tower in towers) {
      val distances =
          towers.mapNotNull { if (it != tower) tower.location.distanceTo(it.location) else null }

      //      Find all the potential antinodes
      val potentialAntinodes = distances.map { Distance(it.row * 2, it.column * 2) }
      val cellsOfAntinode =
          potentialAntinodes.map {
            Cell(tower.location.row + it.row, tower.location.column + it.column)
          }

      val antinodes = cellsOfAntinode.filter { cellIsInMap(it, map) }
      println("Type: $type - found antinodes: ${antinodes.size}")
      foundAntinodes.addAll(antinodes)
    }
  }

  println(
      """
          antinodes: $foundAntinodes
          count: ${foundAntinodes.size}
        """
          .trimIndent())
}

fun partTwo() {
  val map =
      File("input/day8_input.txt").readLines().map { line ->
        line.split("").filter { it.isNotEmpty() }
      }

  val towerLocations =
      map.flatMapIndexed { row, it ->
            it.mapIndexedNotNull { column, cell ->
              if (cell != ".") {
                Tower(cell, Cell(row, column))
              } else {
                null
              }
            }
          }
          .groupBy { it.type }

  val foundAntinodes = mutableSetOf<Cell>()

  for ((type, towers) in towerLocations) {
    for (tower in towers) {
      val distances =
          towers.mapNotNull { if (it != tower) tower.location.distanceTo(it.location) else null }

      //      Find all the potential antinodes
      for (i in 1 until 100) {

        val potentialAntinodes = distances.map { Distance(it.row * i, it.column * i) }
        val cellsOfAntinode =
            potentialAntinodes.map {
              Cell(tower.location.row + it.row, tower.location.column + it.column)
            }

        val antinodes = cellsOfAntinode.filter { cellIsInMap(it, map) }
        println("Type: $type - found antinodes: ${antinodes.size}")
        if (antinodes.isEmpty()) {
          break
        }
        foundAntinodes.addAll(antinodes)
      }
    }
  }

  println(
      """
          antinodes: $foundAntinodes
          count: ${foundAntinodes.size}
        """
          .trimIndent())
}
