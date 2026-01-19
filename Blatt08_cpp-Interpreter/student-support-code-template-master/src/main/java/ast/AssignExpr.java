package ast;

public class AssignExpr extends Expression {
  private String targetName;
  private Expression value;

  public AssignExpr(String targetName, Expression value, int line, int col) {
    super(line, col);
    this.targetName = targetName;
    this.value = value;
  }

  public String getTargetName() {
    return targetName;
  }

  public Expression getValue() {
    return value;
  }
}
