package ast;

public abstract class ASTNode {
  protected int line;
  protected int column;

  public ASTNode(int line, int column) {
    this.line = line;
    this.column = column;
  }

  // Getters
  public int getLine() {
    return line;
  }

  public int getColumn() {
    return column;
  }
}
