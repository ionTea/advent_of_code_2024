package day7

import java.io.File

fun main() {
  partOne()
  partTwo()
}

fun canBecomeValue(
    expectedValue: Long,
    currentValue: Long,
    remaining: List<Long>,
    currentEval: String
): Boolean {
  if (remaining.isEmpty()) {
    return (expectedValue == currentValue)
  }

  if (currentValue > expectedValue) {
    return false
  }

  return canBecomeValue(
      expectedValue,
      currentValue + remaining.first(),
      remaining.drop(1),
      "${currentEval}$currentValue + ${remaining.first()}") ||
      canBecomeValue(
          expectedValue,
          currentValue * remaining.first(),
          remaining.drop(1),
          "${currentEval}$currentValue * ${remaining.first()}")
}

fun partOne() {
  File("input/day7_input.txt")
      .readLines()
      .map { line ->
        val (expectedString, list) = line.split(":")
        val numbers = list.split(" ").filter { it.isNotEmpty() }.map { it.toLong() }
        val expected = expectedString.toLong()

        val canBecomeValue = canBecomeValue(expected, numbers.first(), numbers.drop(1), "")
        println(
            """
            Expected: $expected
            values: ${numbers}
            canBecomeValue: ${canBecomeValue}
        """
                .trimIndent())

        if (canBecomeValue) {
          expected
        } else {
          0
        }
      }
      .sum()
      .let { println("Sum: $it") }
}

fun canBecomeValuePartTwo(
    expectedValue: Long,
    currentValue: Long,
    remaining: List<Long>,
    currentEval: String
): Boolean {
  if (remaining.isEmpty()) {
    println("currentEval: $currentEval")
    return (expectedValue == currentValue)
  }

  if (currentValue > expectedValue) {
    return false
  }

  return canBecomeValuePartTwo(
      expectedValue,
      currentValue = currentValue + remaining.first(),
      remaining = remaining.drop(1),
      currentEval = "${currentEval}$currentValue + ${remaining.first()}") ||
      canBecomeValuePartTwo(
          expectedValue = expectedValue,
          currentValue = currentValue * remaining.first(),
          remaining = remaining.drop(1),
          currentEval = "${currentEval}$currentValue * ${remaining.first()}") ||
      canBecomeValuePartTwo(
          expectedValue,
          currentValue = "${currentValue}${remaining.first()}".toLong(),
          remaining = remaining.drop(1),
          currentEval = "${currentEval}$currentValue || ${remaining.first()}")
}

fun partTwo() {
  File("input/day7_input.txt")
      .readLines()
      .map { line ->
        val (expectedString, list) = line.split(":")
        val numbers = list.split(" ").filter { it.isNotEmpty() }.map { it.toLong() }
        val expected = expectedString.toLong()

        val canBecomeValue = canBecomeValuePartTwo(expected, numbers.first(), numbers.drop(1), "")
        println(
            """
            partTwo
            Expected: $expected
            values: ${numbers}
            canBecomeValue: ${canBecomeValue}
        """
                .trimIndent())

        if (canBecomeValue) {
          expected
        } else {
          0
        }
      }
      .sum()
      .let { println("Sum: $it") }
}
