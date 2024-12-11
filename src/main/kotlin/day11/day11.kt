package day11

import java.io.File

fun main() {
  partOne()
  partTwo()
}

sealed class Stone {
  data class SplitStone(val left: Stone, val right: Stone) : Stone()

  data class SingleStone(val value: String) : Stone()

  fun step(): Stone {
    when (this) {
      is SplitStone -> {
        return SplitStone(left.step(), right.step())
      }
      is SingleStone -> {
        return when {
          value == "0" -> SingleStone("1")
          value.length % 2 == 0 ->
              SplitStone(
                  SingleStone(value.substring(0, value.length / 2).toLong().toString()),
                  SingleStone(value.substring(value.length / 2).toLong().toString()))
          else -> SingleStone("${value.toLong() * 2024}")
        }
      }
    }
  }

  fun flatten(): List<SingleStone> {
    return when (this) {
      is SplitStone -> left.flatten() + right.flatten()
      is SingleStone -> listOf(this)
    }
  }

  fun print(): String {
    return when (this) {
      is SplitStone -> "${left.print()} ${right.print()}"
      is SingleStone -> value
    }
  }

  fun count(): Int {
    return when (this) {
      is SplitStone -> left.count() + right.count()
      is SingleStone -> 1
    }
  }
}

fun partOne() {
  val input =
      File("input/day11_input.txt").readLines().flatMap { line ->
        line.split(" ").map { Stone.SingleStone(it) }
      }

  println(input)
  var next: List<Stone> = input
  for (i in 0 until 25) {
    println("Step $i")
    next = next.flatMap { it.step().flatten() }
    println("Count: ${next.sumOf { it.count() }}")
  }
}

fun partTwo() {
  val input =
      File("input/day11_input.txt").readLines().flatMap { line ->
        line.split(" ").map { Stone.SingleStone(it) }
      }

  val repeatedValues = mutableMapOf<String, Stone>()
  var next: List<Stone> = input.subList(0, 1)
  for (i in 0 until 75) {
    println("Step $i")
    next =
        next.flatMap {
          val result = it.step()
          result.flatten()
        }
    next.forEach { stone ->
      repeatedValues[stone.value] = repeatedValues.getOrDefault(stone.print(), 0) + 1
    }
    println("Repetion: ${repeatedValues.filterValues { it > 1 }.size}")
    println("Count: ${next.size}")
    println("unique values: ${next.distinctBy { it.value }.size}")
  }

  println("Count: ${next.sumOf { it.count() }}")
}
