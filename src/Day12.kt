fun main() {
    fun reflectsX(f1: List<String>, f2: List<String>, differencesAllowed: Int = 0): Boolean {
        if (f1.isEmpty() || f2.isEmpty()) {
            return false
        }
        var differences = 0
        for (i in f1.size - 1 downTo 0) {
            if (f1.size - 1 - i >= f2.size) {
                break
            }
            for (j in f1[0].indices) {
                if (f1[i][j] != f2[f1.size - 1 - i][j]) {
                    differences++
                }
            }
        }
        return differences == differencesAllowed
    }

    fun reflectsY(f1: List<String>, f2: List<String>, differencesAllowed: Int = 0): Boolean {
        if (f1[0].isEmpty() || f2[0].isEmpty()) {
            return false
        }
        var differences = 0
        for (i in f1[0].length - 1 downTo 0) {
            if (f1[0].length - 1 - i >= f2[0].length) {
                break
            }
            for (j in f1.indices) {
                if (f1.map { it[i] }[j] != f2.map { it[f1[0].length - 1 - i] }[j]) {
                    differences++
                }
            }
        }
        return differences == differencesAllowed
    }

    fun findHorizontalReflection(field: List<String>, differencesAllowed: Int = 0): Int {
        for (i in field.indices) {
            if (reflectsX(field.subList(0, i + 1), field.subList(i + 1, field.size), differencesAllowed)) {
                return i + 1
            }
        }
        return 0
    }

    fun findVerticalReflection(field: List<String>, differencesAllowed: Int = 0): Int {
        for (i in field[0].indices) {
            if (reflectsY(field.map { it.substring(0, i + 1) }, field.map { it.substring(i + 1) }, differencesAllowed)) {
                return i + 1
            }
        }
        return 0
    }

    fun part1(input: List<String>): Int {
        return parseMultipleInputs(input).sumOf {
            100 * findHorizontalReflection(it) + findVerticalReflection(it)
        }
    }

    fun part2(input: List<String>): Int {
        return parseMultipleInputs(input).sumOf {
            100 * findHorizontalReflection(it, 1) + findVerticalReflection(it, 1)
        }
    }

    val testInput = readInput("Day13_test")
    check(part1(testInput) == 405)
    check(part2(testInput) == 400)

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}