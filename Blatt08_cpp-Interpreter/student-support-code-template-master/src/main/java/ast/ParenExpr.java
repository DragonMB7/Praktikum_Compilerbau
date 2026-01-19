package ast;

public class ParenExpr extends Expression {
  private Expression expression;

  public ParenExpr(Expression expression, int line, int col) {
    super(line, col);
    this.expression = expression;
  }

  public Expression getExpression() {
    return expression;
  }
}
