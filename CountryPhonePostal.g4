grammar CountryPhonePostal;

file
    : record+ EOF
    ;

record
    : canadaRecord
    | germanyRecord
    ;

canadaRecord
    : CANADA phoneCanada postalCanada
    ;

germanyRecord
    : GERMANY phoneGermany postalGermany
    ;

// ---------------------
// COUNTRY NAMES
// ---------------------
CANADA
    : 'Canada' | 'CANADA'
    ;

GERMANY
    : 'Germany' | 'GERMANY'
    ;

// ---------------------
// PHONE NUMBERS
// ---------------------

phoneCanada
    : CAN_PHONE
    ;

phoneGermany
    : GER_PHONE
    ;

// Canadian phone numbers examples:
// 7805321813
// 1-780-532-1813
// 1 780 532 1813
// +1(780)532-1813
// 780-532-1813
CAN_PHONE
    : (
        ('+1' | '1')? [ -()]*
        DIGIT DIGIT DIGIT [ -()]*
        DIGIT DIGIT DIGIT [ -()]*
        DIGIT DIGIT DIGIT DIGIT
      )
    ;

// German phone numbers examples:
// +49 211 5684962
// 0211 5684962
// 0 (211) 568 4962
GER_PHONE
    : (
        ('+49' [ -()]*)?
        '0'? [ -()]*
        '('? DIGIT DIGIT DIGIT ')'?
        [ -()]*
        DIGIT+ ([ -()] DIGIT+)*
      )
    ;

// ---------------------
// POSTAL CODES
// ---------------------

postalCanada
    : CAN_POSTAL
    ;

postalGermany
    : GER_POSTAL
    ;

// Canadian postal codes examples:
// T6G-1R4, T6G 1R4, T6G1R4, t6g-1r4, etc.
CAN_POSTAL
    : [A-Za-z] DIGIT [A-Za-z] [ -]? DIGIT [A-Za-z] DIGIT
    ;

// Germany postal codes example:
// 01234-12345
GER_POSTAL
    : DIGIT DIGIT DIGIT DIGIT DIGIT '-' DIGIT DIGIT DIGIT DIGIT DIGIT
    ;

// ---------------------
// LEXER RULES
// ---------------------
fragment DIGIT : [0-9] ;

WS : [ \t\r\n]+ -> skip ;
