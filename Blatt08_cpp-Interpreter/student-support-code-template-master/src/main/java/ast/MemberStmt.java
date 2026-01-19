package ast;

public class MemberStmt extends Member {
  private Statement statement;

  public MemberStmt(Statement statement, int line, int col) {
    super(line, col);
    this.statement = statement;
  }

  public Statement getStatement() {
    return statement;
  }
}
