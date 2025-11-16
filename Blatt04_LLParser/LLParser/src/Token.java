public class Token {

    private final TokenType type;
    private finale String lexeme;

    public Token(TokenType pType, String pLexeme){
        this.type = pType;
        this.lexem = pLexem;
    }


    public TokenType getType(){
        return type;
    }

    public String getLexem(){
        return lexem;
    }

}