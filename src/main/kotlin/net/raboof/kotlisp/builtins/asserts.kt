package net.raboof.kotlisp.builtins

import net.raboof.kotlisp.Expr

inline fun <reified T> assertType(obj: Expr): T {
    if (obj !is T) {
        throw badTypeError<T>(obj)
    }
    return obj
}

inline fun <reified T> badTypeError(obj: Expr) : IllegalArgumentException {
    return IllegalArgumentException("expected ${T::class.simpleName} but got ${obj.print()}")
}

fun assertLength(list: List<Expr>, length: Int) {
    if (list.size != length) {
        throw IllegalArgumentException("expected $length arguments but got ${list.size}")
    }
}

fun assertLengthAtLeast(list: List<Expr>, length: Int) {
    if (list.size < length) {
        throw IllegalArgumentException("expected at least $length arguments but got ${list.size}")
    }
}