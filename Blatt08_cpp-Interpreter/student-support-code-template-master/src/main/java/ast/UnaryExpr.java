package ast;

public record UnaryExpr(String operator, Expr expr) implements Expr {
}
