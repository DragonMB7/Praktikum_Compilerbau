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

main    : (VOID_KW | INT_KW) 'main''()' '{' stmt* '}' ;

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

class           : 'class' ID (':' 'public' ID )? '{' 'public:' konstruktordecl? stmt* '}' ;
konstruktordecl : ID '(' paradecl ')' '{' stmt* '}' ;

vardecl:    dataType '&'? ID ( '=' expr)? ';' | ID '&'? '=' expr ';';

funcdecl:   dataType ID '(' paradecl? ')' '{' stmt* '}' ;
paradecl:   dataType '&'? expr (',' dataType '&'? expr)*;

function:   ID '(' parameters? ')' ;
parameters: expr (',' expr)* ;

return :    'return' expr? ';' ;


object:     ID '.' ID ';' | ID '.' function ';' ;


while   :  'while' '(' expr ')' '{' stmt* '}' ;
if      :  'if' '(' expr ')' '{' condblock '}'('else' '{' elseblock '}')? ;

condblock: stmt*;
elseblock: stmt*;

dataType:   BOOL_KW
        |   INT_KW
        |   CHAR_KW
        |   STRING_KW
        |   VOID_KW
        |   ID
        ;

//Print Standardbibliothek
print_KW    : 'print_bool'      #print_b
            | 'print_String'    #print_S
            | 'print_char'      #print_c
            | 'print_int'       #print_i
            ;

// Lexer

BOOL_KW     : 'bool';
INT_KW      : 'int';
CHAR_KW     : 'char';
STRING_KW   : 'String';
VOID_KW     : 'void';


BOOL  : 'true' | 'false';                   //Boolean
ID    : [a-z][a-zA-Z]* ;                    //Identifier
NUM   : [0-9]+ ;                            //Integer
CHAR  : '\'' ( ~['\\] | '\\' . ) '\'';      //Character
STRING  :  '"' (~[\n\r"])* '"' ;            //String

COMMENT         :  ('//' | '#') ~[\n\r]* -> skip;
COMMENT_BLOCK   : '/*' ~[\n\r]* '*/' -> skip;
WS              : [ \t\n]+ -> skip ;



