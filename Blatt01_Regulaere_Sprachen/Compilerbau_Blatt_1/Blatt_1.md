# Blatt 01: Reguläre Sprachen

## **A1.1 — Sprache des regulären Ausdrucks**

### **Gegeben:**

---
### **Schritt-für-Schritt-Erklärung:**

1. **Zerlegung des Ausdrucks**

Der Operator `+` bedeutet **Vereinigung (oder)**:

mit
- `L1 = { a }`
- `L2 = { a(a + b)*a }`

---

2. **Analyse von L2**

`(a + b)*` steht für **beliebig viele (auch null)** Wiederholungen von `a` oder `b`.  
Der Ausdruck `a(a + b)*a` beschreibt also **alle Wörter, die mit `a` beginnen und mit `a` enden**, mit einer beliebigen Folge von `a` und `b` dazwischen.

Beispiele:
`aa`, `abba`, `aba`, `aaa`, `aaba`, usw.


---

3. **Vereinigung**

Da das Wort `a` nicht in `L2` enthalten ist (weil `L2` mindestens zwei `a` enthält), bleibt die Vereinigung notwendig.

**Ergebnis:**

**`L = { a } ∪ { a w a | w ∈ (a + b)* }`**

### **Beispiele für Wörter in L:**

`aa`, `abba`, `aba`, `aaa`, `aaba`, usw.


# A1.2 — Bezeichner in Programmiersprachen

**Gegeben (Kurz):**
- Erlaubte Zeichen: Buchstaben `A–Z`, `a–z`, Ziffern `0–9`, Unterstrich `_`.
- Startzeichen:
    - Variablen: `V` (global) oder `v` (lokal)
    - Funktions-/Methodenparameter: `p`
    - Klassenparameter: `P`
    - sonstige Bezeichner: beliebiger Buchstabe `A–Z`/`a–z`
- Bezeichner dürfen **nicht** mit `_` enden.
- Mindestlänge: **2** Zeichen.

### 1) Regulärer Ausdruck (konkret, vollständig)

```regex
^[A-Za-z][A-Za-z0-9_]*[A-Za-z0-9]$
```


**Erklärung (kurz):**
- ^[A-Za-z] — erster Buchstabe (erfüllt alle Startregeln: V/v/p/P oder sonstiger Buchstabe).

- [A-Za-z0-9_]* — beliebige Folge erlaubter Zeichen (auch `_`) in der Mitte.

- [A-Za-z0-9]$ — letztes Zeichen dürfen nicht `_` sein (Buchstabe oder Ziffer).

- Minimale Länge = 2 (erstes + letztes Zeichen).

**Zwei Beispiele und Matching:**
- V1 → passt: erster V (Bedingung), letztes 1 (nicht `_`).
- pArg2 → passt: erster p, Mitte Arg, letztes 2 (nicht `_`).


### 2) Deterministischer endlicher Automat (DFA)

#### **Zustände**
- `q0` — Startzustand (noch kein Zeichen gelesen)
- `q1` — nach dem ersten Buchstaben gelesen (nicht akzeptierend; garantiert ≥ 1 Zeichen)
- `q_acc` — **akzeptierender Zustand**: das zuletzt gelesene Zeichen ist **Buchstabe oder Ziffer**, und es wurden mindestens 2 Zeichen gelesen
- `q_fehler` — **nicht akzeptierender Zustand**: das zuletzt gelesene Zeichen ist **Unterstrich (`_`)**

#### **Alphabet**

Σ = {Buchstaben (A–Z, a–z), Ziffern (0–9), Unterstrich (`_`)}

#### **Übergänge**

| Zustand    | Eingabe            | Nach       | Beschreibung                                           |
|------------|--------------------|------------|--------------------------------------------------------|
| `q0`       | `letter`           | `q1`       | erstes Zeichen muss Buchstabe sein                     |
| `q0`       | `digit` / `_`      | q_fehler   | ungültig: darf nicht mit Ziffer oder `_` beginnen      |
| `q1`       | `letter` / `digit` | `q_acc`    | zweites gültiges Zeichen → akzeptierend möglich        |
| `q1`       | `_`                | `q_fehler` | Unterstrich nach erstem Buchstaben                     |
| `q_acc`    | `letter` / `digit` | `q_acc`    | weiterhin gültig                                       |
| `q_acc`    | `_`                | `q_fehler` | Unterstrich gelesen → vorübergehend nicht akzeptierend |
| `q_fehler` | `letter` / `digit` | `q_acc`    | Unterstrich gefolgt von gültigem Zeichen               |
| `q_fehler` | `_`                | `q_fehler` | mehrere Unterstriche in Folge                          |


#### **Akzeptierende Zustände**
Ein Wort wird akzeptiert, **wenn es mindestens zwei Zeichen hat**, mit einem **Buchstaben beginnt** und **nicht mit `_` endet**.
Das Wort darf während des Lesens `_` enthalten, aber **nicht am Ende**.

#### Beispiel 1: `V1`
````
q0 --V--> q1 --1--> q_acc (akzeptiert)
````

#### Beispiel 2: `pArg2`
````
q0 --p--> q1 --A--> q_acc --r--> q_acc --g--> q_acc --2--> q_acc (akzeptiert)
````

#### Beispiel 3: `var_`
````
q0 --v--> q1 --a--> q_acc --r--> q_acc ----> q_fehler (nicht akzeptiert, endet mit `_` )
````


### 3. Reguläre (rechte) Grammatik

#### **Terminals**
- `letter` ∈ `[A-Za-z]`
- `digit` ∈ `[0-9]`
- `_` (Unterstrich)

#### **Nichtterminale:**
S, R

#### **Startsymbol:**
S

#### **Produktionen:**
- S → letter R
- R → letter | digit
- R → letter R | digit R
- R → `_` letter | `_` digit
- R → `_` letter R | `_` digit R

#### **Erklärung**
1. `S → letter R`
    - Jedes gültige Wort beginnt mit einem Buchstaben (`letter`).
    - `R` erzeugt den Rest des Wortes.

2. `R → letter | digit`
    - Ermöglicht **ein weiteres Zeichen** (Buchstabe oder Ziffer)
    - Garantiert die **Mindestlänge ≥ 2**.

3. `R → letter R | digit R`
    - Erlaubt beliebige Folgen von Buchstaben und Ziffern.

4. `R → '_' letter | '_' digit`
    - `_` darf in der Mitte stehen, muss aber **von Buchstabe/Ziffer gefolgt** sein.
    - Verhindert, dass ein Wort auf `_` endet.

5. `R → '_' letter R | '_' digit R`
    - Unterstrich gefolgt von Buchstaben/Ziffern **plus Fortsetzung**
    - Erlaubt längere Wörter mit Unterstrichen **in der Mitte**, ohne `_` am Ende.

#### Beispiele

1. Für **V1**:

```text
S
└─ letter ('V')
└─ R
└─ digit ('1')
```

   - S ⇒ letter R ⇒ `V` R ⇒ `V` digit ⇒ `V1`

2. Für **pArg2**:

```text
S
└─ letter ('p')
└─ R
└─ letter R
├─ letter ('A')
└─ R
└─ letter R
├─ letter ('r')
└─ R
└─ digit ('2')
```

   - S ⇒ letter R ⇒ ``p`` R ⇒ ``p`` letter R ⇒ ``p`` ``A`` R ⇒ ``pA`` letter R ⇒ ``pA`` ``r`` R ⇒ ``pAr`` digit ⇒ ``pAr2``


# A1.3 — Gleitkommazahlen in Programmiersprachen

## 1. Aufbau in Python und Java

### **Python**
- Dezimalzahlen mit optionalem Vorzeichen: `123`, `-0.5`, `+3.14`
- Optionaler Exponent: `1e10`, `2.5E-3`
- Ganzzahliger Teil kann fehlen (z.B. `.5` ist gültig)
- Nachkommateil kann fehlen (z.B. `2.` ist gültig)

### **Java**
- Dezimalzahlen ähnlich: `123`, `-0.5`, `3.14`
- Optionaler Exponent: `1e10`, `2.5E-3`
- Optionales Vorzeichen (`+`/`-`)
- Unterschied: Java verlangt mindestens eine Ziffer vor oder nach dem Punkt (`.`)

---

## 2. Regulärer Ausdruck

### **Python**
```regex
^[+-]?(\d+(\.\d*)?|\.\d+)([eE][+-]?\d+)?$
```

### **Java**
```regex
^[+-]?(\d+(\.\d+)?|\d*\.\d+)([eE][+-]?\d+)?$
```

### Erklärung:
- ``^[+-]?`` → optionales Vorzeichen
- ``\d+`` → eine oder mehrere Ziffern
- ``(\.\d*)?`` oder ``(\.\d+)?`` → optionaler Nachkommateil (Python erlaubt .5, Java verlangt mindestens eine Ziffer)
- `([eE][+-]?\d+)?` → optionaler Exponent
- $ → Ende des Strings

**Beispiele für Python gültig:**
    - 123, -0.5, .25, 2., 3.14e10, -1E-3

**Beispiele für Java gültig:**
    - 123, -0.5, 0.25, 2.0, 3.14e10, -1E-3

## 3. Deterministischer endlicher Automat (DFA)


### Zustände (gemeinsam für Python/Java):

- q0 — Startzustand

- q_sign — Vorzeichen gelesen (+/-)

- q_int — Ziffern vor Dezimalpunkt gelesen

- q_dot — Dezimalpunkt gelesen

- q_frac — Nachkomma-Ziffern gelesen

- q_exp — e/E gelesen

- q_exp_sign — optionales Vorzeichen nach e/E

- q_exp_num — Ziffern im Exponenten gelesen

**Akzeptierende Zustände:**
_q_int_, _q_frac_, _q_exp_num_

**Fehkerzustände:**
_q_dead_ bei allen nicht definierten Eingaben

![DFA](Automat.PNG)

**Beispielhafte Abläufe(Python):**

- q0 -(-)-> q_sign -(0)-> q_int -(.)-> q_dot -(5)-> q_frac  
- q0 -(.)-> q_dot -(2)-> q_frac -(5)-> q_frac -(e)-> q_exp -(2)-> q_exp_num


## 4. Reguläre Grammatik

### Nichtterminale
``S``, ``IntPart``, ``FracPart``, ``ExpPart``, ``ExpSign``

### Teminnale
- digit = 0-9
- sign = + | -
- e = e | E
- . = .

### Produktionen (Python):
- S → sign? IntPart FracPart? ExpPart?
- S → sign? . FracPart ExpPart?
- IntPart → digit IntPart? | digit
- FracPart → . digit FracPart? | . digit
- ExpPart → e ExpSign? digit ExpPart? | e ExpSign? digit
- ExpSign → + | - | ε

### Produktionen (Java):
- S → sign? IntPart FracPart? ExpPart?  
- IntPart → digit IntPart? | digit  
- FracPart → . digit FracPart? | . digit  
- ExpPart → e ExpSign? digit ExpPart? | e ExpSign? digit  
- ExpSign → + | - | ε

Bei java nur `S → sign? . FracPart ExpPart?` fehlt.

**Erklärung:**
- IntPart = Ziffern vor dem Punkt

- FracPart = optionaler Nachkommaanteil

- ExpPart = optionaler Exponent

- ExpSign = optionales Vorzeichen im Exponenten

**Beispiel:**
```text
S ⇒ . FracPart ExpPart
   ⇒ . digit FracPart? ExpPart
   ⇒ . 2 5 ε e ExpSign? digit
   ⇒ .25 e 2

```












