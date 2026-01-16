package ast;

import java.util.List;

public class FunctionCall extends Expression {
    private String functionName;
    private List<Expression> arguments;

    public FunctionCall(String functionName, List<Expression> arguments,
                        int line, int col) {
        super(line, col);
        this.functionName = functionName;
        this.arguments = arguments;
    }

    public String getFunctionName() { return functionName; }
    public List<Expression> getArguments() { return arguments; }
}
