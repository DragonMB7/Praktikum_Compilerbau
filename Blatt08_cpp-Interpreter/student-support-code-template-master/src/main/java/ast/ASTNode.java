package ast;

public sealed interface ASTNode
    permits Program, Expr, stmt {
}

