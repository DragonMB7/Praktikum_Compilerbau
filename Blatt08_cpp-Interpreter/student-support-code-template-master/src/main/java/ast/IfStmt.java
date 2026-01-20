package ast;

import SymTable.SingleScopeSymbolTable;

public class IfStmt extends Statement {
  private Expression condition;
  private Block thenBlock;
  private Block elseBlock;
  private SingleScopeSymbolTable ifScope;
  private SingleScopeSymbolTable elseScope;

  public IfStmt(Expression condition, Block thenBlock, Block elseBlock, int line, int col) {
    super(line, col);
    this.condition = condition;
    this.thenBlock = thenBlock;
    this.elseBlock = elseBlock;
    this.ifScope = null;
    this.elseScope = null;
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

  public SingleScopeSymbolTable getIfScope() {return ifScope;}

    public SingleScopeSymbolTable getElseScope() {return elseScope;}

    public void setIfScope(SingleScopeSymbolTable scope) {this.ifScope = scope;}

    public void setElseScope(SingleScopeSymbolTable scope) {this.elseScope = scope;}
}
