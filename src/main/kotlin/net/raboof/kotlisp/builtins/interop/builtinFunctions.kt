package net.raboof.kotlisp.builtins.interop

import net.raboof.kotlisp.*
import net.raboof.kotlisp.builtins.Builtin
import net.raboof.kotlisp.builtins.assertLength
import net.raboof.kotlisp.builtins.assertLengthAtLeast
import net.raboof.kotlisp.builtins.assertType
import java.lang.reflect.Constructor
import java.lang.reflect.Executable
import java.lang.reflect.Method
import java.util.*


val methodCall = Builtin(".") { env, denv, rest ->
    assertLengthAtLeast(rest, 2)
    val first = rest[0]
    val target: Any = when (first) {
        is IObject -> first.obj
        else -> unmarshall(first)
    } ?: throw IllegalArgumentException("target is null")

    val methodName = assertType<Str>(rest[1]).value
    val args = rest.drop(2)
    val params = args.map(::unmarshall).toTypedArray()

    val method = try {
        findMethod(target.javaClass, methodName, params)
    } catch (_: NoSuchElementException) {
        throw IllegalArgumentException("cannot find method $target::$methodName for given arguments")
    }

    marshall(method.invoke(target, *params))
}

val ctor = Builtin("ctor") { env, denv, rest ->
    assertLengthAtLeast(rest, 1)
    val cls = assertType<Str>(rest.first()).value
    val args = rest.drop(1)

    val params = args.map(::unmarshall).toTypedArray()

    val ctor = try {
        findConstructor(loadClass(cls), params)
    } catch (e: NoSuchElementException) {
        throw IllegalArgumentException("cannot find constructor for class $cls for arguments " + e.message)
    }
    marshall(ctor.newInstance(*params))
}

private fun loadClass(cls: String) : Class<*> {
    try {
        return ClassLoader.getSystemClassLoader().loadClass(cls)
    } catch (e: ClassNotFoundException) {
        throw IllegalArgumentException("cannot load class $cls")
    }
}

fun <C> findConstructor(c: Class<C>, initArgs: Array<Any?>): Constructor<*> {
    return c.declaredConstructors.single(selectMatchingParams(initArgs))
}

fun <C> findMethod(c: Class<C>, methodName: String, initArgs: Array<Any?>): Method {
    return c.methods.filter { it.name == methodName }.single(selectMatchingParams(initArgs))
}

// I used ideas from http://stackoverflow.com/a/18136892/1290621 as a starting point
private fun selectMatchingParams(initArgs: Array<Any?>): (Executable) -> Boolean {
    return { method ->
        val types: Array<out Class<*>> = method.parameterTypes
        if (types.size != initArgs.size)
            false
        else {
            initArgs.map { it?.javaClass }.zip(types).all {
                val (got, need) = it
                if (got == null && need.isPrimitive)
                    true
                else if (!need.isAssignableFrom(got))
                    if (need.isPrimitive) {
                        correspondsToPrimitive(got, need)
                    } else {
                        false
                    }
                else
                    true
            }
        }
    }
}

private fun correspondsToPrimitive(proposed: Class<Any>?, primitive: Class<*>?): Boolean {
    return ((Int::class.javaPrimitiveType == primitive && Int::class.javaObjectType == proposed)
            || (Long::class.javaPrimitiveType == primitive && Long::class.javaObjectType == proposed)
            || (Char::class.javaPrimitiveType == primitive && Char::class.javaObjectType == proposed)
            || (Short::class.javaPrimitiveType == primitive && Short::class.javaObjectType == proposed)
            || (Boolean::class.javaPrimitiveType == primitive && Boolean::class.javaObjectType == proposed)
            || (Byte::class.javaPrimitiveType == primitive && Byte::class.javaObjectType == proposed))
}

val fieldGet = Builtin("field-get") { env, denv, rest ->
    assertLength(rest, 2)
    val first = rest[0]
    val target: Any = when (first) {
        is IObject -> first.obj
        else -> unmarshall(first)
    } ?: throw IllegalArgumentException("target is null")

    val propName = assertType<Str>(rest[1]).value
    val property = target.javaClass.fields.single { it.name == propName }
    marshall(property.get(target))
}

val fieldSet = Builtin("field-set") { env, denv, rest ->
    assertLength(rest, 3)
    val first = rest[0]
    val target: Any = when (first) {
        is IObject -> first.obj
        else -> unmarshall(first)
    } ?: throw IllegalArgumentException("target is null")

    val propName = assertType<Str>(rest[1]).value
    val property = target.javaClass.fields.single { it.name == propName }

    property.set(target, unmarshall(rest[2]))

    QExpression.Empty
}


val javaInt = Builtin("int") { env, denv, rest ->
    assertLength(rest, 1)
    val num = assertType<net.raboof.kotlisp.Number>(rest.first())
    IObject(num.value.toInt())
}

val showMethods = Builtin("show-methods") { env, denv, rest ->
    assertLength(rest, 1)
    val first = rest[0]
    val target: Any = when (first) {
        is IObject -> first.obj
        is Str -> loadClass(first.value)
        else -> unmarshall(first)
    } ?: throw IllegalArgumentException("target is null")

    val methods = when (target) {
        is Class<*> -> target.methods
        else -> target.javaClass.methods
    }

    marshall(methods.map { it.name + "(" + it.parameters.joinToString(", ") + ")" })
}

fun unmarshall(expr: Expr): Any? {
    return when (expr) {
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
    return when (obj) {
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