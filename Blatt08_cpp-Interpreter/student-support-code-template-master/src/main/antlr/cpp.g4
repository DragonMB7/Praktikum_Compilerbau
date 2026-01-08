//Grammatik, direkt für die Syntax

grammar cpp;

start: stmt* EOF;

stmt    : vardecl
        | expr ';'
        | object
        ;

vardecl: dataType ID ('=' expr)? ';' | ID '=' expr ';';

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


function:   ID '(' parameters? ')' ;
parameters: expr (',' expr)* ;

object:     ID '.' ID ';' | ID '.' function ';' ;

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
ID    : [a-z][a-zA-Z]* ;                    //Identifier
NUM   : [0-9]+ ;                            //Integer
CHAR  : '\'' ( ~['\\] | '\\' . ) '\'';      //Character
STRING  :  '"' (~[\n\r"])* '"' ;            //String

COMMENT :  '//' ~[\n\r]* -> skip;
IMPORT : 'import' ~[\n\r]* -> skip;
WS    : [ \t\n]+ -> skip ;



