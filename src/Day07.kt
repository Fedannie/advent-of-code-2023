fun main() {
    class Hand(val cards: List<Int>, val withJoker: Boolean) {
        constructor(str: String, withJoker: Boolean) : this(str.toCharArray().map {
            when (it) {
                'A' -> 14
                'K' -> 13
                'Q' -> 12
                'T' -> 10
                'J' -> if (withJoker) 1 else 11
                else -> it.toString().toInt()
            }
        }, withJoker)

        val type: Int
            get() {
                if (withJoker && cards.count { it == 1 } > 0) {
                    val jokers = cards.count { it == 1 }
                    val otherCards = cards.filter { it != 1 }
                    if (otherCards.distinct().size == 1) {
                        return 7
                    }
                    if (otherCards.distinct().size == 2 && (otherCards.count { it == otherCards[0] } == 1 || otherCards.count { it == otherCards[0] } == otherCards.size - 1)) {
                        return 6
                    }
                    if (jokers == 1 && otherCards.distinct().size == 2) {
                        return 5
                    }
                    if (jokers < 3 && otherCards.distinct().size == 3) {
                        return 4
                    }
                    if (jokers == 1 && otherCards.distinct().size == 4) {
                        return 2
                    }
                }

                if (cards.distinct().size == 1) {
                    return 7
                }
                if (cards.distinct().size == 2 && (cards.count { it == cards[0] } == 1 || cards.count { it == cards[0] } == 4)) {
                    return 6
                }
                if (cards.distinct().size == 2 && (cards.count { it == cards[0] } == 2 || cards.count { it == cards[0] } == 3)) {
                    return 5
                }
                if (cards.count { it == cards[0] } == 3 || cards.count { it == cards[1] } == 3 || cards.count { it == cards[2] } == 3) {
                    return 4
                }
                if (cards.distinct().size == 3) {
                    return 3
                }
                if (cards.distinct().size == 4) {
                    return 2
                }
                return 1
            }
    }

    fun solve(input: List<String>, withJoker: Boolean): Long {
        val hands = input.map { Hand(it.split(' ')[0], withJoker) to it.split(' ')[1].toLong() }
        return hands
            .sortedWith(
                compareBy<Pair<Hand, Long>> { it.first.type }
                    .thenBy { it.first.cards[0] }
                    .thenBy { it.first.cards[1] }
                    .thenBy { it.first.cards[2] }
                    .thenBy { it.first.cards[3] }
                    .thenBy { it.first.cards[4] })
            .mapIndexed { ind, pair -> (ind + 1) * pair.second }
            .sum()
    }

    fun part1(input: List<String>): Long {
        return solve(input, withJoker = false)
    }

    fun part2(input: List<String>): Long {
        return solve(input, withJoker = true)
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440L)
    check(part2(testInput) == 5905L)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}