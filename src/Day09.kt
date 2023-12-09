import kotlin.math.abs

fun main() {
    fun findDifferences(values: List<Int>): List<List<Int>> {
        val differences = mutableListOf(values)
        while (differences.last().any { it != 0 }) {
            differences.add(differences.last().zipWithNext { a, b -> b - a })
        }
        return differences
    }

    fun predict(differences: List<List<Int>>, update: (List<Int>, Int) -> Int): Int {
        return differences.foldRight(0, update)
    }

    fun findNext(differences: List<List<Int>>): Int {
        return predict(differences) { values, c -> c + values.last() }
    }

    fun findPrev(differences: List<List<Int>>): Int {
        return predict(differences) { values, c -> values[0] - c }
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { findNext(findDifferences(readIntList(it))) }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { findPrev(findDifferences(readIntList(it))) }
    }

    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114)
    check(part2(testInput) == 2)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}