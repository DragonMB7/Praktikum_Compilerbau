//Grammatik, direkt für die Syntax

grammar cpp;

start: main? stmt* EOF;

stmt    : funcdecl
        | vardecl
        | expr ';'
        | object
        | while
        | if
        | return
        | class
        | print_KW
        ;

main    : (VOID_KW | INT_KW) 'main''()' block ;

// Expressions

expr            : logicalOrExpr ;

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
                | '(' expr ')'
                | function
                ;

class           : 'class' ID (':' 'public' ID )? '{' 'public:' member* '}' ;

member  : konstruktordecl
        | stmt
        ;

konstruktordecl : ID '(' paradecl ')' block ;

vardecl:    dataType '&'? ID ( '=' expr)? ';' | ID '&'? '=' expr ';';

funcdecl:   dataType ID '(' paradecl? ')' block ;
paradecl:   dataType '&'? ID (',' dataType '&'? ID)*;

function:   ID '(' parameters? ')' ;
parameters: ID (',' ID)* ;

return :    'return' expr? ';' ;


object:     ID '.' ID ';' | ID '.' function ';' ;


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

//Print Standardbibliothek
print_KW    : 'print_bool'      #print_bool
            | 'print_String'    #print_String
            | 'print_char'      #print_char
            | 'print_int'       #print_int
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



