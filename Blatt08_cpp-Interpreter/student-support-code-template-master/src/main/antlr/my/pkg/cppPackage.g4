//Grammatik Komponente für Imports, packages u. a.

grammar cppPackage;

@header {
package my.pkg;
}

packageDecl: 'package' ID ';';

ID    : [a-z][a-zA-Z]* ;
NUM   : [0-9]+ ;

WS    : [ \t\n]+ -> skip ;




