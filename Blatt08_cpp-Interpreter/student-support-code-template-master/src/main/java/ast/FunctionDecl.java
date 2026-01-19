package ast;

import java.util.List;

public class FunctionDecl extends Statement {
  private boolean isVirtual;
  private DataType returnType;
  private String name;
  private List<ParamDecl> parameters;
  private Block body;

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
}
