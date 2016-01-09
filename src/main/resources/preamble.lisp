; This file is evaluated before user code

; syntactic sugar for defining a function
(def {fun} (\ {args body} {def (list (first args)) (\ (rest args) body) }))

(def {nil} (eval {}))

; additional logic operations
(fun {not pred} {if {pred} {#f} {#t}})
(fun {neq a b} {not (eq a b)})