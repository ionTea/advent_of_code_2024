package day3

import java.io.File

fun main() {
  partOne()
  partTwo()
}

val mulOpRegex = Regex("""mul\((\d+),(\d+)\)""")

fun partOne() {
  val input = File("input/third_input.txt").readText()

  val result =
      mulOpRegex
          .findAll(input)
          .map {
            println(it.value)
            val (a, b) = it.destructured
            a.toInt() * b.toInt()
          }
          .sum()

  println("Result: $result")
}

val mulOpWithConditionalsRegex = Regex("""mul\((\d+),(\d+)\)|do\(\)|don't\(\)""")

enum class State {
  DO,
  DONT,
}

fun partTwo() {
  val input = File("input/third_input.txt").readText()

  var state = State.DO
  val result =
      mulOpWithConditionalsRegex
          .findAll(input)
          .map {
            val value = it.value
            println(value)
            if (value == "do()") {
              state = State.DO
              return@map 0
            } else if (value == "don't()") {
              state = State.DONT
              return@map 0
            }

            if (state == State.DONT) {
              return@map 0
            }
            val (a, b) = it.destructured
            a.toInt() * b.toInt()
          }
          .sum()

  println("Result: $result")
}
