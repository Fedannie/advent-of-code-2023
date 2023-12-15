fun main() {
    fun tilt(
        fieldPrev: List<List<Char>>,
        colRange: IntProgression,
        rowRange: IntProgression,
        findPlace: (Int, Int, List<List<Char>>) -> Pair<Int, Int>
    ): List<List<Char>> {
        val field: List<MutableList<Char>> = fieldPrev.map { it.toMutableList() }
        for (col in colRange) {
            for (row in rowRange) {
                if (field[row][col] == 'O') {
                    field[row][col] = '.'
                    val newPosition = findPlace(row, col, field)
                    field[newPosition.first][newPosition.second] = 'O'
                }
            }
        }
        return field
    }

    fun tilt(field: List<List<Char>>, direction: Int): List<List<Char>> {
        return when (direction) {
            0 -> tilt(field, 0 ..< field[0].size, field.indices) { row, col, field ->
                var i = row - 1
                while (i >= 0 && field[i][col] == '.') {
                    i--
                }
                i + 1 to col
            }
            1 -> tilt(field, 0 ..< field[0].size, field.indices) { row, col, field ->
                var i = col - 1
                while (i >= 0 && field[row][i] == '.') {
                    i--
                }
                row to i + 1
            }
            2 -> tilt(field, 0 ..< field[0].size, field.size - 1 downTo 0) { row, col, field ->
                var i = row + 1
                while (i < field.size && field[i][col] == '.') {
                    i++
                }
                i - 1 to col
            }
            else -> tilt(field, field[0].size - 1 downTo 0, field.size - 1 downTo 0) { row, col, field ->
                var i = col + 1
                while (i < field[0].size && field[row][i] == '.') {
                    i++
                }
                row to i - 1
            }
        }
    }

    fun count(field: List<List<Char>>): Int {
        return field.mapIndexed { ind, line -> (field.size - ind) * line.count { it == 'O' } }.sum()
    }

    fun part1(input: List<String>): Int {
        return count(tilt(input.map { it.toCharArray().toList() }, 0))
    }

    fun List<List<Char>>.encode(): String {
        return joinToString { it.joinToString() }
    }


    fun cycle(field: List<List<Char>>): List<List<Char>> {
        var next = field
        for (i in 0..< 4) {
            next = tilt(next, i)
        }
        return next
    }

    fun part2(input: List<String>): Int {
        var field = input.map { it.toCharArray().toList() }
        val memory = hashMapOf(field.encode() to 0)
        var i = 0
        while (i < 1000000000) {
            field = cycle(field)
            i++
            if (i < 500 && memory.contains(field.encode())) {
                val cycleLength = i - memory[field.encode()]!!
                while (i < 1000000000 - cycleLength) {
                    i += cycleLength
                }
            }
            memory[field.encode()] = i
        }
        return count(field)
    }

    val testInput = readInput("Day14_test")
    check(part1(testInput) == 136)
    check(part2(testInput) == 64)

    val input = readInput("Day14")
    part1(input).println()
    part2(input).println()
}