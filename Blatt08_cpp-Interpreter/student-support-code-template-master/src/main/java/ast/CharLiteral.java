package ast;

public class CharLiteral extends Expression {
  private char value;

  public CharLiteral(char value, int line, int col) {
    super(line, col);
    this.value = value;
  }

  public char getValue() {
    return value;
  }
}
