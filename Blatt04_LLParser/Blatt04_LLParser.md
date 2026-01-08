# Lexer und Parser selbst implementiert

## A4.1 - First- und Follow-mengen, LL(1)


- $First_1(S) = \{1,3\} $

- $First_1(A) = \{2,\epsilon\}$

- $ Follow_1(S) = Follow(S) \cup Follow_1(A) = \{1,3\} $ 

- $ Follow_1(A) = First_1(S) \cup First_1(S) =  \{1,3\}$ , weil  
        - $ \beta = S$ für beide Ableitungsregeln


### LL(1)-Eigenschaften

Für $S$: keine Regel führt nach $\epsilon$
- $First_1(1AS) \cap First_1(3) = \{2,\epsilon\} \cap \{1,3\} = \empty$

<br>

Für $A$: kann zu $epsilon$ werden

- $First_1(2AS) \cap Follow_1(A) = \{2\} \cap First_1(S) = \{2\} \cap \{1,3\} = \empty$

<br>

$\rightarrow$ Da die Schnittmengen alle Disjunkt sind, weiß der Parser immer nach einem Vorschautoken, welche Regel anzuwenden ist.

<br>

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
program     : sExpr+ EOF		;

sExpr       : '(' op (sExpr)* ')' 
            | let 
            | datatype
            | if 
            | def 
            | defn 
            | list 
            | nth 
            | str 
            | print
            | ID            
            ;			

let         : '(let' '(' (ID sExpr)* ')' (sExpr)* ')' ;
// vorher let         : '(let' '(' (ID datatype)* ')' (sExpr)* ')' ;
def         : '(def' ID sExpr ')' ;
// vorher def         : '(def' ID datatype ')' ;

if          : '(if' sExpr condBlock (condBlock)? ')' ;
condBlock   : do | sExpr    ;
do          : '(do' (sExpr)* ')'    ;

// if          : '(if' sExpr ifBlock (elseBlock)? ')' ;
// ifBlock     : do | sExpr    ;
// elseBlock   : do | sExpr    ;


defn        : '(defn' ID '(' (ID)+ ')' (sExpr)* ')' ;

list        :	'(list' (datatype)* ')' | lPick ;
nth         :	'(nth' list INTEGER ')'         ;

lPick       :	'(head' list ')' | '(tail' list ')' ;

str         :	'(str' (datatype)* ')'         ;
print       :	'(print' STRING ')' | '(print' str ')'  ;


op          : '=' | '<' | '>' | '+' | '*' | '/' | '-' | ID  ;

datatype    : STRING | INTEGER | BOOLEAN    ;


ID          : [a-z][a-zA-Z_]*       ;
STRING      :  '"' (~[\n\r"])* '"'  ;
INTEGER     : [0-9]+                ;
BOOLEAN     : 'true' | 'false'      ;

COMMENT     : ';;' ~[\n\r]* -> skip ;
WHITESPACE  : [ \t\n\r]+ -> skip    ;

```

## Aufgaben 3 & 4 

- siehe Code

## Aufgabe 6

### RUFF

RUFF ist ein Linter und Code-Formatierer für Python. Die Entwickler in ihrem letzten Release bekannt gegeben, dass sie von einem generierten Parser (mittels LALRPOP) auf einen recursive-descent-parser umgestiegen sind um die Performance und Fähigkeit zur präzisen Fehlermeldung zu verbessern. Dafür haben sie einigen Aufwand hingenommen, sind vom Arbeitsergebnis aber überzeugt. Der von LALRPOP generierte Parser bereitete ihnen wohl an vielen Stellen schon Schwierigkeiten damit, neue (syntaktisch manchmal uneindeutige) Python-Syntax zu unterstützen, was auch zu diesem Schritt beitrug.

Der Vorteil der Verwendung eines Parser-Generators zu Beginn des Projekts bestand vermutlich darin, dass dieser Teil des Projekts nur wenig Aufmerksamkeit und lediglich eine Einarbeitung in den Parser-Generator verlangte, anstatt des Abtauchens in das Rabbit-Hole der Impelmentierung eines ganz eigenen Python-Parsers, was angesichts der Python-Syntax nachvollziehbar ist. Dies ermöglichte einen schnellen Start des Projekts.

### GCC

Die bekannte GNU Compiler Collection für C und C++ wurden bereits 2004 bzw 2006 auf handwritten-recursive-descent-parsers umgestellt. Die umstellung erfolgte wohl um Erweiterungen der Sprachen einfacher unterstützen zu können und kontextsensitive Features besser integrieren zu können und Fehler besser behandeln zu können.

### Vorteile von Parser-Generatoren

Die Verwendung von Generatoren bringt in erster Linie einen geringeren Initalaufwand mit sich, sodass die Konzentration auf den Hauptteil des eigentlichen Projekts gerichtet werden kann und erstmal "Ergebnisse liefern" zu können.

Generatoren wie ANTLR sind auch sehr flexibel was die Zielsprache angeht, im Grunde ist nur wichtig, dass die Grammatik korrekt ist. Außerdem werden mehr Klassen von Grammatiken abgebildet, als durch recursive-descent parser.

Gegen ANTLR spricht, dass der generierte Code nicht so gut angepasst werden kann um bspw Performance-Optimierungen zu erreichen oder Spezialfälle, wie komplizierte oder seltene Verschachtelungen abzubilden.
Nachteilhaft kann auch die Abhängigkeit von einem dritten Tool wie ANTLR sein, sofern minimierte Abhängigkeiten ein Ziel des Projekts sind.