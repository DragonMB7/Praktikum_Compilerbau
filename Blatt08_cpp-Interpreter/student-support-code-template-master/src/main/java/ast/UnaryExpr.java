package ast;

public class UnaryExpr extends Expression {
  public enum Operator {
    MINUS,
    NOT,
    PLUS
  }

  private Operator operator;
  private Expression operand;

  public UnaryExpr(Operator operator, Expression operand, int line, int col) {
    super(line, col);
    this.operator = operator;
    this.operand = operand;
  }

  public Operator getOperator() {
    return operator;
  }

  public Expression getOperand() {
    return operand;
  }
}
