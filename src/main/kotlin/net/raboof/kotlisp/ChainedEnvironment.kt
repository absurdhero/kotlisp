package net.raboof.kotlisp

import java.util.*

open class ChainedEnvironment(
        val parent: Environment = Environment.Empty,
        private val map: MutableMap<String, Expr> = HashMap())
: Environment {

    override operator fun get(value: String): Expr {
        return map[value] ?: parent[value] ?: throw IllegalArgumentException("unknown symbol $value")
    }

    override operator fun contains(value: String): Boolean {
        return map.containsKey(value)
    }

    override operator fun set(key: String, value: Expr) {
        map[key] = value
    }

    override fun symbols(): List<String> = map.keys.toList() + parent.symbols()

    override fun toString(): String {
        return "{this: $map, parent: $parent}"
    }

    /** return a new environment with the contents of this environment but with a different parent */
    fun childOf(parent: Environment): ChainedEnvironment {
        return ChainedEnvironment(parent, HashMap(map))
    }

    fun global(): ChainedEnvironment {
        return when (parent) {
            is ChainedEnvironment -> parent.global()
            else -> this
        }
    }
}