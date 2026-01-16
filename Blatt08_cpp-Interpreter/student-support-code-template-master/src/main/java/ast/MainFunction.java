package ast;
import ast.Expression;


public class MainFunction extends ASTNode
{
    private String returnType;
    private Block body;

    public MainFunction(String returnType, Block body, int line, int col) {
        super(line, col);
        this.returnType = returnType;
        this.body = body;
    }

    public String getReturnType() { return returnType; }
    public Block getBody() { return body; }
}
