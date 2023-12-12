fun main() {
    val memory = mutableMapOf<Pair<Int, Int>, Long>()
    fun fits(field: String, fieldInd: Int, damagedCnt: Int): Boolean {
        for (i in fieldInd ..< fieldInd + damagedCnt) {
            if (i == field.length || field[i] == '.') {
                return false
            }
        }
        return fieldInd + damagedCnt == field.length || field[fieldInd + damagedCnt] != '#'
    }

    fun countOptions(field: String, damaged: List<Int>, fieldInd: Int = 0, damagedInd: Int = 0): Long {
        if (damagedInd >= damaged.size && fieldInd >= field.length) {
            return 1
        }
        if (fieldInd >= field.length) {
            return 0
        }
        if (damagedInd >= damaged.size) {
            return if (field.subSequence(fieldInd, field.length).any { it == '#' }) 0 else 1
        }
        if (memory.contains(fieldInd to damagedInd)) {
            return memory[fieldInd to damagedInd]!!
        }
        var result = 0L
        if (field[fieldInd] != '#') {
            result += countOptions(field, damaged, fieldInd + 1, damagedInd)
        }
        if (field[fieldInd] != '.') {
            if (fits(field, fieldInd, damaged[damagedInd])) {
                result += countOptions(field, damaged, fieldInd + damaged[damagedInd] + 1, damagedInd + 1)
            }
        }
        memory[fieldInd to damagedInd] = result
        return result
    }

    fun part1(input: List<String>): Int {
        return input.sumOf {
            memory.clear()
            countOptions(it.split(' ')[0], it.split(' ')[1].split(',').map { it.toInt() })
        }.toInt()
    }

    fun String.repeat(times: Int, separator: String): String {
        return Array(times) { this }.joinToString(separator)
    }

    fun part2(input: List<String>): Long {
        return input.sumOf {
            memory.clear()
            countOptions(it.split(' ')[0].repeat(5, "?"), it.split(' ')[1].repeat(5, ",").split(',').map { it.toInt() })
        }
    }

    val testInput = readInput("Day12_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 525152L)

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}