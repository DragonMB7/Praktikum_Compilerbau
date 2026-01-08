package my.pkg;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class Main {
  static void main(String... args) {
    IO.println("Hello World!");

    // Einlesen über Konsole/Prompt
    String input = IO.readln("expr?> ");

    // Demonstriert den Einsatz von Packages und Grammatiken

    cppPackageLexer lexer = new cppPackageLexer(CharStreams.fromString(input));
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    cppPackageParser parser = new cppPackageParser(tokens);

    ParseTree tree = parser.packageDecl(); // Start-Regel
    IO.println(tree.toStringTree(parser));
  }
}
