import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/main/resources/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun gcd(x: Long, y: Long): Long {
    return if (y == 0L) x else gcd(y, x % y)
}

fun lcm(numbers: List<Long>): Long {
    return numbers.reduce { x, y -> x * (y / gcd(x, y)) }
}

fun readIntLists(input: List<String>, separator: String = " "): List<List<Int>> {
    return input.map { it.split(separator).map { v -> v.toInt() } }
}

fun readIntList(input: String, separator: String = " "): List<Int> {
    return input.split(separator).map { v -> v.toInt() }
}

fun parseMultipleInputs(input: List<String>): List<List<String>> {
    val result = mutableListOf<MutableList<String>>()
    result.add(mutableListOf())
    for (str in input) {
        if (str.isBlank()) {
            result.add(mutableListOf())
        } else {
            result.last().add(str)
        }
    }
    return result.filter { it.isNotEmpty() }
}

data class Point(val x: Int, val y: Int)

operator fun Point.unaryMinus() = Point(-x, -y)
operator fun Point.plus(other: Point) = Point(x + other.x, y + other.y)
operator fun Point.minus(other: Point) = Point(x - other.x, y - other.y)
operator fun Point.times(n: Int): Point = Point(x * n, y * n)

operator fun List<String>.get(coordinates: Point): Char {
    return this[coordinates.x][coordinates.y]
}

operator fun List<List<Int>>.get(coordinates: Point): Int {
    return this[coordinates.x][coordinates.y]
}

fun List<List<Int>>.inRange(coordinates: Point): Boolean {
    return coordinates.x in indices && coordinates.y in this[0].indices
}

fun List<String>.fitsInRange(coordinates: Point): Boolean {
    return coordinates.x in indices && coordinates.y in this[0].indices
}


enum class Direction(val delta: Point) {
    UP(Point(-1, 0)), LEFT(Point(0, -1)), DOWN(Point(1, 0)), RIGHT(Point(0, 1));
    val vertical
        get() = this == UP || this == DOWN

    val horizontal
        get() = !vertical

    val opposite
        get() = when (this) {
            UP -> DOWN
            LEFT -> RIGHT
            RIGHT -> LEFT
            DOWN -> UP
        }

    fun rotateRight(): Direction {
        return when (this) {
            UP -> RIGHT
            LEFT -> UP
            RIGHT -> DOWN
            DOWN -> LEFT
        }
    }

    fun rotateLeft(): Direction {
        return when (this) {
            UP -> LEFT
            LEFT -> DOWN
            RIGHT -> UP
            DOWN -> RIGHT
        }
    }
}

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
