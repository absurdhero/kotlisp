package net.raboof.kotlisp

import java.util.*
import kotlin.collections.set


open class Environment {
    private val map : MutableMap<String, Expr> = HashMap()

    public operator fun get(value: String) : Expr? {
        return map[value]
    }

    public operator fun contains(value: String) : Boolean {
        return map.containsKey(value)
    }

    public operator fun set(key: String, value: Expr) {
        map[key] = value
    }

}