package net.raboof.kotlisp

data class Str(val value: String) : Expr {
    companion object {
        fun unescaped(s: String): String {
            if (s.length <= 1) return s

            var unescaped = StringBuilder()
            for (i in s.indices) {
                if (s[i] == '\\' && i < s.length - 1) {
                    val c = s[i + 1]
                    unescaped.append(
                            when (c) {
                                'r' -> '\r'
                                'n' -> '\n'
                                't' -> '\t'
                                'b' -> '\b'
                                else -> c
                            }
                    )
                } else if (i == 0 || s[i - 1] != '\\') {
                    unescaped.append(s[i])
                }
            }
            return unescaped.toString()
        }

        fun escape(s: String): String {
            var escaped = StringBuilder()
            for (i in s.indices) {
                val c = s[i]
                escaped.append(
                        when (c) {
                            '\r' -> """\r"""
                            '\n' -> """\n"""
                            '\t' -> """\t"""
                            '\b' -> """\b"""
                            '\\' -> """\\"""
                            '"' -> """\""""
                            else -> c
                        }
                )
            }
            return escaped.toString()
        }
    }
    override fun print(): String {
        return "\"${escape(value)}\""
    }

    override fun evaluate(environment: ChainedEnvironment): Expr {
        return this
    }
}