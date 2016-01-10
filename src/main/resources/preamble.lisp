; This file is evaluated before user code

; syntactic sugar for defining a function
(def {fun} (\ {args body} {def (head args) (\ (rest args) body) }))

(fun {unpack f xs} {eval (join (list f) xs)})
(fun {pack f & xs} {f xs})

(def {nil} {})

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

(fun {socket-create host port} {ctor "java.net.Socket" host (int port)})

; returns a function that reads the next line of input from the socket
; BufferedReader(InputStreamReader(Socket().inputStream)).readLine()
(fun {socket-reader s}
     {do
       (= {reader} (ctor "java.io.BufferedReader" (ctor "java.io.InputStreamReader" (. s "getInputStream"))))
       (\ {} {. reader "readLine"})
     })


(fun {socket-writer s}
     { do
       (= {writer} (ctor "java.io.BufferedWriter" (ctor "java.io.OutputStreamWriter" (. s "getOutputStream"))))
       (\ {out} {. writer "write" out})
     })

