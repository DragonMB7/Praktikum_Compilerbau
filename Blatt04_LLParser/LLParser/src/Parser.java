
public class Parser {

    private Token peek;
    private Lexer lexer;

    public Parser(Lexer lex){
        this.lexer = lex;
        peek = lex.nextToken();
    }

    public void consume(){
        peek = lexer.nextToken();
    }

    public boolean match(TokenType t){
        if(peek.getType() == t){
            consume();
            return true;
        } else {
            return false;
        }
    }

    //Checks, if the current peek can be any Form of sExpression
    public boolean isSEXPR(){
        return (peek.getType() == TokenType.LBRACK || peek.getType() == TokenType.LET ||
                peek.getType() == TokenType.BOOL || peek.getType() == TokenType.NUMBER ||
                peek.getType() == TokenType.STRING || peek.getType() == TokenType.ID ||
                peek.getType() == TokenType.IF || peek.getType() == TokenType.DEF ||
                peek.getType() == TokenType.DEFN || peek.getType() == TokenType.LIST ||
                peek.getType() == TokenType.NTH || peek.getType() == TokenType.STR ||
                peek.getType() == TokenType.PRINT
                );
    }

    //Checks, if the current peek can be any Form of Operator
    public boolean isOP(){
        return (peek.getType() == TokenType.PLUS || peek.getType() == TokenType.MINUS ||
                peek.getType() == TokenType.MUL || peek.getType() == TokenType.DIV ||
                peek.getType() == TokenType.GREATER || peek.getType() == TokenType.LOWER ||
                peek.getType() == TokenType.EQUALS || peek.getType() == TokenType.ID
        );
    }


    public void PROGRAM(){
        //fürt die Grammatikregel PROGRAM aus: program: sExpr+ EOF
        do{
            match(TokenType.LBRACK);
            SEXPR();
        } while(peek.getType() != TokenType.EOF);
    }

    private int counter = 0;
    public void SEXPR (){
        counter++;

        if(peek.getType() == TokenType.LBRACK){
            match(TokenType.LBRACK);
        }

        if(peek.getType() != TokenType.EOF) {

            if (isOP()) {
                OP();
                while (peek.getType() != TokenType.RBRACK) {
                    SEXPR();
                    System.out.println("test");
                }
                match(TokenType.RBRACK);
            } else if (peek.getType() == TokenType.LET) {
                LET();
            } else if (peek.getType() == TokenType.STRING || peek.getType() == TokenType.NUMBER || peek.getType() == TokenType.BOOL) {
                DATATYPE();
            } else if (peek.getType() == TokenType.IF) {
                IF();
            } else if (peek.getType() == TokenType.DEF) {
                DEF();
            } else if (peek.getType() == TokenType.DEFN) {
                DEFN();
            } else if (peek.getType() == TokenType.LIST) {
                LIST();
            } else if (peek.getType() == TokenType.NTH) {
                NTH();
            } else if (peek.getType() == TokenType.STR) {
                STR();
            } else if (peek.getType() == TokenType.PRINT) {
                PRINT();
            } else if (peek.getType() == TokenType.ID) {
                match(TokenType.ID);
            } else {
                System.out.println("ERROR:  Unexpected Token " + peek.getType() + "( \"" + peek.getLexem() + "\" )" + counter);
                System.exit(1);
            }

        }

    }

    public void LET (){
        match(TokenType.LBRACK);
        match(TokenType.LET);
        match(TokenType.LBRACK);
        while(peek.getType() != TokenType.RBRACK){
            match(TokenType.ID);
            SEXPR();
        }
        match(TokenType.RBRACK);
        while(peek.getType() != TokenType.RBRACK){
            SEXPR();
        }
        match(TokenType.RBRACK);
    }

    public void DEF (){
        match(TokenType.LBRACK);
        match(TokenType.DEF);
        while(peek.getType() != TokenType.RBRACK){
            match(TokenType.ID);
            SEXPR();
        }
        match(TokenType.RBRACK);
    }

    public void DEFN (){
        match(TokenType.LBRACK);
        match(TokenType.DEFN);
        match(TokenType.ID);
        match(TokenType.LBRACK);
        do{
            match(TokenType.ID);
        }while(peek.getType() != TokenType.RBRACK);
        match(TokenType.RBRACK);
        while(peek.getType() != TokenType.RBRACK){
            SEXPR();
        }
        match(TokenType.RBRACK);
    }

    public void IF (){
        match(TokenType.LBRACK);
        match(TokenType.IF);
        SEXPR();
        CONDBLOCK();
        if(peek.getType() == TokenType.DO || isSEXPR()){    //First(1) auf CONDBLOCK
            CONDBLOCK();
        }
    }

    public void CONDBLOCK (){
        if(peek.getType() == TokenType.DO){
            DO();
        } else if(isSEXPR()){
            SEXPR();
        }
    }

    public void DO (){
        match(TokenType.LBRACK);
        match(TokenType.DO);
        while(peek.getType() != TokenType.RBRACK){
            SEXPR();
        }
    }

    public void LIST (){
        match(TokenType.LBRACK);
        if(peek.getType() == TokenType.LIST){
            match(TokenType.LIST);
            while(peek.getType() != TokenType.RBRACK){
                DATATYPE();
            }
        } else if(peek.getType() == TokenType.HEAD || peek.getType() == TokenType.TAIL){
            LPICK();
        } else {
            System.out.println("ERROR:  Unexpected Token " + peek.getType() + "(" + peek.getLexem() + "); Expected Type: HEAD, TAIL, LIST");
        }
    }

    public void NTH (){
        match(TokenType.LBRACK);
        match(TokenType.NTH);
        LIST();
        match(TokenType.NUMBER);
    }

    public void LPICK (){
        match(TokenType.LBRACK);
        if(peek.getType() == TokenType.HEAD){
            match(TokenType.HEAD);
            LIST();
        } else if(peek.getType() == TokenType.TAIL){
            match(TokenType.TAIL);
        } else {
            System.out.println("ERROR:  Unexpected Token " + peek.getType() + "(" + peek.getLexem() + "); Expected Type: HEAD, TAIL");
        }
    }

    public void STR (){
        match(TokenType.LBRACK);
        match(TokenType.STR);
        while(peek.getType() != TokenType.RBRACK){
            DATATYPE();
        }
        match(TokenType.RBRACK);
    }

    public void PRINT (){
        match(TokenType.LBRACK);
        match(TokenType.PRINT);
        if(peek.getType() == TokenType.STRING){
            match(TokenType.STRING);
        } else if(peek.getType() == TokenType.STR){
            match(TokenType.STR);
        }
        match(TokenType.RBRACK);
    }

    public void  OP(){
        if(peek.getType() == TokenType.PLUS){
            match(TokenType.PLUS);
        } else if(peek.getType() == TokenType.MINUS){
            match(TokenType.MINUS);
        } else if(peek.getType() == TokenType.MUL){
            match(TokenType.MUL);
        }else if(peek.getType() == TokenType.DIV){
            match(TokenType.DIV);
        } else if(peek.getType() == TokenType.GREATER){
            match(TokenType.GREATER);
        } else if(peek.getType() == TokenType.LOWER){
            match(TokenType.LOWER);
        } else if(peek.getType() == TokenType.EQUALS){
            match(TokenType.EQUALS);
        } else if(peek.getType() == TokenType.ID){
            match(TokenType.ID);
        } else {
            System.out.println("ERROR: Unexpected Token " + peek.getType() + "(" + peek.getLexem() + "); Expected Type: PLUS, MINUS, MUL, DIV, GREATER, LOWER, EQUALS, ID");
        }
    }

    public void DATATYPE(){
        if(peek.getType() == TokenType.STRING){
            match(TokenType.STRING);
        } else if(peek. getType() == TokenType.NUMBER){
            match(TokenType.NUMBER);
        } else if(peek.getType() == TokenType.BOOL){
            match(TokenType.BOOL);
        } else {
            System.out.println("ERROR: Unexpected Token " + peek.getType() + "(" + peek.getLexem() + "); Expected Type: STRING, NUMBER, BOOL");
        }
    }

}
