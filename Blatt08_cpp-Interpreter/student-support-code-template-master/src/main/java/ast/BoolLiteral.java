package ast;

public class BoolLiteral extends Expression {
  private boolean value;

  public BoolLiteral(boolean value, int line, int col) {
    super(line, col);
    this.value = value;
  }

  public boolean getValue() {
    return value;
  }
}
