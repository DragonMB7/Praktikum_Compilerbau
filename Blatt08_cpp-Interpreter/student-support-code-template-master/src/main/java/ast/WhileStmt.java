package ast;

import SymTable.SingleScopeSymbolTable;

public class WhileStmt extends Statement {
  private Expression condition;
  private Block block;
  private SingleScopeSymbolTable scope;

  public WhileStmt(Expression condition, Block block, int line, int col) {
    super(line, col);
    this.condition = condition;
    this.block = block;
    this.scope = null;
  }

  public Expression getCondition() {
    return condition;
  }

  public Block getBlock() {
    return block;
  }

  public SingleScopeSymbolTable getScope() {
    return scope;
  }

  public void setScope(SingleScopeSymbolTable scope) {
    this.scope = scope;
  }
}
