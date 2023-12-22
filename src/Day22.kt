import kotlin.math.max
import kotlin.math.min

fun main() {
    data class Point3D(val x: Int, val y: Int, var z: Int) {
        constructor(coordinates: List<Int>) : this(coordinates[0], coordinates[1], coordinates[2])
    }

    fun intersect(rect1: Pair<Point, Point>, rect2: Pair<Point, Point>): Boolean {
        return (rect1.first.x in min(rect2.first.x, rect2.second.x) .. max(rect2.first.x, rect2.second.x) ||
                rect1.second.x in min(rect2.first.x, rect2.second.x) .. max(rect2.first.x, rect2.second.x) ||
                rect2.first.x in min(rect1.first.x, rect1.second.x) .. max(rect1.first.x, rect1.second.x) ||
                rect2.second.x in min(rect1.first.x, rect1.second.x) .. max(rect1.first.x, rect1.second.x)) &&
                (rect1.first.y in min(rect2.first.y, rect2.second.y) .. max(rect2.first.y, rect2.second.y) ||
                        rect1.second.y in min(rect2.first.y, rect2.second.y) .. max(rect2.first.y, rect2.second.y) ||
                        rect2.first.y in min(rect1.first.y, rect1.second.y) .. max(rect1.first.y, rect1.second.y) ||
                        rect2.second.y in min(rect1.first.y, rect1.second.y) .. max(rect1.first.y, rect1.second.y))
    }

    data class Brick(val from: Point3D, val to: Point3D) {
        val supporters = mutableListOf<Brick>()
        val supporting = mutableListOf<Brick>()
        var name: Char = 'a'

        val maxZ: Int
            get() = max(from.z, to.z)
        val projection: Pair<Point, Point>
            get() = Point(from.x, from.y) to Point(to.x, to.y)

        constructor(points: List<Point3D>): this(points[0], points[1])

        fun below(brick: Brick): Boolean {
            return intersect(brick.projection, projection) && to.z < brick.from.z
        }

        override fun toString(): String {
            return "${supporters.map{it.name}.joinToString(",")} -> ${name} -> ${supporting.map{it.name}.joinToString(",")}"
        }
    }

    fun parseInput(input: List<String>): List<Brick> {
        return input.map { brick -> Brick(brick.split('~').map { point -> Point3D(point.split(',').map{ it.toInt() }) }) }.mapIndexed { ind, brick -> brick.name = 'A' + ind; brick }
    }

    fun getSupporters(bricks: List<Brick>, brick: Brick): List<Brick> {
        val allBricksBelow = bricks.filter { it != brick && it.below(brick) }.sortedByDescending { it.maxZ }
        return allBricksBelow.filter { it.maxZ == allBricksBelow[0].maxZ }
    }

    fun letEmFall(bricks: List<Brick>) {
        for (brick in bricks.sortedBy { brick -> min(brick.from.z, brick.to.z) }) {
            val height = brick.to.z - brick.from.z
            brick.from.z = (getSupporters(bricks, brick).firstOrNull()?.to?.z ?: 0) + 1
            brick.to.z = brick.from.z + height
        }
    }

    fun part1(input: List<String>): Int {
        val bricks = parseInput(input)
        letEmFall(bricks)
        for (brick in bricks) {
            brick.supporters.addAll(getSupporters(bricks, brick))
            for (supporter in brick.supporters) {
                supporter.supporting.add(brick)
            }
        }
        return bricks.count { brick -> brick.supporting.all { next -> next.supporters.size > 1 } }
    }

    fun countChain(brick: Brick): Int {
        val visited = hashSetOf(brick)
        var queue = listOf(brick)
        while (queue.isNotEmpty()) {
            visited.addAll(queue)
            queue = queue.flatMap { q -> q.supporting.filter { s -> !visited.contains(s) && s.supporters.all { visited.contains(it) } } }.distinct()
        }
        return visited.size - 1
    }

    fun part2(input: List<String>): Int {
        val bricks = parseInput(input)
        letEmFall(bricks)
        for (brick in bricks) {
            brick.supporters.addAll(getSupporters(bricks, brick))
            for (supporter in brick.supporters) {
                supporter.supporting.add(brick)
            }
        }
        println(bricks.map { brick -> countChain(brick) }.joinToString(","))
        return bricks.sumOf { brick -> countChain(brick) }
    }

    val testInput = readInput("Day22_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 7)

    val input = readInput("Day22")
    part1(input).println()
    part2(input).println()
}
