; This file is evaluated before user code

; syntactic sugar for defining a function
(def {fun} (\ {args body} {def (list (nth 0 args)) (\ (rest args) body) }))

(fun {unpack f xs} {eval (join (list f) xs)})
(fun {pack f & xs} {f xs})

; list operations

(def {nil} {})

(fun {first lst} {nth 0 lst})
(fun {last lst} {nth (- (len lst) 1) lst})
(fun {head lst} {list (nth 0 lst)})

; additional logic operations
(fun {not pred} {if {pred} {#f} {#t}})
(fun {neq a b} {not (eq a b)})

; more control flow

(fun {do & l} {
  if {nil? l}
    {nil}
    {last l}
})

(fun {let b} {
  ((\ {_} b) ())
})

(fun {map f lst} {
  if {empty? lst}
  {}
  {cons (f (first lst)) (map f (rest lst))}
})


; Socket IO
; see scripts/socket.lisp for example usage

(fun {socket-create host port} {ctor "java.net.Socket" host (int port)})

; Returns a function that reads the next line of input from the socket.
; Initially runs: BufferedReader(InputStreamReader(Socket().inputStream))
; Calls readLine() on the buffer for each invocation
(fun {socket-reader s}
     {do
       (= {reader} (ctor "java.io.BufferedReader" (ctor "java.io.InputStreamReader" (. s "getInputStream"))))
       (\ {} {. reader "readLine"})
     })


(fun {socket-writer s}
     { do
       (= {writer} (ctor "java.io.BufferedWriter" (ctor "java.io.OutputStreamWriter" (. s "getOutputStream"))))
       (\ {out} {do (. writer "write" out) (. writer "flush") })
     })
