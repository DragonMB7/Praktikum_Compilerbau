grammar myGrammar;


// Parser
start :  stmt+ EOF ;


stmt    :  vardecl
        |  assign
        |  expr
        |  while
        |  cond
        ;

vardecl :  type ID (':=' expr)? ;
assign  :  ID ':=' expr ;

while   :  'while'  expr 'do' stmt* 'end' ;
cond    :  'if'  expr 'do'  condblock ('else' 'do' elseblock)? 'end';

condblock: stmt*;
elseblock: stmt*;

expr    :  expr '*' expr #MulExpr
        |  expr '/' expr #DivExpr
        |  expr '+' expr #AddExpr
        |  expr '-' expr #SubExpr
        |  expr '>' expr #GreExpr
        |  expr '<' expr #SmaExpr
        |  expr '==' expr #EquExpr
        |  expr '!=' expr #NEquExpr
        |  ID #IDExpr
        |  NUMBER #NUMExpr
        |  STRING #STRExpr
        ;

type    :  'int' | 'string' ;


// Lexer
ID      :  [a-z][a-zA-Z0-9_]* ;
NUMBER  :  [0-9]+ ;
STRING  :  '"' (~[\n\r"])* '"' ;

COMMENT :  '#' ~[\n\r]* -> skip ;
WHITESPACE      :  [ \t\n\r]+ -> skip ;

