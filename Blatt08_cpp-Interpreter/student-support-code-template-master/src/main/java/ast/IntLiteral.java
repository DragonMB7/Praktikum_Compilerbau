package ast;

public class IntLiteral extends Expression {
  private int value;

  public IntLiteral(int value, int line, int col) {
    super(line, col);
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
