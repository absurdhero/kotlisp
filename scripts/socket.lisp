; Socket usage example

; sample function that reads forever
(fun {read-all} {while {#t} {print (read)}})

; open a socket and construct a reader and writer
(def {s} (socket-create "example.net" 10000))
(def {read} (socket-reader s))
(def {write} (socket-writer s))

; write fake IRC commands and read forever
(write "nick foo\r\n")
(write "user foo foo foo :foo\r\n")
(read-all)

