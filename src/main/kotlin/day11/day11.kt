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

fun countStones(
    stone: String,
    depthRemaining: Int,
    cache: MutableMap<Pair<String, Int>, Long>
): Long {
  val cached = cache[Pair(stone, depthRemaining)]
  if (cached != null) {
    return cached
  }

  if (depthRemaining == 0) {
    cache[Pair(stone, depthRemaining)] = 1
    return 1
  }

  val result =
      when {
        stone == "0" -> {
          countStones("1", depthRemaining - 1, cache)
        }
        stone.length % 2 == 0 -> {
          val left = stone.substring(0, stone.length / 2).toLong().toString()
          val right = stone.substring(stone.length / 2).toLong().toString()
          countStones(left, depthRemaining - 1, cache) +
              countStones(right, depthRemaining - 1, cache)
        }
        else -> {
          countStones((stone.toLong() * 2024).toString(), depthRemaining - 1, cache)
        }
      }
  cache[Pair(stone, depthRemaining)] = result
  return result
}

fun partTwo() {
  val input = File("input/day11_input.txt").readLines().flatMap { line -> line.split(" ") }

  val cache = mutableMapOf<Pair<String, Int>, Long>()
  var count = 0L
  for (stone in input) {
    count += countStones(stone, 75, cache)
  }

  println("Count: ${count}")
}
