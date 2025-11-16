import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;

public class LexMain {

    public static void main(String[] args) throws IOException {
        Path file = Path.of("src/TestProgramm.lisp");
        String input = Files.readString(file);
        System.out.println(input);

        Lexer lex = new Lexer(input);

    }

    
}
