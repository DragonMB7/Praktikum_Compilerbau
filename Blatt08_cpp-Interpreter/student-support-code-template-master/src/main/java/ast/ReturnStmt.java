package ast;

public class ReturnStmt extends Statement {
  private Expression expression; // peut être null

  public ReturnStmt(Expression expression, int line, int col) {
    super(line, col);
    this.expression = expression;
  }

  public Expression getExpression() {
    return expression;
  }
}
