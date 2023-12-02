fun main() {
    val numbers = mapOf("one" to 1, "two" to 2, "three" to 3, "four" to 4, "five" to 5, "six" to 6, "seven" to 7, "eight" to 8, "nine" to 9, "1" to 1, "2" to 2, "3" to 3, "4" to 4, "5" to 5, "6" to 6, "7" to 7, "8" to 8, "9" to 9)

    fun part1(input: List<String>): Int {
        return input.map { it.filter { ch -> ch.isDigit() } }
            .sumOf { it[0].toString().toInt() * 10 + it.last().toString().toInt() }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { str ->
            numbers[str.findAnyOf(numbers.keys)?.second]!! * 10 + numbers[str.findLastAnyOf(numbers.keys)?.second]!!
        }
    }

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)
    val testInput2 = readInput("Day01_test2")
    check(part2(testInput2) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
