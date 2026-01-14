package ast;

public sealed interface Expr extends ASTNode
    permits AssignExpr, BinaryExpr, BoolLiteral, CallExpr, CharLiteral,
            IdLiteral, IntLiteral, StringLiteral, UnaryExpr {
}
