package ast;

public class VarDecl extends Statement {
  private DataType type;
  private boolean isReference;
  private String name;
  private Expression initializer;

  public VarDecl(
      DataType type, boolean isReference, String name, Expression initializer, int line, int col) {
    super(line, col);
    this.type = type;
    this.isReference = isReference;
    this.name = name;
    this.initializer = initializer;
  }

  public DataType getType() {
    return type;
  }

  public boolean isReference() {
    return isReference;
  }

  public String getName() {
    return name;
  }

  public Expression getInitializer() {
    return initializer;
  }
}
