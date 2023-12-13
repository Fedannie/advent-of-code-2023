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

operator fun List<String>.get(coordinates: Point): Char {
    return this[coordinates.x][coordinates.y]
}
