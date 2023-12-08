fun main() {
    fun parseGraph(input: List<String>): Map<String, Pair<String, String>> {
        val regex = Regex("[0-9A-Z]+")
        return input.map { regex.findAll(it).flatMap { it.groupValues }.toList() }.map { it[0] to (it[1] to it[2]) }.toMap()
    }

    fun travel(start: String, isFinish: (String) -> Boolean, graph: Map<String, Pair<String, String>>, instructions: String): Int {
        var current = start
        var steps = 0
        while (!isFinish(current)) {
            current = if (instructions[steps % instructions.length] == 'L') {
                graph[current]!!.first
            } else {
                graph[current]!!.second
            }
            steps++
        }
        return steps
    }

    fun part1(input: List<String>): Int {
        val instructions = input[0]
        val graph = parseGraph(input.subList(2, input.size))

        return travel("AAA", { str: String -> str == "ZZZ" }, graph, instructions)
    }


    fun part2(input: List<String>): Long {
        val instructions = input[0]
        val graph = parseGraph(input.subList(2, input.size))

        val nodes = graph.keys.toList().filter { it.endsWith('A') }
        return lcm(nodes.map { travel(it, { str: String -> str.endsWith('Z') }, graph, instructions).toLong() })
    }

    val testInput = readInput("Day08_test")
    check(part1(testInput) == 6)
    val testInput2 = readInput("Day08_test2")
    check(part2(testInput2) == 6L)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}