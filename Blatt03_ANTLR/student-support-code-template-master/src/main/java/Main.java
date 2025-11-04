import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import my.pkg.*;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class Main {
  static void main(String... args) throws IOException, URISyntaxException {
    IO.println("Hello World!");

    String input = "a := 2 ";

    String input2 = "a     := 0\n" +
        "    if    10 < 1\n" +
        "       do\n" +
        "a    :=     42      # Zuweisung des Wertes 42 an die Variable a\n" +
        "else do\n" +
        "        a :=      7\n" +
        "  end";

    String input3 = "while          1 do  a  + 1 end";

      myGrammarLexer lexer = new myGrammarLexer(CharStreams.fromString(input));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      myGrammarParser parser = new myGrammarParser(tokens);

      ParseTree tree = parser.start(); // Start-Regel

      //Traversiert den Baum und gibt richtig eingerückte Eingabe wieder aus
      MyGrammarVisitor visitor = new MyGrammarVisitor();
      Object result = visitor.visit(tree);

      IO.println((String)result);

    //IO.println(tree.toStringTree(parser));

    // Einlesen über den Classpath
    IO.readln("enter?> ");
    try (InputStream in = Main.class.getResourceAsStream("/cpp/vars.cpp")) {
      String text = new String(in.readAllBytes(), StandardCharsets.UTF_8);
      IO.println("\n\n/cpp/vars.cpp");
      IO.println(text);
    }

    // Einlesen über Dateisystem
    IO.readln("enter?> ");
    URL url = Main.class.getResource("/cpp/expr.cpp");
    String txt = Files.readString(Path.of(url.toURI()), StandardCharsets.UTF_8);
    IO.println("\n\n/cpp/expr.cpp");
    IO.println(txt);
  }
}
