# Lexer und Parser selbst implementiert

## A4.1 - First- und Follow-mengen, LL(1)



## A4.2 - Grammatik zur LISP-artigen Sprache

### [Sammlung gültiger, ungültiger und fraglicher S-Expressions](https://github.com/DragonMB7/Praktikum_Compilerbau/tree/main/Blatt04_LLParser/S-Expressions.md)

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

### **Anmerkung:** ``Vermutlich falsch: (if (= input (list))``

### **Begründung:** ``Der Operator "=" kann nur Int, String und Boolean vergleichen``

```
(defn   CountListElems 
        (input) 
        (if (= input (list))    <-- VERMUTLICH FALSCH, SIEHE ANMERKUNG
            0 
            (+ 1 (CountListElems(tail (input))))
        )
)

--------------------------
// alternativ (potentiell)

(defn   CountListElems 
        (input) 
        (if (isEmpty (input))
            0 
            (+ 1 (CountListElems(tail (input))))
        )
)

(defn	isEmpty
	(input)
	(if(= (str input) "()")
		true
		false
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
program :	sExpr+ EOF		

sExpr :	'(' op (sExpr)* ')' |	
	let |	
	datatype |		
	if |						
	def |
	defn |
	list |
	nth |
	str |
	print			

let :	'(let' '(' (ID datatype)* ')' (sExpr)* ')'
def :	'(def' ID datatype ')'

if :	'(if' sExpr ifBlock (elseBlock)? ')'

ifBlock :	do | sExpr
elseBlock :	do | sExpr

do :	'(do' (sExpr)* ')'

defn :	'(' ID '(' (ID)? ')' (sExpr)* ')'

list :	'(list' (datatype)* ')' | lPick
nth :	'(nth' list INTEGER ')'

lPick :	'(head' list ')' | '(tail' list ')'

str :	'(str' (datatype)* ')'

print :	'(print' STRING ')' | '(print' str ')'


op : '=' | '<' | '>' | '+' | '*' | '/' | '-' | ID		//op kann als Operator und variable dienen, deshalb ID, was z.B. eine Variable sein kann

datatype: STRING | INTEGER | BOOLEAN


ID      : [a-z][a-zA-Z_]*
STRING  :  '"' (~[\n\r"])* '"'
INTEGER : [0-9]+
BOOLEN  : 'true' | 'false'

COMMENT: ';;' ~[\n\r]* -> skip
WHITESPACE : [ \t\n\r]+ -> skip

```