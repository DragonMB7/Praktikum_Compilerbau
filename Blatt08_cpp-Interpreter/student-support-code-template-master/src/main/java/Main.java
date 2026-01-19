import ast.*;
import java.io.IOException;
import java.nio.file.Paths;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Main {

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
}
