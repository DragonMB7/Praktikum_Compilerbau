
## LITERALE

```
42          // Integer
"hello"     // String
true        // Boolean
false       // Boolean

```

## VARIABLEN

```
foo                     /// Fehler -> Variablenaufruf ohne Definition von foo

(def x 77)              // x = 77

(def y "zwei")          // y = "zwei" ???
(def z true)            // z = true ???

(def a (+ 2 4))         // a = 6 ???

(def b (list 1 "zwei" false))   // b = (1 "zwei" false)

(def k 5 6)             // Fehler (too many arguments / expected ')' as next token) ???
```


## S-EXPRESSIONS

### Logische Operationen  - Int, String, Boolean

```
(= 1 2)                 // false
(< 1 2)                 // true
(> 1 2)                 // false


(< (= 1 2) 3)           // ??? Sollte nicht gehen

(< (< 1 2) true)        // true ???


(= "First" "Second")    // false
(< "First" "Second")    // ???
(> "First" "Second")    // ???

(= true false)          // false
(< true false)          // false ???
(> true false)          // true ???
```

### Arithmetische Operationen - Int, String

```
(+ 1 1)
(- 1 1)
(* 1 1)
(/ 1 1)

(+ "One" "Two")         // "OneTwo"
(- "One" "Two")         // ???
(* "One" "Two")         // OTnweo ???
(/ "One" "Two")         // ??????


(+ 1 2 3 4)             // 1 + 2 + 3 + 4
(+ (+ (+ 1 2) 3) 4)     // (((1 + 2) + 3) + 4)
(/ (+ 10 2) (+ 2 4))    // ((10 + 2) / (2 + 4))

```

## FUNKTIONEN

### print

```
// Mit der eingebauten Funktion print kann der Wert eines Ausdrucks auf der Konsole ausgegeben werden:
(print "blah")          // "blah"

(print 5)               // ??? -> (print(str 5))
(print (+ 2 4))         // ???
(print (+ "One" "Two")) // "OneTwo" ???

```

### str

```
// Die eingebaute Funktion str verknüpft ihre Argumente und bildet einen String.
// Falls nötig, werden die Argumente vorher in einen String umgewandelt.

(str 6)                     // "6"
(str "One" "Two")           // "OneTwo"
(str "one: " 1 ", two")     // "one: 1, two"

(str true)                  // "true" ???

(str (list 1 "zwei" false)) // ??????

(str (+ 1 2))               // "3" ???
```

### list

```
(list)                          // Geht das so?


(list 1 2)
(list 1 2 3)
(list "eins" "zwei" "drei")
(list 1 "String" true)

(list (list 1 2 3))             // == (list 1 2 3) ???
(list (list 1 2) 3)             // == (list 1 2 3) ???
(list (list 1 2) (list 3 4))    // == (list 1 2 3 4) ???

```
Funktionsaufrufe oder Operationen innerhalb Listen

```
(list (+ 1 2) 4)        // Syntaxfehler "expected Literal as Type"
```

### head, tail, nth:
```
(head (list 1 2 3))             // 1
(tail (list 1 2 3))             // (2 3)
(nth (list "One" 2 false) 3)    // false
```

## FUNKTIONSDEFINITIONEN
```
(defn funcName (params) (body))
(defn ID (paramID+) (s-Expr)+)

(defn func 
        (n) 
        (print 
            str("Hello " n ", how was your day?")
        )                   // Defintion
)

(func "Foobar")             // Aufruf -> Ausgabe: "Hello Foobar, how was your day?"


(defn blah (n) 4)           // ??? 4 ist syntaktisch korrekt, tut aber nichts

(defn fuck 
        (a b c) 
        (print (str a b c))
)                           // ??? Gibt es mehrere Parameter?

(defn miss (print "hello")) // ??? Gibt es leere Parameterlisten?
```

### Let
```
(let (name value) (body using names))
```


## IF
```
(if (cond-sExpr) (then-sExpr))
(if (cond-sExpr) (then-sExpr) (else-sExpr))
(if (cond-sExpr) (do (then-sExpr)+) (else-sExpr))

(def x 4)
(if (x) (print (str x)) (print (str "Not " x)))     // ??? Variablenaufruf als Condition
```
