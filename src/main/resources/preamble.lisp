; This file is evaluated before user code

; syntactic sugar for defining a function
(def {fun} (\ {args body} {def (list (first args)) (\ (rest args) body) }))
