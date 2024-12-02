import java.io.File
import kotlin.math.abs

fun main() {
  partOne()
  partTwo()
}

enum class ValueChange {
  INCREASING,
  DECREASING,
}

fun checkReport(values: List<Int>): Boolean {
  var prev: Int? = null
  var increasing: Boolean? = null

  for (v in values) {
    if (prev == null) {
      prev = v
      continue
    }

    val change = abs(prev - v)
    if (change > 3 || change == 0) {
      return false
    }

    val thisValueChange =
        when {
          prev < v -> ValueChange.INCREASING
          prev > v -> ValueChange.DECREASING
          else -> return false
        }

    if (increasing == null) {
      increasing = thisValueChange == ValueChange.INCREASING
    } else if (increasing != (thisValueChange == ValueChange.INCREASING)) {
      return false
    }

    prev = v
  }

  return true
}

fun partOne() {
  val values = mutableListOf<List<Int>>()
  File("input/second_input.txt").forEachLine { line ->
    values.add(line.split(" ").map { it.toInt() })
  }

  val validReports = values.filter { checkReport(it) }

  println("Valid reports: ${validReports.size}")
}

fun partTwo() {
  //  val values =
  //      mutableListOf<List<Int>>(
  //          listOf(1, 2, 2, 3, 4),
  //          listOf(7, 6, 4, 2, 1),
  //          listOf(1, 2, 7, 8, 9),
  //          listOf(9, 7, 6, 2, 1),
  //          listOf(1, 3, 2, 4, 5),
  //          listOf(8, 6, 4, 4, 1),
  //          listOf(1, 3, 6, 7, 9))

  val values = mutableListOf<List<Int>>()
  File("input/second_input.txt").forEachLine { line ->
    values.add(line.split(" ").map { it.toInt() })
  }

  val validReports =
      values.filter {
        val checkTwo = checkReport2(it)
        val checkThree = checkReport3(it)

        if (checkTwo != checkThree) {
          println("Different results for ${it} - ${checkTwo} - ${checkThree}")
        }

        checkTwo || checkThree
      }
  val validReportsBruteForce = values.filter { checkReport3(it) }

  println("Valid reports 2: ${validReports.size}")
  println("Valid reports brute: ${validReportsBruteForce.size}")
}

fun checkReport2(values: List<Int>): Boolean {
  var prev: Int? = null

  val reportType =
      if (values.first() < values.last()) {
        ValueChange.INCREASING
      } else {
        ValueChange.DECREASING
      }

  var violations = 0
  for (v in values) {
    if (prev == null) {
      prev = v
      continue
    }

    val change = abs(prev - v)
    if (change > 3 || change == 0) {
      violations++
      continue
    }

    val thisValueChange =
        when {
          prev < v -> ValueChange.INCREASING
          prev > v -> ValueChange.DECREASING
          else -> {
            violations++
            continue
          }
        }

    if (reportType != thisValueChange) {
      violations++
      continue
    }

    prev = v
  }

  return violations <= 1
}

fun checkReport3(values: List<Int>): Boolean {
  val versions =
      values.indices.map {
        val v = values.toMutableList()
        v.removeAt(it)
        v
      }

  for (version in versions) {
    if (checkReport(version)) {
      return true
    }
  }

  return false
}
