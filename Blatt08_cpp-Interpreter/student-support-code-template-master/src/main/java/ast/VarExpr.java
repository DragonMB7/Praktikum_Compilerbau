package ast;

public class VarExpr extends Expression {
    private String name;

    public VarExpr(String name, int line, int col) {
        super(line, col);
        this.name = name;
    }

    public String getName() { return name; }
}
