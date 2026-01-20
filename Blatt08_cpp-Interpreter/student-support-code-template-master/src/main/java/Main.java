import SymTable.*;
import ast.*;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Main {
  static MultiScopeSymbolTable symbolTable = new MultiScopeSymbolTable();

  public static void main(String[] args) {
    String filePath = "test.cpp";

    try {

      System.out.println("Reading: " + filePath);
      CharStream input = CharStreams.fromPath(Paths.get(filePath));

      cppLexer lexer = new cppLexer(input);
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      cppParser parser = new cppParser(tokens);

      ParseTree tree = parser.start();

      ASTBuilder builder = new ASTBuilder();
      Program program = (Program) builder.visit(tree);

      printSummary(program);
      generateSymbolTable(program, null);

      System.out.println(symbolTable.lookupSymbol("main").toString());

      System.out.println(program.getMain().getScope().toString());

    } catch (IOException e) {
      System.err.println(
          "Unable to read the File '"
              + filePath
              + "' Verify that it is in the root directory of the project");
    } catch (Exception e) {
      System.err.println("Error during parsing :");
      e.printStackTrace();
    }
  }

  private static void printSummary(Program program) {
    System.out.println("\n AST built successfully!");
    System.out.println("--------------------------------");

    if (program.getMain() != null) {
      System.out.println("[Main Function] " + program.getMain().getReturnType() + " main()");
      System.out.println(
          "  -> " + program.getMain().getBody().getStatements().size() + " statements.");
    }

    System.out.println("[Global Scope] " + program.getStatements().size() + " elements detected.");
    for (Statement stmt : program.getStatements()) {
      System.out.println(
          "  - " + stmt.getClass().getSimpleName() + " (Line " + stmt.getLine() + ")");
    }
    System.out.println("--------------------------------\n");
  }

  // Parent is here, to be able to allocate Scopes to their respective Classes
  private static void generateSymbolTable(ASTNode node, ASTNode parent) {

    if (node == null) {
      return;
    }

    if (node instanceof Program) {

      Program program = (Program) node;
      if (program.getMain() != null) {
        symbolTable.addSymbolToCurrentScope("main", program.getMain().getReturnType(), "main");
        generateSymbolTable(program.getMain().getBody(), program.getMain());
      }

      List<Statement> statements = program.getStatements();

      for (Statement stmt : statements) {
        generateSymbolTable(stmt, null);
      }

    } else if (node instanceof Block) {
      Block block = (Block) node;

      List<Statement> statements = block.getStatements();

      symbolTable.enterScope(parent.getClass() + "_Scope");
      for (Statement stmt : statements) {
        generateSymbolTable(stmt, parent);
      }

      SingleScopeSymbolTable scope = symbolTable.exitScope();

      // Identifies, which Class parent is and sets the scope of parent to this.scope
      if (parent instanceof ConstructorDecl) {
        ConstructorDecl constructorDecl = (ConstructorDecl) parent;

        for (ParamDecl paramDecl : constructorDecl.getParameters()) {
          scope.addSymbol(
              new Symbol(paramDecl.getName(), paramDecl.getType().getTypeName(), "ParamDecl"));
        }
        constructorDecl.setScope(scope);

      } else if (parent instanceof FunctionDecl) {
        FunctionDecl functionDecl = (FunctionDecl) parent;

        for (ParamDecl paramDecl : functionDecl.getParameters()) {
          scope.addSymbol(
              new Symbol(paramDecl.getName(), paramDecl.getType().getTypeName(), "ParamDecl"));
        }
        functionDecl.setScope(scope);

      } else if (parent instanceof IfStmt) {
        IfStmt ifStmt = (IfStmt) parent;
        if (ifStmt.getCondition() != null) {
          ifStmt.setIfScope(scope);
        } else {
          ifStmt.setElseScope(scope);
        }
      } else if (parent instanceof WhileStmt) {
        WhileStmt whileStmt = (WhileStmt) parent;
        whileStmt.setScope(scope);
      } else if (parent instanceof MainFunction) {
        MainFunction mainFunction = (MainFunction) parent;
        mainFunction.setScope(scope);
      } else {
        System.err.println("Error: Statement calls Block Illegaly");
        return;
      }

    } else if (node instanceof FunctionDecl) {
      FunctionDecl funcDecl = (FunctionDecl) node;

      symbolTable.addSymbolToCurrentScope(
          funcDecl.getName(), funcDecl.getReturnType().getTypeName(), "FuncDecl");

      generateSymbolTable(funcDecl.getBody(), funcDecl);
    } else if (node instanceof VarDecl) {
      VarDecl varDecl = (VarDecl) node;

      symbolTable.addSymbolToCurrentScope(
          varDecl.getName(), varDecl.getType().getTypeName(), "VarDecl");
    } else if (node instanceof Expression) {
      Expression expression = (Expression) node;
      symbolTable.addSymbolToCurrentScope("Expression", "", expression.getClass().getName());
    } else if (node instanceof ReturnStmt) {
      ReturnStmt returnStmt = (ReturnStmt) node;
      symbolTable.addSymbolToCurrentScope("returnStmt", "", "ReturnStmt");
    } else if (node instanceof ClassDecl) {
      ClassDecl classDecl = (ClassDecl) node;
      symbolTable.addSymbolToCurrentScope(
          classDecl.getName(), classDecl.getBaseClass(), "ClassDecl");

      symbolTable.enterScope(classDecl.getName() + "_Scope");
      List<Member> members = classDecl.getMembers();
      for (Member member : members) {
        generateSymbolTable(member, classDecl);
      }
      classDecl.setScope(symbolTable.exitScope());
    } else if (node instanceof WhileStmt) {
      WhileStmt whileStmt = (WhileStmt) node;
      symbolTable.addSymbolToCurrentScope("whileStmt", "", "WhileStmt");

      generateSymbolTable(whileStmt.getBlock(), whileStmt);
    } else if (node instanceof IfStmt) {
      IfStmt ifStmt = (IfStmt) node;
      symbolTable.addSymbolToCurrentScope("IfStmt", "", "IfStmt");

      generateSymbolTable(ifStmt.getThenBlock(), ifStmt);
      generateSymbolTable(ifStmt.getElseBlock(), ifStmt);
    } else if (node instanceof Member) {
      MemberStmt memberStmt = (MemberStmt) node;
      generateSymbolTable(memberStmt.getStatement(), parent);
    }
  }
}
