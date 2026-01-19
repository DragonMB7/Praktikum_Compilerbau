package ast;

import SymTable.SingleScopeSymbolTable;

public class IfStmt extends Statement {
  private Expression condition;
  private Block thenBlock;
  private Block elseBlock;
  private SingleScopeSymbolTable scope;

  public IfStmt(Expression condition, Block thenBlock, Block elseBlock, int line, int col) {
    super(line, col);
    this.condition = condition;
    this.thenBlock = thenBlock;
    this.elseBlock = elseBlock;
    this.scope = null;
  }

  public Expression getCondition() {
    return condition;
  }

  public Block getThenBlock() {
    return thenBlock;
  }

  public Block getElseBlock() {
    return elseBlock;
  }

  public SingleScopeSymbolTable getScope() {return scope;}

    public void setScope(SingleScopeSymbolTable scope) {this.scope = scope;}
}
