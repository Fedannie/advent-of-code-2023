fun main() {
    class Lottery(winningNumbers: Set<Int>, allNumbers: List<Int>) {
        val score: Int = allNumbers.count { winningNumbers.contains(it) }
    }

    fun parse(str: String): Lottery {
        val winning = str.split(": ")[1].split(" | ")[0].trim().split(' ').filter { it.isNotEmpty() }.map { it.toInt() }.toSet()
        val all = str.split(" | ")[1].trim().split(' ').filter { it.isNotEmpty() }.map { it.toInt() }.toList()
        return Lottery(winning, all)
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { (1 shl parse(it).score) / 2 }
    }

    fun part2(input: List<String>): Int {
        val scores = input.map { parse(it).score }
        val counts = MutableList(scores.size) { 1 }
        for (card in scores.indices) {
            for (i in 1..scores[card]) {
                counts[card + i] += counts[card]
            }
        }
        return counts.sum()
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
