public class Lexer {

    private char peek;
    private String input;
    private int pos;
    //private char currentChar;


    /*
     * initialisiert Felder und liest erstes Zeichen. Zentrale Methode:
     */    
    public Lexer(String input){
        SetLexer(input);

    }

    public void SetLexer(String input){
        this.input = input;
        this.pos = 0;

        if (input.length() > 0){
            this.peek = input.charAt(0);
        } else {
            this.peek = (char)-1; //EOF
        }
    }

    /*
     * setzt peek auf das nächste Zeichen
     */
    public void consume(){
        pos ++;
        peek = input.charAt(pos);
        // start = (start+1) mod 2n
        // if (start mod n == 0):
        //     fill(buffer[start:start+n-1])
        //     end = (start+n) mod 2n
    }    

    /*
     * gibt jeweils das nächste Token zurück (bzw. EOF am Ende).
     */
    
    // Token nextToken(){
    
    
        // while (peek != EOF):  # globale Variable, über consume()
        // switch (peek()) {
        //
        //	case ' ':
        //	case '\t':
        //	case '\n':
        //		WS();
        //		continue:
        //
        //	case '(':
        //		consume();
        //		return new Token (TokenType.LBRACK, "(");
        //
        //	case ')':
        //		consume();
        //		return new Token (TokenType.RBRACK, ")");
        //
        //	case '+':
        //		consume();
        //		return new Token (TokenType.PLUS, "+");
        //
        //	case '-':
        //		consume();
        //		return new Token (TokenType.MINUS, "-");
        //
        //	case '*':
        //		consume();
        //		return new Token (TokenType.MUL, "*");
        //
        //	case '/':
        //		consume();
        //		return new Token (TokenType.DIV, "/");
        //
        //	case '=':
        //		consume();
        //		return new Token (TokenType.EQUALS, "=");
        //
        //	case '<':
        //		consume();
        //		return new Token (TokenType.LOWER, "<");
        //
        //	case '>':
        //		consume();
        //		return new Token (TokenType.GREATER, ">");
        //
        //	case '"':
        //		return string();	// string() durchäuft den buffer weiter, bis der String geschlossen wird
        //
        //
        //
        //	default:
        //		if (isID(peek)) return identifier();
        //		// Error ausgaben, fals eingabe icht legitim war
        //
        //}
        // return Token(EOF_Type, "<EOF>")
    
    // }

    public void match(char c){
        // consume()
        // if (peek == c): 
        //     return True
        // else: rollBack(); 
        //     return False
    }

    public void rollback(){
        // if (start == end): 
        //     raise Error("roll back error")
        // start = (start-1) mod 2n
    }

    public void WS(){
        // while (peek == ' ' || peek == '\t' || ...): consume()
    }

    public void NAME(){
        // buf = StringBuilder()
        // do { buf.append(peek); 
        //     consume(); 
        // } while (isLetter(peek))
        // return Token(NAME, buf.toString())
    }


}
