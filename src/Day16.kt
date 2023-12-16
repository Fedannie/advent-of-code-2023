import kotlin.math.max

fun main() {
    fun Direction.isSplit(tile: Char): Boolean = (vertical && tile == '-') || (horizontal && tile == '|')
    fun Direction.getNextDirection(tile: Char): Direction {
        return when (tile) {
            '\\' -> when (this) {
                Direction.UP -> Direction.LEFT
                Direction.LEFT -> Direction.UP
                Direction.DOWN -> Direction.RIGHT
                Direction.RIGHT -> Direction.DOWN
            }
            '/' -> when (this) {
                Direction.UP -> Direction.RIGHT
                Direction.LEFT -> Direction.DOWN
                Direction.DOWN -> Direction.LEFT
                Direction.RIGHT -> Direction.UP
            }
            '|' -> if (this.vertical) this else Direction.UP
            '-' -> if (this.horizontal) this else Direction.LEFT
            else -> this
        }
    }

    fun energize(field: List<String>, start: Point, direction: Direction): Int {
        val memory = hashSetOf<Pair<Point, Direction>>()
        val queue = mutableListOf(start - direction.delta to direction)
        while (queue.isNotEmpty()) {
            val current = queue.removeAt(0)
            if (memory.contains(current)) {
                continue
            }
            memory.add(current)
            val next = current.first + current.second.delta
            if (next.x in field.indices && next.y in field[0].indices) {
                val tile = field[next]
                queue.add(next to current.second.getNextDirection(tile))
                if (current.second.isSplit(tile)) {
                    queue.add(next to queue.last().second.opposite)
                }
            }
        }
        return memory.map { it.first }.distinct().size - 1
    }

    fun part1(input: List<String>): Int {
        return energize(input, Point(0, 0), Direction.RIGHT)
    }

    fun part2(input: List<String>): Int {
        var result = 0
        for (i in input.indices) {
            result = max(result, energize(input, Point(i, 0), Direction.RIGHT))
            result = max(result, energize(input, Point(i, input[0].length - 1), Direction.LEFT))
        }
        for (j in input[0].indices) {
            result = max(result, energize(input, Point(0, j), Direction.DOWN))
            result = max(result, energize(input, Point(input.size - 1, j), Direction.UP))
        }
        return result
    }

    val testInput = readInput("Day16_test")
    check(part1(testInput) == 46)
    check(part2(testInput) == 51)

    val input = readInput("Day16")
    part1(input).println()
    part2(input).println()
}