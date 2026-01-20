package interpreter;

import ast.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interpreter {

  private Environment globalEnv = new Environment();
  private Environment currentEnv = globalEnv;

  private Map<String, FunctionDecl> functions = new HashMap<>();
  private Map<String, ClassDecl> classes = new HashMap<>();

  public void interpret(Program program) {
    try {
      preScan(program);

      for (Statement stmt : program.getStatements()) {
        if (!(stmt instanceof FunctionDecl) && !(stmt instanceof ClassDecl)) {
          execute(stmt);
        }
      }

      if (program.getMain() != null) {
        executeBlock(program.getMain().getBody(), new Environment(globalEnv));
      }

    } catch (ReturnException e) {
      System.out.println("Program finished with return code: " + e.value);
    } catch (RuntimeException e) {
      System.err.println("Runtime Error: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private void preScan(Program program) {
    for (Statement stmt : program.getStatements()) {
      if (stmt instanceof FunctionDecl) {
        FunctionDecl fd = (FunctionDecl) stmt;
        functions.put(fd.getName(), fd);
      } else if (stmt instanceof ClassDecl) {
        ClassDecl cd = (ClassDecl) stmt;
        classes.put(cd.getName(), cd);
      }
    }
  }

  private void execute(ASTNode node) {
    if (node instanceof VarDecl) visitVarDecl((VarDecl) node);
    else if (node instanceof ExprStmt) evaluate(((ExprStmt) node).getExpression());
    else if (node instanceof IfStmt) visitIf((IfStmt) node);
    else if (node instanceof WhileStmt) visitWhile((WhileStmt) node);
    else if (node instanceof ReturnStmt) visitReturn((ReturnStmt) node);
    else if (node instanceof Block) executeBlock((Block) node, new Environment(currentEnv));
  }

  private void visitVarDecl(VarDecl decl) {
    Object value = null;

    if (decl.getInitializer() != null) {
      value = evaluate(decl.getInitializer());
    } else if (classes.containsKey(decl.getType().getTypeName())) {
      ClassDecl cd = classes.get(decl.getType().getTypeName());
      CppInstance instance = new CppInstance(cd);
      value = instance;
      callConstructor(instance, cd);
    }

    currentEnv.define(decl.getName(), value);
  }

  private void visitIf(IfStmt stmt) {
    Object cond = evaluate(stmt.getCondition());
    if (isTruthy(cond)) {
      executeBlock(stmt.getThenBlock(), new Environment(currentEnv));
    } else if (stmt.getElseBlock() != null) {
      executeBlock(stmt.getElseBlock(), new Environment(currentEnv));
    }
  }

  private void visitWhile(WhileStmt stmt) {
    while (isTruthy(evaluate(stmt.getCondition()))) {
      executeBlock(stmt.getBlock(), new Environment(currentEnv));
    }
  }

  private void visitReturn(ReturnStmt stmt) {
    Object value = null;
    if (stmt.getExpression() != null) {
      value = evaluate(stmt.getExpression());
    }
    throw new ReturnException(value);
  }

  private void executeBlock(Block block, Environment env) {
    Environment previous = this.currentEnv;
    this.currentEnv = env;
    try {
      for (Statement stmt : block.getStatements()) {
        execute(stmt);
      }
    } finally {
      this.currentEnv = previous;
    }
  }

  private Object evaluate(Expression expr) {
    if (expr instanceof IntLiteral) return ((IntLiteral) expr).getValue();
    if (expr instanceof BoolLiteral) return ((BoolLiteral) expr).getValue();
    if (expr instanceof StringLiteral) return ((StringLiteral) expr).getValue();
    if (expr instanceof CharLiteral) return ((CharLiteral) expr).getValue();

    if (expr instanceof VarExpr) {
      return currentEnv.get(((VarExpr) expr).getName());
    }

    if (expr instanceof ParenExpr) {
      return evaluate(((ParenExpr) expr).getExpression());
    }

    if (expr instanceof BinaryExpr) {
      BinaryExpr b = (BinaryExpr) expr;
      return applyBinaryOp(b.getOperator(), evaluate(b.getLeft()), evaluate(b.getRight()));
    }

    if (expr instanceof UnaryExpr) {
      return applyUnaryOp((UnaryExpr) expr);
    }

    if (expr instanceof AssignExpr) {
      AssignExpr a = (AssignExpr) expr;
      Object value = evaluate(a.getValue());
      currentEnv.assign(a.getTargetName(), value);
      return value;
    }

    if (expr instanceof FunctionCall) return visitFunctionCall((FunctionCall) expr);
    if (expr instanceof MethodCallExpr) return visitMethodCall((MethodCallExpr) expr);
    if (expr instanceof MemberAccessExpr) return visitMemberAccess((MemberAccessExpr) expr);

    throw new RuntimeException("Unsupported expression type: " + expr.getClass().getSimpleName());
  }

  private Object applyBinaryOp(BinaryExpr.Operator op, Object left, Object right) {
    if (op == BinaryExpr.Operator.ADD && (left instanceof String || right instanceof String)) {
      return left.toString() + right.toString();
    }

    if (op == BinaryExpr.Operator.AND) return asBool(left) && asBool(right);
    if (op == BinaryExpr.Operator.OR) return asBool(left) || asBool(right);
    if (op == BinaryExpr.Operator.EQ) return left.equals(right);
    if (op == BinaryExpr.Operator.NE) return !left.equals(right);

    int l = asInt(left);
    int r = asInt(right);

    switch (op) {
      case ADD:
        return l + r;
      case SUB:
        return l - r;
      case MUL:
        return l * r;
      case DIV:
        if (r == 0) throw new RuntimeException("Division by zero!");
        return l / r;
      case MOD:
        return l % r;
      case LT:
        return l < r;
      case LE:
        return l <= r;
      case GT:
        return l > r;
      case GE:
        return l >= r;
      default:
        throw new RuntimeException("Unknown binary operator: " + op);
    }
  }

  private Object applyUnaryOp(UnaryExpr expr) {
    Object val = evaluate(expr.getOperand());
    switch (expr.getOperator()) {
      case MINUS:
        return -asInt(val);
      case PLUS:
        return asInt(val);
      case NOT:
        return !asBool(val);
      default:
        throw new RuntimeException("Unknown unary operator");
    }
  }

  private Object visitFunctionCall(FunctionCall call) {
    String name = call.getFunctionName();

    if (name.equals("print_int") || name.equals("print")) {
      Object val = evaluate(call.getArguments().get(0));
      System.out.println(val);
      return null;
    }

    FunctionDecl func = functions.get(name);
    if (func == null) throw new RuntimeException("Function '" + name + "' is not defined.");

    Environment funcEnv = new Environment(globalEnv);
    mapArgumentsToParams(funcEnv, func.getParameters(), call.getArguments());

    try {
      executeBlock(func.getBody(), funcEnv);
    } catch (ReturnException e) {
      return e.value;
    }
    return null;
  }

  private Object visitMethodCall(MethodCallExpr call) {
    Object obj = currentEnv.get(call.getObjectName());
    if (!(obj instanceof CppInstance)) {
      throw new RuntimeException("Variable '" + call.getObjectName() + "' is not an object.");
    }
    CppInstance instance = (CppInstance) obj;

    FunctionDecl method = findMethod(instance.getClassDecl(), call.getMethodName());
    if (method == null)
      throw new RuntimeException(
          "Méthod '"
              + call.getMethodName()
              + "' not found in class "
              + instance.getClassDecl().getName());

    Environment methodEnv = new Environment(globalEnv);

    injectInstanceFields(methodEnv, instance);

    mapArgumentsToParams(methodEnv, method.getParameters(), call.getArguments());

    try {
      executeBlock(method.getBody(), methodEnv);
    } catch (ReturnException e) {
      updateInstanceFields(methodEnv, instance);
      return e.value;
    }

    updateInstanceFields(methodEnv, instance);
    return null;
  }

  private void callConstructor(CppInstance instance, ClassDecl classDecl) {
    for (Member m : classDecl.getMembers()) {
      if (m instanceof ConstructorDecl) {
        ConstructorDecl c = (ConstructorDecl) m;
        if (c.getName().equals(classDecl.getName())) {
          Environment constrEnv = new Environment(globalEnv);
          executeBlock(c.getBody(), constrEnv);
          updateInstanceFields(constrEnv, instance);
          return;
        }
      }
    }
  }

  private void mapArgumentsToParams(
      Environment env, List<ParamDecl> params, List<Expression> args) {
    if (params.size() != args.size()) {
      throw new RuntimeException(
          "Invalid argument count: expected " + params.size() + ", received " + args.size());
    }
    for (int i = 0; i < params.size(); i++) {
      Object val = evaluate(args.get(i));
      env.define(params.get(i).getName(), val);
    }
  }

  private FunctionDecl findMethod(ClassDecl cd, String methodName) {
    for (Member m : cd.getMembers()) {
      if (m instanceof MemberStmt) {
        Statement stmt = ((MemberStmt) m).getStatement();

        if (stmt instanceof FunctionDecl) {
          FunctionDecl fd = (FunctionDecl) stmt;
          if (fd.getName().equals(methodName)) {
            return fd;
          }
        }
      }
    }
    return null;
  }

  private Object visitMemberAccess(MemberAccessExpr expr) {
    Object obj = currentEnv.get(expr.getObjectName());
    if (obj instanceof CppInstance) {
      return ((CppInstance) obj).getField(expr.getMemberName());
    }
    throw new RuntimeException(
        "Cannot access field '" + expr.getMemberName() + "' on " + expr.getObjectName());
  }

  private void injectInstanceFields(Environment env, CppInstance instance) {
    for (Map.Entry<String, Object> entry : instance.getFields().entrySet()) {
      env.define(entry.getKey(), entry.getValue());
    }
  }

  private void updateInstanceFields(Environment env, CppInstance instance) {
    for (String fieldName : instance.getFields().keySet()) {
      try {
        Object visibleValue = env.get(fieldName);
        instance.setField(fieldName, visibleValue);
      } catch (RuntimeException e) {
      }
    }
  }

  private int asInt(Object o) {
    if (o instanceof Integer) return (int) o;
    throw new RuntimeException(
        "Type Mismatch: expected int, received " + o.getClass().getSimpleName());
  }

  private boolean asBool(Object o) {
    if (o instanceof Boolean) return (boolean) o;
    throw new RuntimeException(
        "Type Mismatch: expected bool, received " + o.getClass().getSimpleName());
  }

  private boolean isTruthy(Object o) {
    if (o instanceof Boolean) return (boolean) o;
    if (o instanceof Integer) return (int) o != 0;
    return false;
  }
}
