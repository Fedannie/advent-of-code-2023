fun main() {
    val highHistory = mutableMapOf<String, MutableList<Int>>()
    abstract class Module(val name: String) {
        val outputs = mutableListOf<Module>()

        abstract fun receive(low: Boolean, from: Module, iteration: Int): List<Pair<Boolean, Module>>
        open fun addInput(module: Module) {}
    }

    class FlipFlop(name: String): Module(name) {
        var state: Boolean = false
        private fun toggle() {
            state = !state
        }

        override fun receive(low: Boolean, from: Module, iteration: Int): List<Pair<Boolean, Module>> {
            if (!low) {
                return listOf()
            }
            toggle()
            if (!state) {
                if (highHistory[name] == null) {
                    highHistory[name] = mutableListOf()
                }
                highHistory[name]?.add(iteration)
            }
            return outputs.map { !state to it }
        }
    }

    class Conjunction(name: String): Module(name) {
        val memory = hashMapOf<String, Boolean>()

        override fun receive(low: Boolean, from: Module, iteration: Int): List<Pair<Boolean, Module>> {
            memory[from.name] = low
            if (memory.values.all { !it }) {
                if (highHistory[name] == null) {
                    highHistory[name] = mutableListOf()
                }
                highHistory[name]?.add(iteration)
            }
            return outputs.map { memory.values.all { !it } to it }
        }

        override fun addInput(module: Module) {
            super.addInput(module)
            memory[module.name] = true
        }
    }

    class Broadcast: Module("broadcast") {
        override fun receive(low: Boolean, from: Module, iteration: Int): List<Pair<Boolean, Module>> {
            return outputs.map { low to it }
        }
    }

    class Empty(name: String): Module(name) {
        override fun receive(low: Boolean, from: Module, iteration: Int): List<Pair<Boolean, Module>> {
            return listOf()
        }
    }

    fun parseInput(input: List<String>): Map<String, Module> {
        val modules = mutableMapOf<String, Module>()
        input.map { str ->
            val type = str.split(" -> ")[0][0]
            val name = str.split(" -> ")[0].substring(1)
            val outputs = str.split(" -> ")[1].split(", ")
            (type to name) to outputs
        }.map {
            val module = when (it.first.first) {
                '%' -> FlipFlop(it.first.second)
                '&' -> Conjunction(it.first.second)
                else -> Broadcast()
            }
            modules[module.name] = module
            module to it.second
        }.forEach {
            for (next in it.second) {
                it.first.outputs.add(modules[next] ?: Empty(next))
                modules[next]?.addInput(it.first)
            }
        }
        return modules
    }

    fun send(module: Module, iteration: Int): Point {
        val queue = mutableListOf(true to module to module)
        var resultLow = 0
        var resultHigh = 0
        while (queue.isNotEmpty()) {
            val next = queue.removeAt(0)
            queue.addAll(next.first.second.receive(next.first.first, next.second, iteration).map { it to next.first.second })
            if (next.first.first) resultLow++ else resultHigh++
        }
        return Point(resultLow, resultHigh)
    }

    fun send(modules: Map<String, Module>, count: Int): Int {
        var result = Point(0, 0)
        for (i in 0 ..< count) {
            result += send(modules["broadcast"] ?: Empty(""), i + 1)
        }
        return result.x * result.y
    }

    fun part1(input: List<String>): Int {
        val modules = parseInput(input)
        return send(modules, 1000)
    }

    fun getAllSubModules(modules: Map<String, Module>, moduleStr: String): List<Module> {
        var level = modules.values.filter { it.outputs.any { module -> module.name == moduleStr } }
        for (i in 0 .. 1) {
            level = modules.values.filter { it.outputs.any { module -> level.contains(module) } }
        }
        return level
    }

    fun part2(input: List<String>): Long {
        val modules = parseInput(input)
        val subModules = getAllSubModules(modules, "rx")
        var i = 1
        while (subModules.any { (highHistory[it.name]?.size ?: 0) < 1 }) {
            send(modules["broadcast"] ?: Empty(""), i)
            i++
        }
        return lcm(highHistory.entries.filter { subModules.any { module -> module.name == it.key } }.map { it.value.first().toLong() })
    }

    val testInput = readInput("Day20_test")
    check(part1(testInput) == 32000000)
    val testInput2 = readInput("Day20_test2")
    check(part1(testInput2) == 11687500)

    val input = readInput("Day20")
    part1(input).println()
    part2(input).println()
}
