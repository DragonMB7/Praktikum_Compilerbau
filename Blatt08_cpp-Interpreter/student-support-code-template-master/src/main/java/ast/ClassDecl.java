package ast;

import SymTable.SingleScopeSymbolTable;
import java.util.List;

public class ClassDecl extends Statement {
  private String name;
  private String baseClass;
  private List<Member> members;
  private SingleScopeSymbolTable scope;

  public ClassDecl(String name, String baseClass, List<Member> members, int line, int col) {
    super(line, col);
    this.name = name;
    this.baseClass = baseClass;
    this.members = members;
    this.scope = null;
  }

  public String getName() {
    return name;
  }

  public String getBaseClass() {
    return baseClass;
  }

  public List<Member> getMembers() {
    return members;
  }

  public SingleScopeSymbolTable getScope() {
    return scope;
  }

  public void setScope(SingleScopeSymbolTable scope) {
    this.scope = scope;
  }
}
