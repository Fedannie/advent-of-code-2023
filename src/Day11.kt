import kotlin.math.abs
import kotlin.math.min
import kotlin.math.max

fun main() {
    fun List<Int>.getRange(from: Int, to: Int): List<Int> {
        val startInd = indexOfFirst { it >= min(from, to) }
        val endInd = indexOfLast { it <= max(to, from) }
        if (startInd >= 0 && endInd >= 0 && startInd <= endInd) {
            return subList(indexOfFirst { it >= min(from, to) }, indexOfLast { it <= max(to, from) } + 1)
        }
        return emptyList()
    }

    class Field(val map: List<String>, val expansion: Int = 1) {
        val galaxies: List<Point>
            get() {
                val result = mutableListOf<Point>()
                for (rowInd in map.indices) {
                    for (columnInd in map[0].indices) {
                        if (map[rowInd][columnInd] == '#') {
                            result.add(Point(rowInd, columnInd))
                        }
                    }
                }
                return result
            }

        val emptyRows = map.mapIndexedNotNull { index, s -> if (s.all { it == '.' }) index else null }
        val emptyCols = map[0].mapIndexedNotNull { index, s -> if (map.all { it[index] == '.' }) index else null }

        fun getDist(g1: Point, g2: Point): Int {
            return abs(g1.x - g2.x) + abs(g1.y - g2.y) +
                    emptyCols.getRange(g1.y, g2.y).size * expansion +
                    emptyRows.getRange(g1.x, g2.x).size * expansion
        }
    }

    fun solve(input: List<String>, expansion: Int = 1): Long {
        val field = Field(input, expansion)
        var result = 0L
        for (g1 in field.galaxies) {
            for (g2 in field.galaxies) {
                result += field.getDist(g1, g2).toLong()
            }
        }
        return result / 2
    }

    fun part1(input: List<String>): Int = solve(input).toInt()
    fun part2(input: List<String>, expansion: Int = 1000000): Long = solve(input, expansion - 1)

    val testInput = readInput("Day11_test")
    check(part1(testInput) == 374)
    check(part2(testInput, 10) == 1030L)
    check(part2(testInput, 100) == 8410L)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}