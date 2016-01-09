package net.raboof.kotlisp.builtins.interop

import net.raboof.kotlisp.*
import net.raboof.kotlisp.builtins.Builtin
import net.raboof.kotlisp.builtins.assertLengthAtLeast
import net.raboof.kotlisp.builtins.assertType
import java.util.*


val methodCall = Builtin(".") { env, rest ->
    assertLengthAtLeast(rest, 2)
    val first = rest[0]
    val target: Any = when(first) {
        is IObject -> first.obj
        else -> unmarshall(first)
    } ?: throw IllegalArgumentException("target is null")

    val methodName = assertType<Str>(rest[1]).value
    val args = rest.drop(2)
    val params = args.map(::unmarshall).toTypedArray()

    val method = try {
        target.javaClass.methods
                ?.filter { methodName == it.name }
                ?.first { it.parameters.map { p -> p.type } == params.map { p -> p?.javaClass } }
    } catch (_: NoSuchElementException) {
        throw IllegalArgumentException("cannot find method for given arguments")
    }

    if (method == null)
        throw IllegalArgumentException("cannot find method $methodName on target ${first.print()}")
    else {
        marshall(method.invoke(target, *params))
    }

}

val ctor = Builtin("ctor") { env, rest ->
    assertLengthAtLeast(rest, 1)
    val cls = assertType<Str>(rest.first()).value
    val args = rest.drop(1)

    val params = args.map(::unmarshall).toTypedArray()

    val ctor = try {
        ClassLoader.getSystemClassLoader().loadClass(cls).constructors
                .single { it.parameters.map { p -> p.type } == params.map { p -> p?.javaClass } }
    } catch (_: NoSuchElementException) {
        throw IllegalArgumentException("cannot find constructor for given arguments")
    }
    marshall(ctor.newInstance(*params))
}

fun unmarshall(expr: Expr): Any? {
    return when(expr) {
        QExpression.Empty -> emptyList<Any>()
        is Str -> expr.value
        is net.raboof.kotlisp.Number -> expr.toLong()
        is QExpression -> expr.exprs.map { unmarshall(it) }
        is True -> true
        is False -> false
        is IObject -> expr.obj
        else -> expr
    }
}

fun marshall(obj: Any?): Expr {
    return when(obj) {
        null -> QExpression.Empty
        is CharSequence -> Str(obj.toString())
        is List<*> -> QExpression(obj.map { marshall(it) })
        is kotlin.Number -> net.raboof.kotlisp.Number(obj.toString())
        true -> True
        false -> False
        is IObject -> obj
        else -> IObject(obj)
    }
}