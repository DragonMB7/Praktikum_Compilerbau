package ast;

public record BinaryExpr(Expr left, String operator, Expr right) implements Expr {
}
