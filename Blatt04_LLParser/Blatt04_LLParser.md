# Lexer und Parser selbst implementiert

## A4.1 - First- und Follow-mengen, LL(1)



## A4.2 - Grammatik zur LISP-artigen Sprache

### 1.a) korrekte Programme

#### Einfachste Programme: Einfachste Ausdrücke - Also Literale
```
23          ;; Integer
"world"     ;; String
true        ;; Boolean
false       ;; Boolean
```

#### Einfache Ausdrücke

```
(+ 1 1)               ;; 1 + 1
(+ 1 2 3 4)           ;; 1 + 2 + 3 + 4
(+ (+ (+ 1 2) 3) 4)   ;; (((1 + 2) + 3) + 4)
(/ (+ 10 2) (+ 2 4))  ;; ((10 + 2) / (2 + 4))
```


#### Komplexere Programme

```
(def x 4)
(def y 6)

(if (< x y)
    (print "x < y")
    (print "y >= x"))

```

#### Funktion zur rekursiven Längenberechnung einer Liste

```
(defn   CountListElems 
        (input) 
        (if (= input (list))
            0 
            (+ 1 (CountListElems(tail (input))))
        )
)
```

### 1.b) fehlerhafte Programme

#### Undefinierte Variablen

```
foo
```

#### Fehlender Operator - Unknown/Undefined Operator

```
(1 2 3)
```

#### Mismatched Data Type

´´´
(+ 2 "String")  ;; Können nicht miteinander verknüpft

(+ true false)  ;; Operator not defined for data Type 'boolean'
´´´

#### Anführungszeichen oder Klammern nicht geschlossen

```
(str "hello" "you"  ;; schließende Klammer fehlt

(str "hello" "you ) ;; schließende Anführungszeichen Fehlen
```

#### Schlüsselwörter ohne umschließende Klammern

```
defn blah (n) (str "bluch" n)

list

if ... 

```

#### If: zu viele S-Expressions (Stack-Overflow?)

´´´
(if (< 1 2) 
    (print "true") 
    (print "false") 
    (print "undefined")
)
´´´


### 2) Grammatik

```
program : sExpr+ EOF

expr    : 


sExpr   : lit
         '(' op sExpr sExpr ')'

op      : ''


lit     : STRING | BOOLEAN | STRING


var     : '(' def ID ')'



STRING  : '"' [a-zA-Z0-9_]* '"'

ID      : [a-z][a-zA-Z_]*
STRING  :  '"' (~[\n\r"])* '"'
INTEGER : [0-9]+
BOOLEN  : 'true' | 'false'
```