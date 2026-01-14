package ast;

import java.util.ArrayList;

public record CallExpr(Expr callee, ArrayList<Expr> args) implements Expr {
}
