package ast;

import java.util.List;

public class Block extends Statement {
  private List<Statement> statements;

  public Block(List<Statement> statements, int line, int col) {
    super(line, col);
    this.statements = statements;
  }

  public List<Statement> getStatements() {
    return statements;
  }
}
