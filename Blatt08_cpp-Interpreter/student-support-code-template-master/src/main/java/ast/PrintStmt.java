package ast;

public class PrintStmt extends Statement {
  public enum PrintType {
    BOOL,
    STRING,
    CHAR,
    INT
  }

  private PrintType type;

  public PrintStmt(PrintType type, int line, int col) {
    super(line, col);
    this.type = type;
  }

  public PrintType getType() {
    return type;
  }
}
