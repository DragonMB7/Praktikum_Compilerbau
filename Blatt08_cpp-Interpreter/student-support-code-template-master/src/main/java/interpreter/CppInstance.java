package interpreter;

import ast.ClassDecl;
import ast.Member;
import ast.MemberStmt;
import ast.VarDecl;
import java.util.HashMap;
import java.util.Map;

public class CppInstance {
  private ClassDecl classDecl;
  private Map<String, Object> fields = new HashMap<>();

  public CppInstance(ClassDecl classDecl) {
    this.classDecl = classDecl;
    initializeFields();
  }

  private void initializeFields() {
    for (Member m : classDecl.getMembers()) {
      if (m instanceof MemberStmt) {
        ast.Statement stmt = ((MemberStmt) m).getStatement();

        if (stmt instanceof VarDecl) {
          VarDecl vd = (VarDecl) stmt;
          fields.put(vd.getName(), 0);
        }
      }
    }
  }

  public Object getField(String name) {
    if (fields.containsKey(name)) return fields.get(name);
    throw new RuntimeException(
        "Field '" + name + "' not found in object of class " + classDecl.getName());
  }

  public void setField(String name, Object value) {
    if (fields.containsKey(name)) {
      fields.put(name, value);
    } else {
      throw new RuntimeException(
          "Field '" + name + "' does not exist in class " + classDecl.getName());
    }
  }

  public Map<String, Object> getFields() {
    return fields;
  }

  public ClassDecl getClassDecl() {
    return classDecl;
  }
}
