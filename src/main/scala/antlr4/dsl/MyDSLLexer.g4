lexer grammar MyDSLLexer;

//TB : [a-zA-Z0-9_]+ '.' [a-zA-Z0-9_]+;
TB : [a-zA-Z0-9_]+;
JOIN: '-';
LEFT_JOIN: '->';
RIGHT_JOIN: '<-';
FULL_JOIN: '<->';
SEMICOLON: ';';
LP: '(';
RP: ')';
COMMA: ',';
DOT: '.';
LEFT_BRACKET: '[';
RIGHT_BRACKET: ']';
LEFT_BRACE: '{';
RIGHT_BRACE: '}';
SPACE              : [ \t\r\n]+     -> channel(HIDDEN);
