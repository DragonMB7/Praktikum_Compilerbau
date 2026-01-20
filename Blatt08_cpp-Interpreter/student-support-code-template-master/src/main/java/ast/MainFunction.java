package ast;

import SymTable.SingleScopeSymbolTable;

public class MainFunction extends ASTNode {
  private String returnType;
  private Block body;
  private SingleScopeSymbolTable scope;

  public MainFunction(String returnType, Block body, int line, int col) {
    super(line, col);
    this.returnType = returnType;
    this.body = body;
    this.scope = null;
  }

  public String getReturnType() {
    return returnType;
  }

  public Block getBody() {
    return body;
  }

  public void setScope(SingleScopeSymbolTable scope) {
    this.scope = scope;
  }

  public SingleScopeSymbolTable getScope() {
    return scope;
  }
}
