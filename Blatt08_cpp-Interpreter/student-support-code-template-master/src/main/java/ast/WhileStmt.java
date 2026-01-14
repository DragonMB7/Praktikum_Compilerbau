package ast;

public record WhileStmt(Expr condition, stmt body) implements stmt {
}
