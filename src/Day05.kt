import kotlin.math.min

fun main() {
    class Mapping(val target: Long, val source: Long, val distance: Long) {
        fun contains(n: Long): Boolean {
            return n in source ..< source + distance
        }

        fun getTarget(n: Long): Long {
            return target + n - source
        }

        fun intersect(interval: Pair<Long, Long>): Boolean {
            return source in interval.first ..< interval.first + interval.second || interval.first in source ..< source + distance
        }

        fun getMapping(interval: Pair<Long, Long>): Pair<Long, Long> {
            return target + interval.first - source to min(distance - interval.first + source, interval.second)
        }
    }

    fun parseMappings(input: List<String>): List<List<Mapping>> {
        val result = mutableListOf<MutableList<Mapping>>()
        for (line in input) {
            if (line.isEmpty()) {
                continue
            }
            if (line.contains(':')) {
                result.add(mutableListOf())
            } else {
                val values = line.split(' ').map { it.toLong() }
                result.last().add(Mapping(values[0], values[1], values[2]))
            }
        }
        return result.map { it.sortedBy { mapping -> mapping.source } }
    }

    fun processMappings(intervals: List<Pair<Long, Long>>, mappings: List<Mapping>): List<Pair<Long, Long>> {
        val result = mutableListOf<Pair<Long, Long>>()
        for (i in intervals.indices) {
            val interval = intervals[i]
            var last = interval.first
            for (mapping in mappings) {
                if (mapping.intersect(interval)) {
                    if (mapping.source > last) {
                        result.add(last to mapping.source - last)
                    }
                    result.add(mapping.getMapping(last to interval.first + interval.second - last))
                    last = mapping.source + mapping.distance
                }
            }
            if (last < interval.first + interval.second) {
                result.add(last to interval.first + interval.second - last)
            }
        }
        return result
    }

    fun part1(input: List<String>): Long {
        val seeds = input[0].split(": ")[1].split(' ').map { it.toLong() }
        val mappings = parseMappings(input.subList(2, input.size))

        return seeds.minOf {
            var currentValue = it
            for (i in mappings.indices) {
                for (mapping in mappings[i]) {
                    if (mapping.contains(currentValue)) {
                        currentValue = mapping.getTarget(currentValue)
                        break
                    }
                }

            }
            currentValue
        }
    }

    fun part2(input: List<String>): Long {
        var intervals = input[0].split(": ")[1].split(' ').map { it.toLong() }.zipWithNext().filterIndexed { index, _ -> index % 2 == 0 }
        val mappings = parseMappings(input.subList(2, input.size))

        for (mappingList in mappings) {
            intervals = processMappings(intervals, mappingList)
        }
        return intervals.minOf { it.first }
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
