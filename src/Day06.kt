fun main() {
    fun parse(input: String): List<Int> {
        return input.split(':')[1].split(' ').filter { it.isNotEmpty() }.map { it.toInt() }
    }

    fun parseOne(input: String): Long {
        return input.split(':')[1].split(' ').filter { it.isNotEmpty() }.flatMap { it.toCharArray().asList() }.map { it.toString().toInt() }.joinToString("").toLong()
    }

    fun fits(t: Long, time: Long, distance: Long): Boolean {
        return t * (time - t) > distance
    }

    fun count(time: Long, distance: Long): Long {
        var result = 0L
        for (i in 0..time) {
            if (fits(i, time, distance)) {
                result++
            }
        }
        return result
    }

    fun part1(input: List<String>): Long {
        val times = parse(input[0])
        val distances = parse(input[1])
        var result = 1L

        for (round in times.indices) {
            result *= count(times[round].toLong(), distances[round].toLong())
        }

        return result
    }

    fun part2(input: List<String>): Long {
        val time = parseOne(input[0])
        val distance = parseOne(input[1])

        return count(time, distance)
    }

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288L)
    check(part2(testInput) == 71503L)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
