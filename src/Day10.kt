enum class Direction(val delta: Point) {
    UP(Point(-1, 0)), LEFT(Point(0, -1)), DOWN(Point(1, 0)), RIGHT(Point(0, 1));

    fun fits(pipe: Char): Boolean {
        return when (this) {
            UP -> pipe == '|' || pipe == '7' || pipe == 'F'
            LEFT -> pipe == 'F' || pipe == '-' || pipe == 'L'
            DOWN -> pipe == '|' || pipe == 'L' || pipe == 'J'
            RIGHT -> pipe == 'J' || pipe == '-' || pipe == '7'
        }
    }

    fun next(pipe: Char): Direction {
        return when (this) {
            UP -> if (pipe == '7') LEFT else if (pipe == 'F') RIGHT else this
            LEFT -> if (pipe == 'F') DOWN else if (pipe == 'L') UP else this
            DOWN -> if (pipe == 'L') RIGHT else if (pipe == 'J') LEFT else this
            RIGHT -> if (pipe == 'J') UP else if (pipe == '7') DOWN else this
        }
    }
}

fun main() {
    fun getDirection(p1: Point, p2: Point): Direction {
        val diff = p1 - p2
        return if (diff.x == -1) {
            Direction.DOWN
        } else if (diff.x == 1) {
            Direction.UP
        } else if (diff.y == -1) {
            Direction.RIGHT
        } else {
            Direction.LEFT
        }
    }

    fun findStart(field: List<String>): Point {
        val x = field.indexOfFirst { it.contains('S') }
        val y = field[x].indexOf('S')
        return Point(x, y)
    }

    fun isInside(point: Point, field: List<String>): Boolean {
        return point.x in field.indices && point.y in 0 ..< field[0].length
    }

    fun findNext(start: Point, field: List<String>): Point {
        for (direction in Direction.entries) {
            val next = start + direction.delta
            if (isInside(next, field) && direction.fits(field[next])) {
                return next
            }
        }
        return start
    }

    fun findLoop(start: Point, field: List<String>): List<Point> {
        val result = mutableListOf(start)
        var current = start
        var next = findNext(start, field)
        while (next != start) {
            val tmp = getDirection(current, next).next(field[next])
            current = next
            next += tmp.delta
            result.add(current)
        }
        return result
    }

    fun part1(input: List<String>): Int {
        val start = findStart(input)
        return findLoop(start, input).size / 2
    }

    fun actsAs(ch1: Char, ch2: Char, loop: List<Point>): Boolean {
        if (ch1 != 'S') {
            return ch1 == ch2
        }
        return when (ch2) {
            'L' -> (loop[1].x - loop[0].x == -1 && loop.last().y - loop[0].y == 1) || (loop.last().x - loop[0].x == -1 && loop[1].y - loop[0].y == 1)
            '7' -> (loop[1].y - loop[0].y == -1 && loop.last().x - loop[0].x == 1) || (loop.last().y - loop[0].y == -1 && loop[1].x - loop[0].x == 1)
            'F' -> (loop[1].y - loop[0].y == 1 && loop.last().x - loop[0].x == 1) || (loop.last().y - loop[0].y == 1 && loop[1].x - loop[0].x == 1)
            'J' -> (loop[1].x - loop[0].x == -1 && loop.last().y - loop[0].y == -1) || (loop.last().x - loop[0].x == -1 && loop[1].y - loop[0].y == -1)
            else -> false
        }
    }

    fun countLines(
        loop: List<Point>,
        field: List<String>,
        pairs: List<Pair<Char, Char>>,
        filterFunction: (Point) -> Boolean
    ): Int {
        val lines = loop.filter(filterFunction).sortedWith(compareBy<Point> { it.x }.thenBy { it.y })
        var i = 0
        var cnt = 0
        while (i < lines.size) {
            var found = false
            for (pair in pairs) {
                if (actsAs(field[lines[i]], pair.first, loop) && actsAs(field[lines[i + 1]], pair.second, loop)) {
                    cnt++
                    i += 2
                    found = true
                    break
                }
            }
            if (!found) {
                cnt++
                i++
            }
        }
        return cnt
    }

    fun isEnclosed(point: Point, loop: List<Point>, field: List<String>): Boolean {
        if (loop.contains(point)) {
            return false
        }

        return countLines(loop, field, listOf('L' to '7', 'F' to 'J')) { s: Point -> s.x == point.x && s.y < point.y && field[s] != '-' } % 2 == 1 ||
                countLines(loop, field, listOf('L' to '7', 'F' to 'J')) { s: Point -> s.x == point.x && s.y > point.y && field[s] != '-' } % 2 == 1 ||
                countLines(loop, field, listOf('7' to 'L', 'F' to 'J')) { s: Point -> s.x < point.x && s.y == point.y && field[s] != '|' } % 2 == 1 ||
                countLines(loop, field, listOf('7' to 'L', 'F' to 'J')) { s: Point -> s.x > point.x && s.y == point.y && field[s] != '|' } % 2 == 1
    }

    fun part2(input: List<String>): Int {
        val start = findStart(input)
        val loop = findLoop(start, input)
        var result = 0
        for (i in input.indices) {
            for (j in input[0].indices) {
                if (isEnclosed(Point(i, j), loop, input)) {
                    result++
                }
            }
        }
        return result
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 8)
    val testInput2 = readInput("Day10_test2")
    check(part2(testInput2) == 8)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}