package ast;

public sealed interface stmt extends ASTNode
        permits VarDecl, FuncDecl, IfStmt, WhileStmt,
                ReturnStmt, PrintStmt, ClassDecl, ExprStmt{
}
