package net.raboof.kotlisp.builtins

import net.raboof.kotlisp.False
import net.raboof.kotlisp.True

val trueSymbol = Builtin("#t", {env, rest -> True })
val falseSymbol = Builtin("#f", {env, rest -> False })
