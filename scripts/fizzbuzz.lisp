; fizzbuzz with if conditions
(fun {fizzbuzz1 x y}
{do
  (print
    (if {eq (% x 15) 0 } {"FizzBuzz"}
      {if {eq (% x 3) 0 } {"Fizz"}
        {if {eq (% x 5) 0 } {"Buzz"} {x}}})
  )
  (if {< x y} {fizzbuzz1 (+ x 1) y} {})})

; fizzbuzz with select
(fun {snd l} {nth 1 l})

(fun {select & cs} {
  if {nil? cs}
    {error "could not find a selector"}
    {if (first (first cs))
      {snd (first cs)}
      {unpack select (rest cs)}}
})

; select is unwieldy without dynamic variable scope or
; something like quasi-quote (for binding/evaluating the variables)
(fun {fizzbuzz2 x y}
{do
  (print
    (select
     (list (list eq (% x 15) 0 ) "FizzBuzz")
     (list (list eq (% x 3) 0 ) "Fizz")
     (list (list eq (% x 5) 0 ) "Buzz")
     (list {#t} x )))
  (if {< x y} {fizzbuzz2 (+ x 1) y} {})})

;    (select ( (= (% x 15) 0 ) "FizzBuzz")
;            ( (= (% x 3) 0 ) "Fizz")
;            ( (= (% x 5) 0 ) "Buzz")
;            ( #t x)

;(fun {foo x} {if {eq x 2} {print x} {}})

;(fizzbuzz1 1 100)
(fizzbuzz2 1 100)
