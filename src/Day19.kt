data class Item(val x: Int, val m: Int, val a: Int, val s: Int)
data class Rule(
    val field: Char,
    val less: Boolean,
    val value: Int,
    val next: String,
    val accepted: Boolean = false,
    val rejected: Boolean = false
) {
    companion object {
        val REJECTED = Rule('.', false, -1, "R", rejected = true)
        val ACCEPTED = Rule('.', false, -1, "A", accepted = true)
        fun fromString(str: String): Rule {
            if (str == "R") {
                return REJECTED
            } else if (str == "A") {
                return ACCEPTED
            } else if (!str.contains(':')) {
                return Rule('.', false, -1, str)
            }
            val value = str.subSequence(2, str.length).split(':')[0].toInt()
            return Rule(str[0], str[1] == '<', value, str.split(':')[1])
        }
    }

    fun getField(char: Char): (Item) -> Int {
        return when (char) {
            'x' -> { item: Item -> item.x }
            'm' -> { item: Item -> item.m }
            'a' -> { item: Item -> item.a }
            else -> { item: Item -> item.s }
        }
    }

    fun fits(item: Item): Boolean {
        if (value == -1) {
            return true
        }
        if (less) {
            return getField(field)(item) < value
        }
        return getField(field)(item) > value
    }
}

data class Segment(var x: IntRange, var m: IntRange, var a: IntRange, var s: IntRange) {
    val combinations: Long = x.count().toLong() * m.count() * a.count() * s.count()

    fun intersect(rule: Rule): Segment? {
        return if (rule.less) {
            when (rule.field) {
                'x' -> if (x.first >= rule.value) null else Segment(x.first ..< rule.value, m, a, s)
                'm' -> if (m.first >= rule.value) null else Segment(x, m.first ..< rule.value, a, s)
                'a' -> if (a.first >= rule.value) null else Segment(x, m, a.first ..< rule.value, s)
                's' -> if (s.first >= rule.value) null else Segment(x, m, a, s.first ..< rule.value)
                else -> this
            }
        } else {
            when (rule.field) {
                'x' -> if (x.last <= rule.value) null else Segment(rule.value + 1 .. x.last, m, a, s)
                'm' -> if (m.last <= rule.value) null else Segment(x, rule.value + 1 .. m.last, a, s)
                'a' -> if (a.last <= rule.value) null else Segment(x, m, rule.value + 1 .. a.last, s)
                's' -> if (s.last <= rule.value) null else Segment(x, m, a, rule.value + 1 .. s.last)
                else -> this
            }
        }
    }

    fun subtract(rule: Rule): Segment? {
        return if (rule.less) {
            when (rule.field) {
                'x' -> if (x.last < rule.value) null else Segment(rule.value .. x.last, m, a, s)
                'm' -> if (m.last < rule.value) null else Segment(x, rule.value .. m.last, a, s)
                'a' -> if (a.last < rule.value) null else Segment(x, m, rule.value .. a.last, s)
                's' -> if (s.last < rule.value) null else Segment(x, m, a, rule.value .. s.last)
                else -> this
            }
        } else {
            when (rule.field) {
                'x' -> if (x.first > rule.value) null else Segment(x.first .. rule.value, m, a, s)
                'm' -> if (m.first > rule.value) null else Segment(x, m.first .. rule.value, a, s)
                'a' -> if (a.first > rule.value) null else Segment(x, m, a.first .. rule.value, s)
                's' -> if (s.first > rule.value) null else Segment(x, m, a, s.first .. rule.value)
                else -> this
            }
        }
    }
}

fun main() {
    fun performRules(item: Item, ruleStr: String, rules: Map<String, List<Rule>>): Int {
        when (ruleStr) {
            "A" -> {
                return item.x + item.m + item.s + item.a
            }
            "R" -> {
                return 0
            }

            else -> {
                for (rule in rules[ruleStr]!!) {
                    if (rule.fits(item)) {
                        return performRules(item, rule.next, rules)
                    }
                }
            }
        }
        return 0
    }

    fun parseRules(input: List<String>): Map<String, List<Rule>> {
        return input.map {
            it.split('{')[0] to it.split('{')[1].split('}')[0].split(',').map { r -> Rule.fromString(r) }
        }.toMap()
    }

    fun parseItems(input: List<String>): List<Item> {
        return input.map {
            val values = it.subSequence(1, it.length - 1).split(',')
            val x = values.first { it.contains('x') }.split('=')[1].toInt()
            val m = values.first { it.contains('m') }.split('=')[1].toInt()
            val s = values.first { it.contains('s') }.split('=')[1].toInt()
            val a = values.first { it.contains('a') }.split('=')[1].toInt()
            Item(x, m, a, s)
        }
    }

    fun part1(input: List<String>): Int {
        val middle = input.indexOfFirst { it.isBlank() }
        val rules = parseRules(input.subList(0, middle))
        val items = parseItems(input.subList(middle + 1, input.size))
        return items.sumOf { performRules(it, "in", rules) }
    }

    fun countPossible(rules: Map<String, List<Rule>>, current: String, segment: Segment?): Long {
        if (current == "A") {
            return segment?.combinations ?: 0
        } else if (current == "R") {
            return 0
        } else if (segment == null) {
            return 0
        }
        var result = 0L
        var currentSegment = segment
        for (rule in rules[current]!!) {
            result += countPossible(rules, rule.next, currentSegment?.intersect(rule))
            currentSegment = currentSegment?.subtract(rule)
        }
        return result
    }

    fun part2(input: List<String>): Long {
        val rules = parseRules(input.subList(0, input.indexOfFirst { it.isBlank() }))
        return countPossible(rules, "in", Segment(1..4000, 1..4000, 1..4000, 1..4000))
    }

    val testInput = readInput("Day19_test")
    check(part1(testInput) == 19114)
    check(part2(testInput) == 167409079868000)

    val input = readInput("Day19")
    part1(input).println()
    part2(input).println()
}
