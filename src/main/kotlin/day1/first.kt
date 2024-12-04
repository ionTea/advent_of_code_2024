package com.iontea.first

import java.io.File
import java.util.PriorityQueue
import kotlin.math.abs

fun main() {
    partOne()
    partTwo()
}

fun partOne() {
    val firstList = PriorityQueue<Int>()
    val secondList = PriorityQueue<Int>()

    File("input/first_input.txt").forEachLine { line ->
        println(line)
        val (first, second) = line.split("   ")
        firstList.add(first.toInt())
        secondList.add(second.toInt())
    }


    var totalDistance = 0
    for (i in 0 until firstList.size) {
        totalDistance += abs(firstList.poll() - secondList.poll())
    }

    println("Response = $totalDistance")
}

fun partTwo() {
    val firstList = mutableMapOf<Int, Int>()
    val secondList = mutableMapOf<Int, Int>()

    File("input/first_input.txt").forEachLine { line ->
        val (first, second) = line.split("   ").map { it.toInt() }
        firstList[first] = firstList.getOrDefault(first, 0) + 1
        secondList[second] = secondList.getOrDefault(second, 0) + 1
    }


    val result = firstList.map {
        val number = it.key
        val occurrences = it.value

        number * occurrences * secondList.getOrDefault(number, 0)
    }.sum()

    println("Result: $result")
}
