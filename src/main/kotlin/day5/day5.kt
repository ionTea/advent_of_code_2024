package day5

import java.io.File

fun main() {
  partOne()
  partTwo()
}

data class Rules(
    val before: Set<Int>,
    val after: Set<Int>,
)

fun validateUpdate(update: List<Int>, structuredRules: Map<Int, Rules>): Boolean {
  for (i in 1 until update.size) {
    val currentValue = update[i]
    val rulesForCurrentValue = structuredRules[currentValue] ?: continue

    val valuesBefore = update.subList(0, i)
    val valuesAfter = update.subList(i + 1, update.size)

    if (valuesAfter.any { it in rulesForCurrentValue.before }) {
      return false
    }

    if (valuesBefore.any { it in rulesForCurrentValue.after }) {
      return false
    }
  }

  return true
}

fun partOne() {
  val orderingRules = mutableListOf<Pair<Int, Int>>()
  val updates = mutableListOf<List<Int>>()

  var isReadingRules = true
  File("input/day5_input.txt").forEachLine { line ->
    if (line.isEmpty()) {
      isReadingRules = false
      return@forEachLine
    }

    if (isReadingRules) {
      val (first, second) = line.split("|").map { it.toInt() }
      orderingRules.add(Pair(first, second))
    } else {
      val update = line.split(",").map { it.toInt() }
      updates.add(update)
    }
  }

  val structuredRules =
      orderingRules.fold(mutableMapOf<Int, Rules>()) { map, rule ->
        map[rule.first] =
            Rules(
                before = map[rule.first]?.before ?: emptySet(),
                after = map[rule.first]?.after?.plus(rule.second) ?: setOf(rule.second),
            )

        map[rule.second] =
            Rules(
                before = map[rule.second]?.before?.plus(rule.first) ?: setOf(rule.first),
                after = map[rule.second]?.after ?: emptySet(),
            )

        map
      }

  val validUpdates = updates.filter { validateUpdate(it, structuredRules) }

  val result = validUpdates.map { it[it.size / 2] }.sum()

  println(
      """
    validUpdates: ${validUpdates}
    result: ${result}
  """
          .trimIndent())
}

enum class Direction {
  LEFT,
  RIGHT,
}

sealed class BinaryTree {
  data class Node(val value: Int, val left: BinaryTree, val right: BinaryTree) : BinaryTree()

  data object Empty : BinaryTree()

  fun insert(
      value: Int,
      comparator: (Int, Int) -> Direction,
  ): BinaryTree {
    return when (this) {
      is Empty -> Node(value, Empty, Empty)
      is Node -> {
        when (comparator(this.value, value)) {
          Direction.LEFT -> Node(this.value, left.insert(value, comparator), right)
          Direction.RIGHT -> Node(this.value, left, right.insert(value, comparator))
        }
      }
    }
  }

  fun toList(): List<Int> {
    return when (this) {
      is Empty -> emptyList()
      is Node -> left.toList() + value + right.toList()
    }
  }
}

fun fixInvalidUpdate(update: List<Int>, structuredRules: Map<Int, Rules>): List<Int> {
  var tree: BinaryTree = BinaryTree.Empty

  update.forEach { value ->
    tree =
        tree.insert(value) { a, b ->
          val rulesForA = structuredRules[a] ?: return@insert Direction.RIGHT
          if (rulesForA.before.contains(b)) Direction.LEFT else Direction.RIGHT
        }
  }

  return tree.toList()
}

fun partTwo() {
  val orderingRules = mutableListOf<Pair<Int, Int>>()
  val updates = mutableListOf<List<Int>>()

  var isReadingRules = true
  File("input/day5_input.txt").forEachLine { line ->
    if (line.isEmpty()) {
      isReadingRules = false
      return@forEachLine
    }

    if (isReadingRules) {
      val (first, second) = line.split("|").map { it.toInt() }
      orderingRules.add(Pair(first, second))
    } else {
      val update = line.split(",").map { it.toInt() }
      updates.add(update)
    }
  }

  val structuredRules =
      orderingRules.fold(mutableMapOf<Int, Rules>()) { map, rule ->
        map[rule.first] =
            Rules(
                before = map[rule.first]?.before ?: emptySet(),
                after = map[rule.first]?.after?.plus(rule.second) ?: setOf(rule.second),
            )

        map[rule.second] =
            Rules(
                before = map[rule.second]?.before?.plus(rule.first) ?: setOf(rule.first),
                after = map[rule.second]?.after ?: emptySet(),
            )

        map
      }

  val invalidUpdates = updates.filter { !validateUpdate(it, structuredRules) }
  val fixed = invalidUpdates.map { fixInvalidUpdate(it, structuredRules) }

  val result = fixed.map { it[it.size / 2] }.sum()

  println(
      """
    fixed: ${fixed}
    result: ${result}
  """
          .trimIndent())
}
