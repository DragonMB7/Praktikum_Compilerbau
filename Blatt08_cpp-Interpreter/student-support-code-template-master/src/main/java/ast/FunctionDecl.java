package ast;

import SymTable.SingleScopeSymbolTable;

import java.util.List;

public class FunctionDecl extends Statement {
  private boolean isVirtual;
  private DataType returnType;
  private String name;
  private List<ParamDecl> parameters;
  private Block body;
  private SingleScopeSymbolTable scope;

  public FunctionDecl(
      boolean isVirtual,
      DataType returnType,
      String name,
      List<ParamDecl> parameters,
      Block body,
      int line,
      int col) {
    super(line, col);
    this.isVirtual = isVirtual;
    this.returnType = returnType;
    this.name = name;
    this.parameters = parameters;
    this.body = body;
    this.scope = null;
  }

  public boolean isVirtual() {
    return isVirtual;
  }

  public DataType getReturnType() {
    return returnType;
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

  public SingleScopeSymbolTable getScope() {return scope;}

    public void setScope(SingleScopeSymbolTable scope) {this.scope = scope;}
}
