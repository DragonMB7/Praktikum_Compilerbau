package ast;

public class BinaryExpr extends Expression {
    public enum Operator {

        OR, AND,

        EQ, NE,

        LT, LE, GT, GE,

        ADD, SUB,

        MUL, DIV, MOD
    }

    private Expression left;
    private Operator operator;
    private Expression right;

    public BinaryExpr(Expression left, Operator operator, Expression right,
                      int line, int col) {
        super(line, col);
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public Expression getLeft() { return left; }
    public Operator getOperator() { return operator; }
    public Expression getRight() { return right; }
}
