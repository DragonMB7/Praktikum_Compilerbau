//Grammatik, direkt für die Syntax

grammar cpp;

start: (main | stmt)* EOF;

stmt    : funcdecl
        | vardecl
        | expr ';'
        | while
        | if
        | return
        | class
        //| classdecl
        ;

main    : (VOID_KW | INT_KW) 'main''()' block ;

class   : 'class' ID (':' 'public' ID )? '{' 'public:' member* '}' ';' ;

//classdecl: ID ID '(' parameters ')' ';' ;


member : konstruktordecl
       | stmt
       ;

konstruktordecl : ID '(' paradecl? ')' block ;

// Expressions

expr            : ID '=' expr
                | logicalOrExpr
                ;

logicalOrExpr   : logicalAndExpr ( '||' logicalAndExpr )* ;

logicalAndExpr  : equalityExpr ( '&&' equalityExpr )* ;

equalityExpr    : relationalExpr ( ('==' | '!=') relationalExpr )* ;

relationalExpr  : additiveExpr ( ('<' | '<=' | '>' | '>=') additiveExpr )* ;

additiveExpr    : mulExpr ( ('+' | '-') mulExpr )* ;

mulExpr         : unaryExpr ( ('*' | '/' | '%') unaryExpr )* ;

unaryExpr       : ('-' | '!' | '+') unaryExpr | primaryExpr ;

primaryExpr     : ID
                | NUM
                | STRING
                | CHAR
                | BOOL
                | ID '.' ID                                // Zugriff auf ein Feld (z.B. obj.wert)
                | ID '.' function                          // Aufruf einer Methode (z.B. obj.berechne())
                | '(' expr ')'
                | function
                ;

vardecl:    dataType '&'? ID ( '=' expr)? ';';

funcdecl : 'virtual'? dataType ID '(' paradecl? ')' block ;
paradecl:   dataType '&'? ID (',' dataType '&'? ID)*;

function:   ID '(' parameters? ')' ;
parameters : expr (',' expr)* ;

return :    'return' expr? ';' ;

while   :  'while' '(' expr ')' '{' stmt* '}' ;
if      :  'if' '(' expr ')' block ('else' block)? ;

block: '{' stmt* '}' ;

dataType:   BOOL_KW
        |   INT_KW
        |   CHAR_KW
        |   STRING_KW
        |   VOID_KW
        |   ID
        ;


// Lexer

BOOL_KW     : 'bool';
INT_KW      : 'int';
CHAR_KW     : 'char';
STRING_KW   : 'String';
VOID_KW     : 'void';


BOOL  : 'true' | 'false';                   //Boolean
ID    : [a-z][a-zA-Z0-9_]* ;                //Identifier
NUM   : [0-9]+ ;                            //Integer
CHAR  : '\'' ( ~['\\] | '\\' . ) '\'';      //Character
STRING  :  '"' (~[\n\r"])* '"' ;            //String

COMMENT         :  ('//' | '#') ~[\n\r]* -> skip;
COMMENT_BLOCK   : '/*' .*? '*/' -> skip;
WS              : [ \t\n]+ -> skip ;



