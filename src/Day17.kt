import java.util.PriorityQueue

fun main() {
    data class Movement(val heatLoss: Int, val position: Point, val direction: Direction, val straight: Int)

    fun <R> PriorityQueue<R>.addIfNew(element: R) {
        if (!contains(element)) {
            add(element)
        }
    }

    fun bfs(field: List<List<Int>>, start: Point, end: Point, minStraight: Int = 0, maxStraight: Int = 3): Int {
        val queue = PriorityQueue(compareBy<Movement> { it.heatLoss }.thenBy { -it.position.x }.thenBy { -it.position.y })
        queue.add(Movement(0, start, Direction.RIGHT, 0))
        queue.add(Movement(0, start, Direction.DOWN, 0))
        val visited = mutableSetOf(start to Direction.RIGHT to 0)
        visited += start to Direction.DOWN to 0
        while (queue.isNotEmpty()) {
            val current = queue.remove()
            if (current.position == end && current.straight in minStraight .. maxStraight ) {
                return current.heatLoss
            }
            visited.add(current.position to current.direction to current.straight)
            val stepStraight = current.position + current.direction.delta
            if (field.inRange(stepStraight) && current.straight < maxStraight && !visited.contains(stepStraight to current.direction to current.straight + 1)) {
                queue.addIfNew(Movement(
                    current.heatLoss + field[stepStraight],
                    stepStraight,
                    current.direction,
                    current.straight + 1))
            }
            for (nextDirection in listOf(current.direction.rotateRight(), current.direction.rotateLeft())) {
                val next = current.position + nextDirection.delta
                if (field.inRange(next) && current.straight >= minStraight && !visited.contains(next to nextDirection to 1)) {
                    queue.addIfNew(Movement(
                        current.heatLoss + field[next],
                        next,
                        nextDirection,
                        1))
                }
            }
        }
        return 0
    }

    fun part1(input: List<String>): Int {
        return bfs(input.map { it.map { it.toString().toInt() } }, Point(0, 0),  Point(input.size - 1, input[0].length - 1))
    }

    fun part2(input: List<String>): Int {
        return bfs(input.map { it.map { it.toString().toInt() } }, Point(0, 0),  Point(input.size - 1, input[0].length - 1), 4, 10)
    }

    val testInput = readInput("Day17_test")
    check(part1(testInput) == 102)
    check(part2(testInput) == 94)
    val testInput2 = readInput("Day17_test2")
    check(part2(testInput2) == 71)

    val input = readInput("Day17")
    part1(input).println()
    part2(input).println()
}