package ast;
import ast.Expression;
public abstract class Statement extends ASTNode
{
    public Statement(int line, int column) {
        super(line, column);
    }
}
