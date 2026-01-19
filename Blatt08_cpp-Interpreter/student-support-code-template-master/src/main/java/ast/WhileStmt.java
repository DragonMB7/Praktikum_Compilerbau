package ast;

import SymTable.SingleScopeSymbolTable;

import java.util.List;

public class WhileStmt extends Statement {
  private Expression condition;
  private List<Statement> body;
  private SingleScopeSymbolTable scope;

  public WhileStmt(Expression condition, List<Statement> body, int line, int col) {
    super(line, col);
    this.condition = condition;
    this.body = body;
    this.scope = null;
  }

  public Expression getCondition() {
    return condition;
  }

  public List<Statement> getBody() {
    return body;
  }

  public SingleScopeSymbolTable getScope() {return scope;}

    public void setScope(SingleScopeSymbolTable scope) {this.scope = scope;}
}
