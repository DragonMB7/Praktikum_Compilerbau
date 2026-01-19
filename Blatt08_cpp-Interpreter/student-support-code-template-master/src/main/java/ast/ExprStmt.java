package ast;

public class ExprStmt extends Statement {
  private Expression expression;

  public ExprStmt(Expression expression, int line, int col) {
    super(line, col);
    this.expression = expression;
  }

  public Expression getExpression() {
    return expression;
  }
}
