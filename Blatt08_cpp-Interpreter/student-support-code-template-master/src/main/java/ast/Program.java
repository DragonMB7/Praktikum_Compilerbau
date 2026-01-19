package ast;

import java.util.List;

public class Program extends ASTNode {
  private MainFunction main;
  private List<Statement> statements;

  public Program(MainFunction main, List<Statement> statements, int line, int col) {
    super(line, col);
    this.main = main;
    this.statements = statements;
  }

  public MainFunction getMain() {
    return main;
  }

  public List<Statement> getStatements() {
    return statements;
  }
}
