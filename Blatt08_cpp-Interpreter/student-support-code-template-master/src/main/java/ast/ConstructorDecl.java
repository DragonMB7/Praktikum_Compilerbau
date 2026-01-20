package ast;

import SymTable.SingleScopeSymbolTable;
import java.util.List;

public class ConstructorDecl extends Member {
  private String name;
  private List<ParamDecl> parameters;
  private Block body;
  private SingleScopeSymbolTable scope;

  public ConstructorDecl(String name, List<ParamDecl> parameters, Block body, int line, int col) {
    super(line, col);
    this.name = name;
    this.parameters = parameters;
    this.body = body;
    this.scope = null;
  }

  public String getName() {
    return name;
  }

  public List<ParamDecl> getParameters() {
    return parameters;
  }

  public Block getBody() {
    return body;
  }

  public SingleScopeSymbolTable getScope() {
    return scope;
  }

  public void setScope(SingleScopeSymbolTable scope) {
    this.scope = scope;
  }
}
