package ast;

public record AssignExpr(String name, Expr expr) implements Expr {
}
