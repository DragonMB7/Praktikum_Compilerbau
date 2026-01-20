import SymTable.*;
import ast.*;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Main {
    MultiScopeSymbolTable symbolTable = new MultiScopeSymbolTable();

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

  //Parent is here, to be able to allocate Scopes to their respective Classes
  private void generateSymbolTable(ASTNode node, ASTNode parent){


      if (node == null){
          return;
      }

      if(node instanceof Program) {

          Program program = (Program) node;
          if(program.getMain() != null) {
              symbolTable.addSymbolToCurrentScope("main", program.getMain().getReturnType(), "main");
              generateSymbolTable(program.getMain().getBody(), program.getMain());
          }

          List<Statement> statements = program.getStatements();

          for (Statement stmt : statements) {
                generateSymbolTable(stmt, null);
          }

      } else if(node instanceof Block) {
          Block block = (Block) node;

          List<Statement> statements = block.getStatements();

          symbolTable.enterScope(parent.getClass() + "_Scope");
          for (Statement stmt : statements) {
              generateSymbolTable(stmt, parent);
          }

          SingleScopeSymbolTable scope  = symbolTable.exitScope();

          //Identifies, which Class parent is and sets the scope if parent to this.scope
          if(parent instanceof ConstructorDecl){
              ConstructorDecl constructorDecl = (ConstructorDecl) parent;
              constructorDecl.setScope(scope);
          }else if(parent instanceof FunctionDecl){
              FunctionDecl functionDecl = (FunctionDecl) parent;
              functionDecl.setScope(scope);
          }else if(parent instanceof IfStmt){
              IfStmt ifStmt = (IfStmt) parent;
              if(ifStmt.getCondition() != null) {
                  ifStmt.setIfScope(scope);
              } else {
                  ifStmt.setElseScope(scope);
              }
          }else if(parent instanceof WhileStmt){
              WhileStmt whileStmt = (WhileStmt) parent;
              whileStmt.setScope(scope);
          }else if(parent instanceof MainFunction){
              MainFunction mainFunction = (MainFunction) parent;
              mainFunction.setScope(scope);
          }else{
              System.err.println("Error: Statement calls Block Illegaly");
          }

      } else if(node instanceof FunctionDecl) {
          FunctionDecl funcDecl = (FunctionDecl) node;

          symbolTable.addSymbolToCurrentScope(funcDecl.getName(), funcDecl.getReturnType().getTypeName(), "FuncDecl");

          generateSymbolTable(funcDecl.getBody(), funcDecl);
      } else if(node instanceof VarDecl) {
          VarDecl varDecl = (VarDecl) node;

          symbolTable.addSymbolToCurrentScope(varDecl.getName(), varDecl.getType().getTypeName(), "VarDecl");
      } else if(node instanceof BinaryExpr) {
          BinaryExpr binaryExpr = (BinaryExpr) node;

          symbolTable.addSymbolToCurrentScope("BinaryExpr", "", "BinaryExpr");
      } else if(node instanceof Expression) {
          Expression expression = (Expression) node;
          generateSymbolTable(expression, expression);
      } else if(node instanceof AssignExpr) {
          AssignExpr assignExpr = (AssignExpr) node;
          symbolTable.addSymbolToCurrentScope(assignExpr.getTargetName(), "", "AssignExpr");
      } else if(node instanceof UnaryExpr) {
          UnaryExpr unaryExpr = (UnaryExpr) node;
          symbolTable.addSymbolToCurrentScope("UnaryExpr", "", "UnaryExpr");
      } else if(node instanceof ParenExpr){
          ParenExpr parenExpr = (ParenExpr) node;
          generateSymbolTable(parenExpr.getExpression(), parenExpr);
      } else if(node instanceof VarExpr){
          VarExpr varExpr = (VarExpr) node;
          symbolTable.addSymbolToCurrentScope(varExpr.getName(), "", "VarExpr");
      } else if(node instanceof ReturnStmt){
          ReturnStmt returnStmt = (ReturnStmt) node;
          symbolTable.addSymbolToCurrentScope("returnStmt", "", "ReturnStmt");
      } else if(node instanceof ClassDecl){
          ClassDecl classDecl = (ClassDecl) node;
          symbolTable.addSymbolToCurrentScope(classDecl.getName(), classDecl.getBaseClass(), "ClassDecl");

          symbolTable.enterScope(classDecl.getName() + "_Scope");
          List<Member> members = classDecl.getMembers();
          for (Member member : members) {
              generateSymbolTable(member, classDecl);
          }
          classDecl.setScope(symbolTable.exitScope());
      } else if(node instanceof MemberStmt){
          MemberStmt memberStmt = (MemberStmt) node;
          symbolTable.addSymbolToCurrentScope("MemberStmt", "", "MemberStmt");
      }

  }
}
