package day4

import java.io.File

fun main() {
  partOne()
  partTwo()
}

val expectSequence = listOf("X", "M", "A", "S")

val directions =
    listOf(
        Pair(0, 1), // up
        Pair(1, 0), // right
        Pair(0, -1), // down
        Pair(-1, 0), // left
        Pair(1, 1), // diagonal up-right
        Pair(-1, -1), // diagonal down-left
        Pair(1, -1), // diagonal down-right
        Pair(-1, 1), // diagonal up-left
    )

fun getNext(inputMatrix: List<List<String>>, x: Int, y: Int): String? {
  if (x < 0 || y < 0 || x >= inputMatrix.size || y >= inputMatrix[x].size) {
    return null
  }

  return inputMatrix[x][y]
}

fun getMatchingSequence(
    inputMatrix: List<List<String>>,
    x: Int,
    y: Int,
    direction: Pair<Int, Int>
): List<String>? {
  val sequence = mutableListOf<String>()
  var currentX = x
  var currentY = y

  for (i in 0 until expectSequence.size) {
    val next = getNext(inputMatrix, currentX, currentY)
    if (next == null || next != expectSequence[i]) {
      return null
    }

    sequence.add(next)
    currentX += direction.first
    currentY += direction.second
  }

  return sequence
}

fun partOne() {
  val inputMatrix = File("input/day4_input.txt").readLines().map { it.split("") }

  var foundWords = 0
  for (x in 0 until inputMatrix.size) {
    for (y in 0 until inputMatrix[x].size) {
      for (direction in directions) {
        val sequence = getMatchingSequence(inputMatrix, x, y, direction)
        if (sequence != null) {
          println("Found sequence: $sequence at $x, $y - direction: $direction")
          foundWords++
        }
      }
    }
  }

  println("Found words: $foundWords")
}

fun findMas(inputMatrix: List<List<String>>, currentX: Int, currentY: Int): Boolean {
  val current = getNext(inputMatrix, currentX, currentY)
  if (current != "A") {
    return false
  }

  val diagonalLR =
      "${getNext(inputMatrix, currentX - 1, currentY + 1)}${current}${getNext(inputMatrix, currentX + 1, currentY - 1)}"
  val diagonalRL =
      "${getNext(inputMatrix, currentX + 1, currentY + 1)}${current}${getNext(inputMatrix, currentX - 1, currentY - 1)}"

  val LRMatch = diagonalLR == "MAS" || diagonalLR == "SAM"
  val RLMatch = diagonalRL == "MAS" || diagonalRL == "SAM"
  return LRMatch && RLMatch
}

fun partTwo() {
  val inputMatrix = File("input/day4_input.txt").readLines().map { it.split("") }

  var foundWords = 0
  for (x in 0 until inputMatrix.size) {
    for (y in 0 until inputMatrix[x].size) {
      val isXMax = findMas(inputMatrix, x, y)
      if (isXMax) {
        println("Found mas at $x, $y")
        foundWords++
      }
    }
  }

  println("Found words: $foundWords")
}
