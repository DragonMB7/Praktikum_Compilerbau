import java.util.Scanner;
import SymTable.*;
import ast.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Repl {

    private final StringBuilder buffer = new StringBuilder();
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        System.out.println("REPL gestartet (:run zum Ausführen, :exit zum Beenden)");

        while (true) {
            System.out.print(">>> ");
            String line = scanner.nextLine();

            if (line.equals(":exit")) break;

            if (line.equals(":run")) {
                compileAndRun(buffer.toString());
                buffer.setLength(0);
            } else {
                buffer.append(line).append("\n");
            }
        }
    }

    private void compileAndRun(String source) {
        try {
            CharStream input = CharStreams.fromString(source);

            cppLexer lexer = new cppLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            cppParser parser = new cppParser(tokens);

            ParseTree tree = parser.start();

            ASTBuilder builder = new ASTBuilder();
            Program program = (Program) builder.visit(tree);

            Interpreter interpreter = new Interpreter();
            interpreter.execute(program);

        } catch (Exception e) {
            System.err.println("Fehler: " + e.getMessage());
        }
    }


}
