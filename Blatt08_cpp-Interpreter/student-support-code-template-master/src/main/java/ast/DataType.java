package ast;

public class DataType extends ASTNode {
    private String typeName;  // "bool", "int", "char", "String", "void", oder Klassenname

    public DataType(String typeName, int line, int col) {
        super(line, col);
        this.typeName = typeName;
    }

    public String getTypeName() { return typeName; }

    public boolean isPrimitive() {
        return typeName.equals("bool") || typeName.equals("int") ||
            typeName.equals("char") || typeName.equals("String") ||
            typeName.equals("void");
    }
}
