package ast;

import java.util.List;

public class MethodCallExpr extends Expression {
    private String objectName;
    private String methodName;
    private List<Expression> arguments;

    public MethodCallExpr(String objectName, String methodName,
                          List<Expression> arguments, int line, int col) {
        super(line, col);
        this.objectName = objectName;
        this.methodName = methodName;
        this.arguments = arguments;
    }

    public String getObjectName() { return objectName; }
    public String getMethodName() { return methodName; }
    public List<Expression> getArguments() { return arguments; }
}
