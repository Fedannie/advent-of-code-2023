import java.math.BigInteger

fun main() {
    data class LPoint(val x: BigInteger, val y: BigInteger)
    data class Entry(val direction: Direction, val n: BigInteger)

    class Field {
        var current = LPoint(BigInteger.ZERO, BigInteger.ZERO)
        val points = mutableListOf(current)

        fun dig(entry: Entry): Field {
            current = LPoint(current.x + BigInteger.valueOf(entry.direction.delta.x.toLong()) * entry.n, current.y + BigInteger.valueOf(entry.direction.delta.y.toLong()) * entry.n)
            points.add(current)
            return this
        }

        private val area: BigInteger
            get() = points.zipWithNext { p1, p2 -> (p1.x + p2.x) * (p1.y - p2.y) }.fold(BigInteger.ZERO) { s, el -> s + el } / BigInteger.TWO

        private val boundary: BigInteger
            get() = points.zipWithNext { p1, p2 -> ((p2.x - p1.x).abs() + BigInteger.ONE) * ((p2.y - p1.y).abs() + BigInteger.ONE) }.fold(BigInteger.ZERO) { s, el -> s + el } - BigInteger.valueOf(points.size.toLong()) + BigInteger.ONE

        fun getInterior(): BigInteger {
            return area + boundary / BigInteger.TWO + BigInteger.ONE
        }
    }

    fun parseEntryPart1(entry: String): Entry {
        return Entry(Direction.entries.find { direction -> direction.name[0] == entry.split(' ')[0][0] }!!, entry.split(' ')[1].toBigInteger())
    }

    fun parseEntryPart2(entry: String): Entry {
        val lastWord = entry.uppercase().split('#')[1].split(')')[0]
        val direction = when (lastWord[lastWord.length - 1]) {
            '0' -> Direction.RIGHT
            '1' -> Direction.DOWN
            '2' -> Direction.LEFT
            else -> Direction.UP
        }
        return Entry(direction, lastWord.substring(0, lastWord.length - 1).toBigInteger(16))
    }

    fun solve(input: List<String>, parseEntry: (String) -> Entry): BigInteger {
        return input.map { parseEntry(it) }.fold(Field()) { field, entry -> field.dig(entry) }.getInterior()
    }

    fun part1(input: List<String>): BigInteger = solve(input, ::parseEntryPart1)

    fun part2(input: List<String>): BigInteger = solve(input, ::parseEntryPart2)

    val testInput = readInput("Day18_test")
    check(part1(testInput).toLong() == 62L)
    check(part2(testInput).toLong() == 952408144115)

    val input = readInput("Day18")
    part1(input).println()
    part2(input).println()
}
