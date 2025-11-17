public class Token {

    private final TokenType type;
    private final String lexeme;

    public Token(TokenType pType, String pLexeme){
        this.type = pType;
        this.lexeme = pLexeme;
    }


    public TokenType getType(){
        return type;
    }

    public String getLexem(){
        return lexeme;
    }

}