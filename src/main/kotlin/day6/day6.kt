package day6

import java.io.File

fun main() {
  partOne()
  partTwo()
}

data class Position(val x: Int, val y: Int)

enum class DIRECTION(val pair: Position) {
  UP(Position(0, -1)),
  DOWN(Position(0, 1)),
  LEFT(Position(-1, 0)),
  RIGHT(Position(1, 0));

  fun asString(): String {
    return when (this) {
      UP -> "^"
      DOWN -> "v"
      LEFT -> "<"
      RIGHT -> ">"
    }
  }

  companion object {
    fun fromString(s: String): DIRECTION? {
      return when (s) {
        "^" -> UP
        "v" -> DOWN
        "<" -> LEFT
        ">" -> RIGHT
        else -> null
      }
    }
  }
}

data class Guard(var direction: DIRECTION, var position: Position, val map: List<List<String>>) {

  private val visited = mutableSetOf<Position>()
  private val visitedWithRotation = mutableMapOf<Pair<Position, DIRECTION>, Int>()

  private fun setPosAndMarkVisited(p: Position) {
    visited.add(p)
    val pair = Pair(p, direction)
    visitedWithRotation[pair] = visitedWithRotation.getOrDefault(pair, 0) + 1
    position = p
  }

  fun isOutOfBounds(): Boolean {
    return isOutOfBounds(position)
  }

  fun isOutOfBounds(pos: Position): Boolean {
    return pos.y < 0 || pos.y >= map.size || pos.x < 0 || pos.x >= map[pos.y].size
  }

  fun isInCycle(): Boolean {
    return visitedWithRotation.getOrDefault(Pair(position, direction), 0) > 1
  }

  fun step() {
    val newPosition = Position(position.x + direction.pair.x, position.y + direction.pair.y)

    if (isOutOfBounds(newPosition)) {
      position = newPosition
      return
    }

    if (map[newPosition.y][newPosition.x] == "#") {
      direction =
          when (direction) {
            DIRECTION.UP -> DIRECTION.RIGHT
            DIRECTION.RIGHT -> DIRECTION.DOWN
            DIRECTION.DOWN -> DIRECTION.LEFT
            DIRECTION.LEFT -> DIRECTION.UP
          }
      return
    }

    setPosAndMarkVisited(newPosition)
  }

  fun printMap() {
    for (row in 0 until map.size) {
      for (column in 0 until map[row].size) {
        if (position == Position(column, row)) {
          print(direction.asString())
        } else if (visited.contains(Position(column, row))) {
          print("X")
        } else if (map[row][column] == "#") {
          print("#")
        } else {
          print(".")
        }
      }
      println()
    }
  }

  fun visitedCount(): Int {
    return visited.size
  }

  fun visitedTiles(): Set<Position> {
    return visited
  }
}

fun getGuard(map: List<List<String>>): Guard {
  for (row in 0 until map.size) {
    for (column in 0 until map[row].size) {
      val tile = map[row][column]
      if (DIRECTION.fromString(tile) != null) {
        return Guard(DIRECTION.fromString(tile)!!, Position(column, row), map)
      }
    }
  }

  throw RuntimeException("No guard found")
}

fun partOne() {
  val map = File("input/day6_input_simple.txt").readLines().map { it.split("") }
  val guard = getGuard(map)
  guard.printMap()

  while (!guard.isOutOfBounds()) {
    guard.step()
  }

  guard.printMap()
  println(
      """
    Total visited: ${guard.visitedCount()}
  """
          .trimIndent())
}

fun partTwo() {
  val map = File("input/day6_input.txt").readLines().map { it.split("") }
  val guard = getGuard(map)

  guard.printMap()

  while (!guard.isOutOfBounds()) {
    guard.step()
  }

  // Try setting an obstruction on each of the visited tiles and see if it causes a cycle
  var cyclesFound = 0
  for (tile in guard.visitedTiles()) {
    if (DIRECTION.fromString(map[tile.y][tile.x]) != null) {
      continue
    }
    val alternativeMap = map.map { it.toMutableList() }
    alternativeMap[tile.y][tile.x] = "#"
    val alternativeGuard = getGuard(alternativeMap)

    while (!alternativeGuard.isOutOfBounds() && !alternativeGuard.isInCycle()) {
      alternativeGuard.step()
    }

    if (alternativeGuard.isInCycle()) {
      cyclesFound++
    }
  }

  println(
      """
    Cycles found: $cyclesFound
  """
          .trimIndent())
}
