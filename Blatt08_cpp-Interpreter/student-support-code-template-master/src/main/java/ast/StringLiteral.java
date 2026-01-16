package ast;

public class StringLiteral extends Expression {
    private String value;

    public StringLiteral(String value, int line, int col) {
        super(line, col);
        this.value = value;
    }

    public String getValue() { return value; }
}
