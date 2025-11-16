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
        // switch (peek):
        //     case ' ': case '\t': case '\n': WS(); continue
        //     case '[': consume(); return Token(LBRACK, '[')
        //     case '<':
        //              if match('='): consume(); return Token(LE, "<=")
        //              else: consume(); return Token(LESS, '<')
        //     ...
        //     default:
        //         if isLetter(peek): return NAME()
        //         raise Error("invalid character: "+peek)
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
