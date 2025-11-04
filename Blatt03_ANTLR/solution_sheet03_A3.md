# Aufgabe 3.3 - AST und AST-PrettyPrinter

## Benötigte Knoten des Parse-Tree

<br>

|Parse-Tree-Regel|AST-Knoten|Entfallene Bestandteile|
|-|-|-|
|`start`|`Program(List<Stmt>)`|`EOF`|
| `stmt`|siehe `vardecl`,`assign`,`expr`,`while`,`cond`| `stmt`
| `vardecl`| `VarDecl(type, id, expr?)`|`':='` mit Knoten links`(id)` und rechts `(expr)`|
| `assign`| `Assign(id, expr)`|`':='` mit Knoten links`(id)` und rechts `(expr)`|
| `while`| `While(expr,` alle, die `stmt` ersetzen)| `'while'`, `'do'`,`'end'`|
| `cond`| `If(expr, then, else?)`| `'if'`, `'do'`, `'else'`, `'end'`|
| `expr`| `ExprAdd(id, id)`,<br>`ExprSub(id, id)`,<br>`ExprMul(id, id)`,<br>`ExprDiv(id, id)`,<br> `ExprGT(id, id)`), <br>`ExprLT(id, id)`), <br>`ExprEQ(id, id)`), <br>`ExprNEQ(id, id)`), <br>| Operatorzeichen (`+`, `-`, `*`, etc.) ersetzt durch Knotentyp || `type`| String (`"int"` / `"string"`)|—|
| `ID`, `NUMBER`, `STRING`| `Expr.Var`, `Expr.Num`, `Expr.Str`| —|
| `COMMENT`, `WHITESPACE`, `EOF`| —| vollständig ignoriert|

