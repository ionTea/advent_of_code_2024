package day9

import java.io.File

fun main() {
  partOne()
  partTwo()
}

sealed class Block {
  abstract val index: Long
  abstract val size: Long

  data class FileBlock(override val index: Long, override val size: Long, val fileId: Long) :
      Block()

  data class EmptyBlock(override val index: Long, override val size: Long) : Block()
}

fun parseInput(input: List<Long>): List<Block> {
  return input.mapIndexed { index, line ->
    val i = index.toLong()
    val isEmptyBlock = index % 2 != 0
    if (isEmptyBlock) {
      Block.EmptyBlock(index = i, size = line)
    } else {
      Block.FileBlock(index = i, fileId = i / 2, size = line)
    }
  }
}

fun partOne() {
  val input =
      File("input/day9_input_simple.txt").readLines().flatMap { line ->
        line.split("").mapNotNull { if (it.isNotEmpty()) it.toLong() else null }
      }

  val blocks = parseInput(input)
  val emptyBlocks = blocks.filterIsInstance<Block.EmptyBlock>().toMutableList()
  val fileBlocks = blocks.filterIsInstance<Block.FileBlock>().toMutableList()

  while (emptyBlocks.isNotEmpty()) {
    fileBlocks.sortBy { it.index }
    emptyBlocks.sortBy { it.index }
    val emptyBlock = emptyBlocks.removeFirst()
    val file = fileBlocks.removeLast()

    if (emptyBlock.index > file.index) {
      fileBlocks.add(file)
      break
    }

    if (file.size == emptyBlock.size) {
      fileBlocks.add(
          Block.FileBlock(index = emptyBlock.index, fileId = file.fileId, size = file.size))
    } else if (file.size < emptyBlock.size) {
      // split the empty block and add back the remaining
      val newFileBlock =
          Block.FileBlock(index = emptyBlock.index, fileId = file.fileId, size = file.size)
      val remainingEmptyBlock = emptyBlock.copy(size = emptyBlock.size - file.size)
      emptyBlocks.addFirst(remainingEmptyBlock)
      fileBlocks.add(newFileBlock)
    } else {
      // split the file block and add back the remaining
      val newFileBlock =
          Block.FileBlock(index = emptyBlock.index, fileId = file.fileId, size = emptyBlock.size)
      val remainingFileBlock = file.copy(size = file.size - emptyBlock.size)
      fileBlocks.add(remainingFileBlock)
      fileBlocks.add(newFileBlock)
    }
  }

  fileBlocks.sortBy { it.index }

  val sum =
      fileBlocks
          .flatMap { file -> (0..<file.size).map { file.fileId } }
          .mapIndexed() { index, fileId -> fileId * index }
          .sum()

  println("Sum: $sum")
}

fun partTwo() {
  val input =
      File("input/day9_input.txt").readLines().flatMap { line ->
        line.split("").mapNotNull { if (it.isNotEmpty()) it.toLong() else null }
      }

  val blocks = parseInput(input)
  val emptyBlocks = blocks.filterIsInstance<Block.EmptyBlock>().toMutableList()
  val fileBlocks = blocks.filterIsInstance<Block.FileBlock>().reversed().toMutableList()

  val newFileBlocks = mutableListOf<Block.FileBlock>()

  for (file in fileBlocks) {
    emptyBlocks.sortBy { it.index }
    val emptyBlock = emptyBlocks.firstOrNull { it.size >= file.size && it.index < file.index }

    if (emptyBlock == null) {
      newFileBlocks.add(file)
      continue
    }

    emptyBlocks.remove(emptyBlock)
    if (emptyBlock.size > file.size) {
      emptyBlocks.add(
          Block.EmptyBlock(index = emptyBlock.index, size = emptyBlock.size - file.size))
    }

    newFileBlocks.add(file.copy(index = emptyBlock.index))
    emptyBlocks.add(Block.EmptyBlock(index = file.index, size = file.size))
  }

  val completeList = (newFileBlocks + emptyBlocks).sortedBy { it.index }

  val sum =
      completeList
          .flatMap { file ->
            (0..<file.size).map {
              when (file) {
                is Block.EmptyBlock -> 0
                is Block.FileBlock -> file.fileId
              }
            }
          }
          .mapIndexed() { index, fileId -> fileId * index }
          .sum()

  println("Sum: $sum")
}
