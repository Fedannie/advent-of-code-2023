fun main() {
    fun String.hash(): Int = this.fold(0) { hash, ch -> ((hash + ch.code) * 17) % 256 }

    class State {
        val boxes = List<MutableList<Pair<String, Int>>>(256) { mutableListOf() }

        private fun extract(lens: String) = boxes[lens.hash()].removeIf { it.first == lens }

        private fun add(lens: String, value: Int) {
            val box = lens.hash()
            val ind = boxes[box].indexOfFirst { it.first == lens }
            if (ind < 0) {
                boxes[box].add(lens to value)
            } else {
                boxes[box][ind] = lens to value
            }
        }

        fun power(): Int {
            return boxes.mapIndexed { index, lenses ->
                (index + 1) * lenses.mapIndexed { lensInd, lens -> (1 + lensInd) * lens.second }.sum()
            }.sum()
        }

        fun perform(operation: String): State {
            if (operation.contains('=')) {
                add(operation.split('=')[0], operation.split('=')[1].toInt())
            } else {
                extract(operation.split('-')[0])
            }
            return this
        }
    }

    fun part1(input: List<String>): Int {
        return input[0].split(',').sumOf { it.hash() }
    }

    fun part2(input: List<String>): Int {
        return input[0].split(',').fold(State()) { state, operation -> state.perform(operation) }.power()
    }

    val testInput = readInput("Day15_test")
    check(part1(testInput) == 1320)
    check(part2(testInput) == 145)

    val input = readInput("Day15")
    part1(input).println()
    part2(input).println()
}