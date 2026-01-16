package ast;

public class ParamDecl extends ASTNode {
    private DataType type;
    private boolean isReference;
    private String name;

    public ParamDecl(DataType type, boolean isReference, String name,
                     int line, int col) {
        super(line, col);
        this.type = type;
        this.isReference = isReference;
        this.name = name;
    }

    public DataType getType() { return type; }
    public boolean isReference() { return isReference; }
    public String getName() { return name; }
}
