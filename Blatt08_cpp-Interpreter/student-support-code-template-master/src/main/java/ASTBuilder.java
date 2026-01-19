import ast.*;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

public class ASTBuilder extends cppBaseVisitor<ASTNode> {

  private int getLine(ParserRuleContext ctx) {
    return ctx.start.getLine();
  }

  private int getColumn(ParserRuleContext ctx) {
    return ctx.start.getCharPositionInLine();
  }

  @Override
  public Program visitStart(cppParser.StartContext ctx) {
    MainFunction main = null;
    List<Statement> statements = new ArrayList<>();

    for (int i = 0; i < ctx.getChildCount() - 1; i++) {
      ParseTree child = ctx.getChild(i);

      if (child instanceof cppParser.MainContext) {
        main = (MainFunction) visit(child);
      } else if (child instanceof cppParser.StmtContext) {
        Statement stmt = (Statement) visit(child);
        if (stmt != null) {
          statements.add(stmt);
        }
      }
    }

    return new Program(main, statements, getLine(ctx), getColumn(ctx));
  }

  @Override
  public MainFunction visitMain(cppParser.MainContext ctx) {
    String returnType;
    if (ctx.VOID_KW() != null) {
      returnType = "void";
    } else {
      returnType = "int";
    }

    Block body = (Block) visit(ctx.block());

    return new MainFunction(returnType, body, getLine(ctx), getColumn(ctx));
  }

  @Override
  public ASTNode visitStmt(cppParser.StmtContext ctx) {
    if (ctx.funcdecl() != null) {
      return visit(ctx.funcdecl());
    } else if (ctx.vardecl() != null) {
      return visit(ctx.vardecl());
    } else if (ctx.expr() != null) {
      Expression expr = (Expression) visit(ctx.expr());
      return new ExprStmt(expr, getLine(ctx), getColumn(ctx));
    } else if (ctx.object() != null) {
      return visit(ctx.object());
    } else if (ctx.while_() != null) {
      return visit(ctx.while_());
    } else if (ctx.if_() != null) {
      return visit(ctx.if_());
    } else if (ctx.return_() != null) {
      return visit(ctx.return_());
    } else if (ctx.class_() != null) {
      return visit(ctx.class_());
    }
    return null;
  }

  @Override
  public FunctionDecl visitFuncdecl(cppParser.FuncdeclContext ctx) {
    boolean isVirtual = (ctx.getChild(0).getText().equals("virtual"));

    DataType returnType = (DataType) visit(ctx.dataType());
    String name = ctx.ID().getText();

    List<ParamDecl> parameters = new ArrayList<>();
    if (ctx.paradecl() != null) {
      parameters = visitParamdecl(ctx.paradecl());
    }

    Block body = (Block) visit(ctx.block());

    return new FunctionDecl(
        isVirtual, returnType, name, parameters, body, getLine(ctx), getColumn(ctx));
  }

  @Override
  public VarDecl visitVardecl(cppParser.VardeclContext ctx) {
    DataType type = null;
    boolean isReference = false;
    String name;
    Expression initializer = null;

    if (ctx.dataType() != null) {
      type = (DataType) visit(ctx.dataType());
      isReference = ctx.getChild(1).getText().equals("&");

      if (isReference) {
        name = ctx.ID().getText();
      } else {
        name = ctx.ID().getText();
      }

      if (ctx.expr() != null) {
        initializer = (Expression) visit(ctx.expr());
      }
    } else {
      name = ctx.ID().getText();
      for (int i = 0; i < ctx.getChildCount(); i++) {
        if (ctx.getChild(i).getText().equals("&")) {
          isReference = true;
          break;
        }
      }
      initializer = (Expression) visit(ctx.expr());
    }

    return new VarDecl(type, isReference, name, initializer, getLine(ctx), getColumn(ctx));
  }

  @Override
  public WhileStmt visitWhile(cppParser.WhileContext ctx) {
    Expression condition = (Expression) visit(ctx.expr());

    List<Statement> body = new ArrayList<>();
    for (cppParser.StmtContext stmtCtx : ctx.stmt()) {
      Statement stmt = (Statement) visit(stmtCtx);
      if (stmt != null) {
        body.add(stmt);
      }
    }

    return new WhileStmt(condition, body, getLine(ctx), getColumn(ctx));
  }

  @Override
  public IfStmt visitIf(cppParser.IfContext ctx) {
    Expression condition = (Expression) visit(ctx.expr());
    Block thenBlock = (Block) visit(ctx.block(0));

    Block elseBlock = null;
    if (ctx.block().size() > 1) {
      elseBlock = (Block) visit(ctx.block(1));
    }

    return new IfStmt(condition, thenBlock, elseBlock, getLine(ctx), getColumn(ctx));
  }

  @Override
  public ReturnStmt visitReturn(cppParser.ReturnContext ctx) {
    Expression expression = null;
    if (ctx.expr() != null) {
      expression = (Expression) visit(ctx.expr());
    }

    return new ReturnStmt(expression, getLine(ctx), getColumn(ctx));
  }

  @Override
  public Block visitBlock(cppParser.BlockContext ctx) {
    List<Statement> statements = new ArrayList<>();
    for (cppParser.StmtContext stmtCtx : ctx.stmt()) {
      Statement stmt = (Statement) visit(stmtCtx);
      if (stmt != null) {
        statements.add(stmt);
      }
    }

    return new Block(statements, getLine(ctx), getColumn(ctx));
  }

  @Override
  public ClassDecl visitClass(cppParser.ClassContext ctx) {
    String name = ctx.ID(0).getText();

    String baseClass = null;
    if (ctx.ID().size() > 1) {
      baseClass = ctx.ID(1).getText();
    }

    List<Member> members = new ArrayList<>();
    for (cppParser.MemberContext memberCtx : ctx.member()) {
      Member member = (Member) visit(memberCtx);
      if (member != null) {
        members.add(member);
      }
    }

    return new ClassDecl(name, baseClass, members, getLine(ctx), getColumn(ctx));
  }

  @Override
  public Member visitMember(cppParser.MemberContext ctx) {
    if (ctx.konstruktordecl() != null) {
      return (Member) visit(ctx.konstruktordecl());
    } else if (ctx.stmt() != null) {
      Statement stmt = (Statement) visit(ctx.stmt());
      return new MemberStmt(stmt, getLine(ctx), getColumn(ctx));
    }
    return null;
  }

  @Override
  public ConstructorDecl visitKonstruktordecl(cppParser.KonstruktordeclContext ctx) {
    String name = ctx.ID().getText();

    List<ParamDecl> parameters = new ArrayList<>();
    if (ctx.paradecl() != null) {
      parameters = visitParamdecl(ctx.paradecl());
    }

    Block body = (Block) visit(ctx.block());

    return new ConstructorDecl(name, parameters, body, getLine(ctx), getColumn(ctx));
  }

  public List<ParamDecl> visitParamdecl(cppParser.ParadeclContext ctx) {
    List<ParamDecl> parameters = new ArrayList<>();

    DataType type = (DataType) visit(ctx.dataType(0));
    boolean isReference = false;

    int idIndex = 0;
    for (int i = 0; i < ctx.getChildCount(); i++) {
      ParseTree child = ctx.getChild(i);
      if (child.getText().equals("&")) {
        isReference = true;
        break;
      }
      if (child == ctx.ID(idIndex)) {
        break;
      }
    }

    String name = ctx.ID(0).getText();
    parameters.add(new ParamDecl(type, isReference, name, getLine(ctx), getColumn(ctx)));

    for (int i = 1; i < ctx.dataType().size(); i++) {
      type = (DataType) visit(ctx.dataType(i));
      name = ctx.ID(i).getText();
      isReference = false;

      parameters.add(new ParamDecl(type, isReference, name, getLine(ctx), getColumn(ctx)));
    }

    return parameters;
  }

  @Override
  public DataType visitDataType(cppParser.DataTypeContext ctx) {
    String typeName;

    if (ctx.BOOL_KW() != null) {
      typeName = "bool";
    } else if (ctx.INT_KW() != null) {
      typeName = "int";
    } else if (ctx.CHAR_KW() != null) {
      typeName = "char";
    } else if (ctx.STRING_KW() != null) {
      typeName = "String";
    } else if (ctx.VOID_KW() != null) {
      typeName = "void";
    } else {
      typeName = ctx.ID().getText();
    }

    return new DataType(typeName, getLine(ctx), getColumn(ctx));
  }

  @Override
  public Expression visitExpr(cppParser.ExprContext ctx) {
    if (ctx.ID() != null && ctx.expr() != null) {
      String targetName = ctx.ID().getText();
      Expression value = (Expression) visit(ctx.expr());
      return new AssignExpr(targetName, value, getLine(ctx), getColumn(ctx));
    } else if (ctx.logicalOrExpr() != null) {
      return (Expression) visit(ctx.logicalOrExpr());
    }

    throw new RuntimeException("Invalid expression at line " + getLine(ctx));
  }

  @Override
  public Expression visitLogicalOrExpr(cppParser.LogicalOrExprContext ctx) {
    Expression left = (Expression) visit(ctx.logicalAndExpr(0));

    for (int i = 1; i < ctx.logicalAndExpr().size(); i++) {
      Expression right = (Expression) visit(ctx.logicalAndExpr(i));
      left = new BinaryExpr(left, BinaryExpr.Operator.OR, right, getLine(ctx), getColumn(ctx));
    }

    return left;
  }

  @Override
  public Expression visitLogicalAndExpr(cppParser.LogicalAndExprContext ctx) {
    Expression left = (Expression) visit(ctx.equalityExpr(0));

    for (int i = 1; i < ctx.equalityExpr().size(); i++) {
      Expression right = (Expression) visit(ctx.equalityExpr(i));
      left = new BinaryExpr(left, BinaryExpr.Operator.AND, right, getLine(ctx), getColumn(ctx));
    }

    return left;
  }

  @Override
  public Expression visitEqualityExpr(cppParser.EqualityExprContext ctx) {
    Expression left = (Expression) visit(ctx.relationalExpr(0));

    for (int i = 1; i < ctx.relationalExpr().size(); i++) {
      String op = ctx.getChild(2 * i - 1).getText();
      BinaryExpr.Operator operator =
          op.equals("==") ? BinaryExpr.Operator.EQ : BinaryExpr.Operator.NE;

      Expression right = (Expression) visit(ctx.relationalExpr(i));
      left = new BinaryExpr(left, operator, right, getLine(ctx), getColumn(ctx));
    }

    return left;
  }

  @Override
  public Expression visitRelationalExpr(cppParser.RelationalExprContext ctx) {
    Expression left = (Expression) visit(ctx.additiveExpr(0));

    for (int i = 1; i < ctx.additiveExpr().size(); i++) {
      String op = ctx.getChild(2 * i - 1).getText();
      BinaryExpr.Operator operator;

      switch (op) {
        case "<":
          operator = BinaryExpr.Operator.LT;
          break;
        case "<=":
          operator = BinaryExpr.Operator.LE;
          break;
        case ">":
          operator = BinaryExpr.Operator.GT;
          break;
        case ">=":
          operator = BinaryExpr.Operator.GE;
          break;
        default:
          throw new RuntimeException("Unknown operator: " + op);
      }

      Expression right = (Expression) visit(ctx.additiveExpr(i));
      left = new BinaryExpr(left, operator, right, getLine(ctx), getColumn(ctx));
    }

    return left;
  }

  @Override
  public Expression visitAdditiveExpr(cppParser.AdditiveExprContext ctx) {
    Expression left = (Expression) visit(ctx.mulExpr(0));

    for (int i = 1; i < ctx.mulExpr().size(); i++) {
      String op = ctx.getChild(2 * i - 1).getText();
      BinaryExpr.Operator operator =
          op.equals("+") ? BinaryExpr.Operator.ADD : BinaryExpr.Operator.SUB;

      Expression right = (Expression) visit(ctx.mulExpr(i));
      left = new BinaryExpr(left, operator, right, getLine(ctx), getColumn(ctx));
    }

    return left;
  }

  @Override
  public Expression visitMulExpr(cppParser.MulExprContext ctx) {
    Expression left = (Expression) visit(ctx.unaryExpr(0));

    for (int i = 1; i < ctx.unaryExpr().size(); i++) {
      String op = ctx.getChild(2 * i - 1).getText();
      BinaryExpr.Operator operator;

      switch (op) {
        case "*":
          operator = BinaryExpr.Operator.MUL;
          break;
        case "/":
          operator = BinaryExpr.Operator.DIV;
          break;
        case "%":
          operator = BinaryExpr.Operator.MOD;
          break;
        default:
          throw new RuntimeException("Unknown operator: " + op);
      }

      Expression right = (Expression) visit(ctx.unaryExpr(i));
      left = new BinaryExpr(left, operator, right, getLine(ctx), getColumn(ctx));
    }

    return left;
  }

  @Override
  public Expression visitUnaryExpr(cppParser.UnaryExprContext ctx) {
    if (ctx.unaryExpr() != null) {
      String op = ctx.getChild(0).getText();
      UnaryExpr.Operator operator;

      switch (op) {
        case "-":
          operator = UnaryExpr.Operator.MINUS;
          break;
        case "!":
          operator = UnaryExpr.Operator.NOT;
          break;
        case "+":
          operator = UnaryExpr.Operator.PLUS;
          break;
        default:
          throw new RuntimeException("Unknown unary operator: " + op);
      }

      Expression operand = (Expression) visit(ctx.unaryExpr());
      return new UnaryExpr(operator, operand, getLine(ctx), getColumn(ctx));
    } else {
      return (Expression) visit(ctx.primaryExpr());
    }
  }

  @Override
  public Expression visitPrimaryExpr(cppParser.PrimaryExprContext ctx) {
    // 1. NUM
    if (ctx.NUM() != null) {
      int value = Integer.parseInt(ctx.NUM().getText());
      return new IntLiteral(value, getLine(ctx), getColumn(ctx));
    }

    // 2. STRING
    if (ctx.STRING() != null) {
      String text = ctx.STRING().getText();
      String value = text.substring(1, text.length() - 1);
      value = processEscapes(value);
      return new StringLiteral(value, getLine(ctx), getColumn(ctx));
    }

    // 3. CHAR
    if (ctx.CHAR() != null) {
      String text = ctx.CHAR().getText();
      String charStr = text.substring(1, text.length() - 1);
      char value = charStr.startsWith("\\") ? processCharEscape(charStr) : charStr.charAt(0);
      return new CharLiteral(value, getLine(ctx), getColumn(ctx));
    }

    // 4. BOOL
    if (ctx.BOOL() != null) {
      boolean value = ctx.BOOL().getText().equals("true");
      return new BoolLiteral(value, getLine(ctx), getColumn(ctx));
    }

    // 5. '(' expr ')'
    if (ctx.expr() != null) {
      Expression expr = (Expression) visit(ctx.expr());
      return new ParenExpr(expr, getLine(ctx), getColumn(ctx));
    }

    if (ctx.function() != null && ctx.ID().isEmpty()) {
      return (Expression) visit(ctx.function());
    }

    if (ctx.ID().size() == 1 && ctx.function() != null) {
      String objectName = ctx.ID(0).getText();
      FunctionCall funcCall = (FunctionCall) visit(ctx.function());
      return new MethodCallExpr(
          objectName,
          funcCall.getFunctionName(),
          funcCall.getArguments(),
          getLine(ctx),
          getColumn(ctx));
    }

    if (ctx.ID().size() == 2 && ctx.function() == null) {
      String objectName = ctx.ID(0).getText();
      String memberName = ctx.ID(1).getText();
      return new MemberAccessExpr(objectName, memberName, getLine(ctx), getColumn(ctx));
    }

    if (ctx.ID().size() == 1 && ctx.function() == null) {
      return new VarExpr(ctx.ID(0).getText(), getLine(ctx), getColumn(ctx));
    }

    throw new RuntimeException(
        "primaryExpr non reconnu à la ligne " + getLine(ctx) + ": " + ctx.getText());
  }

  @Override
  public FunctionCall visitFunction(cppParser.FunctionContext ctx) {
    String functionName = ctx.ID().getText();

    List<Expression> arguments = new ArrayList<>();
    if (ctx.parameters() != null) {
      for (cppParser.ExprContext exprCtx : ctx.parameters().expr()) {
        Expression arg = (Expression) visit(exprCtx);
        arguments.add(arg);
      }
    }

    return new FunctionCall(functionName, arguments, getLine(ctx), getColumn(ctx));
  }

  private String processEscapes(String str) {
    return str.replace("\\n", "\n")
        .replace("\\t", "\t")
        .replace("\\r", "\r")
        .replace("\\\\", "\\")
        .replace("\\\"", "\"")
        .replace("\\0", "\0");
  }

  private char processCharEscape(String str) {
    if (str.equals("\\n")) return '\n';
    if (str.equals("\\t")) return '\t';
    if (str.equals("\\r")) return '\r';
    if (str.equals("\\\\")) return '\\';
    if (str.equals("\\'")) return '\'';
    if (str.equals("\\0")) return '\0';
    return str.charAt(1);
  }
}
