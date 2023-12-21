fun main() {
    fun part1(input: List<String>, stepsN: Int): Int {
        val start = Point(input.indexOfFirst { it.contains('S') }, input.first { it.contains('S') }.indexOf('S'))
        var next = listOf(start)
        for (i in 0 ..< stepsN) {
            next = next.flatMap { point ->
                val result = mutableListOf<Point>()
                for (direction in Direction.entries) {
                    val nextPoint = direction.delta + point
                    if (input.fitsInRange(nextPoint) && input[nextPoint] != '#') {
                        result.add(nextPoint)
                    }
                }
                return@flatMap result
            }.distinct()
        }
        return next.size
    }

    fun cleverMod(i: Int, j: Int): Int {
        return (i % j + j) % j
    }

    fun f(n: Int, parameters: List<Int>): Long {
        val a0 = parameters[0].toLong()
        val a1 = parameters[1] - parameters[0].toLong()
        val a2 = parameters[2] - parameters[1].toLong()

        return a0 + a1 * n + (a2 - a1) * n * (n - 1) / 2
    }

    fun part2(input: List<String>, stepsN: Int): Long {
        val start = Point(input.indexOfFirst { it.contains('S') }, input.first { it.contains('S') }.indexOf('S'))
        var next = listOf(start)
        val parameters = mutableListOf<Int>()
        var i = 0
        while (parameters.size < 3) {
            val prevLength = next.size
            next = next.flatMap { point ->
                val result = mutableListOf<Point>()
                for (direction in Direction.entries) {
                    val nextPoint = direction.delta + point
                    if (input[Point(cleverMod(nextPoint.x, input.size), cleverMod(nextPoint.y, input[0].length))] != '#') {
                        result.add(nextPoint)
                    }
                }
                return@flatMap result
            }.distinct()
            if (i % input.size == stepsN % input.size) {
                parameters.add(prevLength)
            }
            i += 1
        }
        return f(stepsN / input.size, parameters)
    }

    val testInput = readInput("Day21_test")
    check(part1(testInput, 6) == 16)
    val testInput2 = readInput("Day21_test2")
    println(part2(testInput2, 5000))
    check(part2(testInput2, 5000) == 16900669L)

    val input = readInput("Day21")
    part1(input, 64).println()
    part2(input, 26501365).println()
}
