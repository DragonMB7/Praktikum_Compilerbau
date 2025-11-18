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
        if(pos >= input.length())
        {
            peek = (char)-1 ;
        }
        else
        {
            peek = input.charAt(pos) ;
        }
    }

    public boolean isEOF()
    {
        if(pos > input.length()-1)
        {
            return true ;
        }
        return false ;
    }



    /*
     * gibt jeweils das nächste Token zurück (bzw. EOF am Ende).
     */

    Token nextToken() {


        while (!isEOF()) {
            switch (peek) {

                case ' ':
                    WS();
                    continue;
                case '\t':
                    WS();
                    continue;
                case '\n':
                    WS();
                    continue;
                case '\r':
                    WS();
                    continue;

                case ';':
                    if (match(';')) {
                        COMMENT();
                        break;
                    }
                    System.out.println("Error: unexpected Token ;");
                    break;

                case '(':
                    consume();
                    return new Token(TokenType.LBRACK, "(");

                case ')':
                    consume();
                    return new Token(TokenType.RBRACK, ")");

                case '+':
                    consume();
                    return new Token(TokenType.PLUS, "+");

                case '-':
                    consume();
                    return new Token(TokenType.MINUS, "-");

                case '*':
                    consume();
                    return new Token(TokenType.MUL, "*");

                case '/':
                    consume();
                    return new Token(TokenType.DIV, "/");

                case '=':
                    consume();
                    return new Token(TokenType.EQUALS, "=");

                case '<':
                    consume();
                    return new Token(TokenType.LOWER, "<");

                case '>':
                    consume();
                    return new Token(TokenType.GREATER, ">");

                case '"':
                    return STRING();    // string() durchäuft den buffer weiter, bis der String geschlossen wird

                case 'i':
                    if (match('f')) {
                        consume();
                        return new Token(TokenType.IF, "if");
                    }

                case 'l':
                    if (match('e')) {
                        if (match('t')) {
                            consume();
                            return new Token(TokenType.LET, "let");
                        }
                    } else if (match('i')) {
                        if (match('s')) {
                            if (match('t')) {
                                consume();
                                return new Token(TokenType.LIST, "list");
                            }
                        }
                    }

                case 'd':
                    if (match('e')) {
                        if (match('f')) {
                            if (match('n')) {
                                consume();
                                return new Token(TokenType.DEFN, "defn");
                            }
                            consume();
                            return new Token(TokenType.DEF, "def");
                        }
                    } else if (match('o')) {
                        return new Token(TokenType.DO, "do");
                    }
                    break;

                case 'n':
                    if (match('t')) {
                        if (match('h')) {
                            return new Token(TokenType.NTH, "nth");
                        }
                    }

                case 's':
                    if (match('t')) {
                        if (match('r')) {
                            return new Token(TokenType.STR, "str");
                        }
                    }

                case 'p':
                    if (match('r')) {
                        if (match('i')) {
                            if (match('n')) {
                                if (match('t')) {
                                    return new Token(TokenType.PRINT, "print");
                                }
                            }
                        }
                    }

                case 'h':
                    if (match('e')) {
                        if (match('a')) {
                            if (match('d')) {
                                return new Token(TokenType.HEAD, "head");
                            }
                        }
                    }

                case 't':
                    if (match('a')) {
                        if (match('i')) {
                            if (match('l')) {
                                return new Token(TokenType.TAIL, "tail");
                            }
                        }
                    }


                default:
                    if (Character.isLetter(peek) && Character.isLowerCase(peek)) {
                        return NAME();
                    } else if (Character.isDigit(peek)) {
                        return NUMBER();
                    }
                    System.out.println("Error: unexpected Token");

            }
        }
        return new Token(TokenType.EOF, "<EOF>");
    }

    public boolean match(char c){
        consume() ;
        if (peek == c) {
            return true;
        }
        else {
            rollBack();
            return false ;
        }
    }

    public void rollBack(){
        pos-- ;
        if(pos < 0)
        {
            peek = (char)-1 ;
        }
        peek = input.charAt(pos) ;
    }

    public void WS(){
        while (peek == ' ' || peek == '\t' || peek == '\n' || peek == '\r')
        {
            consume();
        }
    }

    public void COMMENT(){
        while (peek != '\n' && peek != '\r' && !isEOF()){
            consume();
        }
    }

    public Token NAME(){

        if(peek == 't'){
            if(match('r')){
                if(match('u')){
                    if(match('e')){
                        consume();
                        return new Token(TokenType.BOOL, "true");
                    }
                }
            }
        }else if(peek == 'f'){
            if(match('a')){
                if(match('l')){
                    if(match('s')){
                        if(match('e')) {
                            consume();
                            return new Token(TokenType.BOOL, "false");
                        }
                    }
                }
            }
        }

        String out = String.valueOf(peek);

        consume();
        while (peek != ' ' && peek != '\t' && peek != '\n' && peek != '\r' && Character.isLetterOrDigit(peek) || peek == '_'){

            /*if(!Character.isLetterOrDigit(peek) && peek != '_'){
                System.out.println("Error: incompatible Token " + peek );
                break;
            } */

            out += peek;
            consume();

        }
        return new Token(TokenType.ID, out);
    }

    public Token NUMBER()
    {
        String num = "" ;
        while(isDigit(peek))
        {
            num = num + peek ;
            consume();
        }

        return new Token(TokenType.NUMBER, num) ;
    }

    public Token STRING(){

        String out = "";
        consume();
        while(peek != '"'){
            if(isEOF()){
                System.out.println("Error: unexpected EOF");
                break;
            }
            out += peek;
            consume();
        }
        consume();  //letzes " noch consumen, weil sonse peek für den Nächsten Durchlauf von nextToken immer noch " ist

        return new Token(TokenType.STRING, out);
    }

    public boolean isLetter(char c){
        return Character.isLetterOrDigit(c);
    }

    public boolean isDigit(char c)
    {
//      return (c == '0') || (c == '1') || (c == '2') || (c == '3') || (c == '4') || (c == '5') || (c == '6') || (c == '7') || (c == '8') || (c == '9') ;
        return Character.isDigit(c) ;
    }


}
