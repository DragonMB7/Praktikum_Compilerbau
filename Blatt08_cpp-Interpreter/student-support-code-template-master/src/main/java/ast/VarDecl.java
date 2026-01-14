package ast;

public record VarDecl(String name, Expr initializer) implements stmt {
}
