package ast;

import java.util.List;

public class WhileStmt extends Statement {
    private Expression condition;
    private List<Statement> body;

    public WhileStmt(Expression condition, List<Statement> body, int line, int col) {
        super(line, col);
        this.condition = condition;
        this.body = body;
    }

    public Expression getCondition() { return condition; }
    public List<Statement> getBody() { return body; }
}
