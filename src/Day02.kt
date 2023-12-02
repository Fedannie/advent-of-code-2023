import kotlin.math.max

fun main() {
    class Trial(val red: Int, val green: Int, val blue: Int) {
        fun pass(redTotal: Int, greenTotal: Int, blueTotal: Int): Boolean {
            return red <= redTotal && blue <= blueTotal && green <= greenTotal
        }
    }

    fun parseTrial(trial: String): Trial {
        var red = 0
        var green = 0
        var blue = 0

        trial.split(", ").forEach {
            val words = it.trim().split(' ')
            val number = words[0].toInt()
            val color = words[1]
            when (color) {
                "red" -> red = number
                "green" -> green = number
                "blue" -> blue = number
            }
        }
        return Trial(red, green, blue)
    }

    fun mergeTrial(trial1: Trial, trial2: Trial): Trial {
        return Trial(max(trial1.red, trial2.red), max(trial1.green, trial2.green), max(trial1.blue, trial2.blue))
    }

    fun parseGames(input: List<String>): List<Trial> {
        return input.map { game ->
            game.split(':')[1]
                .split(';')
                .map { parseTrial(it) }
                .reduce { game1, game2 -> mergeTrial(game1, game2) }
        }
    }

    fun part1(input: List<String>): Int {
        val redTotal = 12
        val greenTotal = 13
        val blueTotal = 14

        return parseGames(input)
            .mapIndexedNotNull { ind, game -> if (game.pass(redTotal, greenTotal, blueTotal)) ind + 1 else null }
            .sum()
    }

    fun part2(input: List<String>): Int {
        return parseGames(input).sumOf { game -> game.red * game.blue * game.green }
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
