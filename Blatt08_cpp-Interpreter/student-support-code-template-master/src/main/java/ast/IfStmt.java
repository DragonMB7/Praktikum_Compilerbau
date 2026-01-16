package ast;

public class IfStmt extends Statement
{
    private Expression condition;
    private Block thenBlock;
    private Block elseBlock;

    public IfStmt(Expression condition, Block thenBlock, Block elseBlock,
                  int line, int col) {
        super(line, col);
        this.condition = condition;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;
    }

    public Expression getCondition() { return condition; }
    public Block getThenBlock() { return thenBlock; }
    public Block getElseBlock() { return elseBlock; }
}
