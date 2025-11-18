import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;

public class LexMain {

    public static void main(String[] args) throws IOException {
        Path file = Path.of("C:\\Users\\mBuenger\\Praktikum_Compilerbau\\Blatt04_LLParser\\LLParser\\src\\TestProgramm.lisp");
        String input = Files.readString(file);
        System.out.println(input);

        Lexer lex = new Lexer(input);

        Parser parser = new Parser(lex);

        /*

        Token token = lex.nextToken();

        int counter = 0;
        while(token.getType() != TokenType.EOF){
            counter++;
            System.out.println(token.getLexem() + " " + counter);
            token = lex.nextToken();
        }
        System.out.println(lex.nextToken().getLexem());

         */

        parser.PROGRAM();


    }

    
}
