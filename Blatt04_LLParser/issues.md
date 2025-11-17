
# 2) SPEZIALREGELN vs GENERISCHE `(op sExpr*)` — KONFLIKTE

Ihr habt:

```antlr
sExpr : '(' op sExpr* ')'
     | list
     | nth
     | str
     | print
     ...
```

Und gleichzeitig:

```antlr
op : '=' | '<' | '>' | '+' | '*' | '/' | '-' | ID
```

Das bedeutet:

* `head` ist ein **ID**
* `tail` ist ein **ID**
* `list` ist ein **ID**
* `nth` ist ein **ID**
* `str` ist ein **ID**
* `print` ist ein **ID**

Das heißt:

**ALLE Spezialfunktionen sind OPERATOR-Namen.**

Damit kann *jede* Spezialform auch als generisches `(op ...)` geparst werden.

---

## Beispiel 3: `(print "hi")`

Welche Regel passt?

### Variante A: spezielle Regel

```
print → '(print' STRING ')'
```

### Variante B: generische Regel

```
sExpr → '(' op sExpr* ')'
op    → ID ("print")
sExpr → STRING
```

Beide passen **gleichzeitig**!


aber **ein handgeschriebener LL(1)-Parser hat hier ein Problem**, denn:

beim Lesen von `(` weiß der Parser noch nicht, ob es

* ein `print`-Spezialfall
  oder
* ein generisches S-Expression

wird.

Das ist das klassische **LL(1)-Lookahead-Problem**:

* `(` ist immer der Anfang *aller* S-Expressions
* danach kommt ein Identifier (`print`)
* aber ID kann sowohl

  * ein Operator sein
  * als auch ein Spezialform-Schlüsselwort

---

## Ableitungskonflikte im Parse:

```
(print "hi")
```

SExpr → print           → OK
SExpr → '(' op sExpr* ')'  → OK

**Beide sind möglich — ohne weitere Information.**

---

# 3) MEHRDEUTIGKEIT bei LIST/HEAD/TAIL wegen doppelter Definition

Ihr habt:

```antlr
list : '(list' (datatype)* ')' | lPick ;
lPick : '(head' list ')' | '(tail' list ')' ;
```

Was passiert mit:

```
(head (list 1 2))
```

### Ableitung A (über lPick)

```
lPick → '(head' list ')'
list → '(list' datatype* ')'
```

### Ableitung B (über generische Regel):

```
sExpr → '(' op sExpr* ')'
op → ID ("head")
sExpr* → ( (list 1 2) )
```


# KURZFAZIT — DIE PROBLEME IN 5) NOCHMAL KLAR


### ✔ Problem B

**Sonderregeln konkurrieren mit der generischen `(op …)`-Regel.**
Beispiele wie `(print "hi")` können doppelt abgeleitet werden.

### ✔ Problem C

**Doppeldeutige Ableitungen führen zu LL(1)-Lookahead-Problemen.**

### ✔ Problem D

**ANTLR kommt damit dank Prioritäten klar — ein handgeschriebener Parser aber nicht unbedingt.**

---