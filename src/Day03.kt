data class Entry(val char: Char, val x: Int, val y: Int)

private fun List<String>.fit(x: Int, y: Int): Boolean {
    return x in indices && y in this[x].indices
}

fun fetchGears(field: List<String>): List<Entry> {
    val results = mutableListOf<Entry>()
    for (i in field.indices) {
        for (j in field.indices) {
            if (!field[i][j].isLetterOrDigit() && field[i][j] != '.') {
                results.add(Entry(field[i][j], i, j))
            }
        }
    }
    return results
}

fun getAdjacentNumbers(gear: Entry, input: List<String>): List<Int> {
    val results = mutableListOf<Int>()
    for (x in gear.x - 1 .. gear.x + 1) {
        if (input.fit(x, gear.y) && input[x][gear.y].isDigit()) {
            results.add(extractNumber(input, x, gear.y))
        } else {
            for (y in listOf(gear.y - 1, gear.y + 1)) {
                if (input.fit(x, y) && input[x][y].isDigit()) {
                    results.add(extractNumber(input, x, y))
                }
            }
        }
    }
    return results
}

fun extractNumber(input: List<String>, x: Int, y: Int): Int {
    var first = y
    while (first > 0 && input[x][first - 1].isDigit()) {
        first--
    }
    var last = y
    while (last < input[x].length - 1 && input[x][last + 1].isDigit()) {
        last++
    }
    return input[x].substring(first, last + 1).toInt()
}

fun main() {
    fun part1(input: List<String>): Int {
        val gears = fetchGears(input)
        return gears.flatMap { getAdjacentNumbers(it, input) }.sum()
    }


    fun part2(input: List<String>): Int {
        val gears = fetchGears(input)
        return gears.filter { it.char == '*' }.map { getAdjacentNumbers(it, input) }.filter { it.size == 2 }.sumOf { it[0] * it[1] }
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
