; This file is evaluated before user code

; syntactic sugar for defining a function
(def {fun} (\ {args body} {def (head args) (\ (rest args) body) }))

(def {nil} {})

; additional logic operations
(fun {not pred} {if {pred} {#f} {#t}})
(fun {neq a b} {not (eq a b)})

; more control flow

(fun {do & l} {
  if (== l nil)
    {nil}
    {last l}
})

(fun {let b} {
  ((\ {_} b) ())
})
