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